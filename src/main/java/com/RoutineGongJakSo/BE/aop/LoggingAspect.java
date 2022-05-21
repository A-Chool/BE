package com.RoutineGongJakSo.BE.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

//    @Around("@annotation(StopWatch)")
    @Around("execution(public * com.RoutineGongJakSo.BE..*(*))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        Object proceed = null;
        String className = joinPoint.getSignature().getName();
        try {
            stopWatch.start();

            proceed = joinPoint.proceed();
        }
        finally {
            stopWatch.stop();
            log.info("{} 실행 시간:  {}", className, stopWatch.getTotalTimeMillis());
        }

        return proceed;
    }
}
