# MonitoringSystem
***
## 环境
- JDK 17
- Maven 3.9.5
- SpringCloudAlibaba 2022.0.0.0-RC2
- nacos 2.2.3
- SpringBoot 3.2.0
- Mysql 8.0
***



# 帮助

***

## 问题大全
1. 注意： SpringBoot 3.2.0 集成mybatisplus需要  mybatis-spring 3.0.X 版本，否则会报如下错误：
  Invalid value type for attribute 'factoryBeanObjectType'‘': java.lang.String    
    ```
    <!-- mybatis plus -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.5.5</version>
    </dependency>
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis-spring</artifactId>
        <version>3.0.3</version>
    </dependency>
    ```
2. 前后端交互 时间格式化的两种方法，本项目采用第一种方法解决
    ```
        1.在相应类的属性上使用注解格式化时间
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createData;
        
        2.在application.yml文件中指定配置
        spring:
          jackson:
            date-format: yyyy-MM-dd HH:mm:ss
            time-zone: GMT+8
    ```
3. @RestControllerAdvice注解，本项目用于配置全局异常处理GlobalExceptionHandler类。
    ```
    通过@ControllerAdvice注解可以将对于控制器的全局配置放在同一个位置。
    注解了@RestControllerAdvice的类的方法可以使用@ExceptionHandler、@InitBinder、@ModelAttribute注解到方法上。
    @RestControllerAdvice注解将作用在所有注解了@RequestMapping的控制器的方法上。
    @ExceptionHandler：用于指定异常处理方法。当与@RestControllerAdvice配合使用时，用于全局处理控制器里的异常。
    @InitBinder：用来设置WebDataBinder，用于自动绑定前台请求参数到Model中。
    @ModelAttribute：本来作用是绑定键值对到Model中，当与@ControllerAdvice配合使用时，可以让全局的@RequestMapping都能获得在此处设置的键值对
    ```
4. nacos2.2.3启动报错
    ```
    org.springframework.context.ApplicationContextException: Unable to start web server; nested exception is org.springframework.boot.web.server.WebServerException: Unable to start embedded Tomcat
        at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.onRefresh(ServletWebServerApplicationContext.java:163)
        at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:577)
        at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:145)
        at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:745)
        at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:423)
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:307)
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1317)
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1306)
        at com.alibaba.nacos.Nacos.main(Nacos.java:35)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:498)
        at org.springframework.boot.loader.MainMethodRunner.run(MainMethodRunner.java:49)
        at org.springframework.boot.loader.Launcher.launch(Launcher.java:108)
        at org.springframework.boot.loader.Launcher.launch(Launcher.java:58)
        at org.springframework.boot.loader.PropertiesLauncher.main(PropertiesLauncher.java:467)
    Caused by: org.springframework.boot.web.server.WebServerException: Unable to start embedded Tomcat
    at org.springframework.boot.web.embedded.tomcat.TomcatWebServer.initialize(TomcatWebServer.java:142)
    at org.springframework.boot.web.embedded.tomcat.TomcatWebServer.<init>(TomcatWebServer.java:104)
    at org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory.getTomcatWebServer(TomcatServletWebServerFactory.java:479)
    at org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory.getWebServer(TomcatServletWebServerFactory.java:211)
    at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.createWebServer(ServletWebServerApplicationContext.java:182)
    at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.onRefresh(ServletWebServerApplicationContext.java:160)
    ... 16 common frames omitted
   ```
   解决方法：执行以下代码，把结果赋值给nacos/conf/application.properties的nacos.core.auth.plugin.nacos.token.secret.key，然后启动就可以了
   ```
    import javax.crypto.KeyGenerator;
    import javax.crypto.SecretKey;
    import java.security.NoSuchAlgorithmException;
    import java.util.Base64;
    
    public class SecretKeyGenerator {
    
        public static SecretKey generate() {
            try {
                // 使用AES算法生成256位的SecretKey
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                keyGenerator.init(256);
                return keyGenerator.generateKey();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
        }
     
        public static void main(String[] args) {
            SecretKey secretKey = generate();
            System.out.println(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        }
    }
   ```
5.  配置nacos启动报错: Add a spring.config.import=nacos: property to your configuration. 	If configuration is not required add spring.config.import=optional:nacos: instead. 	To disable this check, set spring.cloud.nacos.config.import-check.enabled=false.
    ```
    #添加pom
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-bootstrap</artifactId>
      <version>3.1.4</version>           
    </dependency>
    ```
6. Nacos 使用服务名调不了服务，提示找不到host ，服务提供者消费者均已在Nacos Sever中注册成功.
    ```
   以上不能以服务名访问的解决方法：添加了@LoadBalanced 还需要在pom.xml文件中添加

    <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-loadbalancer</artifactId>
    </dependency>
   
    否则只添加@LoadBalanced是提示找不到服务名的
   ```
7. 
***