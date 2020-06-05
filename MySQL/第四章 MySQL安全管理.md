## MySQL安全管理

### 数据库系统安全相关因素
- 外围网络
- 主机
- 数据库
- 代码

### MySQL权限系统介绍
关权限信息主要存储在几个被称为 grant tables 的系统表中，即: mysql.User，mysql.db，mysql.Host，mysql.table_priv 和 mysql.column_priv。由于权限信息数据量比较小，而且访问又非常频繁，所以Mysql 在启 动的时候，就会将所有的权限信息都Load 到内存中保存在几个特定的结构中。
所以才有我 们每次手工修改了权限相关的表之后， 都需要执“行FLUSH PRIVILEGES”命令重新加载 MySQL 的权限信息。

- 权限级别
    - Global Level(全局权限控制)
    - Database Level(作用域为所指定整个数据库中的所有对象)
    - Table Level(作用范围是授权语句中所指定数据库的指定表)
    - Column Level(作用范围为某个表的某个列)
    - Routine Level(权限主要只有EXECUTE、ALTER ROUTINE两种)