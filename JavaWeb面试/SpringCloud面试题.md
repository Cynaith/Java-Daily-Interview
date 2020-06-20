## Spring Cloud面试题

### 什么是Spring Cloud  
Spring Cloud是微服务系统架构的一站式解决方案。Spring Cloud离不开Spring Boot，
使我们能在Spring Boot的基础上轻松地实现微服务项目的构建

### Spring Cloud有什么优势  
使用 Spring Boot 开发分布式微服务时，我们面临以下问题：
- 与分布式系统相关的复杂性-这种开销包括网络问题，延迟开销，带宽问题，安全问题。
- 服务发现-服务发现工具管理群集中的流程和服务如何查找和互相交谈。它涉及一个服务目录，在该目录中注册服务，然后能够查找并连接到该目录中的服务。
- 冗余-分布式系统中的冗余问题。
- 负载平衡 --负载平衡改善跨多个计算资源的工作负荷，诸如计算机，计算机集群，网络链路，中央 处理单元，或磁盘驱动器的分布。
- 性能-问题 由于各种运营开销导致的性能问题。
- 部署复杂性-Devops 技能的要求。

### 服务注册和发现是什么意思？
Eureka就是一个服务发现框架。
- 服务注册  
当Eureka客户端向Eureka Server注册时，它提供自身的元数据(IP地址、端口、运行状况指示符URL、主页等)。
- 服务发现  
一个注册中心来记录分布式系统中的全部服务的信息，以便其他服务能够快速的找到这些已注册的服务。

### 负载平衡の意义是什么  
优化资源使用、最大化吞吐量、最小化响应时间、避免单一资源的过载。  
通过冗余来提高可靠性和可用性。

### 什么是Hystrix 熔断器？  
是一个为了防止服务血崩、提高整个系统的弹性、能够进行熔断和降级的库。  

例如可以使用@HystrixCommand注解来标注某个方法，每当调用时间超过指定时间时，断路器会中断对这个方法的调用。
- 降级  
为了更好的用户体验，当一个方法调用异常时，通过执行另一种代码逻辑来给用户友好的回复。可以通过设置`fallbackMethod`来给方法设置备用的代码逻辑。

### 负载均衡Ribbon  
- RestTemplate(RestTemplate是Spring提供的一个访问Http服务的客户端类)  
  ```java
  @Autowired
  private RestTemplate restTemplate;
  // 这里是提供者A的ip地址，但是如果使用了 Eureka 那么就应该是提供者A的名称
  private static final String SERVICE_PROVIDER_A = "http://localhost:8081";

  @PostMapping("/judge")
  public boolean judge(@RequestBody Request request) {
      String url = SERVICE_PROVIDER_A + "/service1";
      return restTemplate.postForObject(url, request, Boolean.class);
  }
  ```
  Eureka中的注册、续约等，底层都是使用RestTemplate

- Ribbon有什么用  
是Netflix公司开源的负载均衡项目，**运行在消费者端**。
    - 与Nginx区别  
      Nginx先将请求集中起来，然后通过负载均衡器进行负载均衡。  
      Ribbon先在客户端进行负载均衡才进行请求。

- Ribbon负载均衡算法
    - RoundRobinRule(默认) : 轮询策略
      若经过一轮轮询没有找到可用的provider，其最多轮询10轮。若最终还没有找到，则返回null。
    - RandomRule : 随机策略
      从所有可用的provider中随机选择一个。
    - RetryRule : 重试策略
      先按照RoundRobinRule策略获取provider，若获取失败，则在指定的时限内重试。默认的时限为500毫秒。

### Open Feign  
运行在消费者端，内置Ribbon。  
```java
// 使用 @FeignClient 注解来指定提供者的名字
@FeignClient(value = "eureka-client-provider")
public interface TestClient {
    // 这里一定要注意需要使用的是提供者那端的请求相对路径，这里就相当于映射了
    @RequestMapping(value = "/provider/xxx",
    method = RequestMethod.POST)
    CommonResponse<List<Plan>> getPlans(@RequestBody planGetRequest request);
}
```
在Controller可以像原来调用Service层代码一样调用它。
```java
@RestController
public class TestController {
    // 这里就相当于原来自动注入的 Service
    @Autowired
    private TestClient testClient;
    // controller 调用 service 层代码
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public CommonResponse<List<Plan>> get(@RequestBody planGetRequest request) {
        return testClient.getPlans(request);
    }
}
```

### Zuul
Zuul是从设备和 web 站点到 Netflix 流应用后端的所有请求的前门。

- 服务名屏蔽
  ```yaml
  zuul:
    ignore-services: "*"
  ```
- 路径屏蔽
  屏蔽掉的路径 URI，即只要用户请求中包含指定的 URI 路径，那么该请求将无法访问到指定的服务。通过该方式可以限制用户的权限。
  ```yaml
  zuul:
    ignore-patterns: **/auto/**
  ```
- 令牌桶限流
  ```java
  package com.lgq.zuul.filter;
  
  import com.google.common.util.concurrent.RateLimiter;
  import com.netflix.zuul.ZuulFilter;
  import com.netflix.zuul.context.RequestContext;
  import com.netflix.zuul.exception.ZuulException;
  import lombok.extern.slf4j.Slf4j;
  import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
  import org.springframework.stereotype.Component;
  
  @Component
  @Slf4j
  public class RouteFilter extends ZuulFilter {
      // 定义一个令牌桶，每秒产生2个令牌，即每秒最多处理2个请求
      private static final RateLimiter RATE_LIMITER = RateLimiter.create(2);
      @Override
      public String filterType() {
          return FilterConstants.PRE_TYPE;
      }
  
      @Override
      public int filterOrder() {
          return -5;
      }
  
      @Override
      public Object run() throws ZuulException {
          log.info("放行");
          return null;
      }
  
      @Override
      public boolean shouldFilter() {
          RequestContext context = RequestContext.getCurrentContext();
          if(!RATE_LIMITER.tryAcquire()) {
              log.warn("访问量超载");
              // 指定当前请求未通过过滤
              context.setSendZuulResponse(false);
              // 向客户端返回响应码429，请求数量过多
              context.setResponseStatusCode(429);
              return false;
          }
          return true;
      }
  }
  ```
  这样就可以将请求数量控制在一秒钟两个。
  
### Spring Cloud配置管理——Config

### Spring Cloud Bus
