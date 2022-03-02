# Java

### ❤JDK8 新特性  
- Stream
- 函数式接口
- Optional
- 接口默认方法
- Lambda表达式

### ❤线程池怎么使用  
使用Executors类下的静态方法，实质上是调用返回ThreadPoolExecutor类的实例。  
- ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) 
    - corePoolSize 核心线程池大小。
    - maximumPoolSize 最大线程池大小。
    - keepAliveTime 和 unit 线程空闲后的存活时间。
    - workQueue 用于存放任务的阻塞队列。
    - handler 当队列和最大线程池都满了之后的饱和策略。
    
- workQueue的选择
    - 无界队列
        - LinkedBlockingQueue(任务耗时较长时可能会导致大量新任务在队列中堆积最终导致OOM)
    - 有界队列
        - FIFO原则: ArrayBlockingQueue
        - 优先级队列: PriorityBlockingQueue
### 接口与抽象类
- 略
- 接口负责定义public属性和方法，并且声明与其他对象的依赖关系，抽象类负责公共构造部分的实现，实现类准确的实现业务逻辑，同时在适当的时候对父类进行细化。

### ❤线程的五种状态
- 开始状态（new）、就绪状态（runnable）、运行状态（running）、阻塞状态（blocked）、结束状态（dead）
1. 线程刚创建 --new状态
2. 调用start()方法，进入就绪状态。与其他线程竞争cpu资源
3. 当该线程竞争到了cpu资源，进入running状态
4. 线程因为某种原因放弃CPU使用权，暂时停止运行。直到线程进入就绪状态之间处于blocked状态
    1. 等待阻塞：运行的线程执行wait()方法，该线程会释放占用的所有资源，JVM会把该线程放入“等待池”中，
      进入这个状态后，是不能自动唤醒的，必须依靠其他线程调用notify()或notifyAll()方法才能被唤醒
    2. 同步阻塞：运行的线程在获取对象的同步锁时，若该同步锁被别的线程占用，则JVM会把该线程放入“锁池”中。
    3. 其他阻塞：运行的线程执行sleep()或join()方法，或者发出了I/O请求时，JVM会把该线程置为阻塞状态。
    当sleep()状态超时、join()等待线程终止或者超时、或者I/O处理完毕时，线程重新转入就绪状态。
5. 当线程正常执行结束会进入dead状态（一个未捕获的异常也会使线程终止）

注:
- yield()只是使当前线程重新回到runnable状态
- sleep()会让出cpu，不会释放锁
- join()会让出cpu，释放锁
- wait() 和 notify() 方法与suspend()和 resume()的区别在于wait会释放锁，suspend不会释放锁
- wait() 和 notify()只能运行在Synchronized代码块中，因为wait()需要释放锁，如果不在同步代码块中，就无锁可以释放
- 当线程调用wait()方法后会进入等待队列（进入这个状态会释放所占有的所有资源，与阻塞状态不同）,
进入这个状态后，是不能自动唤醒的，必须依靠其他线程调用notify()或notifyAll()方法才能被唤醒
### Error和Exception

### final、finalize、finally

### Heap和Stack

### Arrays.sort用的排序  
双轴快排
### synchronized和Lock的区别
synchronized是jvm层面的锁，不需要主动释放锁
Lock是具体类，需要主动释放锁
### Lock底层

### synchronized底层

### 怎么循环获取Map里的值

### 怎么遍历ArrayList

### 介绍集合类


