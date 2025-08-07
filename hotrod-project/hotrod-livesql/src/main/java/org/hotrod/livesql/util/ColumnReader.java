package org.hotrod.livesql.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hotrod.converter.TypeConverter;
import org.hotrod.livesql.queries.typesolver.TypeHandler;

public class ColumnReader {

  public static Object read(ResultSet rs, int ordinal, TypeHandler<?, ?> th, Connection conn) throws SQLException {
    if (th == null) { // No typeHandler: use the JDBC default value
      return rs.getObject(ordinal);
    } else if (th.getConverter() == null) { // TypeHandler with no converter: use the defined class
      return rs.getObject(ordinal, th.getJavaClass());
    } else { // TypeHandler with converter: read as defined class and apply converter
      Object raw = rs.getObject(ordinal, th.getRawClass());
      TypeConverter<?, ?> converter = th.getConverter();
      return decode(raw, converter, conn);
    }
  }

  private static Object decode(final Object raw, final TypeConverter<?, ?> converter, final Connection conn) {

    Method m;
    try {
      m = TypeConverter.class.getMethod("decode", Object.class, Connection.class);
    } catch (NoSuchMethodException | SecurityException e) {
      throw new RuntimeException("Could not use converter", e);
    }

    Object value;
    try {
      value = m.invoke(converter, raw, conn);
    } catch (InvocationTargetException e) {
      throw new RuntimeException("Converter's decode() method threw an exception", e);
    } catch (IllegalAccessException | IllegalArgumentException e) {
      throw new RuntimeException("Could not invoke converter's decode() method", e);
    }

    return value;
  }

}
