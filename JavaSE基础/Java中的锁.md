## Java中的锁
### 乐观锁
> 读多写少

- 拿数据时认为别人不会修改
- 更新时会判断在此期间是否有线程去更新这个数据
    - 在写时先读出当前版本号，然后加锁(比较跟上一次的版本号，如果一样则更新),如果失败则要重复读-比较-写的操作。
Java中的乐观锁基本都是通过CAS操作实现的，CAS是一种更新的原子操作，比较当前值跟传入值是否一样，一样则更新，否则失败。

### 悲观锁
> 总是假设最坏的情况，每次去拿数据的时候都认为别人会修改，所以每次在拿数据的时候都会上锁

- Java中的synchronized和ReentrantLock等
- 关系型数据库中的行锁、表锁、读锁、写锁等
### 自旋锁  
### CAS  
通过工作空间中的值与主内存中的值比较，如果相同则进行修改，不同则重试或异常。  

缺点: 
1. 循环时间长开销很大
2. 只能保证一个共享变量的原子操作
3. ABA问题  eg:栈操作 (解决方法: 加版本号)

适用场景: 
1. 多读场景，冲突一般较少
### Synchronized同步锁
> 一次只能允许一个线程进入被锁住的代码块

- synchronized保证了线程的原子性。(被保护的代码块是一次被执行的，没有任何线程会同时访问)
- synchronized还保证了可见性。(当执行完synchronized之后，修改后的变量对其他的线程是可见的)

适用场景: 
1. 多写场景，冲突一般较多
### 重入锁
```java
public class Widget {

    // 锁住了
    public synchronized void doSomething() {
        ...
    }
}

public class LoggingWidget extends Widget {

    // 锁住了
    public synchronized void doSomething() {
        System.out.println(toString() + ": calling doSomething");
        super.doSomething();
    }
}
```
1. 当线程A进入到LoggingWidget的doSomething()方法时，此时拿到了LoggingWidget实例对象的锁。
2. 随后在方法上又调用了父类Widget的doSomething()方法，它又是被synchronized修饰。
3. 那现在我们LoggingWidget实例对象的锁还没有释放，进入父类Widget的doSomething()方法还需要一把锁吗？
不需要的！<br/>
因为锁的持有者是“线程”，而不是“调用”。线程A已经是有了LoggingWidget实例对象的锁了，当再需要的时候可以继续“开锁”进去的！
### Lock与synchronized
都是悲观锁  
Lock显式锁灵活
<br/>
Lock锁在刚出来的时候很多性能方面都比Synchronized锁要好，但是从JDK1.6开始Synchronized锁就做了各种的优化
<br/>
优化操作：适应自旋锁，锁消除，锁粗化，轻量级锁，偏向锁。

### *synchronized底层实现原理  
主要依靠 Lock-Free 的队列，基本思路是 自旋后阻塞，竞争切换后继续竞争锁，稍微牺牲了公平性，但获得了高吞吐量。

> 详情查看 《synchronized源码》文章
### *Atomic 底层实现原理
通过Unsafe类，使用CAS方式来实现原子操作  

弊端: 如果并发量很大的话，cpu会花费大量的时间在试错上面，相当于一个spin(自旋)的操作。
如果并发量小的情况，这些消耗可以忽略不计。  

优化: 并发量高的情况下可以使用LongAdder类实现。(内部的实现有点类似ConcurrentHashMap的分段锁)

### 公平锁
线程将按照它们发出请求的顺序来获取锁
### 非公平锁
线程发出请求的时可以“插队”获取锁
- Lock
- synchronized
### ReentrantLock

### 轻量级锁
### 偏向锁
### 分段锁
### 锁优化



##### refer from :
https://www.cnblogs.com/jyroy/p/11365935.html