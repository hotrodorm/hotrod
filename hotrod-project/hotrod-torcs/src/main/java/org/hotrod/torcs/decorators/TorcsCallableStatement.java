package org.hotrod.torcs.decorators;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Logger;

import org.hotrod.torcs.DataSourceReference;
import org.hotrod.torcs.Torcs;
import org.hotrod.torcs.setters.name.NameAsciiStreamSetter;
import org.hotrod.torcs.setters.name.NameBigDecimalSetter;
import org.hotrod.torcs.setters.name.NameBinaryStreamSetter;
import org.hotrod.torcs.setters.name.NameBlobSetter;
import org.hotrod.torcs.setters.name.NameBooleanSetter;
import org.hotrod.torcs.setters.name.NameByteSetter;
import org.hotrod.torcs.setters.name.NameBytesSetter;
import org.hotrod.torcs.setters.name.NameCharacterStreamSetter;
import org.hotrod.torcs.setters.name.NameClobSetter;
import org.hotrod.torcs.setters.name.NameDateSetter;
import org.hotrod.torcs.setters.name.NameDoubleSetter;
import org.hotrod.torcs.setters.name.NameFloatSetter;
import org.hotrod.torcs.setters.name.NameIntSetter;
import org.hotrod.torcs.setters.name.NameLongSetter;
import org.hotrod.torcs.setters.name.NameNCharacterStreamSetter;
import org.hotrod.torcs.setters.name.NameNClobSetter;
import org.hotrod.torcs.setters.name.NameNStringSetter;
import org.hotrod.torcs.setters.name.NameNullSetter;
import org.hotrod.torcs.setters.name.NameObjectSetter;
import org.hotrod.torcs.setters.name.NameRowIdSetter;
import org.hotrod.torcs.setters.name.NameSQLXMLSetter;
import org.hotrod.torcs.setters.name.NameShortSetter;
import org.hotrod.torcs.setters.name.NameStringSetter;
import org.hotrod.torcs.setters.name.NameTimeSetter;
import org.hotrod.torcs.setters.name.NameTimestampSetter;
import org.hotrod.torcs.setters.name.NameURLSetter;

public class TorcsCallableStatement extends TorcsPreparedStatement implements CallableStatement {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(TorcsCallableStatement.class.getName());

  private CallableStatement callableWrapped;

  public TorcsCallableStatement(CallableStatement callableWrapped, TorcsConnection conn, Torcs torcs,
      DataSourceReference dataSourceReference, String sql) {
    super(callableWrapped, conn, torcs, dataSourceReference, sql);
    this.callableWrapped = callableWrapped;
  }

  // Implementation methods

  @Override
  public Array getArray(int parameterIndex) throws SQLException {
    return this.callableWrapped.getArray(parameterIndex);
  }

  @Override
  public Array getArray(String parameterName) throws SQLException {
    return this.callableWrapped.getArray(parameterName);
  }

  @Override
  public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
    return this.callableWrapped.getBigDecimal(parameterIndex);
  }

  @Override
  public BigDecimal getBigDecimal(String parameterName) throws SQLException {
    return this.callableWrapped.getBigDecimal(parameterName);
  }

  @Override
  @Deprecated
  public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
    return this.callableWrapped.getBigDecimal(parameterIndex, scale);
  }

  @Override
  public Blob getBlob(int parameterIndex) throws SQLException {
    return this.callableWrapped.getBlob(parameterIndex);
  }

  @Override
  public Blob getBlob(String parameterName) throws SQLException {
    return this.callableWrapped.getBlob(parameterName);
  }

  @Override
  public boolean getBoolean(int parameterIndex) throws SQLException {
    return this.callableWrapped.getBoolean(parameterIndex);
  }

  @Override
  public boolean getBoolean(String parameterName) throws SQLException {
    return this.callableWrapped.getBoolean(parameterName);
  }

  @Override
  public byte getByte(int parameterIndex) throws SQLException {
    return this.callableWrapped.getByte(parameterIndex);
  }

  @Override
  public byte getByte(String parameterName) throws SQLException {
    return this.callableWrapped.getByte(parameterName);
  }

  @Override
  public byte[] getBytes(int parameterIndex) throws SQLException {
    return this.callableWrapped.getBytes(parameterIndex);
  }

  @Override
  public byte[] getBytes(String parameterName) throws SQLException {
    return this.callableWrapped.getBytes(parameterName);
  }

  @Override
  public Reader getCharacterStream(int parameterIndex) throws SQLException {
    return this.callableWrapped.getCharacterStream(parameterIndex);
  }

  @Override
  public Reader getCharacterStream(String parameterName) throws SQLException {
    return this.callableWrapped.getCharacterStream(parameterName);
  }

  @Override
  public Clob getClob(int parameterIndex) throws SQLException {
    return this.callableWrapped.getClob(parameterIndex);
  }

  @Override
  public Clob getClob(String parameterName) throws SQLException {
    return this.callableWrapped.getClob(parameterName);
  }

  @Override
  public Date getDate(int parameterIndex) throws SQLException {
    return this.callableWrapped.getDate(parameterIndex);
  }

  @Override
  public Date getDate(String parameterName) throws SQLException {
    return this.callableWrapped.getDate(parameterName);
  }

  @Override
  public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
    return this.callableWrapped.getDate(parameterIndex, cal);
  }

  @Override
  public Date getDate(String parameterName, Calendar cal) throws SQLException {
    return this.callableWrapped.getDate(parameterName, cal);
  }

  @Override
  public double getDouble(int parameterIndex) throws SQLException {
    return this.callableWrapped.getDouble(parameterIndex);
  }

  @Override
  public double getDouble(String parameterName) throws SQLException {
    return this.callableWrapped.getDouble(parameterName);
  }

  @Override
  public float getFloat(int parameterIndex) throws SQLException {
    return this.callableWrapped.getFloat(parameterIndex);
  }

  @Override
  public float getFloat(String parameterName) throws SQLException {
    return this.callableWrapped.getFloat(parameterName);
  }

  @Override
  public int getInt(int parameterIndex) throws SQLException {
    return this.callableWrapped.getInt(parameterIndex);
  }

  @Override
  public int getInt(String parameterName) throws SQLException {
    return this.callableWrapped.getInt(parameterName);
  }

  @Override
  public long getLong(int parameterIndex) throws SQLException {
    return this.callableWrapped.getLong(parameterIndex);
  }

  @Override
  public long getLong(String parameterName) throws SQLException {
    return this.callableWrapped.getLong(parameterName);
  }

  @Override
  public Reader getNCharacterStream(int parameterIndex) throws SQLException {
    return this.callableWrapped.getNCharacterStream(parameterIndex);
  }

  @Override
  public Reader getNCharacterStream(String parameterName) throws SQLException {
    return this.callableWrapped.getNCharacterStream(parameterName);
  }

  @Override
  public NClob getNClob(int parameterIndex) throws SQLException {
    return this.callableWrapped.getNClob(parameterIndex);
  }

  @Override
  public NClob getNClob(String parameterName) throws SQLException {
    return this.callableWrapped.getNClob(parameterName);
  }

  @Override
  public String getNString(int parameterIndex) throws SQLException {
    return this.callableWrapped.getNString(parameterIndex);
  }

  @Override
  public String getNString(String parameterName) throws SQLException {
    return this.callableWrapped.getNString(parameterName);
  }

  @Override
  public Object getObject(int parameterIndex) throws SQLException {
    return this.callableWrapped.getObject(parameterIndex);
  }

  @Override
  public Object getObject(String parameterName) throws SQLException {
    return this.callableWrapped.getObject(parameterName);
  }

  @Override
  public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
    return this.callableWrapped.getObject(parameterIndex, map);
  }

  @Override
  public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
    return this.callableWrapped.getObject(parameterName, map);
  }

  @Override
  public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
    return this.callableWrapped.getObject(parameterIndex, type);
  }

  @Override
  public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
    return this.callableWrapped.getObject(parameterName, type);
  }

  @Override
  public Ref getRef(int parameterIndex) throws SQLException {
    return this.callableWrapped.getRef(parameterIndex);
  }

  @Override
  public Ref getRef(String parameterName) throws SQLException {
    return this.callableWrapped.getRef(parameterName);
  }

  @Override
  public RowId getRowId(int parameterIndex) throws SQLException {
    return this.callableWrapped.getRowId(parameterIndex);
  }

  @Override
  public RowId getRowId(String parameterName) throws SQLException {
    return this.callableWrapped.getRowId(parameterName);
  }

  @Override
  public SQLXML getSQLXML(int parameterIndex) throws SQLException {
    return this.callableWrapped.getSQLXML(parameterIndex);
  }

  @Override
  public SQLXML getSQLXML(String parameterName) throws SQLException {
    return this.callableWrapped.getSQLXML(parameterName);
  }

  @Override
  public short getShort(int parameterIndex) throws SQLException {
    return this.callableWrapped.getShort(parameterIndex);
  }

  @Override
  public short getShort(String parameterName) throws SQLException {
    return this.callableWrapped.getShort(parameterName);
  }

  @Override
  public String getString(int parameterIndex) throws SQLException {
    return this.callableWrapped.getString(parameterIndex);
  }

  @Override
  public String getString(String parameterName) throws SQLException {
    return this.callableWrapped.getString(parameterName);
  }

  @Override
  public Time getTime(int parameterIndex) throws SQLException {
    return this.callableWrapped.getTime(parameterIndex);
  }

  @Override
  public Time getTime(String parameterName) throws SQLException {
    return this.callableWrapped.getTime(parameterName);
  }

  @Override
  public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
    return this.callableWrapped.getTime(parameterIndex, cal);
  }

  @Override
  public Time getTime(String parameterName, Calendar cal) throws SQLException {
    return this.callableWrapped.getTime(parameterName, cal);
  }

  @Override
  public Timestamp getTimestamp(int parameterIndex) throws SQLException {
    return this.callableWrapped.getTimestamp(parameterIndex);
  }

  @Override
  public Timestamp getTimestamp(String parameterName) throws SQLException {
    return this.callableWrapped.getTimestamp(parameterName);
  }

  @Override
  public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
    return this.callableWrapped.getTimestamp(parameterIndex, cal);
  }

  @Override
  public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
    return this.callableWrapped.getTimestamp(parameterName, cal);
  }

  @Override
  public URL getURL(int parameterIndex) throws SQLException {
    return this.callableWrapped.getURL(parameterIndex);
  }

  @Override
  public URL getURL(String parameterName) throws SQLException {
    return this.callableWrapped.getURL(parameterName);
  }

  @Override
  public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
    this.callableWrapped.registerOutParameter(parameterIndex, sqlType);
  }

  @Override
  public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
    this.callableWrapped.registerOutParameter(parameterName, sqlType);
  }

  @Override
  public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
    this.callableWrapped.registerOutParameter(parameterIndex, sqlType, scale);
  }

  @Override
  public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
    this.callableWrapped.registerOutParameter(parameterIndex, sqlType, typeName);
  }

  @Override
  public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
    this.callableWrapped.registerOutParameter(parameterName, sqlType, scale);
  }

  @Override
  public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
    this.callableWrapped.registerOutParameter(parameterName, sqlType, typeName);
  }

  // TODO: marker

  @Override
  public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
    this.callableWrapped.setAsciiStream(parameterName, x);
    this.registerSetter(new NameAsciiStreamSetter(parameterName, x));
  }

  @Override
  public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
    this.callableWrapped.setAsciiStream(parameterName, x, length);
    this.registerSetter(new NameAsciiStreamSetter(parameterName, x, length));
  }

  @Override
  public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
    this.callableWrapped.setAsciiStream(parameterName, x, length);
    this.registerSetter(new NameAsciiStreamSetter(parameterName, x, length));
  }

  @Override
  public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
    this.callableWrapped.setBigDecimal(parameterName, x);
    this.registerSetter(new NameBigDecimalSetter(parameterName, x));
  }

  @Override
  public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
    this.callableWrapped.setBinaryStream(parameterName, x);
    this.registerSetter(new NameBinaryStreamSetter(parameterName, x));
  }

  @Override
  public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
    this.callableWrapped.setBinaryStream(parameterName, x, length);
    this.registerSetter(new NameAsciiStreamSetter(parameterName, x, length));
  }

  @Override
  public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
    this.callableWrapped.setBinaryStream(parameterName, x, length);
    this.registerSetter(new NameAsciiStreamSetter(parameterName, x, length));
  }

  @Override
  public void setBlob(String parameterName, Blob x) throws SQLException {
    this.callableWrapped.setBlob(parameterName, x);
    this.registerSetter(new NameBlobSetter(parameterName, x));
  }

  @Override
  public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
    this.callableWrapped.setBlob(parameterName, inputStream);
    this.registerSetter(new NameAsciiStreamSetter(parameterName, inputStream));
  }

  @Override
  public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
    this.callableWrapped.setBlob(parameterName, inputStream, length);
    this.registerSetter(new NameAsciiStreamSetter(parameterName, inputStream, length));
  }

  @Override
  public void setBoolean(String parameterName, boolean x) throws SQLException {
    this.callableWrapped.setBoolean(parameterName, x);
    this.registerSetter(new NameBooleanSetter(parameterName, x));
  }

  @Override
  public void setByte(String parameterName, byte x) throws SQLException {
    this.callableWrapped.setByte(parameterName, x);
    this.registerSetter(new NameByteSetter(parameterName, x));
  }

  @Override
  public void setBytes(String parameterName, byte[] x) throws SQLException {
    this.callableWrapped.setBytes(parameterName, x);
    this.registerSetter(new NameBytesSetter(parameterName, x));
  }

  @Override
  public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
    this.callableWrapped.setCharacterStream(parameterName, reader);
    this.registerSetter(new NameCharacterStreamSetter(parameterName, reader));
  }

  @Override
  public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
    this.callableWrapped.setCharacterStream(parameterName, reader, length);
    this.registerSetter(new NameCharacterStreamSetter(parameterName, reader, length));
  }

  @Override
  public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
    this.callableWrapped.setCharacterStream(parameterName, reader, length);
    this.registerSetter(new NameCharacterStreamSetter(parameterName, reader, length));
  }

  @Override
  public void setClob(String parameterName, Clob x) throws SQLException {
    this.callableWrapped.setClob(parameterName, x);
    this.registerSetter(new NameClobSetter(parameterName, x));
  }

  @Override
  public void setClob(String parameterName, Reader reader) throws SQLException {
    this.callableWrapped.setClob(parameterName, reader);
    this.registerSetter(new NameClobSetter(parameterName, reader));
  }

  @Override
  public void setClob(String parameterName, Reader reader, long length) throws SQLException {
    this.callableWrapped.setClob(parameterName, reader, length);
    this.registerSetter(new NameClobSetter(parameterName, reader, length));
  }

  @Override
  public void setDate(String parameterName, Date x) throws SQLException {
    this.callableWrapped.setDate(parameterName, x);
    this.registerSetter(new NameDateSetter(parameterName, x));
  }

  @Override
  public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
    this.callableWrapped.setDate(parameterName, x, cal);
    this.registerSetter(new NameDateSetter(parameterName, x, cal));
  }

  @Override
  public void setDouble(String parameterName, double x) throws SQLException {
    this.callableWrapped.setDouble(parameterName, x);
    this.registerSetter(new NameDoubleSetter(parameterName, x));
  }

  @Override
  public void setFloat(String parameterName, float x) throws SQLException {
    this.callableWrapped.setFloat(parameterName, x);
    this.registerSetter(new NameFloatSetter(parameterName, x));
  }

  @Override
  public void setInt(String parameterName, int x) throws SQLException {
    this.callableWrapped.setInt(parameterName, x);
    this.registerSetter(new NameIntSetter(parameterName, x));
  }

  @Override
  public void setLong(String parameterName, long x) throws SQLException {
    this.callableWrapped.setLong(parameterName, x);
    this.registerSetter(new NameLongSetter(parameterName, x));
  }

  @Override
  public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
    this.callableWrapped.setNCharacterStream(parameterName, value);
    this.registerSetter(new NameNCharacterStreamSetter(parameterName, value));
  }

  @Override
  public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
    this.callableWrapped.setNCharacterStream(parameterName, value, length);
    this.registerSetter(new NameNCharacterStreamSetter(parameterName, value, length));
  }

  @Override
  public void setNClob(String parameterName, NClob value) throws SQLException {
    this.callableWrapped.setNClob(parameterName, value);
    this.registerSetter(new NameNClobSetter(parameterName, value));
  }

  @Override
  public void setNClob(String parameterName, Reader reader) throws SQLException {
    this.callableWrapped.setNClob(parameterName, reader);
    this.registerSetter(new NameNClobSetter(parameterName, reader));
  }

  @Override
  public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
    this.callableWrapped.setNClob(parameterName, reader, length);
    this.registerSetter(new NameNClobSetter(parameterName, reader, length));
  }

  @Override
  public void setNString(String parameterName, String value) throws SQLException {
    this.callableWrapped.setNString(parameterName, value);
    this.registerSetter(new NameNStringSetter(parameterName, value));
  }

  @Override
  public void setNull(String parameterName, int sqlType) throws SQLException {
    this.callableWrapped.setNull(parameterName, sqlType);
    this.registerSetter(new NameNullSetter(parameterName, sqlType));
  }

  @Override
  public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
    this.callableWrapped.setNull(parameterName, sqlType, typeName);
    this.registerSetter(new NameNullSetter(parameterName, sqlType, typeName));
  }

  @Override
  public void setObject(String parameterName, Object x) throws SQLException {
    this.callableWrapped.setObject(parameterName, x);
    this.registerSetter(new NameObjectSetter(parameterName, x));
  }

  @Override
  public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
    this.callableWrapped.setObject(parameterName, x, targetSqlType);
    this.registerSetter(new NameObjectSetter(parameterName, x, targetSqlType));
  }

  @Override
  public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
    this.callableWrapped.setObject(parameterName, x, targetSqlType, scale);
    this.registerSetter(new NameObjectSetter(parameterName, x, targetSqlType, scale));
  }

  @Override
  public void setRowId(String parameterName, RowId x) throws SQLException {
    this.callableWrapped.setRowId(parameterName, x);
    this.registerSetter(new NameRowIdSetter(parameterName, x));
  }

  @Override
  public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
    this.callableWrapped.setSQLXML(parameterName, xmlObject);
    this.registerSetter(new NameSQLXMLSetter(parameterName, xmlObject));
  }

  @Override
  public void setShort(String parameterName, short x) throws SQLException {
    this.callableWrapped.setShort(parameterName, x);
    this.registerSetter(new NameShortSetter(parameterName, x));
  }

  @Override
  public void setString(String parameterName, String x) throws SQLException {
    this.callableWrapped.setString(parameterName, x);
    this.registerSetter(new NameStringSetter(parameterName, x));
  }

  @Override
  public void setTime(String parameterName, Time x) throws SQLException {
    this.callableWrapped.setTime(parameterName, x);
    this.registerSetter(new NameTimeSetter(parameterName, x));
  }

  @Override
  public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
    this.callableWrapped.setTime(parameterName, x, cal);
    this.registerSetter(new NameTimeSetter(parameterName, x, cal));
  }

  @Override
  public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
    this.callableWrapped.setTimestamp(parameterName, x);
    this.registerSetter(new NameTimestampSetter(parameterName, x));
  }

  @Override
  public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
    this.callableWrapped.setTimestamp(parameterName, x, cal);
    this.registerSetter(new NameTimestampSetter(parameterName, x, cal));
  }

  @Override
  public void setURL(String parameterName, URL val) throws SQLException {
    this.callableWrapped.setURL(parameterName, val);
    this.registerSetter(new NameURLSetter(parameterName, val));
  }

  @Override
  public boolean wasNull() throws SQLException {
    return this.callableWrapped.wasNull();
  }

}
