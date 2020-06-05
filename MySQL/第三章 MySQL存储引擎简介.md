## MySQL存储引擎简介
MyISAM是MySQL默认的存储引擎。从5.1开始，引入新概念：插件式存储引擎体系结构(MyISAM、Innodb、NDB Cluster、Maria、Falcon等)。

### MyISAM存储引擎
MyISAM支持以下三种类型的索引:
1. B-Tree (balance tree)
2. R-Tree (用于存储空间和多维数据的字段索引)
3. Full-text (全文索引 b-tree)


### Innodb存储引擎

特点:
1. 支持事务安装  
    未提交读 Read uncommitted
    已提交读 Read committed
    可重复读 Repeatable read
    可串行化 Serializable
2. 数据多版本读取  
通过实现undo日志来实现数据的多版本读取。
3. 锁定机制的改进  
改变了MyISAM的锁机制，实现了行锁。
4. 实现外键
