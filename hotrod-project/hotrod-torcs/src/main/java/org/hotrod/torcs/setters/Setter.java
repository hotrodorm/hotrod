package org.hotrod.torcs.setters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Setter {

  protected int index;

  protected Setter(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }

  public abstract void applyTo(PreparedStatement ps) throws SQLException;

  public abstract Object value();

}

//void  setArray(int parameterIndex, Array x)
//void  setAsciiStream(int parameterIndex, InputStream x)
//void  setAsciiStream(int parameterIndex, InputStream x, int length)
//void  setAsciiStream(int parameterIndex, InputStream x, long length)
//void  setBigDecimal(int parameterIndex, BigDecimal x)
//void  setBinaryStream(int parameterIndex, InputStream x)
//void  setBinaryStream(int parameterIndex, InputStream x, int length)
//void  setBinaryStream(int parameterIndex, InputStream x, long length)
//void  setBlob(int parameterIndex, Blob x)
//void  setBlob(int parameterIndex, InputStream inputStream)
//void  setBlob(int parameterIndex, InputStream inputStream, long length)
//void  setBoolean(int parameterIndex, boolean x)
//void  setByte(int parameterIndex, byte x)
//void  setBytes(int parameterIndex, byte[] x)
//void  setCharacterStream(int parameterIndex, Reader reader)
//void  setCharacterStream(int parameterIndex, Reader reader, int length)
//void  setCharacterStream(int parameterIndex, Reader reader, long length)
//void  setClob(int parameterIndex, Clob x)
//void  setClob(int parameterIndex, Reader reader)
//void  setClob(int parameterIndex, Reader reader, long length)
//void  setDate(int parameterIndex, Date x)
//void  setDate(int parameterIndex, Date x, Calendar cal)
//void  setDouble(int parameterIndex, double x)
//void  setFloat(int parameterIndex, float x)
//void  setInt(int parameterIndex, int x)
//void  setLong(int parameterIndex, long x)
//void  setNCharacterStream(int parameterIndex, Reader value)
//void  setNCharacterStream(int parameterIndex, Reader value, long length)
//void  setNClob(int parameterIndex, NClob value)
//void  setNClob(int parameterIndex, Reader reader)
//void  setNClob(int parameterIndex, Reader reader, long length)
//void  setNString(int parameterIndex, String value)
//void  setNull(int parameterIndex, int sqlType)
//void  setNull(int parameterIndex, int sqlType, String typeName)
//void  setObject(int parameterIndex, Object x)
//void  setObject(int parameterIndex, Object x, int targetSqlType)
//void  setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength)
//default void  setObject(int parameterIndex, Object x, SQLType targetSqlType)
//default void  setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength)
//void  setRef(int parameterIndex, Ref x)
//void  setRowId(int parameterIndex, RowId x)
//void  setShort(int parameterIndex, short x)
//void  setSQLXML(int parameterIndex, SQLXML xmlObject)
//void  setString(int parameterIndex, String x)
//void  setTime(int parameterIndex, Time x)
//void  setTime(int parameterIndex, Time x, Calendar cal)
//void  setTimestamp(int parameterIndex, Timestamp x)
//void  setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
//void  setUnicodeStream(int parameterIndex, InputStream x, int length)
//void  setURL(int parameterIndex, URL x)
