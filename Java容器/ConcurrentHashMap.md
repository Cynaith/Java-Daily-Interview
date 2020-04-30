## ConcurrentHashMap1.8
> 为什么有ConcurrentMap?<br/>
> 因为HashMap在多线程条件下，会出现死循环。并且HashTable效率低下。

### 1.7和1.8的区别
- 1.7 Segment + HashEntry + Unsafe 锁分段
- 1.8 Synchronized + CAS + Node + Unsafe 
优化了锁的粒度，CAS失败后加锁

### 初始化
- 底层为散列表+红黑树
- 支持高并发，线程安全
- 检索不用加锁，get方法为非堵塞
- key、value不允许null

> 构造方法与HashMap相同

在初始化表时，通过volatile标记的sizeCtl是否为-1来判断是否有表正在扩容。
- 有表正在初始化 -> Thread.yield();
- 没有 -> sizeCtl=-1 ->初始化
### CAS算法
> Compare and swap : 比较与交换 <br/>
> 乐观锁

CAS有三个操作数
1. 内存值 V
2. 旧的预期值 A
3. 要修改的新值 B

当且仅当 A==B时，将V修改为B，否则什么都不做
- 当多个线程尝试使用CAS同时更新同一个变量时，只有其中一个线程能更新变量的值(A和内存值V相同时，将内存值V修改为B)，而其它线程都失败，失败的线程并不会被挂起，而是被告知这次竞争中失败，并可以再次尝试(否则什么都不做)

### volatile 
> 保证变量对所有线程可见
1. 禁止指令重排(内存屏障)
2. 不保证原子性

### put
> 与HashMap相同-赖扩容
### get
1. get方法没有加锁
2. 重写了Node节点,使用volatile修饰,保证获取到最新的值。

