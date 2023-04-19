package app;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;

import org.hotrod.runtime.converter.TypeConverter;

public class IntegerArrayConverter implements TypeConverter<java.sql.Array, Integer[]> {

  @Override
  public Integer[] decode(Array raw, Connection conn) throws SQLException {
    if (raw == null)
      return null;
    return (Integer[]) raw.getArray();
  }

  @Override
  public Array encode(Integer[] value, Connection conn) throws SQLException {
    return conn.createArrayOf("integer", value);
  }

}
