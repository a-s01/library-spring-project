package com.epam.spring.library.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Aspect
@Configuration
public class LoggingAspect {

    @Pointcut("execution(public * *(..))")
    private void anyPublicMethod() {}

    @Pointcut("within(com.epam.spring.library.controller..*)")
    private void inControllerLayer() {}

    @Pointcut("within(com.epam.spring.library.service..*)")
    private void inServiceLayer() {}

    @Pointcut("within(com.epam.spring.library.repository..*)")
    private void inDataLayer() {}

    @Pointcut("within(com.epam.spring.library.config..*)")
    private void inConfig() {}

    @Pointcut("execution(* com.epam.spring.library.repository.BaseRepository.*(..))")
    private void lowLevelRepositoryImpl() {}

    @Before("anyPublicMethod() && (inControllerLayer() || inServiceLayer() ||"
            + " (inDataLayer() && !lowLevelRepositoryImpl()))")
    public void logStart(JoinPoint joinPoint) {
        log.info("{} with args({}) started",
                 joinPoint.getSignature().toShortString(),
                 joinPoint.getArgs());
    }

    @After("anyPublicMethod() && (inControllerLayer() || inServiceLayer() "
           + "|| (inDataLayer() && !lowLevelRepositoryImpl()))")
    public void logEnd(JoinPoint joinPoint) {
        log.info("finish {} with args({})",
                 joinPoint.getSignature().toShortString(),
                 joinPoint.getArgs());
    }

    @Before("anyPublicMethod() && inConfig()")
    public void logInitBean(JoinPoint joinPoint) {
        log.info("init bean {}", joinPoint.getSignature().getName());
    }

    @After("anyPublicMethod() && inConfig()")
    public void logFinishInitBean(JoinPoint joinPoint) {
        log.info("success: bean {} was created",
                 joinPoint.getSignature().getName());
    }
}