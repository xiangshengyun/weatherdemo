package com.xiangsy.demo.infrastructure.aop;

import java.util.Optional;
import java.util.concurrent.Semaphore;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.xiangsy.demo.infrastructure.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: xiangsy
 * @version: 2021-04-09 19:35
 * @description:
 */
@Aspect
@Slf4j
@Component
@ConditionalOnProperty(name = "aop.enabled", havingValue = "true", matchIfMissing = true)
public class ServiceAspect {
    private static Semaphore semaphore = new Semaphore(100);

    /**
     * serviceMethodCall
     */
    @Pointcut("execution(public * com.xiangsy.demo..*.service.impl..*.*(..))")
    public void serviceMethodCall() {
        // do nothing
    }

    @Around("serviceMethodCall() && @annotation(com.xiangsy.demo.infrastructure.annotation.CallLimit)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        if (semaphore.availablePermits() == 0) {
            log.info("The number of inonvation exceeds the maximum, please wait patiently");
        }

        try {
            semaphore.acquire();
            return pjp.proceed();
        } catch (BizException e1) {
            throw e1;
        } catch (Exception e) {
            log.error("weatherServiceImpl.getTemperature() access error: ", e);
        } finally {
            semaphore.release();
        }
        return Optional.ofNullable(null);
    }

}
