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

### monitor 

### synchronized底层源码

### 锁优化