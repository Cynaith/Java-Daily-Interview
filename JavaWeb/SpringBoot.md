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
