# springcloud 学习
	Eureka ：
1）	介绍：是服务治理的一个框架，分为服务端和客户端，服务端作为服务注册中心支持高可用。客户端主要处理服务的注册和发现，并且通过心跳来更新它的服务租约。
2）	搭建：
~服务端：
需要添加spring-cloud-starter-eureka-server 依赖
在启动类添加@EnableEurekaServer注解
配置文件指定是否注册自己，集群下注册自己到其他节点
# 强制不注册自己、不检索服务
#eureka.client.register-with-eureka=false
#eureka.client.fetch-registry=false

#false表示在此eureka服务器中关闭自我保护模式,所谓自我保护模式,默认true
eureka.server.enableSelfPreservation=false
eureka.client.serviceUrl.defaultZone=http://peer2:8081/eureka
~客户端
服务注册：添加依赖、配置文件指定注册中心、启动类添加@enableDiscoveryClient。
服务发现：添加ribbon依赖、注入RestTemplate类并添加@LoadBalance注解、使用restTemplate 类api 即可调用，负载均衡默认轮询的方式。

3）	服务治理机制：

~服务提供者：
     服务注册==》 启动的时候会带上自己的元数据发送rest请求将自己注册到eureka server ，eureka server 会存储在上层结构map中，第一层的key是服务名、第二层的key是具体实例名。Eureka.client.register.with-eureka=true,如果为false 将不会注册。
     服务同步==》 注册中心之间因为相互注册为服务，当服务提供者发送注册请求到一个服务中心时，它会将请求转发给其它相连的注册中心，从而实现服务同步。
     服务续约==》 注册完后，提供者会维护一个心跳给eureka server，以防止将服务实例从服务列表剔除出去。默认每隔30发送一次，服务失效的时间默认为90秒。
       Eureka.instance.lease-renewal-interval-in-seconds=30
       Eureka.instance.lease-expiration-duration-in-seconds=90

~ 服务消费者
      获取服务===》发送请求到eureka server ，来获取注册的服务清单。
Eureka server 会维护一份只读的服务清单来返回给客户端，同时该缓存清单每隔30秒更新一次。
  Eureka.client.registry-fetch-interval-seconds=30 修改缓存清单更新时间。
·············
~服务注册中心
  失效剔除==》每隔60秒将超时90秒没有续约的服务剔除。
  自我保护==》会统计心跳失败的比例在15分钟之内是否低于85%，如果低于，会将注册信息保护起来。所以客户端必须有容错机制。
     Eureka.server.enable-self-preservation=false 关闭保护机制。


	Spring Cloud Ribbon

     ~Ribbon 是一个基于http 和tcp 的客户端负载均衡工具，所有客户端节点都维护这自己要访问的服务端清单，而这些服务端清单都来自于服务注册中心。在客户端负载均衡中也需要心跳去维护服务端清单的健康性。

   ~ RestTemplate 详解
      1)   Get 请求：
getForEntity(String url,class,Object…/Map)
         	getForObject(String url,class,Object…/Map)
       2)  Post请求：
       postForEntity(Stringurl,Object request ,class,Object…/Map)
      3) postForLocation : 请求提交资源并返回新资源的url
postForLocation(Stringurl,Object request ,Object…/Map)
         不需要指定返回参数类型。
4)	 put 请求
put(url,request,object /map)
5) delete 请求
   	 Delete（String url ，object… urlVariables）
Delete（String url ，Map urlVariables）
Delete（String ur）

~ 负载均衡实现
 1）  LoadBalancerClient 接口 ：
   
ServiceInstance   Choose（String serviceId）；
URI             reconstructURI(ServiceInstance, URI original);
<T> T           execute(String serviced,LoadBalancerRequest<T> req);
Choose 方法是选择一个服务器实例，reconstructURI 方法是拼接远程请求url，execute 执行请求。
            
        2) LoadBalancerAutoConfiguration 类 是负载均衡的核心配置类
  
           通过注解可以知道必须满足的条件：
1.	必须有restTemplate 这个bean，
2.	必须有LoadBalacerClient的实现类

           LoadBalancerAutoConfiguration类实现：
1.	创建了LoadBalancerInterceptor拦截器
2.	RestTemplateCustomizer 定制器 给restTemplate 实例列表绑定拦截器。
         
4）	LoadBalancerInterceptor 拦截器 实现负载均衡
     由于在使用restTemplate 采用 服务名作为host ，所以直接从HttpRequst中通过getHost() 拿来服务名再调用 execute方法去选择并发起请求
5）	RibbonLoadBalancerClient 类 真正执行请求
通过IloadBalancer 实现 的负载均衡 实例有三个。
BaseLoadBalancer 、ZoneAwareLoadBalancer、DynamicServerListLoadBalancer
默认采用 ZoneAwareLoadBalancer

执行流程：
通过zoneAwareLoadBalancer 的chooseServer方法选择一个服务实例，然后包装成RibbonServer 对象返回给 拦截器， 拦截器再通过LoadBalancerRequest的 apply方法 向实际的服务实例发起请求。
        
         ~ 负载均衡器
              BaseLoadBalancer ：
1.	维护了两个服务实例列表，一个存储所有服务实例清单，
一个存储正常服务实例清单
2.	定义了检查服务实例是否正常的IPing对象，会直接启动一个
用于检查server是否正常的任务，每隔10秒钟
3.	定义了负载均衡的处理规则IRule对象，线性处理规则。
4.	ChooseServer 挑选具体的服务实例
5.	markServerDown 标记某个服务实例暂停。
              
              DynamicServerListLoadBalancer：
1.	实现了服务实例清单在运行期动态更新ServerListUpdater
  在服务实例清单初始化延迟一秒后开始执行，每隔30秒执行。
2.	对服务实例清单的过滤功能。ServerListFilter
         ~ 负载均衡策略
              RandomRule：随机
              RoundRobbinRule：轮询
              WeightedResponseRule：权重
         		累计权重+总响应时间-实例平均响应时间


	Hystrix：
每个服务之间依赖都是通过远程调用的方式执行，这样就有可能因为网络原因或依赖服务自身的问题出现调用故障或延迟，若此时调用方的请求不断增加，最后就会因为等待出现故障的依赖方响应形成任务积压，最终导致自身服务的瘫痪、
故障蔓延。
   Hystrix就是解决这样的问题，起到断路器的作用，当发生故障之后，通过断路器的故障监控，向调用方返回一个错误响应，而不是长时间的等待。

1）	使用：
1．	依赖 spring-cloud-starter-hystrix
2．	启动类使用@EnableCircuitBreaker注解表示开启断路器功能
3．	在服务调用方法上增加@HystrixCommand(fallbackMethod=”fallback”)
来指定回调方法。
         
2）	特性：
Hystrix 默认超时时间为2000秒
3）	原理分析

1.	创建HystrixCommand对象或HystrixObservableCommand 对象来表示对依赖服务的操作请求。这里使用命令模式实现调用者和操作者的解耦。

2.	命令执行：
HystrixCommand ==》
execute（）方法 返回一个单一的结果对象或抛异常
queue（）方法 直接返回future对象，里边包含单一结果对象
                HystrixObservableCommand==》
                        Observer（）对象代表操作的多个结果，他是一个hot observable
                        toObservable()方法同样返回observable对象，cold observable
                 通过RxJava 实现 ，使用观察者模式实现。

3.	结果是否被缓存
若当前命令请求的缓存功能是被启用的，并且该命令缓存命中，那么缓存的结果会立即以observable对象的返回。
4.	断路器是否打开
在命令结果没有缓存命中的时候，Hystrix在执行命令前需要检查断路器是否打开，如果打开则不会执行命令，而是转接到fallback处理
5.	如果断路器是关闭的，则会检查是否有可用资源来执行。如果有则执行命令，如果没有则直接到fallback处理。
6.	HystrixObserableCommond.construct（）或HystrixCommand.run()来请求依赖服务。如果执行时间超过了命令设置的超时值，则会抛TimeOuteException异常，转接到fallback处理。若果没有异常，hystrix会记录日志并采集监控报告之后返回结果。

7.	计算断路器的健康度
Hystrix会将 成功、拒绝、超时、失败 等信息报告给断路器来统计。断路器通过这些信息来决定是否打开断路器。来对某个依赖服务熔断，知道恢复期结束。若恢复期结束后，还是未达到健康指标则再次熔断。
8.	Fallback处理
当命令执行失败的时候会进入fallback进行回退处理。成为服务降级。
4）	断路器原理
1.	HystrixCircuitBreaker 接口定义
  三个抽象方法：
boolean allowRequest(); 每个命令的请求都通过他判断是否被执行
    boolean isOpen();  判断当前断路器是否开启
void markSuccess();     用来闭合断路器
              三个静态类：
                 Factory静态类：
维护了一个hystrix命令和断路器关系的集合，每一个hystrix命 
令都需要一个key来标识，并通过key能找到对应的断路器。
                 NoOpCircuitBreaker静态类：
 实现了HystrixCircuitBreaker接口，定义了什么都不做的断路器
实现，它允许所有的请求，并且断路器始终关闭。
                  HystrixCircuitBreakerImpl静态类：
                         是HystrixCircuitBreaker的实现类，定义了断路器的四个核心对
                         象
               
2.	HystrixCircuitBreakerImpl
HystrixCommandProperties properties ：HystrixCommand实例属性对象
HystrixCommandMetrics metrics ：用来让HystrixCommand记录各类度量指标的队象。
AtomicBoolean ：断路器是否打开的标志。
AtomicLong ： 断路器打开或者上次测试的时间戳。
           
5）	设置请求缓存
@CacheResult(cacheKeyMethod=”getUserByIdCacheKey”) 注解 设置缓存 
@CacheKey 指定key 优先级低
@CacheRemove(commandKey=”getUserByIdCacheKey”)  清理缓存
