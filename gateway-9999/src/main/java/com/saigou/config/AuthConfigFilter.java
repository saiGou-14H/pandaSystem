package com.saigou.config;

import cn.hutool.jwt.JWTUtil;
import com.saigou.properties.AuthProperties;
import com.saigou.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Configuration
@EnableConfigurationProperties(AuthProperties.class)
@RequiredArgsConstructor
public class AuthConfigFilter implements GlobalFilter , Ordered {
    private final Logger log =  LoggerFactory.getLogger(AuthConfigFilter.class);
    private final JwtUtil jwtUtil;
    private final AuthProperties authProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (isExclude(request.getURI().getPath())){
            return chain.filter(exchange);
        }
        if (request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            String token = jwtUtil.getHeaderToken(request);
            if (token != null) {
                if (jwtUtil.verifyToken(token) && JWTUtil.parseToken(token).getPayloads().containsKey("id") ) {
                    log.info("token验证成功");
                    log.info("userId:" + jwtUtil.getUserId(token));
                    ServerHttpRequest newRequest = request.mutate().header("userId", jwtUtil.getUserId(token).toString()).build();
                    exchange.mutate().request(newRequest).build();
                    return chain.filter(exchange);
                }
            }
        }
        log.info("token验证失败");
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private boolean isExclude(String url){
        for (String path : authProperties.getExcludePaths()){
            System.out.println(path);
            if (antPathMatcher.match(path,url)){
                return true;
            }
        }
        return false;
    }
}
