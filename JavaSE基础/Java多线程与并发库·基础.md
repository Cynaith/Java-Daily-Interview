## Java多线程与并发库·基础
### Timer和TimerTask
使用定时器,间隔 4 秒执行一次，再间隔 2 秒执行一次，以此类推执行。
```java
class TimerTastCus extends TimerTask{
    @Override
    public void run() {
    count = (count +1)%2;
    System.err.println("Boob boom ");
    new Timer().schedule(new TimerTastCus(), 2000+2000*count);
    } 
}
Timer timer = new Timer();
timer.schedule(new TimerTastCus(), 2000+2000*count);
```
### 线程互斥与同步
> 在引入多线程后，由于线程的异步性，会使系统造成混乱。
- 间接相互制约(互斥)
    - 对于共享资源，一个线程执行，其他线程都要等待。
- 直接相互制约(同步)
    - 如果B线程的执行前提是A线程执行完，那么线程B在A执行完之前都将处于堵塞。
    
### 线程局部变量ThreadLocal
> 用于实现线程内的数据共享，即对于相同的程序代码，多个模块在同一个 线程中运行时要共享一份数据，而在另外线程中运行时又共享另外一份数据。
> <br/> 减少线程内数据传递复杂度

- 每个线程调用全局 ThreadLocal 对象的 set 方法，在 set 方法中，首先根据当前线程获取当前线程的 ThreadLocalMap 对象，
然后往这个 map 中插入一条记录，key 其实是 ThreadLocal 对象，value 是各自的 set 方法传进去的值。
也就是每个线程其实都有一份自己独享的 ThreadLocalMap 对象，该对象的 Key 是 ThreadLocal 对象，值是用户设置的具体值。
在线程结束时可以调用 ThreadLocal.remove()方法，这样会更快释放内存，不调用也可以，因为线程结束后也可以自动释放相关的 ThreadLocal 变量。

- ThreadLocal 的应用场景:
    - 订单处理包含一系列操作:减少库存量、增加一条流水台账、修改总账，这几个操作要在同一个 事务中完成，通常也即同一个线程中进行处理，
    如果累加公司应收款的操作失败了，则应该把前面 的操作回滚，否则，提交所有操作，这要求这些操作使用相同的数据库连接对象，而这些操作的代码分别位于不同的模块类中。
    - 银行转账包含一系列操作: 把转出帐户的余额减少，把转入帐户的余额增加，这两个操作要在 同一个事务中完成，它们必须使用相同的数据库连接对象，
    转入和转出操作的代码分别是两个不同 的帐户对象的方法。