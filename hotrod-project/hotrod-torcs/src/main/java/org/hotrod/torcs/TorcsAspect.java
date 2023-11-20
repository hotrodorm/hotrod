package org.hotrod.torcs;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import javax.sql.DataSource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hotrod.torcs.setters.ArraySetter;
import org.hotrod.torcs.setters.AsciiStreamSetter;
import org.hotrod.torcs.setters.BigDecimalSetter;
import org.hotrod.torcs.setters.BinaryStreamSetter;
import org.hotrod.torcs.setters.BlobSetter;
import org.hotrod.torcs.setters.BooleanSetter;
import org.hotrod.torcs.setters.ByteSetter;
import org.hotrod.torcs.setters.BytesSetter;
import org.hotrod.torcs.setters.CharacterStreamSetter;
import org.hotrod.torcs.setters.ClobSetter;
import org.hotrod.torcs.setters.DateSetter;
import org.hotrod.torcs.setters.DoubleSetter;
import org.hotrod.torcs.setters.FloatSetter;
import org.hotrod.torcs.setters.IntSetter;
import org.hotrod.torcs.setters.LongSetter;
import org.hotrod.torcs.setters.NCharacterStreamSetter;
import org.hotrod.torcs.setters.NClobSetter;
import org.hotrod.torcs.setters.NStringSetter;
import org.hotrod.torcs.setters.NullSetter;
import org.hotrod.torcs.setters.ObjectSetter;
import org.hotrod.torcs.setters.RefSetter;
import org.hotrod.torcs.setters.RowIdSetter;
import org.hotrod.torcs.setters.SQLXMLSetter;
import org.hotrod.torcs.setters.Setter;
import org.hotrod.torcs.setters.ShortSetter;
import org.hotrod.torcs.setters.StringSetter;
import org.hotrod.torcs.setters.TimeSetter;
import org.hotrod.torcs.setters.TimestampSetter;
import org.hotrod.torcs.setters.URLSetter;
import org.hotrod.torcs.setters.UnicodeStreamSetter;
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
    private Map<Integer, Setter> setters;

    public void clearParameters() {
      this.setters = new HashMap<>();
    }

    public void registerSetter(final Setter setter) {
//      System.out
//          .println("**** Setter (" + setter.getClass().getName() + "): #" + setter.getIndex() + ": " + setter.value());
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
    System.out.println("measureGetConnection()");

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
    System.out.println("measurePrepareStatement()");
    this.threadData.get().clearParameters();
    return addProxy(joinPoint, sql, this.psProxies);
  }

  // Adding aspect to the Statement

  private WeakHashMap<Integer, Object> stProxies = new WeakHashMap<>();

  @Around(value = "execution(* java.sql.Connection.createStatement(..)) && args()")
  private Object measureStatement(final ProceedingJoinPoint joinPoint) throws Throwable {
    System.out.println("measureStatement() #1");
    return addProxy(joinPoint, null, this.stProxies);
  }

  @Around(value = "execution(* java.sql.Connection.createStatement(..)) && args(resultSetType, resultSetConcurrency)")
  private Object measureStatement(final ProceedingJoinPoint joinPoint, final int resultSetType,
      final int resultSetConcurrency) throws Throwable {
    System.out.println("measureStatement() #2");
    return addProxy(joinPoint, null, this.stProxies);
  }

  @Around(value = "execution(* java.sql.Connection.createStatement(..)) && args(resultSetType, resultSetConcurrency, resultSetHoldability)")
  private Object measureStatement(final ProceedingJoinPoint joinPoint, final int resultSetType,
      final int resultSetConcurrency, final int resultSetHoldability) throws Throwable {
    System.out.println("measureStatement() #3");
    return addProxy(joinPoint, null, this.stProxies);
  }

  // Adding aspect the the CallablaStatement

  private WeakHashMap<Integer, Object> csProxies = new WeakHashMap<>();

  @Around(value = "execution(* java.sql.Connection.prepareCall(..)) && args(sql)")
  private Object measurePrepareCall(final ProceedingJoinPoint joinPoint, final String sql) throws Throwable {
    System.out.println("measurePrepareCall()");
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
    System.out.println("st.executeQuery(sql) sql=" + sql);
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

//void  setArray(int parameterIndex, Array x)

  @Before(value = "execution(* java.sql.PreparedStatement.setArray(..)) && args(parameterIndex, x)")
  private void adviceSetArray(final JoinPoint joinPoint, final int parameterIndex, final Array x) throws Throwable {
    this.threadData.get().registerSetter(new ArraySetter(parameterIndex, x));
  }

//void  setAsciiStream(int parameterIndex, InputStream x)
//void  setAsciiStream(int parameterIndex, InputStream x, int length)
//void  setAsciiStream(int parameterIndex, InputStream x, long length)

  @Before(value = "execution(* java.sql.PreparedStatement.setAsciiStream(..)) && args(parameterIndex, x)")
  private void adviceSetAsciiStream(final JoinPoint joinPoint, final int parameterIndex, final InputStream x)
      throws Throwable {
    this.threadData.get().registerSetter(new AsciiStreamSetter(parameterIndex, x));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setAsciiStream(..)) && args(parameterIndex, x, length)")
  private void adviceSetAsciiStream(final JoinPoint joinPoint, final int parameterIndex, final InputStream x,
      long length) throws Throwable {
    this.threadData.get().registerSetter(new AsciiStreamSetter(parameterIndex, x));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setAsciiStream(..)) && args(parameterIndex, x, length)")
  private void adviceSetAsciiStream(final JoinPoint joinPoint, final int parameterIndex, final InputStream x,
      int length) throws Throwable {
    this.threadData.get().registerSetter(new AsciiStreamSetter(parameterIndex, x));
  }

//void  setBigDecimal(int parameterIndex, BigDecimal x)

  @Before(value = "execution(* java.sql.PreparedStatement.setBigDecimal(..)) && args(parameterIndex, x)")
  private void adviceSetBigDecimal(final JoinPoint joinPoint, final int parameterIndex, final BigDecimal x)
      throws Throwable {
    this.threadData.get().registerSetter(new BigDecimalSetter(parameterIndex, x));
  }

//void  setBinaryStream(int parameterIndex, InputStream x)
//void  setBinaryStream(int parameterIndex, InputStream x, int length)
//void  setBinaryStream(int parameterIndex, InputStream x, long length)

  @Before(value = "execution(* java.sql.PreparedStatement.setBinaryStream(..)) && args(parameterIndex, x)")
  private void adviceSetBinaryStream(final JoinPoint joinPoint, final int parameterIndex, final InputStream x)
      throws Throwable {
    this.threadData.get().registerSetter(new BinaryStreamSetter(parameterIndex, x));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setBinaryStream(..)) && args(parameterIndex, x, length)")
  private void adviceSetBinaryStream(final JoinPoint joinPoint, final int parameterIndex, final InputStream x,
      int length) throws Throwable {
    this.threadData.get().registerSetter(new BinaryStreamSetter(parameterIndex, x, length));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setBinaryStream(..)) && args(parameterIndex, x, length)")
  private void adviceSetBinaryStream(final JoinPoint joinPoint, final int parameterIndex, final InputStream x,
      long length) throws Throwable {
    this.threadData.get().registerSetter(new BinaryStreamSetter(parameterIndex, x, length));
  }

//void  setBlob(int parameterIndex, Blob x)
//void  setBlob(int parameterIndex, InputStream inputStream)
//void  setBlob(int parameterIndex, InputStream inputStream, long length)

  @Before(value = "execution(* java.sql.PreparedStatement.setBlob(..)) && args(parameterIndex, x)")
  private void adviceSetBlob(final JoinPoint joinPoint, final int parameterIndex, final Blob x) throws Throwable {
    this.threadData.get().registerSetter(new BlobSetter(parameterIndex, x));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setBlob(..)) && args(parameterIndex, inputStream)")
  private void adviceSetBlob(final JoinPoint joinPoint, final int parameterIndex, final InputStream inputStream)
      throws Throwable {
    this.threadData.get().registerSetter(new BlobSetter(parameterIndex, inputStream));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setBlob(..)) && args(parameterIndex, inputStream, length)")
  private void adviceSetBlob(final JoinPoint joinPoint, final int parameterIndex, final InputStream inputStream,
      final int length) throws Throwable {
    this.threadData.get().registerSetter(new BlobSetter(parameterIndex, inputStream));
  }

//void  setBoolean(int parameterIndex, boolean x)

  @Before(value = "execution(* java.sql.PreparedStatement.setBoolean(..)) && args(parameterIndex, x)")
  private void adviceSetBoolean(final JoinPoint joinPoint, final int parameterIndex, final boolean x) throws Throwable {
    this.threadData.get().registerSetter(new BooleanSetter(parameterIndex, x));
  }

//void  setByte(int parameterIndex, byte x)

  @Before(value = "execution(* java.sql.PreparedStatement.setByte(..)) && args(parameterIndex, x)")
  private void adviceSetByte(final JoinPoint joinPoint, final int parameterIndex, final byte x) throws Throwable {
    this.threadData.get().registerSetter(new ByteSetter(parameterIndex, x));
  }

//void  setBytes(int parameterIndex, byte[] x)

  @Before(value = "execution(* java.sql.PreparedStatement.setBytes(..)) && args(parameterIndex, x)")
  private void adviceSetBytes(final JoinPoint joinPoint, final int parameterIndex, final byte[] x) throws Throwable {
    this.threadData.get().registerSetter(new BytesSetter(parameterIndex, x));
  }

//void  setCharacterStream(int parameterIndex, Reader reader)
//void  setCharacterStream(int parameterIndex, Reader reader, int length)
//void  setCharacterStream(int parameterIndex, Reader reader, long length)

  @Before(value = "execution(* java.sql.PreparedStatement.setCharacterStream(..)) && args(parameterIndex, reader)")
  private void adviceSetCharacterStream(final JoinPoint joinPoint, final int parameterIndex, final Reader reader)
      throws Throwable {
    this.threadData.get().registerSetter(new CharacterStreamSetter(parameterIndex, reader));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setCharacterStream(..)) && args(parameterIndex, reader, length)")
  private void adviceSetCharacterStream(final JoinPoint joinPoint, final int parameterIndex, final Reader reader,
      final int length) throws Throwable {
    this.threadData.get().registerSetter(new CharacterStreamSetter(parameterIndex, reader, length));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setCharacterStream(..)) && args(parameterIndex, reader, length)")
  private void adviceSetCharacterStream(final JoinPoint joinPoint, final int parameterIndex, final Reader reader,
      final long length) throws Throwable {
    this.threadData.get().registerSetter(new CharacterStreamSetter(parameterIndex, reader, length));
  }

//void  setClob(int parameterIndex, Clob x)
//void  setClob(int parameterIndex, Reader reader)
//void  setClob(int parameterIndex, Reader reader, long length)

  @Before(value = "execution(* java.sql.PreparedStatement.setClob(..)) && args(parameterIndex, x)")
  private void adviceSetClob(final JoinPoint joinPoint, final int parameterIndex, final Clob x) throws Throwable {
    this.threadData.get().registerSetter(new ClobSetter(parameterIndex, x));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setClob(..)) && args(parameterIndex, reader)")
  private void adviceSetClob(final JoinPoint joinPoint, final int parameterIndex, final Reader reader)
      throws Throwable {
    this.threadData.get().registerSetter(new ClobSetter(parameterIndex, reader));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setClob(..)) && args(parameterIndex, reader, length)")
  private void adviceSetClob(final JoinPoint joinPoint, final int parameterIndex, final Reader reader,
      final long length) throws Throwable {
    this.threadData.get().registerSetter(new ClobSetter(parameterIndex, reader, length));
  }

//void  setDate(int parameterIndex, Date x)
//void  setDate(int parameterIndex, Date x, Calendar cal)

  @Before(value = "execution(* java.sql.PreparedStatement.setDate(..)) && args(parameterIndex, x)")
  private void adviceSetDate(final JoinPoint joinPoint, final int parameterIndex, final Date x) throws Throwable {
    this.threadData.get().registerSetter(new DateSetter(parameterIndex, x));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setDate(..)) && args(parameterIndex, x, cal)")
  private void adviceSetDate(final JoinPoint joinPoint, final int parameterIndex, final Date x, final Calendar cal)
      throws Throwable {
    this.threadData.get().registerSetter(new DateSetter(parameterIndex, x, cal));
  }

//void  setDouble(int parameterIndex, double x)

  @Before(value = "execution(* java.sql.PreparedStatement.setDouble(..)) && args(parameterIndex, x)")
  private void adviceSetDouble(final JoinPoint joinPoint, final int parameterIndex, final double x) throws Throwable {
    this.threadData.get().registerSetter(new DoubleSetter(parameterIndex, x));
  }

//void  setFloat(int parameterIndex, float x)

  @Before(value = "execution(* java.sql.PreparedStatement.setFloat(..)) && args(parameterIndex, x)")
  private void adviceSetFloat(final JoinPoint joinPoint, final int parameterIndex, final float x) throws Throwable {
    this.threadData.get().registerSetter(new FloatSetter(parameterIndex, x));
  }

//void  setInt(int parameterIndex, int x)

  @Before(value = "execution(* java.sql.PreparedStatement.setInt(..)) && args(parameterIndex, x)")
  private void adviceSetInt(final JoinPoint joinPoint, final int parameterIndex, final int x) throws Throwable {
    this.threadData.get().registerSetter(new IntSetter(parameterIndex, x));
  }

//void  setLong(int parameterIndex, long x)

  @Before(value = "execution(* java.sql.PreparedStatement.setLong(..)) && args(parameterIndex, x)")
  private void adviceSetLong(final JoinPoint joinPoint, final int parameterIndex, final long x) throws Throwable {
    this.threadData.get().registerSetter(new LongSetter(parameterIndex, x));
  }

//void  setNCharacterStream(int parameterIndex, Reader value)
//void  setNCharacterStream(int parameterIndex, Reader value, long length)

  @Before(value = "execution(* java.sql.PreparedStatement.setNCharacterStream(..)) && args(parameterIndex, value)")
  private void adviceSetNCharacterStream(final JoinPoint joinPoint, final int parameterIndex, final Reader value)
      throws Throwable {
    this.threadData.get().registerSetter(new NCharacterStreamSetter(parameterIndex, value));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setNCharacterStream(..)) && args(parameterIndex, value, length)")
  private void adviceSetNCharacterStream(final JoinPoint joinPoint, final int parameterIndex, final Reader value,
      final long length) throws Throwable {
    this.threadData.get().registerSetter(new NCharacterStreamSetter(parameterIndex, value, length));
  }

//void  setNClob(int parameterIndex, NClob value)
//void  setNClob(int parameterIndex, Reader reader)
//void  setNClob(int parameterIndex, Reader reader, long length)

  @Before(value = "execution(* java.sql.PreparedStatement.setNClob(..)) && args(parameterIndex, value)")
  private void adviceSetNClob(final JoinPoint joinPoint, final int parameterIndex, final NClob value) throws Throwable {
    this.threadData.get().registerSetter(new NClobSetter(parameterIndex, value));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setNClob(..)) && args(parameterIndex, reader)")
  private void adviceSetNClob(final JoinPoint joinPoint, final int parameterIndex, final Reader reader)
      throws Throwable {
    this.threadData.get().registerSetter(new NClobSetter(parameterIndex, reader));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setNClob(..)) && args(parameterIndex, reader, length)")
  private void adviceSetNClob(final JoinPoint joinPoint, final int parameterIndex, final Reader reader,
      final long length) throws Throwable {
    this.threadData.get().registerSetter(new NClobSetter(parameterIndex, reader, length));
  }

//void  setNString(int parameterIndex, String value)

  @Before(value = "execution(* java.sql.PreparedStatement.setNString(..)) && args(parameterIndex, value)")
  private void adviceSetNString(final JoinPoint joinPoint, final int parameterIndex, final String value)
      throws Throwable {
    this.threadData.get().registerSetter(new NStringSetter(parameterIndex, value));
  }

//void  setNull(int parameterIndex, int sqlType)
//void  setNull(int parameterIndex, int sqlType, String typeName)

  @Before(value = "execution(* java.sql.PreparedStatement.setNull(..)) && args(parameterIndex, sqlType)")
  private void adviceSetNull(final JoinPoint joinPoint, final int parameterIndex, final int sqlType) throws Throwable {
    this.threadData.get().registerSetter(new NullSetter(parameterIndex, sqlType));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setNull(..)) && args(parameterIndex, sqlType, typeName)")
  private void adviceSetNull(final JoinPoint joinPoint, final int parameterIndex, final int sqlType,
      final String typeName) throws Throwable {
    this.threadData.get().registerSetter(new NullSetter(parameterIndex, sqlType, typeName));
  }

//void  setObject(int parameterIndex, Object x)
//void  setObject(int parameterIndex, Object x, int targetSqlType)
//void  setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength)
//default void  setObject(int parameterIndex, Object x, SQLType targetSqlType)
//default void  setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength)

  @Before(value = "execution(* java.sql.PreparedStatement.setObject(..)) && args(parameterIndex, x)")
  private void adviceSetObject(final JoinPoint joinPoint, final int parameterIndex, final Object x) throws Throwable {
    this.threadData.get().registerSetter(new ObjectSetter(parameterIndex, x));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setObject(..)) && args(parameterIndex, x, targetSqlType)")
  private void adviceSetObject(final JoinPoint joinPoint, final int parameterIndex, final Object x,
      final int targetSqlType) throws Throwable {
    this.threadData.get().registerSetter(new ObjectSetter(parameterIndex, x, targetSqlType));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setObject(..)) && args(parameterIndex, x, targetSqlType, scaleOrLength)")
  private void adviceSetObject(final JoinPoint joinPoint, final int parameterIndex, final Object x,
      final int targetSqlType, final int scaleOrLength) throws Throwable {
    this.threadData.get().registerSetter(new ObjectSetter(parameterIndex, x, targetSqlType, scaleOrLength));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setObject(..)) && args(parameterIndex, x, targetSqlType)")
  private void adviceSetObject(final JoinPoint joinPoint, final int parameterIndex, final SQLType x,
      final int targetSqlType) throws Throwable {
    this.threadData.get().registerSetter(new ObjectSetter(parameterIndex, x, targetSqlType));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setObject(..)) && args(parameterIndex, x, targetSqlType, scaleOrLength)")
  private void adviceSetObject(final JoinPoint joinPoint, final int parameterIndex, final SQLType x,
      final int targetSqlType, final int scaleOrLength) throws Throwable {
    this.threadData.get().registerSetter(new ObjectSetter(parameterIndex, x, targetSqlType, scaleOrLength));
  }

//void  setRef(int parameterIndex, Ref x)

  @Before(value = "execution(* java.sql.PreparedStatement.setRef(..)) && args(parameterIndex, x)")
  private void adviceSetRef(final JoinPoint joinPoint, final int parameterIndex, final Ref x) throws Throwable {
    this.threadData.get().registerSetter(new RefSetter(parameterIndex, x));
  }

//void  setRowId(int parameterIndex, RowId x)

  @Before(value = "execution(* java.sql.PreparedStatement.setRowId(..)) && args(parameterIndex, x)")
  private void adviceSetRowId(final JoinPoint joinPoint, final int parameterIndex, final RowId x) throws Throwable {
    this.threadData.get().registerSetter(new RowIdSetter(parameterIndex, x));
  }

//void  setShort(int parameterIndex, short x)

  @Before(value = "execution(* java.sql.PreparedStatement.setShort(..)) && args(parameterIndex, x)")
  private void adviceSetShort(final JoinPoint joinPoint, final int parameterIndex, final short x) throws Throwable {
    this.threadData.get().registerSetter(new ShortSetter(parameterIndex, x));
  }

//void  setSQLXML(int parameterIndex, SQLXML xmlObject)

  @Before(value = "execution(* java.sql.PreparedStatement.setSQLXML(..)) && args(parameterIndex, xmlObject)")
  private void adviceSetSQLXML(final JoinPoint joinPoint, final int parameterIndex, final SQLXML xmlObject)
      throws Throwable {
    this.threadData.get().registerSetter(new SQLXMLSetter(parameterIndex, xmlObject));
  }

//void  setString(int parameterIndex, String x)

  @Before(value = "execution(* java.sql.PreparedStatement.setString(..)) && args(parameterIndex, x)")
  private void adviceSetString(final JoinPoint joinPoint, final int parameterIndex, final String x) throws Throwable {
    this.threadData.get().registerSetter(new StringSetter(parameterIndex, x));
  }

//void  setTime(int parameterIndex, Time x)
//void  setTime(int parameterIndex, Time x, Calendar cal)

  @Before(value = "execution(* java.sql.PreparedStatement.setTime(..)) && args(parameterIndex, x)")
  private void adviceSetTime(final JoinPoint joinPoint, final int parameterIndex, final Time x) throws Throwable {
    this.threadData.get().registerSetter(new TimeSetter(parameterIndex, x));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setTime(..)) && args(parameterIndex, x, cal)")
  private void adviceSetTime(final JoinPoint joinPoint, final int parameterIndex, final Time x, final Calendar cal)
      throws Throwable {
    this.threadData.get().registerSetter(new TimeSetter(parameterIndex, x, cal));
  }

//void  setTimestamp(int parameterIndex, Timestamp x)
//void  setTimestamp(int parameterIndex, Timestamp x, Calendar cal)

  @Before(value = "execution(* java.sql.PreparedStatement.setTimestamp(..)) && args(parameterIndex, x)")
  private void adviceSetTimestamp(final JoinPoint joinPoint, final int parameterIndex, final Timestamp x)
      throws Throwable {
    this.threadData.get().registerSetter(new TimestampSetter(parameterIndex, x));
  }

  @Before(value = "execution(* java.sql.PreparedStatement.setTimestamp(..)) && args(parameterIndex, x, cal)")
  private void adviceSetTimestamp(final JoinPoint joinPoint, final int parameterIndex, final Timestamp x,
      final Calendar cal) throws Throwable {
    this.threadData.get().registerSetter(new TimestampSetter(parameterIndex, x, cal));
  }

//void  setUnicodeStream(int parameterIndex, InputStream x, int length)

  @Before(value = "execution(* java.sql.PreparedStatement.setUnicodeStream(..)) && args(parameterIndex, x, length)")
  private void adviceSetUnicodeStream(final JoinPoint joinPoint, final int parameterIndex, final InputStream x,
      final int length) throws Throwable {
    this.threadData.get().registerSetter(new UnicodeStreamSetter(parameterIndex, x, length));
  }

//void  setURL(int parameterIndex, URL x)

  @Before(value = "execution(* java.sql.PreparedStatement.setURL(..)) && args(parameterIndex, x)")
  private void adviceSetURL(final JoinPoint joinPoint, final int parameterIndex, final URL x) throws Throwable {
    this.threadData.get().registerSetter(new URLSetter(parameterIndex, x));
  }

  // ======================================
  // End of PreparedStatement.set() methods
  // ======================================

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
