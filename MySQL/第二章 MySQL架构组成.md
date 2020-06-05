## MySQL架构组成

### MySQL物理文件组成
- 日志文件
    - 错误日志:Error Log
    
    - 二进制日志: Binary Log & Binary Log Index  
    二进制日志，就是常说的binlog，当我们通过“--log-bin\[=file_name\]”打开了记录的功能之后，
    MySQL 会将所有修改数据库数据的 query 以二进制形式记录到日志文件中。 当然，日志中并不仅限于query 语句这么简单，
    还包括每一条query所执行的时间，所消耗的资源， 以及相关的事务信息，所以 binlog 是事务安全的。
    - 更新日志: update Log  
    功能与binlog类似，只不过不是二进制格式，是以简单文本格式记录内容。
    - 查询日志: query Log  
    记录MySQL中所有的query，开启后对性能有较大影响。
    - 慢查询日志: slow query log  
    记录执行时间较长的query，简单文本格式。
    - Innodb的在线redo日志: innodb redo log  
    是一个事务安全的存储引擎。

- 数据文件
多数存储引擎的数据文件都存放在和MyISAM 数据文件位置相同的目录下，但是每个数据文件的扩展名却各不一样。
如MyISAM 用“.MYD”作为扩展 名，Innodb 用“.ibd”，Archive 用“.arc”，CSV 用“.csv”，等等。
    - .frm文件  
    存储与表相关的元数据信息。包括表结构的定义信息。
    - .MYD文件  
    专属于MyISAM存储引擎，存放MyISAM表的数据。
    - .MYI文件  
    专属于MyISAM存储引擎，主要存放MyISAM表的索引信息。
    - .ibd文件和ibdata文件  
    存放Innodb数据的文件，独享表空间的存储方式使用.ibd文件存放数据，共享表空间存放数据，则会使用ibdata文件存放。
- Replication相关文件
    - master.info  
    存放在Slave端的数据目录下，存放该Slave的Master端的相关信息，包括Master的主机地址、连接用户、端口、当前日志位置、已经读取到的日志位置等信息。
    - relay log 和 relay log index
    - relay-log.info  
    类似于master.info，存放通过Slave的I/O线程写入到本地的relay log的相关信息。供Slave端的SQL线程以及某些管理操作随时能够获取当前复制的相关信息。
- 其他文件
    - system config file  
    MySQL的系统配置文件为my.cnf
    - pid file 
    是mysqld应用程序在Unix/Linux环境下的一个进程文件，存放着自己的进程id。
    - socket file  
    socket文件也是在Unix/Linux环境下才有的，用户在此环境下客户端连接可以不通过TCP/IP而直接使用Unix socket来连接MySQL。
    
### MySQL Server系统架构
第一层通常叫做SQL Layer，在 MySQL 数据库系统处理底层数据之前的所有工作都是在这一层完成的，包括权限判断，sql 解析，执行计划优化， query cache 的处理等等;
第二层就是存储引擎层，我们通常叫做 Storage Engine Layer，也就是底层数据存取操作实现部分，由多种存储引擎共同组成
![MySQL架构](https://github.com/Cynaith/Java-Daily-Interview/blob/master/MySQL/MySQL%E6%9E%B6%E6%9E%84.png)
- SQL Layer
    - 初始化模块
    - 核心API
    - 网络交互模块
    - Client & Server交互协议模块
    - 用户模块
    - 访问控制模块
    - 连接管理、连接进程和线程管理
    - Query解析和转发模块
    - Query Cache模块
    - Query优化器模块
    - 表变更模块
    - 表维护模块
    - 状态管理模块
    - 表管理器
    - 日志记录模块
    - 复制模块
    - 存储引擎接口模块