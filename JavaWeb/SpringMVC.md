## Spring MVC  

### Spring MVC 用DispatcherServlet处理请求  
#### Dispatcherervlet中默认的Bean
- HandlerMapping
    - **处理器映射**
    - 会根据规则将进入容器的请求映射到具体的处理器
- HandlerAdapter
    - **处理器适配器**
    - 获得请求对应的处理器后，适配器负责调用该处理器
- HandlerExceptionResolver
    - **处理器异常解析器**
    - 将捕获的异常映射到不同的视图上
- ViewResolver
    - **视图解析器**
    - 将代表逻辑视图名称的字符串，映射到实际视图类型View上
- LocalResolver & LocalContextResolver
    - **地区解析器 和 地区上下文解析器**
    - 负责解析客户端所在的地区和时间信息
- ThemeResolver
    - **主题解析器**
    - 负责解析主题
- MultipartResolver
    - **解析multi-part的传输请求**
- FlashMapManager
    - **FlashMap管理器**
    - FlashMap : 能够做到重定向后隐藏传送参数

#### DispatcherServlet处理流程
当有请求经过DispatcherServlet时
- 先搜索应用的上下文对象WebApplicationContext，并当作属性绑定到该请求上
- 将地区、主题解析器绑定到请求上
- 如配置multipart文件处理器，框架会查找文件是否为多部连续上传的。若是则包装成MultipartHttpServletRequest
- 为该请求查找一个合适的处理器
- 如果处理器返回一个模型，那么框架将先渲染视图


1. 用户发送请求至前端控制器DispatcherServlet
2. DispatcherServlet收到请求后根据HandlerMapping找到对应控制器
3. DispatcherServlet通过HandlerAdapter调用对应控制器
4. Controller执行完成后返回ModelAndView
5. HandlerAdapter将执行结果返回至DispatcherServlet
6. DispatcherServlet将ModelAndView传给ViewResolver视图解析器
7. DispatcherServlet将视图解析器返回的View渲染视图并返回给用户