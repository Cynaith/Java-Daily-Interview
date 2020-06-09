## Spring Boot 整合 cassandra

- 导入依赖  
  ```xml
    <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-cassandra -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-cassandra</artifactId>
        <version>2.2.5.RELEASE</version>
    </dependency>
  ```

- 配置application.yml  
  ```yaml
     spring:
       data:
         cassandra:
           cluster-name: Test Cluster
           keyspace-name: ifile
           contact-points: 106.12.130.1
           port: 9042
  ```
- 查看cluster-name  
  `select cluster_name from system.local;`
  
  