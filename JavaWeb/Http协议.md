## Http协议
### 常见的Http请求头
- **Accept-Charset** 用于指定客户端接受的字符集
- **Accept-Encoding** 用于指定可接受的内容编码，如Accept-Encoding:gzip.deflate
- **Accept-Language** 用于指定一种自然语言，如Accept-Language:zn-cn
- **Host** 用于指定被请求资源的主机和端口号
- **User-Agent** 客户端将它的操作系统、浏览器、和其他属性告诉服务器
- **Connection** 当前连接是否保持，如Connection：Keep-Alive
### 常见Http响应头
- **Server** 使用的服务器名称，如Server:Apache/1.3.6(Unix)
- **Content-Type** 用来指明发送给接收者的实体正文的媒体类型，如Content-Type:text/html;charset=GBK
- **Content-Encoding** 与请求头的Accept-Encoding对应，告诉浏览器服务端采用的是什么压缩编码
- **Content-Language** 描述资源所用的自然语言，与Accept-Language对应
- **Content-Length** 指明实体正文的长度，用以字节方式存储的十进制数字来表示
- **Keep-Alive** 保持廉洁的时间，如Keep-Alive：timeout=5，max=120
### Http的长连接和短连接
- Http/1.0默认短连接
    - 浏览器与服务器每进行一次HTTP操作，就建立一次连接，任务结束就中断连接
- Http/1.1默认长连接
    - 数据传输完成了保持 TCP 连接不断开(不发 RST 包、不四次握手)，等待在同域名下继续用这个通道传输数据
- 区别
    - 可扩展性
        - 在消息中增加版本号，用于兼容性判断。
        - HTTP/1.1 增加了 OPTIONS 方法，它允许客户端获取一个服务器支持的方法列表。
        - 为了与未来的协议规范兼容，HTTP/1.1 在请求消息中包含了 Upgrade 头域，通过该头域，
        客户端可以让服务器知道它能够支持的其它备用通信协议，服务器可以据此进行协议切换，使用备用协议与客户端进行通信。
    - 缓存
        - 在 HTTP/1.0 中，使用 Expire 头域来判断资源的 fresh 或 stale，并使用条件请求(conditional request)来判 断资源是否仍有效。HTTP/1.1 在 1.0 的基础上加入了一些 cache 的新特性，当缓存对象的 Age 超过 Expire 时变为 stale 对象，cache 不需要直接抛弃 stale 对象，而是与源服务器进行重新激活(revalidation)。
    - 带宽优化
        - HTTP/1.0 中，存在一些浪费带宽的现象，例如客户端只是需要某个对象的一部分，而服务器却将整个对象送过来 了。例如，客户端只需要显示一个文档的部分内容，又比如下载大文件时需要支持断点续传功能，而不是在发生断连后 不得不重新下载完整的包。
        - HTTP/1.1 中在请求消息中引入了 range 头域，它允许只请求资源的某个部分。在响应消息中 Content-Range 头 域声明了返回的这部分对象的偏移值和长度。如果服务器相应地返回了对象所请求范围的内容，则响应码为 206 (Partial Content)，它可以防止 Cache 将响应误以为是完整的一个对象。
        
### Http常见的状态码有哪些?
- 200 OK
- 301 Moved Permanently(永久移除)，请求的 URL 已移走。Response 中应该包含一个 Location URL, 说明资源现在所处的位置。
- 302 found 重定向        
- 400 Bad Request //客户端请求有语法错误，不能被服务器所理解
- 401 Unauthorized //请求未经授权，这个状态代码必须和 WWW-Authenticate 报头域一起使用
- 403 Forbidden //服务器收到请求，但是拒绝提供服务
- 404 Not Found //请求资源不存在，eg:输入了错误的 URL
- 500 Internal Server Error //服务器发生不可预期的错误
- 503 Server Unavailable //服务器当前不能处理客户端的请求，一段时间后可能恢复正常
### Get和Post的区别?
- 数据位置
    - Get会将数据附在URL后
    - Post会把数据放置在HTTP包的包体中。
- 数据大小
    - Get方式通过URL提交数据，那么Get提交的长度与URL长度有关
        - IE :2083 
        - firefox  :65536
        - chrome :8182
        - Safari :至少80000
        - Opera :190000
        - Apache :8192
        - IIS :16382
        - Perl HTTP::Daemon :16384
        - nginx : 可以通过配置修改
### Http中重定向与请求转发的区别
转发是服务器行为；重定向是客户端行为
- 重定向
    - 两次请求，浏览器地址发生改变，可以访问自己web之外的资源，传输的数据会丢失
- 请求转发
    - 一次请求，浏览器地址不变，访问的是自己本身的web资源，传输的数据不会丢失。
