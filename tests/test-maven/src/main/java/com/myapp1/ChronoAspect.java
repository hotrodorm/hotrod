package com.myapp1;

import java.sql.PreparedStatement;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ChronoAspect implements ApplicationContextAware {

  private ThreadLocal<String> sql = new ThreadLocal<String>();

  @Autowired
  private SQLMetrics sqlMetrics;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    System.out.println("(Aspect 0) [at " + System.identityHashCode(this) + "] this.sqlMetrics=" + this.sqlMetrics); // NULL!
  }

  @Around(value = "execution(* org.apache.commons.dbcp.BasicDataSource.getConnection())")
  public Object measureGetConnection(final ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      Object conn = joinPoint.proceed();
      AspectJProxyFactory proxyFactory = new AspectJProxyFactory(conn);
      proxyFactory.addAspect(ChronoAspect.class);
      Object proxyConn = proxyFactory.getProxy();
      return proxyConn;

    } catch (Throwable e) {
      throw e;
    }
  }

  @Around(value = "execution(* java.sql.Connection.prepareStatement(..)) && args(sql)")
  public Object measurePrepareStatement(final ProceedingJoinPoint joinPoint, final String sql) throws Throwable {
    try {
      this.sql.set(sql);
      Object ps = joinPoint.proceed();
      AspectJProxyFactory proxyFactory = new AspectJProxyFactory(ps);
      proxyFactory.addAspect(ChronoAspect.class);
      PreparedStatement proxyPS = proxyFactory.getProxy();
      return proxyPS;

    } catch (Throwable e) {
      throw e;
    }
  }

  @Around(value = "execution(* java.sql.PreparedStatement.execute(..))")
  public Object measureQueryExecution2(final ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    try {
      Object ps = joinPoint.proceed();
      long end = System.currentTimeMillis();
      System.out.println("(Aspect 1) [at " + System.identityHashCode(this) + "] this.sqlMetrics=" + this.sqlMetrics); // NULL!
      this.sqlMetrics.record(this.sql.get(), end - start, true);
      return ps;

    } catch (Throwable e) {
      long end = System.currentTimeMillis();
      System.out.println("(Aspect 2) [at " + System.identityHashCode(this) + "] this.sqlMetrics=" + this.sqlMetrics); // NULL!
      this.sqlMetrics.record(this.sql.get(), end - start, false);
      throw e;
    }
  }

  public String toString() {
    return this.getClass().getName() + "[" + System.identityHashCode(this) + "]";
  }

}
