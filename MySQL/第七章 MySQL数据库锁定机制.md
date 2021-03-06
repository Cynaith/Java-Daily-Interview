## MySQL数据库锁定机制
### 简介
数据库锁定机制简单来说就是数据库为了保证数据的一致性而使各种共享资源在被并发访问访问变得有序所设计的一种规则。
- 行级锁定
    - 特点: 所定对象颗粒度小，能够给予应用程序尽可能大的并发处理能力。
    - 弊端: 容易发生死锁
- 表级锁定
    - 特点: 实现逻辑简单，带来的系统负面影响最小，获取锁和释放锁的速度很快。可以避免死锁问题。
    - 弊端: 出现锁定资源争用的概率最高。
- 页级锁定
    - 特点: 锁定颗粒度介于行与表之间，处理能力介于两者之间。
    - 弊端: 会发生死锁。
### 各种锁定机制分析
#### 表级锁定
- 读锁定

  一个新的客户端请求在申请获取读锁定资源的时候，需要满足两个条件:
  1. 请求锁定的资源当前没有被写锁定
  2. 写锁定等待队列(Pending write-lock queue)中没有更高优先级的写锁定等待

  如果满足了上面两个条件之后，该请求会被立即通过，并将相关的信息存入 Current read-lock queue 中，
  而如果上面两个条件中任何一个没有满足，都会被迫进入等待队列Pending read-lock queue 中等待资源的释放。

- 写锁定

  1. 当客户端请求写锁定的时候，MySQL 首先检查在 Current write-lock queue 是否已经有锁定相同资源的信息存在。
  2. 如果 Current write-lock queue 没有，则再检查 Pending write-lock queue。
  3. 如果在 Pending write-lock queue 中找到了，自己也需要进入等待队列并暂停自身线程等待锁定资源。
  4. 如果Pending write-lock queue 为空，则再检测 Current read-lock queue，如果有锁定存在，则同样需要 进入 Pending write-lock queue 等待。

  特殊情况:(当遇到这两种特殊情况的时候，写锁定会立即获得而进入Current write-lock queue 中)   
  1. 请求锁定的类型为 WRITE_DELAYED;
  2. 请求锁定的类型为 WRITE_CONCURRENT_INSERT 或者是 TL_WRITE_ALLOW_WRITE ，同时 Current read lock 是 READ_NO_INSERT 的锁定类型。

### 合理利用锁机制优化 MySQL
- 缩短锁定时间(让Query执行时间尽可能短)  
  a) 尽量减少大的复杂 Query，将复杂 Query 分拆成几个小的 Query 分布进行  
  b) 尽可能的建立足够高效的索引，让数据检索更迅速  
  c) 尽量让 MyISAM 存储引擎的表只存放必要的信息，控制字段类型  
  d) 利用合适的机会优化 MyISAM 表数据文件  
- 分离能并行的操作  
  虽然MyISAM的表锁是读写互相堵塞的，但是MyISAM还有一个特性，那就是Concurrent Insert(并发插入)的特性。  
  MyISAM存储引擎有一个控制是否打开Concurrent Insert功能的参数选项:concurrent_insert，可以设置为 0，1 或者 2。三个值的具体说明如下:  
  a) concurrent_insert=2，无论 MyISAM 存储引擎的表数据文件的中间部分是否存在因为删除数据 而留下的空闲空间，都允许在数据文件尾部进行Concurrent Insert;  
  b) concurrent_insert=1，当 MyISAM 存储引擎表数据文件中间不存在空闲空间的时候，可以从文 件尾部进行Concurrent Insert;  
  c) concurrent_insert=0，无论 MyISAM 存储引擎的表数据文件的中间部分是否存在因为删除数据 而留下的空闲空间，都不允许Concurrent Insert。  
- 合理利用读写优先级  
  默认情况下是写优先级要大于读优先级。所以，如果我们可以根据各自系统环境的差异决定读与写的优先级。  
  **例如:** 
  1. 如果我们的系统是一个以读为主，而且要优先保证查询性能的话，我们可以通过设置系统参数选项 low_priority_updates=1，将写的优先级设置为比读的优先级低，即可让告诉 MySQL 尽量先处理读请求。
  2. 这里我们完全可以利用这个特性，将concurrent_insert参数设置为1，甚至如果数据被删除的可能 性很小的时候，如果对暂时性的浪费少量空间并不是特别的在乎的话，
  将 concurrent_insert 参数设置 为 2 都可以尝试。当然，数据文件中间留有空域空间，在浪费空间的时候，还会造成在查询的时候需要读取更多的数据，所以如果删除量不是很小的话，
  还是建议将concurrent_insert 设置为 1 更为合适。
  
- Innodb 行锁优化建议  
  Innodb 存储引擎由于实现了行级锁定，虽然在锁定机制的实现方面所带来的性能损耗可能比表级锁定会要更高一些，但是在整体并发处理能力方面要远远优于MyISAM 的表级锁定的。
  当系统并发量较高的 时候，Innodb 的整体性能和 MyISAM 相比就会有比较明显的优势了。但是，Innodb 的行级锁定同样也有其 脆弱的一面，当我们使用不当的时候，
  可能会让Innodb 的整体性能表现不仅不能比MyISAM 高，甚至可能会更差。
  
  1. 尽可能让所有的数据检索都通过索引来完成，从而避免 Innodb 因为无法通过索引键加锁而升级为表级锁定;
  2. 合理设计索引，让 Innodb 在索引键上面加锁的时候尽可能准确，尽可能的缩小锁定范围，避免造成不必要的锁定而影响其他 Query 的执行;
  3. 尽可能减少基于范围的数据检索过滤条件，避免因为间隙锁带来的负面影响而锁定了不该锁定的记录;
  4. 尽量控制事务的大小，减少锁定的资源量和锁定时间长度
  5. 在业务环境允许的情况下，尽量使用较低级别的事务隔离，以减少 MySQL 因为实现事务隔离级别所带来的附加成本;
  
- Innodb减少死锁的建议
  
  1. 类似业务模块中，尽可能按照相同的访问顺序来访问，防止产生死锁;
  2. 在同一个事务中，尽可能做到一次锁定所需要的所有资源，减少死锁产生概率;
  3. 对于非常容易产生死锁的业务部分，可以尝试使用升级锁定颗粒度，通过表级锁定来减少死锁产生的概率;