package org.hotrod.torcs;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import javax.sql.DataSource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hotrod.torcs.setters.DoubleSetter;
import org.hotrod.torcs.setters.FloatSetter;
import org.hotrod.torcs.setters.IntSetter;
import org.hotrod.torcs.setters.LongSetter;
import org.hotrod.torcs.setters.Setter;
import org.hotrod.torcs.setters.ShortSetter;
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
//    private Map<Integer, PreparedParameter> parameters;
    private Map<Integer, Setter> setters;

    public void clearParameters() {
//      this.parameters = new HashMap<>();
      this.setters = new HashMap<>();
    }

//    private void registerParameter(final int index, final Object value, final int type) {
//      System.out.println("**** param #" + index + ": " + value + " (" + type + ")");
//      this.parameters.put(index, new PreparedParameter(index, value, type));
//    }

    public void registerSetter(final Setter setter) {
      System.out
          .println("**** Setter (" + setter.getClass().getName() + "): #" + setter.getIndex() + ": " + setter.value());
      this.setters.put(setter.getIndex(), setter);
    }
  }

  public class PreparedParameter {

    private int index;
    private Object value;
    private int type;

    protected PreparedParameter(int index, Object value, int type) {
      this.index = index;
      this.value = value;
      this.type = type;
    }

    public int getIndex() {
      return index;
    }

    public Object getValue() {
      return value;
    }

    public int getType() {
      return type;
    }

    public String toString() {
      return "#" + this.index + ": type=" + this.type + " value=" + value;
    }

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
//      System.out.println(
//          "caller=" + System.identityHashCode(caller) + ":" + (caller == null ? "null" : caller.getClass().getName()));
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
    this.threadData.get().clearParameters();
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
    this.threadData.get().clearParameters();
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

  // ===============================
  // PreparedStatement.set() methods
  // ===============================

//  @Before(value = "execution(* java.sql.PreparedStatement.setArray(..)) && args(parameterIndex, x)")
//  private void adviceSet(final JoinPoint joinPoint, final int parameterIndex, final Array x) throws Throwable {
//    this.threadData.get().registerParameter(parameterIndex, x, java.sql.Types.ARRAY);
//  }

  @Before(value = "execution(* java.sql.PreparedStatement.setInt(..)) && args(parameterIndex, x)")
  private void adviceSetInt(final JoinPoint joinPoint, final int parameterIndex, final int x) throws Throwable {
    this.threadData.get().registerSetter(new IntSetter(parameterIndex, x));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setLong(..)) && args(parameterIndex, x)")
  private void adviceSetLong(final JoinPoint joinPoint, final int parameterIndex, final long x) throws Throwable {
    this.threadData.get().registerSetter(new LongSetter(parameterIndex, x));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setShort(..)) && args(parameterIndex, x)")
  private void adviceSetShort(final JoinPoint joinPoint, final int parameterIndex, final short x) throws Throwable {
    this.threadData.get().registerSetter(new ShortSetter(parameterIndex, x));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setFloat(..)) && args(parameterIndex, x)")
  private void adviceSetFloat(final JoinPoint joinPoint, final int parameterIndex, final float x) throws Throwable {
    this.threadData.get().registerSetter(new FloatSetter(parameterIndex, x));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setDouble(..)) && args(parameterIndex, x)")
  private void adviceSetDouble(final JoinPoint joinPoint, final int parameterIndex, final double x) throws Throwable {
    this.threadData.get().registerSetter(new DoubleSetter(parameterIndex, x));
  }

//  @Before(value = "execution(* java.sql.PreparedStatement.setObject(..)) && args(parameterIndex, x)")
//  private void adviceSetObject(final JoinPoint joinPoint, final int parameterIndex, final Object x) throws Throwable {
//    System.out.println("=== adviceSetObject()");
//  }

//  @Around(value = "execution(* java.sql.PreparedStatement.setObject(..)) && args(parameterIndex, x, targetSqlType)")
//  private void adviceSetObject2(final ProceedingJoinPoint joinPoint, final int parameterIndex, final Object x,
//      final int targetSqlType) throws Throwable {
////    this.threadData.get().registerParameter(parameterIndex, x, targetSqlType);
//    joinPoint.proceed();
//  }

  // Measuring

  private Object measureSQLExecution(final ProceedingJoinPoint joinPoint, final String sql) throws Throwable {
//    System.out.println("Measuring: " + sql);
    long start = System.currentTimeMillis();
    DataSourceReference r = this.getDataSourceReference(this.threadData.get().dataSource);
    try {
      Object ps = joinPoint.proceed();
      long end = System.currentTimeMillis();

      this.torcs.record(r, sql, this.threadData.get().setters, (int) (end - start), null);
//      System.out.println("Measured: " + (end - start) + "ms");
      return ps;

    } catch (Throwable t) {
      long end = System.currentTimeMillis();
      this.torcs.record(r, sql, this.threadData.get().setters, (int) (end - start), t);
//      System.out.println("Measured (exception): " + (end - start) + "ms");
      throw t;
    }
  }

  // DataSource references cache

  private Object measureSQLExecution(final ProceedingJoinPoint joinPoint) throws Throwable {
    return measureSQLExecution(joinPoint, this.threadData.get().sql);
  }

  private Map<Integer, DataSourceReference> dataSourcesByHash = new HashMap<>();
  private int nextReferenceId = 0;

  public boolean multipleDataSources() {
    return this.dataSourcesByHash.size() > 1;
  }

  private DataSourceReference getDataSourceReference(final DataSource dataSource) {
    DataSourceReference r = this.dataSourcesByHash.get(System.identityHashCode(dataSource));
    if (r == null) {
      r = addDataSourceReference(dataSource);
    }
    return r;
  }

  private synchronized DataSourceReference addDataSourceReference(final DataSource dataSource) {
    DataSourceReference r = new DataSourceReference(this.nextReferenceId++, dataSource);
    this.dataSourcesByHash.put(System.identityHashCode(dataSource), r);
    return r;
  }

}
