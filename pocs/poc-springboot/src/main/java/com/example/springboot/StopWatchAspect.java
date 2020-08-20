package com.example.springboot;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class StopWatchAspect {

  @Around(value = "execution(public int com.example.springboot.daos.CuentaDAO.*(..))")
  public Object measure(final ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    try {
      Object result = joinPoint.proceed();
      long end = System.currentTimeMillis();
      Metrics.record(joinPoint.toLongString(), end - start, true);
      return result;
    } catch (Throwable e) {
      long end = System.currentTimeMillis();
      Metrics.record(joinPoint.toLongString(), end - start, false);
      throw e;
    }
  }

}
