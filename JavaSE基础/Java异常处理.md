## Java异常处理
### Java中异常分哪些种
按照异常需要处理的时机分为编译时异常(也叫强制性异常)也叫 CheckedException 和运行时异常 (也叫非强制性异常)也叫 RuntimeException。
只有 java 语言提供了 Checked 异常，Java 认为 Checked 异常都是可以被处理的异常，所以 Java 程序必须显式处理 Checked 异常。
如果程序没有处理 Checked 异 常，该程序在编译时就会发生错误无法编译。这体现了 Java 的设计哲学:没有完善错误处理的代码根本没有机会被执行。
<br/>
- 处理Checked的两种方法
    - 当前方法知道如何处理该异常，则用 try...catch 块来处理该异常。
    - 当前方法不知道如何处理，则在定义该方法是声明抛出该异常。
运行时异常只有当代码在运行时才发行的异常，编译时不需要 try catch。
Runtime如除数是0和数组下标越界等，其产生频繁，处理麻烦，若显示申明或者捕获将会对程序的可读性和运行效率影响很大。
所以由系统自动检 测并将它们交给缺省的异常处理程序。当然如果你有处理要求也可以显示捕获它们。

### 调用下面的方法，得到的返回值是什么?
```java
public int getNUm(){
    try {
        int a = 1/0;
        return 1;
    }catch (Exception e) {
        return 2;
    }finally{
        return 3; }
    }
}
```
代码在走到第 3 行的时候遇到了一个 MathException，这时第四行的代码就不会执行了，
代码直接跳转到 catch 语句中，走到第 6 行的时候，异常机制有这么一个原则如果在 catch 中遇到了 return 或者异常等能使该函数终止的话 
那么有 finally 就必须先执行完 finally 代码块里面的代码然后再返回值。因此代码又跳到第 8 行，可惜第 8 行是一个 return 语句，那么这个时候方法就结束了，因此第 6 行的返回结果就无法被真正返回。
如果 finally 仅仅是处理了一个 释放资源的操作，那么该道题最终返回的结果就是 2。因此上面返回值是 3。

### error 和 exception 的区别?
Error 类一般是指与虚拟机相关的问题，如系统崩溃，虚拟机错误，内存空间不足，方法调用栈溢出等。
对于这类 错误的导致的应用程序中断，仅靠程序本身无法恢复和和预防，遇到这样的错误，建议让程序终止。
<br/>
Exception 类表示程序可以处理的异常，可以捕获且可能恢复。遇到这类异常，应该尽可能处理异常，使程序恢复 运行，而不应该随意终止异常。

### java异常处理机制
- Error
    - 表示应用程序本身无法克服和恢复的一种严重问题。
- Exception
    - 系统异常
    系统异常是软件本身缺陷所导致的 问题，也就是软件开发人员考虑不周所导致的问题，软件使用者无法克服和恢复这种问题，
    但在这种问题下还可以让 软件系统继续运行或者让软件死掉，例如，数组脚本越界(ArrayIndexOutOfBoundsException)，空指针异常 (NullPointerException)、类转换异常(ClassCastException)。
    - 普通异常
    普通异常是运行环境的变化或异常所导致的问题， 是用户能够克服的问题，例如，网络断线，硬盘空间不够，发生这样的异常后，程序不应该死掉。
java 为系统异常和普通异常提供了不同的解决方案，编译器强制普通异常必须 try..catch 处理或用 throws 声明继续抛给上层调用方法处理，所以普通异常也称为 checked 异常，
而系统异常可以处理也可以不处理，所以，编译器不强制用 try..catch 处理或用 throws 声明，所以系统异常也称为 unchecked 异常。

### 常见的RuntimeException
- java.lang.NullPointerException
空指针异常
> 原因:调用了未初始化的对象或是不存在的对象
- java.lang.ClassNotFoundException
指定的类找不到
> 原因:类的名称和路径加载错误;通常都是程序试图通过字符串来加载某个类时可能引发异常。
- java.lang.NumberFormatException
字符串转换为数字异常
> 原因:字符型数据中包含非数字型字符。
- java.lang.IndexOutOfBoundsException
数组角标越界异常
> 常见于操作数组对象时发生
- java.lang.IllegalArgumentException
方法传递参数错误
- java.lang.ClassCastException
数据类型转换异常
- java.lang.NoClassDefFoundException
未找到类定义错误
- SQLException
SQL异常
> 常见于操作数据库时的SQL语句错误
- java.lang.InstantiationException
实例化异常
- java.lang.NoSuchMethodException
方法不存在异常

### throw和throws区别
- throw
   - throw 语句用在方法体内，表示抛出异常，由方法体内的语句处理。
   - throw 是具体向外抛出异常的动作，所以它抛出的是一个异常实例，执行 throw 一定是抛出了某种异常。
- throws
    - throws 语句是用在方法声明后面，表示如果抛出异常，由该方法的调用者来进行异常的处理。
    - throws 表示出现异常的一种可能性，并不一定会发生这种异常。

### final、finally、finalize 的区别?
**final:**  用于声明属性，方法和类，分别表示属性不可变，方法不可覆盖，被其修饰的类不可继承。
**finally:** 异常处理语句结构的一部分，表示总是执行。
**finalize:** Object 类的一个方法，在垃圾回收器执行的时候会调用被回收对象的此方法，可以覆盖此方法提供垃圾收集时的其他资源回收，例如关闭文件等。该方法更像是一个对象生命周期的临终方法，当该方法 被系统调用则代表该对象即将“死亡”，但是需要注意的是，我们主动行为上去调用该方法并不会导致该对 象“死亡”，这是一个被动的方法(其实就是回调方法)，不需要我们调用。

