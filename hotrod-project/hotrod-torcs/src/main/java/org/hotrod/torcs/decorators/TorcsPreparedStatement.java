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
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.hotrod.torcs.DataSourceReference;
import org.hotrod.torcs.Torcs;
import org.hotrod.torcs.setters.index.ArraySetter;
import org.hotrod.torcs.setters.index.AsciiStreamSetter;
import org.hotrod.torcs.setters.index.BigDecimalSetter;
import org.hotrod.torcs.setters.index.BinaryStreamSetter;
import org.hotrod.torcs.setters.index.BlobSetter;
import org.hotrod.torcs.setters.index.BooleanSetter;
import org.hotrod.torcs.setters.index.ByteSetter;
import org.hotrod.torcs.setters.index.BytesSetter;
import org.hotrod.torcs.setters.index.CharacterStreamSetter;
import org.hotrod.torcs.setters.index.ClobSetter;
import org.hotrod.torcs.setters.index.DateSetter;
import org.hotrod.torcs.setters.index.DoubleSetter;
import org.hotrod.torcs.setters.index.FloatSetter;
import org.hotrod.torcs.setters.index.IndexSetter;
import org.hotrod.torcs.setters.index.IntSetter;
import org.hotrod.torcs.setters.index.LongSetter;
import org.hotrod.torcs.setters.index.NCharacterStreamSetter;
import org.hotrod.torcs.setters.index.NClobSetter;
import org.hotrod.torcs.setters.index.NStringSetter;
import org.hotrod.torcs.setters.index.NullSetter;
import org.hotrod.torcs.setters.index.ObjectSetter;
import org.hotrod.torcs.setters.index.RefSetter;
import org.hotrod.torcs.setters.index.RowIdSetter;
import org.hotrod.torcs.setters.index.SQLXMLSetter;
import org.hotrod.torcs.setters.index.ShortSetter;
import org.hotrod.torcs.setters.index.StringSetter;
import org.hotrod.torcs.setters.index.TimeSetter;
import org.hotrod.torcs.setters.index.TimestampSetter;
import org.hotrod.torcs.setters.index.URLSetter;
import org.hotrod.torcs.setters.index.UnicodeStreamSetter;
import org.hotrod.torcs.setters.name.NameSetter;

public class TorcsPreparedStatement extends TorcsStatement implements PreparedStatement {

  private static final Logger log = Logger.getLogger(TorcsStatement.class.getName());

  private PreparedStatement wrapped;
  protected String sql;
  protected Map<Integer, IndexSetter> indexSetters;
  protected Map<String, NameSetter> nameSetters;

  public TorcsPreparedStatement(PreparedStatement wrapped, TorcsConnection conn, Torcs torcs,
      DataSourceReference dataSourceReference, String sql) {
    super(wrapped, conn, torcs, dataSourceReference);
    this.wrapped = wrapped;
    this.sql = sql;
    this.indexSetters = new HashMap<>();
  }

  protected void registerSetter(final IndexSetter setter) {
    this.indexSetters.put(setter.getIndex(), setter);
  }

  protected void registerSetter(final NameSetter setter) {
    this.nameSetters.put(setter.getName(), setter);
  }

  @Override
  public void addBatch() throws SQLException {
    this.wrapped.addBatch();
  }

  @Override
  public void clearParameters() throws SQLException {
    this.wrapped.clearParameters();
    this.indexSetters.clear();
  }

  @Override
  public boolean execute() throws SQLException {
    log.fine("execute");
    long start = System.currentTimeMillis();
    try {
      boolean r = this.wrapped.execute(); // delegate
      long end = System.currentTimeMillis();
      this.torcs.record(super.dataSourceReference, sql, indexSetters, nameSetters, (int) (end - start), null);
      return r;
    } catch (Throwable t) {
      log.fine("executed failed");
      long end = System.currentTimeMillis();
      this.torcs.record(super.dataSourceReference, sql, indexSetters, nameSetters, (int) (end - start), t);
      throw t;
    }
  }

  @Override
  public ResultSet executeQuery() throws SQLException {
    log.fine("execute");
    long start = System.currentTimeMillis();
    try {
      ResultSet r = this.wrapped.executeQuery(); // delegate
      long end = System.currentTimeMillis();
      this.torcs.record(super.dataSourceReference, sql, indexSetters, nameSetters, (int) (end - start), null);
      return r;
    } catch (Throwable t) {
      log.fine("executed failed");
      long end = System.currentTimeMillis();
      this.torcs.record(super.dataSourceReference, sql, indexSetters, nameSetters, (int) (end - start), t);
      throw t;
    }
  }

  @Override
  public int executeUpdate() throws SQLException {
    log.fine("execute");
    long start = System.currentTimeMillis();
    try {
      int r = this.wrapped.executeUpdate(); // delegate
      long end = System.currentTimeMillis();
      this.torcs.record(super.dataSourceReference, sql, indexSetters, nameSetters, (int) (end - start), null);
      return r;
    } catch (Throwable t) {
      log.fine("executed failed");
      long end = System.currentTimeMillis();
      this.torcs.record(super.dataSourceReference, sql, indexSetters, nameSetters, (int) (end - start), t);
      throw t;
    }
  }

  @Override
  public long executeLargeUpdate() throws SQLException {
    log.fine("execute");
    long start = System.currentTimeMillis();
    try {
      long r = this.wrapped.executeLargeUpdate(); // delegate
      long end = System.currentTimeMillis();
      this.torcs.record(super.dataSourceReference, sql, indexSetters, nameSetters, (int) (end - start), null);
      return r;
    } catch (Throwable t) {
      log.fine("executed failed");
      long end = System.currentTimeMillis();
      this.torcs.record(super.dataSourceReference, sql, indexSetters, nameSetters, (int) (end - start), t);
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
    this.registerSetter(new ArraySetter(parameterIndex, x));
  }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
    this.wrapped.setAsciiStream(parameterIndex, x);
    this.registerSetter(new AsciiStreamSetter(parameterIndex, x));
  }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
    this.wrapped.setAsciiStream(parameterIndex, x, length);
    this.registerSetter(new AsciiStreamSetter(parameterIndex, x));
  }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
    this.wrapped.setAsciiStream(parameterIndex, x, length);
    this.registerSetter(new AsciiStreamSetter(parameterIndex, x));
  }

  @Override
  public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
    this.wrapped.setBigDecimal(parameterIndex, x);
    this.registerSetter(new BigDecimalSetter(parameterIndex, x));
  }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
    this.wrapped.setBinaryStream(parameterIndex, x);
    this.registerSetter(new BinaryStreamSetter(parameterIndex, x));
  }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
    this.wrapped.setBinaryStream(parameterIndex, x, length);
    this.registerSetter(new BinaryStreamSetter(parameterIndex, x, length));
  }

  @Override
  public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
    this.wrapped.setBinaryStream(parameterIndex, x, length);
    this.registerSetter(new BinaryStreamSetter(parameterIndex, x, length));
  }

  @Override
  public void setBlob(int parameterIndex, Blob x) throws SQLException {
    this.wrapped.setBlob(parameterIndex, x);
    this.registerSetter(new BlobSetter(parameterIndex, x));
  }

  @Override
  public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
    this.wrapped.setBlob(parameterIndex, inputStream);
    this.registerSetter(new BlobSetter(parameterIndex, inputStream));
  }

  @Override
  public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
    this.wrapped.setBlob(parameterIndex, inputStream, length);
    this.registerSetter(new BlobSetter(parameterIndex, inputStream));
  }

  @Override
  public void setBoolean(int parameterIndex, boolean x) throws SQLException {
    this.wrapped.setBoolean(parameterIndex, x);
    this.registerSetter(new BooleanSetter(parameterIndex, x));
  }

  @Override
  public void setByte(int parameterIndex, byte x) throws SQLException {
    this.wrapped.setByte(parameterIndex, x);
    this.registerSetter(new ByteSetter(parameterIndex, x));
  }

  @Override
  public void setBytes(int parameterIndex, byte[] x) throws SQLException {
    this.wrapped.setBytes(parameterIndex, x);
    this.registerSetter(new BytesSetter(parameterIndex, x));
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
    this.wrapped.setCharacterStream(parameterIndex, reader);
    this.registerSetter(new CharacterStreamSetter(parameterIndex, reader));
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
    this.wrapped.setCharacterStream(parameterIndex, reader, length);
    this.registerSetter(new CharacterStreamSetter(parameterIndex, reader, length));
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
    this.wrapped.setCharacterStream(parameterIndex, reader, length);
    this.registerSetter(new CharacterStreamSetter(parameterIndex, reader, length));
  }

  @Override
  public void setClob(int parameterIndex, Clob x) throws SQLException {
    this.wrapped.setClob(parameterIndex, x);
    this.registerSetter(new ClobSetter(parameterIndex, x));
  }

  @Override
  public void setClob(int parameterIndex, Reader reader) throws SQLException {
    this.wrapped.setClob(parameterIndex, reader);
    this.registerSetter(new ClobSetter(parameterIndex, reader));
  }

  @Override
  public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
    this.wrapped.setClob(parameterIndex, reader, length);
    this.registerSetter(new ClobSetter(parameterIndex, reader, length));
  }

  @Override
  public void setDate(int parameterIndex, Date x) throws SQLException {
    this.wrapped.setDate(parameterIndex, x);
    this.registerSetter(new DateSetter(parameterIndex, x));
  }

  @Override
  public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
    this.wrapped.setDate(parameterIndex, x, cal);
    this.registerSetter(new DateSetter(parameterIndex, x, cal));
  }

  @Override
  public void setDouble(int parameterIndex, double x) throws SQLException {
    this.wrapped.setDouble(parameterIndex, x);
    this.registerSetter(new DoubleSetter(parameterIndex, x));
  }

  @Override
  public void setFloat(int parameterIndex, float x) throws SQLException {
    this.wrapped.setFloat(parameterIndex, x);
    this.registerSetter(new FloatSetter(parameterIndex, x));
  }

  @Override
  public void setInt(int parameterIndex, int x) throws SQLException {
    this.wrapped.setInt(parameterIndex, x);
    this.registerSetter(new IntSetter(parameterIndex, x));
  }

  @Override
  public void setLong(int parameterIndex, long x) throws SQLException {
    this.wrapped.setLong(parameterIndex, x);
    this.registerSetter(new LongSetter(parameterIndex, x));
  }

  @Override
  public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
    this.wrapped.setNCharacterStream(parameterIndex, value);
    this.registerSetter(new NCharacterStreamSetter(parameterIndex, value));
  }

  @Override
  public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
    this.wrapped.setNCharacterStream(parameterIndex, value, length);
    this.registerSetter(new NCharacterStreamSetter(parameterIndex, value, length));
  }

  @Override
  public void setNClob(int parameterIndex, NClob value) throws SQLException {
    this.wrapped.setNClob(parameterIndex, value);
    this.registerSetter(new NClobSetter(parameterIndex, value));
  }

  @Override
  public void setNClob(int parameterIndex, Reader reader) throws SQLException {
    this.wrapped.setNClob(parameterIndex, reader);
    this.registerSetter(new NClobSetter(parameterIndex, reader));
  }

  @Override
  public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
    this.wrapped.setNClob(parameterIndex, reader, length);
    this.registerSetter(new NClobSetter(parameterIndex, reader, length));
  }

  @Override
  public void setNString(int parameterIndex, String value) throws SQLException {
    this.wrapped.setNString(parameterIndex, value);
    this.registerSetter(new NStringSetter(parameterIndex, value));
  }

  @Override
  public void setNull(int parameterIndex, int sqlType) throws SQLException {
    this.wrapped.setNull(parameterIndex, sqlType);
    this.registerSetter(new NullSetter(parameterIndex, sqlType));
  }

  @Override
  public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
    this.wrapped.setNull(parameterIndex, sqlType, typeName);
    this.registerSetter(new NullSetter(parameterIndex, sqlType, typeName));
  }

  @Override
  public void setObject(int parameterIndex, Object x) throws SQLException {
    this.wrapped.setObject(parameterIndex, x);
    this.registerSetter(new ObjectSetter(parameterIndex, x));
  }

  @Override
  public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
    this.wrapped.setObject(parameterIndex, x, targetSqlType);
    this.registerSetter(new ObjectSetter(parameterIndex, x, targetSqlType));
  }

  @Override
  public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
    this.wrapped.setObject(parameterIndex, x, targetSqlType);
    this.registerSetter(new ObjectSetter(parameterIndex, x, targetSqlType, scaleOrLength));
  }

  @Override
  public void setObject(int parameterIndex, Object x, SQLType targetSqlType) throws SQLException {
    this.wrapped.setObject(parameterIndex, x, targetSqlType);
    this.registerSetter(new ObjectSetter(parameterIndex, x, targetSqlType));
  }

  @Override
  public void setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
    this.wrapped.setObject(parameterIndex, x, targetSqlType);
    this.registerSetter(new ObjectSetter(parameterIndex, x, targetSqlType, scaleOrLength));
  }

  @Override
  public void setRef(int parameterIndex, Ref x) throws SQLException {
    this.wrapped.setRef(parameterIndex, x);
    this.registerSetter(new RefSetter(parameterIndex, x));
  }

  @Override
  public void setRowId(int parameterIndex, RowId x) throws SQLException {
    this.wrapped.setRowId(parameterIndex, x);
    this.registerSetter(new RowIdSetter(parameterIndex, x));
  }

  @Override
  public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
    this.wrapped.setSQLXML(parameterIndex, xmlObject);
    this.registerSetter(new SQLXMLSetter(parameterIndex, xmlObject));
  }

  @Override
  public void setShort(int parameterIndex, short x) throws SQLException {
    this.wrapped.setShort(parameterIndex, x);
    this.registerSetter(new ShortSetter(parameterIndex, x));
  }

  @Override
  public void setString(int parameterIndex, String x) throws SQLException {
    this.wrapped.setString(parameterIndex, x);
    this.registerSetter(new StringSetter(parameterIndex, x));
  }

  @Override
  public void setTime(int parameterIndex, Time x) throws SQLException {
    this.wrapped.setTime(parameterIndex, x);
    this.registerSetter(new TimeSetter(parameterIndex, x));
  }

  @Override
  public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
    this.wrapped.setTime(parameterIndex, x, cal);
    this.registerSetter(new TimeSetter(parameterIndex, x, cal));
  }

  @Override
  public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
    this.wrapped.setTimestamp(parameterIndex, x);
    this.registerSetter(new TimestampSetter(parameterIndex, x));
  }

  @Override
  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
    this.wrapped.setTimestamp(parameterIndex, x, cal);
    this.registerSetter(new TimestampSetter(parameterIndex, x, cal));
  }

  @Override
  public void setURL(int parameterIndex, URL x) throws SQLException {
    this.wrapped.setURL(parameterIndex, x);
    this.registerSetter(new URLSetter(parameterIndex, x));
  }

  @Override
  @Deprecated
  public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
    this.wrapped.setUnicodeStream(parameterIndex, x, length);
    this.registerSetter(new UnicodeStreamSetter(parameterIndex, x, length));
  }

}
