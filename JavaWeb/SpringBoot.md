## Spring Boot
### Spring Boot有哪些优点
1. 减少开发，测试时间
2. 使用JavaConfig避免使用XML
3. 避免大量Maven导入和各种版本冲突
4. 通过提供默认值快速开发
5. 没有单独的Web服务器需要，不需要启动Tomcat
6. 需要更少的配置(因为没有web.xml)。只需添加@Configuration注释的类，
然后添加用@Bean注释的方法，Spring将自动加载对象并像以前一样对其进行管理。

### Spring Boot核心注解
- @SpringBootConfiguration
    - 组合了@Configuration竹节，实现配置文件功能
- @EnableAutoConfiguration
    - 打开自动配置功能，也可以关闭某个自动配置的选项，如关闭数据源自动配置功能
    `@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })`
- @ComponentScan
    - Spring组件扫描

### Spring Boot 怎么读取配置
- @PropertySource
- @Value
- @Environment
- @ConfigurationProperties

### Spring Boot2.x 新特性
- 配置变更
- JDK版本升级
- 第三方类库升级
- 响应式Spring编程支持
- 配置属性绑定

### Spring Boot、Spring MVC、Spring 区别
- Spring
    - 依赖注入
- Spring MVC 
    - Spring MVC提供分离式的方式开发Web应用，通过运用像DispatchServlet、
    ModelAndView和ViewResolver等一些简单的概念，使开发Web应用变得简单。
- Spring Boot
    - Spring 和 Spring MVC 的问题在于需要配置大量参数
    - Spring Boot通过自动配置来解决此问题。

### Spring Boot 配置加载顺序  
(先加载properties文件)
1. 当前项目目录下的一个/config子目录
2. 当前项目目录
3. 项目的resources即一个classpath下的/config包
4. 项目的resources即classpath根路径（root）
   
- 配置的属性使用最先读取到的
- 创建SpringBoot项目时，一般的配置文件放置在项目的resources目录下，因为配置文件的修改，通过热部署不用重新启动项目，而热部署的作用范围是classpath下

### Spring Boot 热部署  
- 使用springloaded配置pom.xml文件，使用mvn spring-boot:run启动
- 使用springloaded本地加载启动，配置jvm参数 -javaagent:<jar包地址> -noverify
- 使用devtools工具包，操作简单，但是每次需要重新部署                               

### 热部署底层原理  
底层使用两个ClassLoader，一个ClassLoader加载不会改变的类(三方jar包)，另一个ClassLoader加载会更改的类(称为restart ClassLoader)

### Spring Boot安全设计
- 在生产中使用HTTPS
- 使用Snyk检查依赖关系
- 升级到最新版本
- 启用CSRF保护
- 使用内容安全策略防止XSS攻击
- 使用OpenID Connect进行身份验证
- 管理密码？使用密码哈希！
- 安全地存储秘密
- 使用OWASP的ZAP测试您的应用程序

### Spring Boot自动配置原理
#### SpringApplication的run方法  
首先开启一个SpringApplicationRunListeners监听器，然后创建应用上下文ConfigurableApplicationContext，
并通过该上下文加载应用所需的类和环境配置，最后启用一个应用实例。
```java
public ConfigurableApplicationContext run(String... args) {
//        用于记录时间
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ConfigurableApplicationContext context = null;
        自定义SpringApplication启动错误的回调接口
        Collection<SpringBootExceptionReporter> exceptionReporters = new ArrayList();
//        设置jdk系统属性java.awt.headless
        this.configureHeadlessProperty();
//        获取启动时监听器实例
        SpringApplicationRunListeners listeners = this.getRunListeners(args);
        listeners.starting();

        Collection exceptionReporters;
        try {
//            参数封装，也就是在命令行下启动应用带的参数，如--server.port=9000
            ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
//            准备环境：1、加载外部化配置的资源到environment；2、触发ApplicationEnvironmentPreparedEvent事件
            ConfigurableEnvironment environment = this.prepareEnvironment(listeners, applicationArguments);
            this.configureIgnoreBeanInfo(environment);
            Banner printedBanner = this.printBanner(environment);
//            创建应用上下文，并实例化了其三个属性：reader、scanner和beanFactory
            context = this.createApplicationContext();
//             获取异常报道器，即加载spring.factories中的SpringBootExceptionReporter实现类
            exceptionReporters = this.getSpringFactoriesInstances(SpringBootExceptionReporter.class, new Class[]{ConfigurableApplicationContext.class}, context);
//            准备上下文
            this.prepareContext(context, environment, listeners, applicationArguments, printedBanner);
            this.refreshContext(context);
            this.afterRefresh(context, applicationArguments);
            stopWatch.stop();
            if (this.logStartupInfo) {
                (new StartupInfoLogger(this.mainApplicationClass)).logStarted(this.getApplicationLog(), stopWatch);
            }

            listeners.started(context);
            this.callRunners(context, applicationArguments);
        } catch (Throwable var10) {
            this.handleRunFailure(context, var10, exceptionReporters, listeners);
            throw new IllegalStateException(var10);
        }

        try {
            listeners.running(context);
            return context;
        } catch (Throwable var9) {
            this.handleRunFailure(context, var9, exceptionReporters, (SpringApplicationRunListeners)null);
            throw new IllegalStateException(var9);
        }
    }
```
#### 创建应用上下文  
上文通过调用this.createApplicationContext方法来获取应用上下文。
```java
protected ConfigurableApplicationContext createApplicationContext() {
        Class<?> contextClass = this.applicationContextClass;
        if (contextClass == null) {
            try {
                switch(this.webApplicationType) {
                case SERVLET:
                    contextClass = Class.forName("org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext");
                    break;
                case REACTIVE:
                    contextClass = Class.forName("org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext");
                    break;
                default:
                    contextClass = Class.forName("org.springframework.context.annotation.AnnotationConfigApplicationContext");
                }
            } catch (ClassNotFoundException var3) {
                throw new IllegalStateException("Unable create a default ApplicationContext, please specify an ApplicationContextClass", var3);
            }
        }

        return (ConfigurableApplicationContext)BeanUtils.instantiateClass(contextClass);
    }
```
该方法会根据推断的应用类型实例化不同的ApplicationContext。

**准备上下文**  
```java
private void prepareContext(ConfigurableApplicationContext context, ConfigurableEnvironment environment, SpringApplicationRunListeners listeners, ApplicationArguments applicationArguments, Banner printedBanner) {
//        设置上下文的environment
        context.setEnvironment(environment);
//        应用上下文处理
        this.postProcessApplicationContext(context);
        在context refresh之前，对其应用ApplicationContextInitializer
        this.applyInitializers(context);
//        上下文准备
        listeners.contextPrepared(context);
//        打印启动日志和启动应用的Profile
        if (this.logStartupInfo) {
            this.logStartupInfo(context.getParent() == null);
            this.logStartupProfileInfo(context);
        }
//        获取BeanFactory
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
//        注册单例Bean，命令行参数bean
        beanFactory.registerSingleton("springApplicationArguments", applicationArguments);
        if (printedBanner != null) {
//        注册banner bean
            beanFactory.registerSingleton("springBootBanner", printedBanner);
        }

        if (beanFactory instanceof DefaultListableBeanFactory) {
            ((DefaultListableBeanFactory)beanFactory).setAllowBeanDefinitionOverriding(this.allowBeanDefinitionOverriding);
        }

        if (this.lazyInitialization) {
            context.addBeanFactoryPostProcessor(new LazyInitializationBeanFactoryPostProcessor());
        }

        Set<Object> sources = this.getAllSources();
        Assert.notEmpty(sources, "Sources must not be empty");
//        将bean加载至应用上下文中
        this.load(context, sources.toArray(new Object[0]));
//        向上下文中添加ApplicationListener，并广播ApplicationPreparedEvent事件
        listeners.contextLoaded(context);
    }
```
prepareContext:  
1. 将context中的environment替换成SpringApplication中创建的environment
2. 将SpringApplication中的initializers应用到context中
3. 加载两个单例bean到beanFactory中
    1. 向beanFactory中注册了一个名叫springApplicationArguments的单例bean，该bean封装了我们的命令行参数；
    2. 向beanFactory中注册了一个名叫springBootBanner的单例bean。
4. 加载bean定义资源
5. 将SpringApplication中的listeners注册到context中，并广播ApplicationPreparedEvent事件  

**刷新应用上下文**  

AbstractApplicationContext实现了ConfigurableApplicationContext接口，而ConfigurableApplicationContext接口继承自ApplicationContext，所以ApplicationContext能被强制转换成AbstractApplicationContext。
而refresh方法会在哪个类中被调用取决于创建ApplicationContext时的应用环境，若是servlet应用，会调用ServletWebServerApplicationContext的refresh方法；是reactive应用，则会调用ReactiveWebServerApplicationContext的refresh方法。
它们的refresh方法又会调用共有的父类GenericReactiveWebApplicationContext的refresh方法，从而进入spring-framework的刷新流程。
