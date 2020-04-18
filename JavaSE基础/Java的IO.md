## Java的IO
### Java中有几种类型的流
- 按照流的方向
    - 输入流 inputStream
    - 输出流 outputStream
- 按照实现功能分
    - 节点流
        - 可以从或向一个特定的地方(节点)读写数据。如 FileReader
    - 处理流
        - 是对一个 已存在的流的连接和封装，通过所封装的流的功能调用实现数据读写。如 BufferedReader。
        处理流的构造方法总是要 带一个其他的流对象做参数。一个流对象经过其他流的多次包装，称为流的链接。
- 按照处理数据的单位
    - 字节流
        - 字节流继承于 InputStream 和 OutputStream
    - 字符流   
        - 字符流继承于 InputStreamReader 和 OutputStreamWriter 
### 如何将Java对象序列化到文件
在Java中能够被序列化的类必须先实现 Serializable 接口，该接口没有任何抽象方法只是起到一个标记作用。
```java
//对象输出流
ObjectOutputStream objectOutputStream = 
    new ObjectOutputStream(new FileOutputStream(new File("D://obj")));
objectOutputStream.writeObject(new User("zhangsan", 100));
objectOutputStream.close();

//对象输入流
ObjectInputStream objectInputStream =
    new ObjectInputStream(new FileInputStream(new File("D://obj")));
User user = (User)objectInputStream.readObject();
objectInputStream.close();
```

### 字节流和字符流的区别
> 字节流读取的时候，读到一个字节就返回一个字节; 
> 字符流使用了字节流读到一个或多个字节(中文对应的字节 数是两个，在 UTF-8 码表中是 3 个字节)时。
> 先去查指定的编码表，将查到的字符返回。

- 字节流
    - 可以处理所有类型数据，如图片、MP3、AVI视频文件
    - 字节流主要是操作 byte 类型数据，以 byte 数组为准，主要操作类就是 OutputStream、 InputStream
- 字符流
    - 只能处理字符数据
    - java 提供了 Reader、Writer 两个专门操作字符流的类
    
### 如何实现对象克隆
- 实现Cloneable接口并重写clone()方法
- 实现Serializable接口，通过对象的序列化和反序列化实现克隆，可以实现真正的深度克隆
```java
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
public class MyUtil {
    private MyUtil() {
        throw new AssertionError();
    }
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T clone(T obj) throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bout);
        oos.writeObject(obj);
        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bin);
        return (T) ois.readObject();
        // 说明:调用 ByteArrayInputStream 或 ByteArrayOutputStream 对象的 close 方法没有任何意义
        // 这两个基于内存的流只要垃圾回收器清理对象就能够释放资源，这一点不同于对外部资源(如文件流)的释放
    }
}
```

### 什么是序列化
- 序列化就是一种用来处理对象流的机制，所谓对象流也就是将对象的内容进行流化。
可以对流化后的对象进行读 写操作，也可将流化后的对象传输于网络之间。序列化是为了解决在对对象流进行读写操作时所引发的问题。