package org.hotrod.livesql.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.hotrod.converter.TypeConverter;
import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.queries.typesolver.TypeHandler;

public abstract class ColumnReader {

  private static final Logger log = Logger.getLogger(ColumnReader.class.getName());

  private ColumnReader() {
    log.fine("init");
  }

  public static Object read(ResultSet rs, int ordinal, TypeHandler<?, ?> th, Connection conn) throws SQLException {
    if (th == null) { // No typeHandler: use the JDBC default value
      return rs.getObject(ordinal);
    } else if (th.getConverter() == null) { // TypeHandler with no converter: use the defined class
      Object o = th.getResultSetGetter().get(rs, ordinal);
      return o;
    } else { // TypeHandler with converter: read as defined class and apply converter
      Object raw = th.getResultSetGetter().get(rs, ordinal);
      TypeConverter<?, ?> converter = th.getConverter();
      return decode(raw, converter, conn);
    }
  }

  private static Object decode(final Object raw, final TypeConverter<?, ?> converter, final Connection conn) {

    Method m;
    try {
      m = TypeConverter.class.getMethod("decode", Object.class, Connection.class);
    } catch (NoSuchMethodException | SecurityException e) {
      throw new LiveSQLException("Could not use converter", e);
    }

    Object value;
    try {
      value = m.invoke(converter, raw, conn);
    } catch (InvocationTargetException e) {
      throw new LiveSQLException("Converter's decode() method threw an exception", e);
    } catch (IllegalAccessException | IllegalArgumentException e) {
      throw new LiveSQLException("Could not invoke converter's decode() method", e);
    }

    return value;
  }

}
