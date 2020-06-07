## MySQL数据库Query的优化

### MySQL Query Optimizer
在MySQL中有一个专门负责优化SELECT语句的优化器模块。主要功能是通过计算分析系统中收集的各种统计信息，
为客户端请求的Query给出他认为的最优的执行计划。

### Optimizer 基本工作原理
当客户端向MySQL请求一条Query，到命令解析器模块完成请求分类区别出是 SELECT 并转发给 Query Optimizer 之后，
Query Optimizer 首先会对整条 Query 进行，优化处理掉一些常量表达式的预算，直接换算成常量值。
并对 Query 中的查询条件进行简化和转换，如去掉一些无用或者显而易见的条件，结构调整等等。
然后则是分析 Query 中的 Hint 信息(如果有)，看显示 Hint 信息是否可以完全确定该 Query 的执行计划。
如果没有 Hint 或者 Hint 信息还不足以完全确定执行计划，则会读取所涉及对象的统计信息，根据 Query 进行写相应的计算分析，然后再得出最后的执行计划。

### Query语句优化的基本思路和原则
(基本思路)
1. 优化更需要优化的 Query;
2. 定位优化对象的性能瓶颈;
3. 明确的优化目标;
4. 从 Explain 入手;
(原则)
5. 多使用 profile
6. 永远用小结果集驱动大的结果集;
7. 尽可能在索引中完成排序;
8. 只取出自己需要的 Columns;
9. 仅仅使用最有效的过滤条件;
10. 尽可能避免复杂的Join和子查询;

- 优化更需要优化的 Query  
  一般来说，高并发低消耗的Query比低并发高消耗更应该优化。
- 定位优化对象的性能瓶颈  
  拿到一条需要优化的Query之后，首先要判断Query的瓶颈是IO还是CPU(如数据运算)  
  注: 5.0系列版本中，可以通过自带的PROFILING功能清楚的找出Query瓶颈所在。(5.1非正式版除外)
- 明确的优化目标  
  首先要清楚的了解数据库目前的整体状态，同时也要清楚的知道数据库中与该Query相关的数据库对象的各种信息。还要了解该 Query 在整个应用系统中所实现的功能。
  
  如果该Query 实现的应用 系统功能比较重要，我们就必须让目标更偏向于理想值一些，即使在其他某些方面作出一些让步与牺 牲，比如调整 schema 设计，调整索引组成等，
  可能都是需要的。而如果该 Query 所实现的是一些并 是太关键的功能，那我们可以让目标更偏向悲观值一些，而尽量保证其他更重要的Query 的性能。

- 从Explain入手  
  根据以上三点我们可以得知优化的目标，所以我们要从Explain入手查看Query在数据库中是以一个什么样的执行计划来实现的。
- 永远用小结果集驱动大的结果集  
  MySQL的Join是通过嵌套循环来实现的。驱动结果集越大，需要的循环就越多。IO消耗以及CPU消耗也就越多。
- 只取出自己需要的Columns，尤其是在需要排序的时候  
  从网络带宽、网络传输的缓冲区来看，数据越多，消耗越大。  
  MySQL4.1后，排序会一次性将所需要的Columns全部取出，在排序区排序。
- 仅仅使用最有效的过滤条件  
  WHERE子句过滤条件不是越多越好。  
  要保证索引键长度尽量小(UTF-8字符集占用3个字节)。
- 尽量避免复杂的Join和子查询  
  可以将复杂的Join语句拆分成多个简单的Query语句，牺牲单个query的短暂响应时间而提高整体的处理能力也是非常值得的。
  
### Explain的使用
- ID: Query Optimizer所选定的执行计划中查询的序列号。
- Select_type: 所使用的查询类型，主要有以下这几种查询类型
    - DEPENDENT SUBQUERY: 子查询中内层的第一个SELECT，依赖于外部查询的结果集。
    - DEPENDENT UNION: 子查询中的UNION，且为UNION中从第二个SELECT开始的后面所有的SELECT，同样依赖于外部查询的结果集。
    - PRIMARY: 子查询中最外层查询，不是主键查询。
    - SIMPLE: 除子查询或UNION之外的其他查询。
    - SUBQUERY: 子查询内层查询的第一个SELECT，结果不依赖于外部查询结果集。
    - UNCACHEABLE SUBQUERY: 结果集无法缓存的子查询。
    - UNION: UNION语句中第二个SELECT开始的后面所有SELECT，第一个SELECT为PRIMARY
    - UNION RESULT: UNION中的合并结果。
- Table: 显示这一步所访问的数据库中的表的名称。
- Type: 对表的访问方式
    - all: 全表扫描
    - const: 读常量，且最多只有一条记录匹配(如唯一索引)。
    - eq_ref: 最多只会有一条匹配结果，一般是通过主键或者唯一索引来访问。
    - fulltext: 
    - index: 全索引扫描
    - index_merge: 查询中同时使用两个(或更多)索引，然后对索引结果进行merge 之后再读取表数据。
    - index_subquery: 子查询中的返回结果字段组合是一个索引(或索引组合)，但不是一个主键或者唯一索引。
    - rang: 索引范围扫描。
    - ref: Join 语句中被驱动表索引引用查询。
    - ref_or_null: 与 ref 的唯一区别就是在使用索引引用查询之外再增加一个空值的查询。
    _ system: 系统表，表中只有一行数据。
    - unique_subquery: 子查询中的返回结果字段组合是主键或者唯一约束。
- Possible_keys: 该查询可以利用的索引，如果没有任何索引可以使用，就会显示成null，这一项内容对于优化时候索引的调整非常重要。
- Key: MySQL Query Optimizer 从 possible_keys中选择所使用的索引。
- Key_len: 被选中使用索引的长度。
- Ref: 列出是通过常量(const)，还是某个表的某个字段(如果是join)来过滤(通过 key)的。
- Rows: MySQL Query Optimizer通过系统收集到的统计信息估算出来的结果集记录条数。
- Extra: 查询每一步实现的额外细节信息


### Profiling的使用
- 开启profiling参数  
  `set profiling = 1;`
- 在开启 Query Profiler 功能之后，MySQL 就会自动记录所有执行的 Query 的 profile 信息了
- 获取系统中保存的所有 Query 的 profile 概要信息  
  `show profiles;`
- 针对单个 Query 获取详细的 profile 信息
  `show profile cpu, block io for query (概要信息中ID);` 
  
### 合理设计并使用索引
#### B-Tree索引
MySQL中的B-Tree索引的物理文件大多是以Balance Tree的结构来存储的。各种存储引擎存放时都会对存储结构稍作改造(Innodb存储引擎使用的实际上是B+Tree
(在每一个Leaf Node上面除了存放索引键相关信息之外，还存储了指向该Leaf Node相邻的后一个Lead Node的指针信息))。  
Innodb存在两种不同形式的索引，一种是Cluster形式的主键索引(除主键外还包括其他字段数据)，一种是普通的B-Tree索引(只存放主键信息)。所以，在Innodb中通过主键来访问数据效率是非常高的。

#### Hash索引
弊端:   
1. 仅仅只能满足 =、IN、<=> 不能使用范围查询。
2. 不能排序
3. 不能利用部分索引建查询
4. 不能表扫描
5. 遇到大量Hash值相等情况后性能低
#### Full-text索引(全文索引)
仅在MyISAM中支持 仅支持CHAR、VARCHAR、TEXT

#### R-Tree
主要用来解决空间数据检索的问题。

#### 索引的利处
提高数据检索效率、降低排序成本、降低CPU资源消耗

#### 索引的弊端
索引是独立于基础数据之外的另一部分数据，会增加IO消耗与CPU消耗。

#### 如何判断是否需要索引
1. 较频繁的作为查询条件的字段应该创建索引。
2. 唯一性太差的字段不适合单独创建索引
3. 更新非常频繁的字段不适合创建索引
4. 不会出现在WHERE子句中的字段不该创建索引

#### 单键索引还是组合索引
只要不是其中某个过滤字段在大多数场景下都能过滤出90%以上的数据，而且其他的过滤字段会存在频繁的更新，我一般更倾向于创建组合索引，尤其是在并发量较高的场景下更是应该如此。

还应该 尽量让一个索引被多个 Query 语句所利用，尽量减少同一个表上面索引的数量，减少因为数据更新所带 来的索引更新成本，同时还可以减少因为索引所消耗的存储空间。
#### Query的索引选择
1. 在Query 中增加 Hint 提 示 MySQL Query Optimizer 告诉他该使用哪个索引而不该使用哪个索引。(MySQL默认使用的索引并不一定是最优索引)
2. 调整查询条件来达到相同的目的。

建议:  
1. 对于单键索引，尽量选择针对当前 Query 过滤性更好的索引。
2. 在选择组合索引的时候，当前 Query 中过滤性最好的字段在索引字段顺序中排列越靠前越好。
3. 在选择组合索引的时候，尽量选择可以能够包含当前 Query 的 WHERE 子句中更多字段的索引。
4. 尽可能通过分析统计信息和调整 Query 的写法来达到选择合适索引的目的而减少通过使用 Hint 人为控制索引的选择，因为这会使后期的维护成本增加，同时增加维护所带来的潜在风险。

MySQL中索引的限制:  
1. MyISAM 存储引擎索引键长度总和不能超过 1000 字节。
2. BLOB 和 TEXT 类型的列只能创建前缀索引。
3. MySQL 目前不支持函数索引。
4. 使用不等于(!= 或者 <>)的时候 MySQL 无法使用索引。
5. 过滤字段使用了函数运算后(如 abs(column))，MySQL 无法使用索引。
6. Join 语句中 Join 条件字段类型不一致的时候 MySQL 无法使用索引。
7. 使用 LIKE 操作的时候如果条件以通配符开始( '%abc...')MySQL 无法使用索引。
8. 使用非等值查询的时候 MySQL 无法使用 Hash 索引。

#### Join实现原理及优化
原理:  
通过驱动表 的结果集作为循环基础数据，然后一条一条的通过该结果集中的数据作为过滤条件到下一个表中查询数据，然后合并结果。如果还有第三个参与 Join，
则再通过前两个表的 Join 结果集作为循环基础数据， 再一次通过循环查询条件到第三个表中查询数据，如此往复。

优化:  
1. 尽可能减少 Join 语句中的 Nested Loop 的循环总次数。
2. 优先优化 Nested Loop 的内层循环。
3. 保证 Join 语句中被驱动表上 Join 条件字段已经被索引。
4. 当无法保证被驱动表的 Join 条件字段被索引且内存资源充足的前提下，不要太吝惜 Join Buffer 的设置。

#### Order By、Group By、Distinct 优化
- Order By
    - 利用有序索引可以直接取得有序数据，在调整索引之前，还需要评估调整改索引对其他Query所带来的影响。
    - 无法使用索引时，应采用MySQL4.1版本后改进的排序算法。
        - 加大 max_length_for_sort_data 参数的设置。(增大排序区大小，使其能够一次扫描，避免二次扫描消耗IO)
        - 去掉不必要的返回字段。 
        - 增大 sort_buffer_size 参数设置。
- Group By
    - 使用松散(Loose)索引扫描实现 GROUP BY
      实际上就是当 MySQL 完全利用索引扫描来实现 GROUP BY 的 时候，并不需要扫描所有满足条件的索引键即可完成操作得出结果。  
        - 使用松散索引扫描需要满足以下条件:  
          1. 查询在单一表上。
          2. group by指定的所有列是索引的一个最左前缀，并且没有其它的列。比如表t1（ c1,c2,c3,c4）上建立了索引（c1,c2,c3）。
          如果查询包含“group by c1,c2”，那么可以使用松散索引扫描。但是“group by c2,c3”(不是索引最左前缀)和“group by c1,c2,c4”(c4字段不在索引中)。
          3. 如果在选择列表select list中存在聚集函数，只能使用 min()和max()两个聚集函数，并且指定的是同一列（如果min()和max()同时存在）。这一列必须在索引中，且紧跟着group by指定的列。比如，select t1,t2,min(t3),max(t3) from t1  group by c1,c2。
          4. 如果查询中存在除了group by指定的列之外的索引其他部分，那么必须以常量的形式出现（除了min()和max()两个聚集函数）。比如，select c1,c3 from t1 group by c1,c2不能使用松散索引扫描。而select c1,c3 from t1 where c3 =  3 group by c1,c2可以使用松散索引扫描。
          5. 索引中的列必须索引整个数据列的值(full column values must be indexed)，而不是一个前缀索引。比如，c1 varchar(20), INDEX (c1(10)),这个索引没发用作松散索引扫描。 
          6. 5.5后AVG(DISTINCT), SUM(DISTINCT)和COUNT(DISTINCT)可以使用松散索引扫描。COUNT(DISTINCT)可以使用多列参数。
          7. 5.5后可以在查询中没有group by和distinct条件。 `SELECT COUNT(DISTINCT c1), SUM(DISTINCT c1) FROM t1;`

    - 使用紧凑(Tight)索引扫描实现 GROUP BY
      紧凑索引扫描实现 GROUP BY 和松散索引扫描的区别主要在于他需要在扫描索引的时候，读取所有满足条件的索引键，然后再根据读取的数据来完成 GROUP BY 操作得到相应结果。  
    - 使用临时表实现 GROUP BY  
      前面两种 GROUP BY 的实现方式都是在有可以利用的索引的时候使用的，当 MySQL Query Optimizer 无法找到合适的索引可以利用的时候，
      就不得不先读取需要的数据，然后通过临时表来完成 GROUP BY 操作。

    如果不遵守最左原则，where b = xxx，就会使用紧凑索引遍历的方式，进行全表扫表。而我们如果真的有些情况，
      需要查出符合 B 条件但对 A 不进行限制的情况，应该如何处理，可能有些同学会新开一个独立索引仅包含 B 列，
      这样虽然能达到我们的目的，但难免有些多余。mysql 也是也能很好的进行跳跃松散索引扫描，只是 mysql 优化器无法自动进行，
      必须在查询语句中进行必要的声明。 select * from xxx where B = xxx group by A; 添加 group by 字段后，
      会先根据 A 索引分组后，会在每个 A 的范围内使用索引进行快速查询定位所需要的 B 列，这就叫做松散索引扫描，
      比新建一个索引的效率会慢 A 的 distinct 倍，但省去了新索引的消耗。

#### DISTINCT 的实现与优化
DISTINCT与GROUP BY同样可以通过松散索引扫描或者紧凑索引扫描来实现，在无法使用DISTINCT时，MySQL只能通过临时表来完成。(仅DISTINCT的Query无法使用索引时，不会对临时表数据进行filesort，如果使用Group By 就无法避免filesort)

- 松散索引  
`explain select distinct id from user\G` 的 Extra 为 `Using index for group-by`  
在实现 DISTINCT 的过程中，同样也是需要分组的，然后再从每组数据中取出一条返回给客户端。
- 紧凑索引  
使用where时，会先(Using Where)回表扫描，再利用索引直接Using index。
- 无法单独使用索引  
会使用临时表
- 与Group By结合  
使用聚合函数会filesort