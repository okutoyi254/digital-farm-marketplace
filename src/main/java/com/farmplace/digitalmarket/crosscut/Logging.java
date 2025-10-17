package com.farmplace.digitalmarket.crosscut;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class Logging {


    @Pointcut("execution(* com.farmplace.digitalmarket.service.services.BusinessServiceImpl.*(..))")
    public void logCustomerMethods(){}

    @Around("logCustomerMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint)throws Throwable{

        String methodName=joinPoint.getSignature().getName();

        Object []args= joinPoint.getArgs();

        log.info("Entering {} with args {}",methodName,args);

        long start=System.currentTimeMillis();

        Object result;

        try
        {
            result=joinPoint.proceed();
        }
        catch (Throwable ex){
            log.error("Exception in {}: {}",methodName,ex.getMessage());
            throw ex;
        }

        long timeTaken=System.currentTimeMillis()-start;
        log.info("Existing {} with result {} ({} ms)",methodName,result,timeTaken);

        return result;

    }
}
