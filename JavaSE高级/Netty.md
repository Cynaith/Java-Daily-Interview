## Netty
### 组件
- NioEventLoop
    - 底层封装了Selector，实现多路复用，由唯一绑定的线程去进行三大步骤循环操作：
    监听事件、处理事件、执行事件。
    - 监听端口，每一个socket都会注册到一个EventLoop上
- NioServerSocketChannel NioSocketChannel
    - 一个是服务端通道，一个是客户端通道。
    接收客户端链接和处理数据读写都是通过通道进行的。
- ChannelPipeline
    - 每个Channel都与唯一的一个Pipeline关联。当Channel读取到数据以后，后续的具体操作都交给管道Pipeline去进行。
- Handler
    - 在ChannelPipeline中可以放入用户自定义的Handler，用于处理具体的业务处理，这里面也包括解码器和编码器这两种重要的Handler。

### 粘包和拆包
tcp是流式套接字，这个就是造成了收到的内容和传输的的断句是不同的。
这个可以类比古代没有标点，断句就可能有多种的变化。例如“没有鸡鸭也可以"这句。
你可能收到是的没有鸡鸭也可以，也可能收到的是没有鸡，然后又收到鸭也可以。
粘包说的是两次发送的一次收到了，拆包则是一次发送的，分两次收到。

- Netty处理
    - 自定义分隔符
        - `channel.pipeline().addLast(new DelimiterBasedFrameDecoder(10000, Unpooled.copiedBuffer("$".getBytes())));`
    - 定长解析器
        - `channel.pipeline().addLast(new FixedLengthFrameDecoder(1));`