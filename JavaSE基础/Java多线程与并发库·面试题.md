## Java多线程与并发库·面试题
### 多线程的创建 
- 继承 Thread 类
    - 但 Thread 本质上也是实现了 Runnable 接口的一个实例，它代表一个线程的实例，并 且，启动线程的唯一方法就是通过 Thread 类的 start()实例方法。
    start()方法是一个 native 方法，它将启动一个新线 程，并执行 run()方法。这种方式实现多线程很简单，通过自己的类直接 extend Thread，
    并复写 run()方法，就可以 启动新线程并执行自己定义的 run()方法。
- 实现 Runnable 接口
- 使用 ExecutorService、Callable、Future 实现有返回结果的多线程
### 在 java 中 wait 和 sleep 方法的不同?
- wait会释放锁 而sleep会一直持有锁
- wait通常用于线程间交互，sleep通常用与暂停执行

### synchronized 和 volatile 关键字的作用
变量被volatitle修饰
> volatile 本质是在告诉 jvm 当前变量在寄存器(工作内存)中的值是不确定的，需要从主存中读取;
- 保证了不同线程对这个变量进行操作时的可见性，即一个线程修改了某个变量的值，这新值对其他线程来说是 立即可见的。
- 禁止进行指令重排序。

- 作用范围
    - volatile 仅能使用在变量级别
    - synchronized 则可以使用在变量、方法、和类级别的
- 原子性
    - volatile 仅能实现变量的修改可见性，并不能保证原子性
    - synchronized 则可以保证变量的修改可见性和原子性
- 阻塞
    - volatile 不会造成线程的阻塞
    - synchronized 可能会造成线程的阻塞
- 优化
    - volatile 标记的变量不会被编译器优化
    - synchronized 标记的变量可以被编译器优化
### 输出
```java
/**
 * @USER: lynn
 * @DATE: 2020/4/23
 **/
public class VolatileTest {
    public static void main(String[] args) {
        final Counter counter = new Counter();
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    counter.inc();
                }
            }).start();
        }
        System.out.println(counter);
    }
}
class Counter {
    private volatile int count = 0;
    public void inc() {
        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count++;
    }
    @Override
    public String toString() {
        return "[count=" + count + "]";
    }
}
```
- 在 java 的内存模型中每一个线程运行时都有一个线程栈，线程栈保存了线程运行时候变量值信息。当线程访问某 一个对象时候值的时候，首先通过对象的引用找到对应在堆内存的变量的值，然后把堆内存变量的具体值 load 到线程 本地内存中，建立一个变量副本，之后线程就不再和对象在堆内存变量值有任何关系，而是直接修改副本变量的值，在 修改完之后的某一个时刻(线程退出之前)，自动把线程变量副本的值回写到对象在堆中变量。这样在堆中的对象的值 就产生变化了。
  也就是说上面主函数中开启了 1000 个子线程，每个线程都有一个变量副本，每个线程修改变量只是临时修改了 自己的副本，当线程结束时再将修改的值写入在主内存中，这样就出现了线程安全问题。因此结果就不可能等于 1000 了，一般都会小于 1000。

### 什么是线程池，如何使用?
线程池就是事先将多个线程对象放到一个容器中，当使用的时候就不用 new 线程而是直接去池中拿线程即可，节省了开辟子线程的时间，提高的代码执行效率。
- 在 JDK 的 java.util.concurrent.Executors 中提供了生成多种线程池的静态方法。
```java
ExecutorService newCachedThreadPool = Executors.newCachedThreadPool(); 
ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(4);
ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(4);
ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
```
然后调用他们的 execute 方法即可。

### 常用的线程池有哪些?
- newSingleThreadExecutor:创建一个单线程的线程池，此线程池保证所有任务的执行顺序按照任务的提交顺序执行。
- newFixedThreadPool:创建固定大小的线程池，每次提交一个任务就创建一个线程，直到线程达到线程池的最大 大小。
- newCachedThreadPool:创建一个可缓存的线程池，此线程池不会对线程池大小做限制，线程池大小完全依赖于操作系统(或者说 JVM)能够创建的最大线程大小。
- newScheduledThreadPool:创建一个大小无限的线程池，此线程池支持定时以及周期性执行任务的需求。

### 请叙述一下您对线程池的理解?
合理利用线程池能够带来三个好处。
- 降低资源消耗。通过重复利用已创建的线程降低线程创建和销毁造成的消耗。
- 提高响应速度。当任务到达时，任务可以不需要等到线程创建就能立即执行。
- 提高线程的可管理性。线程是稀缺资源，如果无限制的创建，不仅会消耗系统资源，还会降低系统的稳定性，使用线程池可以进行统一的分配，调优和监控。