## HashMap 1.7
> 链表+数组 Entry节点
- 数据插入过程使用头插法 
    - resize()调用了一个transfer方法，将里面Entry进行rehash,在多线程条件下会造成链表的循环
## HashMap 1.8
> 链表+数组+红黑树 Node节点
- 尾插法
    - 虽然解决了链表循环的问题，但是多线程操作下不能保证数据的一致性。
    > HashTable只是在方法上加synchonized 同一个时间只允许一个线程访问,不适用于并发量高的环境
- 扩容
    - 初始化容量Capacity为16,加载因子loadFactor为0.75,计算出一个threshold(阈值) = capacity * loadFactor。
    在put元素时会先判断添加此元素后 size 是否大于 threshold如果大于就resize()两倍大,再将Node插入新HashMap。 
- 线程安全
    - ~~HashTable~~
    - 使用Collections.synchronizedMap()
    - ConcurrentHashMap
        - CAS+synchronized
    
## Hash冲突解决方法 
https://blog.csdn.net/zxl2016/article/details/87628507                                       
    