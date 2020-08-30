package com.myapp1;

import java.sql.PreparedStatement;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ChronoAspect {

  private ThreadLocal<String> sql = new ThreadLocal<String>();

  @Around(value = "execution(* org.apache.commons.dbcp.BasicDataSource.getConnection())")
  public Object measureGetConnection(final ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    try {
      Object conn = joinPoint.proceed();
      long end = System.currentTimeMillis();

      AspectJProxyFactory proxyFactory = new AspectJProxyFactory(conn);
      proxyFactory.addAspect(ChronoAspect.class);
      Object proxyConn = proxyFactory.getProxy();
      return proxyConn;

    } catch (Throwable e) {
      long end = System.currentTimeMillis();
      throw e;
    }
  }

  @Around(value = "execution(* java.sql.Connection.prepareStatement(..)) && args(sql)")
  public Object measurePrepareStatement(final ProceedingJoinPoint joinPoint, final String sql) throws Throwable {
    try {
      this.sql.set(sql);
//      System.out.println("--------- sql=" + sql);
      Object ps = joinPoint.proceed();
      AspectJProxyFactory proxyFactory = new AspectJProxyFactory(ps);
      proxyFactory.addAspect(ChronoAspect.class);
      PreparedStatement proxyPS = proxyFactory.getProxy();
      return proxyPS;

    } catch (Throwable e) {
      throw e;
    }
  }

//  @Around(value = "execution(* java.sql.PreparedStatement.executeQuery(..))")
//  public Object measureQueryExecution1(final ProceedingJoinPoint joinPoint) throws Throwable {
//    long start = System.currentTimeMillis();
//    try {
//      Object ps = joinPoint.proceed();
//      long end = System.currentTimeMillis();
//      System.out.println(">>>>>>>>>>> 1 Elapsed time: " + (end - start) + " ms");
//      return ps;
//
//    } catch (Throwable e) {
//      long end = System.currentTimeMillis();
//      System.out.println(">>>>>>>>>>> 1 Elapsed time ERROR: " + (end - start) + " ms");
//      throw e;
//    }
//  }

  @Around(value = "execution(* java.sql.PreparedStatement.execute(..))")
  public Object measureQueryExecution2(final ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    try {
      Object ps = joinPoint.proceed();
      long end = System.currentTimeMillis();
//      System.out.println(">>>>>>>>>>> 2 Elapsed time: " + (end - start) + " ms");
      SQLMetrics.record(this.sql.get(), end - start, true);
      return ps;

    } catch (Throwable e) {
      long end = System.currentTimeMillis();
      SQLMetrics.record(this.sql.get(), end - start, false);
//      System.out.println(">>>>>>>>>>> 2 Elapsed time ERROR: " + (end - start) + " ms");
      throw e;
    }
  }

  public String toString() {
    return this.getClass().getName() + "[" + System.identityHashCode(this) + "]";
  }

//  @Around(value = "execution(* java.sql.PreparedStatement.executeBatch(..))")
//  public Object measureQueryExecution3(final ProceedingJoinPoint joinPoint) throws Throwable {
//    long start = System.currentTimeMillis();
//    try {
//      Object ps = joinPoint.proceed();
//      long end = System.currentTimeMillis();
//      System.out.println(">>>>>>>>>>> 3 Elapsed time: " + (end - start) + " ms");
//      return ps;
//
//    } catch (Throwable e) {
//      long end = System.currentTimeMillis();
//      System.out.println(">>>>>>>>>>> 3 Elapsed time ERROR: " + (end - start) + " ms");
//      throw e;
//    }
//  }
//
//  @Around(value = "execution(* java.sql.PreparedStatement.executeUpdate(..))")
//  public Object measureQueryExecution4(final ProceedingJoinPoint joinPoint) throws Throwable {
//    long start = System.currentTimeMillis();
//    try {
//      Object ps = joinPoint.proceed();
//      long end = System.currentTimeMillis();
//      System.out.println(">>>>>>>>>>> 4 Elapsed time: " + (end - start) + " ms");
//      return ps;
//
//    } catch (Throwable e) {
//      long end = System.currentTimeMillis();
//      System.out.println(">>>>>>>>>>> 4 Elapsed time ERROR: " + (end - start) + " ms");
//      throw e;
//    }
//  }

}
