## Java中的锁
### 乐观锁
> 读多写少

- 拿数据时认为别人不会修改
- 更新时会判断在此期间是否有线程去更新这个数据
    - 在写时先读出当前版本号，然后加锁(比较跟上一次的版本号，如果一样则更新),如果失败则要重复读-比较-写的操作。
Java中的乐观锁基本都是通过CAS操作实现的，CAS是一种更新的原子操作，比较当前值跟传入值是否一样，一样则更新，否则失败。

### 悲观锁
### 自旋锁
### Synchronized同步锁
> 一次只能允许一个线程进入被锁住的代码块

- synchronized保证了线程的原子性。(被保护的代码块是一次被执行的，没有任何线程会同时访问)
- synchronized还保证了可见性。(当执行完synchronized之后，修改后的变量对其他的线程是可见的)
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
Lock显式锁灵活
<br/>
Lock锁在刚出来的时候很多性能方面都比Synchronized锁要好，但是从JDK1.6开始Synchronized锁就做了各种的优化(毕竟亲儿子，牛逼)
<br/>
优化操作：适应自旋锁，锁消除，锁粗化，轻量级锁，偏向锁。
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