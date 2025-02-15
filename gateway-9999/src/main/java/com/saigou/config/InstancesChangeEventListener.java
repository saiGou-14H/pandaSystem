package com.saigou.config;

import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.NotifyCenter;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import com.alibaba.nacos.common.utils.JacksonUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_URL;
import static org.springframework.cloud.loadbalancer.core.CachingServiceInstanceListSupplier.SERVICE_INSTANCE_CACHE_NAME;

/**
 * 监听注册中心实例注册状态改变事件，微服务实例状态改变后刷新swagger ui的组(一个组等于一个微服务)
 *
 * @author vains
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class InstancesChangeEventListener extends Subscriber<InstancesChangeEvent> {

    private final String LB_SCHEME = "lb";

    private final RouteDefinitionLocator locator;

    @Resource
    private CacheManager defaultLoadBalancerCacheManager;

    private final SwaggerUiConfigProperties swaggerUiConfigProperties;

    /**
     * 获取配置文件中默认配置的swagger组
     */
    private final Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> defaultUrls;


    public InstancesChangeEventListener(RouteDefinitionLocator locator,
                                        SwaggerUiConfigProperties swaggerUiConfigProperties) {
        this.locator = locator;
        this.swaggerUiConfigProperties = swaggerUiConfigProperties;
        // 构造器中初始化配置文件中的swagger组
        this.defaultUrls = swaggerUiConfigProperties.getUrls();
    }

    @Override
    public void onEvent(InstancesChangeEvent event) {
        if (log.isDebugEnabled()) {
            log.info("Spring Gateway 接收实例刷新事件：{}, 开始刷新缓存", JacksonUtils.toJson(event));
        }
        Cache cache = defaultLoadBalancerCacheManager.getCache(SERVICE_INSTANCE_CACHE_NAME);
        if (cache != null) {
            cache.evict(event.getServiceName());
        }
        // 刷新group
        this.refreshGroup();

        if (log.isDebugEnabled()) {
            log.info("Spring Gateway 实例刷新完成");
        }
    }

    /**
     * 刷新swagger的group
     */
    public void refreshGroup() {
        // 获取网关路由
        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
        if (ObjectUtils.isEmpty(definitions)) {
            return;
        }

        // 根据路由规则生成 swagger组 配置
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> swaggerUrls = definitions.stream()
                // 只处理在注册中心注册过的(lb://service)
                .filter(definition -> definition.getUri().getScheme().equals(LB_SCHEME))
                .map(definition -> {
                    // 生成 swagger组 配置，以微服务在注册中心中的名字当做组名、请求路径(我这里使用的是自动扫描生成的，所以直接用了这个，其它自定义的按需修改)
                    String authority = definition.getUri().getAuthority();
                    return new AbstractSwaggerUiConfigProperties.SwaggerUrl(authority, authority + DEFAULT_API_DOCS_URL, authority);
                })
                .collect(Collectors.toSet());

        // 如果在配置文件中有添加其它 swagger组 配置则将两者合并
        if (!ObjectUtils.isEmpty(defaultUrls)) {
            swaggerUrls.addAll(defaultUrls);
        }

        // 重置配置文件
        swaggerUiConfigProperties.setUrls(swaggerUrls);

        if (log.isDebugEnabled()) {
            String groups = swaggerUrls.stream()
                    .map(AbstractSwaggerUiConfigProperties.SwaggerUrl::getName)
                    .collect(Collectors.joining(","));
            log.debug("刷新Spring Gateway Doc Group成功，获取到组：{}.", groups);
        }
    }

    @PostConstruct
    public void registerToNotifyCenter() {
        // 注册监听事件
        NotifyCenter.registerSubscriber((this));
    }

    @Override
    public Class<? extends Event> subscribeType() {
        return InstancesChangeEvent.class;
    }
}
