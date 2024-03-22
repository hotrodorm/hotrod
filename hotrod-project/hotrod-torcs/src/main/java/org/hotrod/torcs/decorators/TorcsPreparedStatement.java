package org.hotrod.torcs.decorators;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Logger;

import org.hotrod.torcs.DataSourceReference;
import org.hotrod.torcs.Torcs;

public class TorcsPreparedStatement extends TorcsStatement implements PreparedStatement {

  private static final Logger log = Logger.getLogger(TorcsStatement.class.getName());

  private PreparedStatement wrapped;
  protected String sql;

  public TorcsPreparedStatement(PreparedStatement wrapped, TorcsConnection conn, Torcs torcs,
      DataSourceReference dataSourceReference, String sql) {
    super(wrapped, conn, torcs, dataSourceReference);
    this.wrapped = wrapped;
    this.sql = sql;
    log.info("constructor -- this.wrapped=" + this.wrapped);
  }

  @Override
  public void addBatch() throws SQLException {
    this.wrapped.addBatch();
  }

  @Override
  public void clearParameters() throws SQLException {
    this.wrapped.clearParameters();
  }

  @Override
  public boolean execute() throws SQLException {
    log.info("execute");
    long start = System.currentTimeMillis();
    try {
      boolean r = this.wrapped.execute(); // delegate
      long end = System.currentTimeMillis();
      this.torcs.record(super.dataSourceReference, sql, null, (int) (end - start), null);
      return r;
    } catch (Throwable t) {
      log.info("executed failed");
      long end = System.currentTimeMillis();
      this.torcs.record(super.dataSourceReference, sql, null, (int) (end - start), null);
      throw t;
    }
  }

  @Override
  public ResultSet executeQuery() throws SQLException {
    log.info("execute");
    long start = System.currentTimeMillis();
    try {
      ResultSet r = this.wrapped.executeQuery(); // delegate
      long end = System.currentTimeMillis();
      this.torcs.record(super.dataSourceReference, sql, null, (int) (end - start), null);
      return r;
    } catch (Throwable t) {
      log.info("executed failed");
      long end = System.currentTimeMillis();
      this.torcs.record(super.dataSourceReference, sql, null, (int) (end - start), null);
      throw t;
    }
  }

  @Override
  public int executeUpdate() throws SQLException {
    log.info("execute");
    long start = System.currentTimeMillis();
    try {
      int r = this.wrapped.executeUpdate(); // delegate
      long end = System.currentTimeMillis();
      this.torcs.record(super.dataSourceReference, sql, null, (int) (end - start), null);
      return r;
    } catch (Throwable t) {
      log.info("executed failed");
      long end = System.currentTimeMillis();
      this.torcs.record(super.dataSourceReference, sql, null, (int) (end - start), null);
      throw t;
    }
  }

  @Override
  public long executeLargeUpdate() throws SQLException {
    log.info("execute");
    long start = System.currentTimeMillis();
    try {
      long r = this.wrapped.executeLargeUpdate(); // delegate
      long end = System.currentTimeMillis();
      this.torcs.record(super.dataSourceReference, sql, null, (int) (end - start), null);
      return r;
    } catch (Throwable t) {
      log.info("executed failed");
      long end = System.currentTimeMillis();
      this.torcs.record(super.dataSourceReference, sql, null, (int) (end - start), null);
      throw t;
    }
  }

  @Override
  public ResultSetMetaData getMetaData() throws SQLException {
    return this.wrapped.getMetaData();
  }

  @Override
  public ParameterMetaData getParameterMetaData() throws SQLException {
    return this.wrapped.getParameterMetaData();
  }

  @Override
  public void setArray(int parameterIndex, Array x) throws SQLException {
    this.wrapped.setArray(parameterIndex, x);
  }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
    this.wrapped.setAsciiStream(parameterIndex, x);
  }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
    this.wrapped.setAsciiStream(parameterIndex, x, length);
  }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
    this.wrapped.setAsciiStream(parameterIndex, x, length);
  }

  @Override
  public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
    this.wrapped.setBigDecimal(parameterIndex, x);
  }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
    this.wrapped.setBinaryStream(parameterIndex, x);
  }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
    this.wrapped.setBinaryStream(parameterIndex, x, length);
  }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
    this.wrapped.setBinaryStream(parameterIndex, x, length);
  }

  @Override
  public void setBlob(int parameterIndex, Blob x) throws SQLException {
    this.wrapped.setBlob(parameterIndex, x);
  }

  @Override
  public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
    this.wrapped.setBlob(parameterIndex, inputStream);
  }

  @Override
  public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
    this.wrapped.setBlob(parameterIndex, inputStream, length);
  }

  @Override
  public void setBoolean(int parameterIndex, boolean x) throws SQLException {
    this.wrapped.setBoolean(parameterIndex, x);
  }

  @Override
  public void setByte(int parameterIndex, byte x) throws SQLException {
    this.wrapped.setByte(parameterIndex, x);
  }

  @Override
  public void setBytes(int parameterIndex, byte[] x) throws SQLException {
    this.wrapped.setBytes(parameterIndex, x);
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
    this.wrapped.setCharacterStream(parameterIndex, reader);
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
    this.wrapped.setCharacterStream(parameterIndex, reader, length);
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
    this.wrapped.setCharacterStream(parameterIndex, reader, length);
  }

  @Override
  public void setClob(int parameterIndex, Clob x) throws SQLException {
    this.wrapped.setClob(parameterIndex, x);
  }

  @Override
  public void setClob(int parameterIndex, Reader reader) throws SQLException {
    this.wrapped.setClob(parameterIndex, reader);
  }

  @Override
  public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
    this.wrapped.setClob(parameterIndex, reader, length);
  }

  @Override
  public void setDate(int parameterIndex, Date x) throws SQLException {
    this.wrapped.setDate(parameterIndex, x);
  }

  @Override
  public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
    this.wrapped.setDate(parameterIndex, x, cal);
  }

  @Override
  public void setDouble(int parameterIndex, double x) throws SQLException {
    this.wrapped.setDouble(parameterIndex, x);

  }

  @Override
  public void setFloat(int parameterIndex, float x) throws SQLException {
    this.wrapped.setFloat(parameterIndex, x);
  }

  @Override
  public void setInt(int parameterIndex, int x) throws SQLException {
    this.wrapped.setInt(parameterIndex, x);
  }

  @Override
  public void setLong(int parameterIndex, long x) throws SQLException {
    this.wrapped.setLong(parameterIndex, x);
  }

  @Override
  public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
    this.wrapped.setNCharacterStream(parameterIndex, value);
  }

  @Override
  public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
    this.wrapped.setNCharacterStream(parameterIndex, value, length);
  }

  @Override
  public void setNClob(int parameterIndex, NClob value) throws SQLException {
    this.wrapped.setNClob(parameterIndex, value);
  }

  @Override
  public void setNClob(int parameterIndex, Reader reader) throws SQLException {
    this.wrapped.setNClob(parameterIndex, reader);
  }

  @Override
  public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
    this.wrapped.setNClob(parameterIndex, reader, length);
  }

  @Override
  public void setNString(int parameterIndex, String value) throws SQLException {
    this.wrapped.setNString(parameterIndex, value);
  }

  @Override
  public void setNull(int parameterIndex, int sqlType) throws SQLException {
    this.wrapped.setNull(parameterIndex, sqlType);
  }

  @Override
  public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
    this.wrapped.setNull(parameterIndex, sqlType, typeName);
  }

  @Override
  public void setObject(int parameterIndex, Object x) throws SQLException {
    this.wrapped.setObject(parameterIndex, x);
  }

  @Override
  public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
    this.wrapped.setObject(parameterIndex, x, targetSqlType);
  }

  @Override
  public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
    this.wrapped.setObject(parameterIndex, x, targetSqlType);
  }

  @Override
  public void setRef(int parameterIndex, Ref x) throws SQLException {
    this.wrapped.setRef(parameterIndex, x);
  }

  @Override
  public void setRowId(int parameterIndex, RowId x) throws SQLException {
    this.wrapped.setRowId(parameterIndex, x);
  }

  @Override
  public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
    this.wrapped.setSQLXML(parameterIndex, xmlObject);
  }

  @Override
  public void setShort(int parameterIndex, short x) throws SQLException {
    this.wrapped.setShort(parameterIndex, x);
  }

  @Override
  public void setString(int parameterIndex, String x) throws SQLException {
    this.wrapped.setString(parameterIndex, x);
  }

  @Override
  public void setTime(int parameterIndex, Time x) throws SQLException {
    this.wrapped.setTime(parameterIndex, x);
  }

  @Override
  public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
    this.wrapped.setTime(parameterIndex, x, cal);
  }

  @Override
  public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
    this.wrapped.setTimestamp(parameterIndex, x);
  }

  @Override
  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
    this.wrapped.setTimestamp(parameterIndex, x, cal);
  }

  @Override
  public void setURL(int parameterIndex, URL x) throws SQLException {
    this.wrapped.setURL(parameterIndex, x);
  }

  @Override
  @Deprecated
  public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
    this.wrapped.setUnicodeStream(parameterIndex, x, length);
  }

}
