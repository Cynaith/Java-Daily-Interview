# 深入理解synchronized
https://zhuanlan.zhihu.com/p/150791816
### synchronized使用场景  
1. 修饰代码块，给对象加锁
2. 修饰静态方法，对当前类的Class对象加锁
3. 修饰普通方法，对当前实例对象this加锁
### JVM中，对象在内存中的布局
JVM中，对象在内存中分三块区域:对象头、实例数据、对齐填充

- 对象头
    - Mark Word(Mark Oop)  
      存储对象的HashCode、分代年龄、锁标志位信息。
    - 类元信息  
      对象指向它的类元数据的指针，虚拟机通过这个指针来确定这个对象是哪个类的实例。
- 实例数据  
  这部分主要是存放类的数据信息，父类的信息
- 对齐填充

### synchronized底层(基于字节码)
在进入同步代码前先获取锁，获取后锁计数器+1，执行完后-1;获取失败就阻塞式等待锁的释放。

- 同步方法  
  通过方法flags标志控制同步，方法的flags里多了一个ACC_SYNCHRONIZED标志，用来告诉jvm该方法是同步方法。
- 同步代码块  
  由monitorenter指令进入，monitorexit释放锁。  
  **注**: 字节码中会存在两个monitorexit指令，由编译器自动生成，会在发生异常时释放锁。
### monitor  
monitor是一个监视器，底层源码由C++编写
### synchronized底层源码

### 锁优化  
  JDK1.6之前，synchronized是重量级锁，之后引入了偏向锁和轻量级锁的概念。  
 
  无锁状态--->偏向锁状态--->轻量级锁状态--->重量级锁状态 (锁升级的状态是不可逆的)
  
### 与Lock区别
- synchronized是关键字，Lock是类
- Lock可以判断是否可以获取到锁
- synchronized可以自动释放锁
- 使用Lock锁，线程不会一直阻塞
- synchronized的锁可重入、不可中断、非公平，而Lock锁可重入、可判断、可公平（两者皆可）
- Lock锁适合大量同步的代码的同步问题，synchronized锁适合代码少量的同步问题。