package org.hotrod.torcs;

import java.util.WeakHashMap;

import javax.sql.DataSource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TorcsAspect {

  private ThreadLocal<QueryData> threadData = new ThreadLocal<>();

  public class QueryData {
    private DataSource dataSource;
    private String sql;
  }

  @Autowired
  private Torcs torcs;

  // Adding aspect to the Connection

  private WeakHashMap<Integer, Object> connProxies = new WeakHashMap<>();

  @Around(value = "execution(* javax.sql.DataSource.getConnection())")
  private Object measureGetConnection(final ProceedingJoinPoint joinPoint) throws Throwable {
//    System.out.println("measureGetConnection()");   

    if (this.threadData.get() == null) {
      this.threadData.set(new QueryData());
    }

    try {

      Object caller = joinPoint.getThis();
      System.out.println(
          "caller=" + System.identityHashCode(caller) + ":" + (caller == null ? "null" : caller.getClass().getName()));
      DataSource ds = (DataSource) caller;
      this.threadData.get().dataSource = ds;

      Object conn = joinPoint.proceed();

      Object proxy = this.connProxies.get(System.identityHashCode(conn));
      if (proxy != null) {
        return proxy;
      }

      AspectJProxyFactory proxyFactory = new AspectJProxyFactory(conn);
      proxyFactory.addAspect(this);
      proxy = proxyFactory.getProxy();
      this.connProxies.put(System.identityHashCode(conn), proxy);
      return proxy;

    } catch (Throwable e) {
      throw e;
    }
  }

  // Adding aspect to the PreparedStatement

  private WeakHashMap<Integer, Object> psProxies = new WeakHashMap<>();

  @Around(value = "execution(* java.sql.Connection.prepareStatement(..)) && args(sql)")
  private Object measurePrepareStatement(final ProceedingJoinPoint joinPoint, final String sql) throws Throwable {
//    System.out.println("measurePrepareStatement()");
    return addProxy(joinPoint, sql, this.psProxies);
  }

  // Adding aspect to the Statement

  private WeakHashMap<Integer, Object> stProxies = new WeakHashMap<>();

  @Around(value = "execution(* java.sql.Connection.createStatement(..)) && args(sql)")
  private Object measureStatement(final ProceedingJoinPoint joinPoint, final String sql) throws Throwable {
//    System.out.println("measureStatement()");
    return addProxy(joinPoint, sql, this.stProxies);
  }

  // Adding aspect the the CallablaStatement

  private WeakHashMap<Integer, Object> csProxies = new WeakHashMap<>();

  @Around(value = "execution(* java.sql.Connection.prepareCall(..)) && args(sql)")
  private Object measurePrepareCall(final ProceedingJoinPoint joinPoint, final String sql) throws Throwable {
//    System.out.println("measurePrepareCall()");
    return addProxy(joinPoint, sql, this.csProxies);
  }

  private Object addProxy(final ProceedingJoinPoint joinPoint, final String sql,
      final WeakHashMap<Integer, Object> proxies) throws Throwable {
    try {
      this.threadData.get().sql = sql;
      Object obj = joinPoint.proceed();

      Object proxy = proxies.get(System.identityHashCode(obj));
      if (proxy != null) {
        return proxy;
      }

      AspectJProxyFactory proxyFactory = new AspectJProxyFactory(obj);
      proxyFactory.addAspect(this);
      proxy = proxyFactory.getProxy();
      proxies.put(System.identityHashCode(obj), proxy);
      return proxy;

    } catch (Throwable e) {
      throw e;
    }
  }

  // Intercepting the PreparedStatement (declared methods)

  @Around(value = "execution(* java.sql.PreparedStatement.execute(..))")
  private Object adviceExecute(final ProceedingJoinPoint joinPoint) throws Throwable {
    return measureSQLExecution(joinPoint);
  }

  @Around(value = "execution(* java.sql.PreparedStatement.executeLargeUpdate(..))")
  private Object adviceExecuteLargeUpdate(final ProceedingJoinPoint joinPoint) throws Throwable {
    return measureSQLExecution(joinPoint);
  }

  @Around(value = "execution(* java.sql.PreparedStatement.executeQuery(..))")
  private Object adviceExecExecuteQuery(final ProceedingJoinPoint joinPoint) throws Throwable {
    return measureSQLExecution(joinPoint);
  }

  @Around(value = "execution(* java.sql.PreparedStatement.executeUpdate(..))")
  private Object adviceExecuteUpdate(final ProceedingJoinPoint joinPoint) throws Throwable {
    return measureSQLExecution(joinPoint);
  }

  // Intercepting the java.sql.Statement (declared methods)

  @Around(value = "execution(* java.sql.Statement.execute(..)) && args(sql)")
  private Object adviceExecute(final ProceedingJoinPoint joinPoint, final String sql) throws Throwable {
    return measureSQLExecution(joinPoint, sql);
  }

  @Around(value = "execution(* java.sql.Statement.execute(..)) && args(sql, autoGeneratedKeys)")
  private Object adviceExecute(final ProceedingJoinPoint joinPoint, final String sql, final int autoGeneratedKeys)
      throws Throwable {
    return measureSQLExecution(joinPoint, sql);
  }

  @Around(value = "execution(* java.sql.Statement.execute(..)) && args(sql, columnIndexes)")
  private Object adviceExecute(final ProceedingJoinPoint joinPoint, final String sql, final int[] columnIndexes)
      throws Throwable {
    return measureSQLExecution(joinPoint, sql);
  }

  @Around(value = "execution(* java.sql.Statement.execute(..)) && args(sql, columnNames)")
  private Object adviceExecute(final ProceedingJoinPoint joinPoint, final String sql, final String[] columnNames)
      throws Throwable {
    return measureSQLExecution(joinPoint, sql);
  }

  @Around(value = "execution(* java.sql.Statement.executeBatch(..))")
  private Object adviceExecuteBatch(final ProceedingJoinPoint joinPoint) throws Throwable {
    return measureSQLExecution(joinPoint);
  }

  @Around(value = "execution(* java.sql.Statement.executeLargeBatch(..))")
  private Object adviceExecuteLargeBatch(final ProceedingJoinPoint joinPoint) throws Throwable {
    return measureSQLExecution(joinPoint);
  }

  @Around(value = "execution(* java.sql.Statement.executeLargeUpdate(..)) && args(sql)")
  private Object adviceExecuteLargeUpdate(final ProceedingJoinPoint joinPoint, final String sql) throws Throwable {
    return measureSQLExecution(joinPoint, sql);
  }

  @Around(value = "execution(* java.sql.Statement.executeLargeUpdate(..)) && args(sql, autoGeneratedKeys)")
  private Object adviceExecuteLargeUpdateAK(final ProceedingJoinPoint joinPoint, final String sql,
      final int autoGeneratedKeys) throws Throwable {
    return measureSQLExecution(joinPoint, sql);
  }

  @Around(value = "execution(* java.sql.Statement.executeLargeUpdate(..)) && args(sql, columnIndexes)")
  private Object adviceExecuteLargeUpdate(final ProceedingJoinPoint joinPoint, final String sql,
      final int[] columnIndexes) throws Throwable {
    return measureSQLExecution(joinPoint, sql);
  }

  @Around(value = "execution(* java.sql.Statement.executeLargeUpdate(..)) && args(sql, columnNames)")
  private Object adviceExecuteLargeUpdate(final ProceedingJoinPoint joinPoint, final String sql,
      final String[] columnNames) throws Throwable {
    return measureSQLExecution(joinPoint, sql);
  }

  @Around(value = "execution(* java.sql.Statement.executeQuery(..)) && args(sql)")
  private Object adviceExecuteQuery(final ProceedingJoinPoint joinPoint, final String sql) throws Throwable {
    return measureSQLExecution(joinPoint, sql);
  }

  @Around(value = "execution(* java.sql.Statement.executeUpdate(..)) && args(sql)")
  private Object adviceExecuteUpdate(final ProceedingJoinPoint joinPoint, final String sql) throws Throwable {
    return measureSQLExecution(joinPoint, sql);
  }

  @Around(value = "execution(* java.sql.Statement.executeUpdate(..)) && args(sql, autoGeneratedKeys)")
  private Object adviceExecuteUpdateAK(final ProceedingJoinPoint joinPoint, final String sql,
      final int autoGeneratedKeys) throws Throwable {
    return measureSQLExecution(joinPoint, sql);
  }

  @Around(value = "execution(* java.sql.Statement.executeUpdate(..)) && args(sql, columnIndexes)")
  private Object adviceExecuteUpdate(final ProceedingJoinPoint joinPoint, final String sql, final int[] columnIndexes)
      throws Throwable {
    return measureSQLExecution(joinPoint, sql);
  }

  @Around(value = "execution(* java.sql.Statement.executeUpdate(..)) && args(sql, columnNames)")
  private Object adviceExecuteUpdate(final ProceedingJoinPoint joinPoint, final String sql, final String[] columnNames)
      throws Throwable {
    return measureSQLExecution(joinPoint, sql);
  }

  // Measuring

  private Object measureSQLExecution(final ProceedingJoinPoint joinPoint) throws Throwable {
    return measureSQLExecution(joinPoint, this.threadData.get().sql);
  }

  private Object measureSQLExecution(final ProceedingJoinPoint joinPoint, final String sql) throws Throwable {
//    System.out.println("Measuring: " + sql);
    long start = System.currentTimeMillis();
    try {
      Object ps = joinPoint.proceed();
      long end = System.currentTimeMillis();
      this.torcs.record(this.threadData.get().dataSource, sql, (int) (end - start), null);
//      System.out.println("Measured: " + (end - start) + "ms");
      return ps;

    } catch (Throwable t) {
      long end = System.currentTimeMillis();
      this.torcs.record(this.threadData.get().dataSource, sql, (int) (end - start), t);
//      System.out.println("Measured (exception): " + (end - start) + "ms");
      throw t;
    }
  }

}
