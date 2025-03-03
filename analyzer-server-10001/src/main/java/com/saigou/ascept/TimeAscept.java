package com.saigou.ascept;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TimeAscept {
    //自定义注解
    @Pointcut("@annotation(com.saigou.annotation.TimeAnnotation)")
    public void time() {
    }
    @Around("time()")
    public Object timeAspect(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long end = System.currentTimeMillis();
        System.out.println("["+"1"+"]"+"方法执行时间：" + (end - start) + "ms");
        return result;
    }
}
