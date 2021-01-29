# Spring
### Spring IOC  
IOC容器其实就是一个大工厂，它用来管理我们所有的对象以及依赖关系。  
- 通过反射获取类的信息
- 使用xml或注解来描述类之间的关系
- 构建对象之间的依赖关系

IOC实现对象之间的创建和依赖:
- 根据Bean配置信息，在Spring容器内部创建Bean定义注册表。
- 根据注册表加载、实例化Bean、建立Bean之间依赖关系
- 将Bean放入内存中的Map缓存池
### ❤依赖注入的方式
- 构造方法注入
- setter注入
- 基于注解的注入
    - @Autowired
    - @Resource 默认按照名称装配
### ❤Spring AOP

### Spring AOP能做什么

### 如何记录全局的接口日志
- 拦截器实现HandlerInterceptor接口
    - 在preHandle()中接受Object handler 转型为 HandlerMethod对象，调用handler.getBean().getClass().getName()获取被拦截的类信息。
    - 使用request.getRequestURI()获取uri
- 网关
- AOP
### Spring 声明事务的两种方式
- 注解式事务
- 编程式事务
### ❤怎么破坏事务 
