## MySQL Server性能优化
### MySQL安装优化
- 二进制发行版本  
  没有太多可以优化的地方
- 源码安装  
  可以自行调整编译参数，可以在MySQL源码所在文件夹下面通过执行`./configure -help`得到可以设置的所有编译参数。一些重要的编译参数如下:  
  1. `-prefix` 设定安装路径，默认为"/usr/local"
  2. `-datadir` 设定MySQL数据文件存放路径
  3. `-with-charset` 设定系统的默认字符集
  4. `-with-collation` 系统默认的校验规则
  5. `-with-extra-charsets` 除了默认字符集之外需要编译安装的字符集
  6. `-with-unix-socket-path` 设定socket文件地址
  7. `-with-tcp-port` 指定特定监听端口，默认为3306
  8. `-with-mysqld-user` 指定运行mysqld的os用户，默认为mysql
  9. `-without-query-cache` 禁用Query Cache功能
  10. `-without-innodb` 禁用 Innodb存储引擎
  11. `-with-partition` 在5.1版本中开启partition支持特性
  12. `-enable-thread-safe-client` 以线程方式编译客户端
  13. `-with-pthread` 强制使用pthread线程库编译
  14. `-with-named-thread-libs` 指定使用某个特定的线程库编译
  15. `-without-debug` 使用非debug模式
  16. `-with-mysqld-ldflags` mysqld的额外link参数
  17. `-with-client-ldflags` client的额外link参数
  
  ```xml
    ./configure --prefix=/usr/local/mysql \
    --without-debug \
    --without-bench \
    --enable-thread-safe-client \
    --enable-assembler \
    --enable-profiling \
    --with-mysqld-ldflags=-all-static \ --with-client-ldflags=-all-static \ --with-charset=latin1 \
    --with-extra-charset=utf8,gbk \
    --with-innodb \
    --with-csv-storage-engine \ --with-federated-storage-engine \ --with-mysqld-user=mysql \
    --without-embedded-server \ --with-server-suffix=-community \ --with-unix-socket-path=/usr/local/mysql/sock/mysql.sock
  ```
  
### MySQL日志设置优化
#### 日志产生的性能影响
日志的记录带来的直接性能损耗就是IO资源，默认情况下系统仅打开错误日志。会对性能产生影响的MySQL日志主要就是Binlog了
#### Binlog相关参数及优化策略  
使用`show variables like '%binlog%';`可以获得关于Binlog的相关参数。
- binlog_cache_size: 在事务过程中容纳二进制日志 SQL 语句的缓存大小。
- max_binlog_cache_size : 和"binlog_cache_size"相对应，但是所代表的是 binlog 能够使用的最大 cache 内存大小。
- max_binlog_size : Binlog 日志最大值，一般来说设置为 512M 或者 1G，但不能超过 1G。
- sync_binlog : 这个参数是对于 MySQL 系统来说是至关重要的，他不仅影响到Binlog 对 MySQL 所带来的性能损耗，而且还影响到 MySQL 中数据的完整性。
    - sync_binlog=0 当事务提交之后，MySQL 不做 fsync 之类的磁盘同步指令刷新 binlog_cache 中 的信息到磁盘，而让 Filesystem 自行决定什么时候来做同步，或者 cache 满了之后才同步到磁 盘。
    - sync_binlog=n，当每进行 n 次事务提交之后，MySQL 将进行一次 fsync 之类的磁盘同步指令来 将 binlog_cache 中的数据强制写入磁盘。
#### MySQL的复制
MySQL的复制是通过Master端的Binlog利用IO线程通过网络复制到Slave端，Binlog产生量不能改变，但是可以控制不许要复制的db或table。
- Binlog_Do_DB : 设定哪些数据库(Schema)需要记录 Binlog。
- Binlog_Ignore_DB : 设定哪些数据库(Schema)不要记录 Binlog。
- Replicate_Do_DB : 设定需要复制的数据库(Schema)，多个 DB 用逗号(“,”)分隔。
- Replicate_Ignore_DB : 设定可以忽略的数据库(Schema)。
- Replicate_Do_Table : 设定需要复制的 Table。
- Replicate_Ignore_Table : 设定可以忽略的 Table。
- Replicate_Wild_Do_Table : 功能同 Replicate_Do_Table，但可以带通配符来进行设置。
- Replicate_Wild_Ignore_Table : 功能同 Replicate_Ignore_Table，可带通配符设置。

上面这八个参数中的前面两个是设置在 Master 端的，而后面六个参数则是设置在 Slave 端 的。虽然前面两个参数和后面六个参数在功能上并没有非常直接的关系，
但是对于优化 MySQL 的 Replication 来说都可以启到相似的功能。区别如下:  
  
- 如果在 Master 端设置前面两个参数，不仅仅会让Master 端的 Binlog 记录所带来的 IO 量减少， 还会让 Master 端的 IO 线程就可以减少 Binlog 的读取量，
传递给 Slave 端的 IO 线程的 Binlog 量自然就会较少。这样做的好处是可以减少网络IO，减少 Slave 端 IO 线程的 IO 量，减少 Slave 端的 SQL 线程的工作量，
从而最大幅度的优化复制性能。当然，在 Master 端设置也存在一定的 弊端，因为 MySQL 的判断是否需要复制某个 Event 不是根据产生该 Event 的 Query 所更改的数据所在的 DB，
而是根据执行 Query 时刻所在的默认 Schema，也就是我们登录时候指定的 DB 或者运 行“USE DATABASE”中所指定的 DB。
只有当前默认 DB 和配置中所设定的 DB 完全吻合的时候 IO 线程才会将该 Event 读取给 Slave 的 IO 线程。所以如果在系统中出现在默认DB 和设定需要复制 的 DB 不一样的情况下改变了需要复制的DB 中某个 Table 的数据的时候，
该 Event 是不会被复制 到 Slave 中去的，这样就会造成 Slave 端的数据和 Master 的数据不一致的情况出现。同样，如 果在默认 Schema 下更改了不需要复制的 Schema 中的数据，
则会被复制到 Slave 端，当 Slave 端 并没有该 Schema 的时候，则会造成复制出错而停止;
- 而如果是在 Slave 端设置后面的六个参数，在性能优化方面可能比在 Master 端要稍微逊色一 点，因为不管是需要还是不需要复制的 Event 都被会被 IO 线程读取到 Slave 端，
这样不仅仅增 加了网络IO量，也给Slave端的IO线程增加了Relay Log的写入量。但是仍然可以减少Slave 的 SQL 线程在 Slave 端的日志应用量。虽然性能方面稍有逊色，
但是在Slave 端设置复制过滤机 制，可以保证不会出现因为默认Schema的问题而造成Slave和Master数据不一致或者复制出错 的问题。
#### Slow Query Log相关参数及使用建议
使用`show variables like 'log_slow%';`/`show variables like 'long_query%';`可以查看Slow Query Log相关设置。
- log_slow_queries : 显示系统是否已经打开Slow Query Log。
- long_query_time : 当前系统设置的Slow Query记录执行时间超过多长的Query  
但可以设置的最短查询时间为1s，如果进一步缩短慢查询的时间限制，可以使用Percona提供的microslow-patch，甚至能通过一些特定规则来过滤SQL。  

Slow Query Log 数据量小，带来的IO损耗也就小。但是系统需要计算每一条Query的执行时间，主要是CPU的消耗。
### Query Cache优化
简单的来说就是将客户端请求的 Query
语句(当然仅限于 SELECT 类型的 Query)通过一定的 hash 算法进行一个计算而得到一个 hash 值，存放 在一个 hash 桶中。
同时将该 Query 的结果集(Result Set)也存放在一个内存 Cache 中的。存放 Query hash值的链表中的每一个hash值所在的节点中同时还存放了该Query所对应的 Result Set 的Cache所在的内存地址，
以及该 Query 所涉及到的所有 Table 的标识等其他一些相关信息。系统接受到任何一个 SELECT 类型的 Query 的时候，
首先计算出其 hash 值，然后通过该 hash 值到 Query Cache 中去匹配，如 果找到了完全相同的Query，则直接将之前所Cache的 Result Set 返回给客户端而完全不需要进行后面的任何步骤即可完成这次请求。
而后端的任何一个表的任何一条数据发生变化之后，也会通知 Query Cache，需要将所有与该 Table 有关的 Query 的 Cache 全部失效，并释放出之前占用的内存地址，以便后 面其他的 Query 能够使用。

- 负面影响
    - Query语句的hash运算及hash查找资源消耗CPU。
    - Query Cache失效问题。
    - Query Cache缓存的是Result Set，存在同一条记录被Cache存放多次的可能性。
- 适度使用
    - 根据Query Cache失效机制来判断哪些表适合使用Query哪些表不适合。
    - 对于那些变化非常小，大部分时候都是静态的数据，我们可以添加 SQL_CACHE 的 SQL Hint， 强制 MySQL 使用 Query Cache，从而提高该表的查询性能。
    - 有些SQL的 Result Set 很大，如果使用Query Cache很容易造成Cache内存的不足，或者将 之前一些老的Cache 冲刷出去。对于这一类Query我们有两种方法可以解决，
    一是使用SQL_NO_CACHE参 数来强制他不使用 Query Cache 而每次都直接从实际数据中去查找，另一种方法是通过设定 “query_cache_limit”参数值来控制 Query Cache 中所 Cache 的最大 Result Set ，
    系统默认为 1M(1048576)。当某个 Query 的 Result Set 大于“query_cache_limit”所设定的值的时候， Query Cache 是不会 Cache 这个 Query 的。
#### Query Cache 的相关系统参数变量和状态变量  
使用`show variables like '%query_cache%';`可以获取MySQL中Query Cache相关的系统参数变量。  
- have_query_cache : 该 MySQL 是否支持 Query Cache;
- query_cache_limit : Query Cache 存放的单条 Query 最大 Result Set ，默认 1M;
- query_cache_min_res_unit : Query Cache 每个 Result Set 存放的最小内存大小，默认4k;
- query_cache_size : 系统中用于 Query Cache 内存的大小;
- query_cache_type : 系统是否打开了 Query Cache 功能;
- query_cache_wlock_invalidate : 针对于 MyISAM 存储引擎，设置当有 WRITE LOCK 在某个Table 上面的时候，
读请求是要等待 WRITE LOCK 释放资源之后再查询还是允许直接从 Query Cache中读取结果，默认为FALSE(可以直接从Query Cache中取得结果)。  

使用` show status like 'Qcache%';` 可以了解到Query Cache的使用情况。
- Qcache_free_blocks : Query Cache 中目前还有多少剩余的 blocks。如果该值显示较大， 则说明 Query Cache 中的内存碎片较多了，可能需要寻找合适的机会进行整理()。
- Qcache_free_memory : Query Cache 中目前剩余的内存大小。通过这个参数我们可以较为准 确的观察出当前系统中的Query Cache内存大小是否足够，是需要增加还是过多了。
- Qcache_hits : 多少次命中。通过这个参数我们可以查看到Query Cache 的基本效果。
- Qcache_inserts : 多少次未命中然后插入。通过“Qcache_hits”和“Qcache_inserts”两个参数我们就可以算出Query Cache的命中率了:Query Cache 命中率 = Qcache_hits / ( Qcache_hits + Qcache_inserts )。
- Qcache_lowmem_prunes : 多少条 Query 因为内存不足而被清除出 Query Cache 。通过“Qcache_lowmem_prunes”和“Qcache_free_memory”相互结合，
能够更清楚的了解到我们系 统中Query Cache的内存大小是否真的足够，是否非常频繁的出现因为内存不足而有Query被换出。
- Qcache_not_cached : 因为 query_cache_type 的设置或者不能被 cache 的 Query 的数量。
- Qcache_queries_in_cache : 当前 Query Cache 中 cache 的 Query 数量。
- Qcache_total_blocks : 当前 Query Cache 中的 block 数量。

#### Query Cache的限制
- 5.1.17 之前的版本不能 Cache 绑定变量的 Query，但是从 5.1.17 版本开始，Query Cache 已经开始支持绑定变量的 Query 了。
- 所有子查询中的外部查询 SQL 不能被 Cache。
- 在 Procedure，Function 以及 Trigger 中的 Query 不能被 Cache。
- 包含其他很多每次执行可能得到不一样结果的函数的Query 不能被 Cache。

### MySQL Server其他常用优化

#### 网络连接与连接线程
网络连接配置:
- max_connections : 整个MySQL允许的最大连接数。
- max_user_connections : 每个用户允许的最大连接数。
- net_buffer_length : 网络包传输中，传输消息之前的 net buffer 初始化大小。
- max_allowed_packet : 在网络传输中，一次传消息输量的最大值。
- back_log : 在 MySQL 的连接请求等待队列中允许存放的最大连接请求数。  
连接线程配置:
- thread_cache_size : Thread Cache池中应该存放的连接线程数。
- thread_stack : 每个连接线程被创建的时候，MySQL 给他分配的内存大小。

使用`show variables like 'thread%';`查看连接线程相关的系统变量的设置值。  
使用`show status like 'connections';` 查看系统被连接的次数以及使用`show status like '%thread%'`查看当前系统中连接线程的状态值。

#### Table Cache相关的优化  
使用` show variables like 'table_cache';`查看 table_cache 的设置。  
使用`show status like 'open_tables'`查看当前系统中的使用状况。  

什么情况下会关闭Table Cache中的Cache: 
1. Table Cache 的 Cache 池满了，而某个连接线程需要打开某个不在Table Cache 中的表时，MySQL 会通过一定的算法关闭某些没有在使用中的描述符。
2. 当我们执行 Flush Table 等命令的时候，MySQL 会关闭当前 Table Cache 中 Cache 的所有文件描述符。
3. 当 Table Cache 中 Cache 的量超过 table_cache 参数设置的值的时候。

#### Sort Buffer，Join Buffer 和 Read Buffer
在 Query 执行过程中的两种 Buffer 会对数据库的整体性能产生影响。`show variables like '%buffer%';`

- join_buffer_size : 当我们的 Join 是 ALL ， index ， rang 或者 index_merge 的时候使用的 Buffer。  
  实际上这种 Join 被称为 Full Join。实际上参与 Join 的每一个表都需要一个 Join Buffer，所以在 Join 出现的时候，至少是两个。
  Join Buffer 的设置在 MySQL 5.1.23 版本之前最大为 4GB，但是从 5.1.23 版本开始，在除了 Windows 之外的 64 位的平台上可以超出 4BG 的限制。系统默认是 128KB。
- sort_buffer_size : 系统中对数据进行排序的时候使用的Buffer。  
  Sort Buffer同样是针对单个Thread的，所以当多个Thread同时进行排序的时候，系统中就会出现 多个 Sort Buffer。一般我们可以通过增大 Sort Buffer 的大小来提高 ORDER BY 或者是 GROUP BY 的处理性能。
  系统默认大小为 2MB，最大限制和 Join Buffer 一样，在 MySQL 5.1.23 版本之前最大 为 4GB，从 5.1.23 版本开始，在除了 Windows 之外的 64 位的平台上可以超出 4GB 的限制。