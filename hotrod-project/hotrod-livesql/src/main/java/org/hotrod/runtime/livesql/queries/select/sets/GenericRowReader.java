package org.hotrod.runtime.livesql.queries.select.sets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.hotrod.converter.TypeConverter;
import org.hotrod.dynamicsql.RowReader;
import org.hotrod.livesql.Row;
import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.LiveSQLPreparedQuery;
import org.hotrod.runtime.livesql.queries.typesolver.ResultSetColumnMetadata;
import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler;
import org.hotrod.runtime.livesql.queries.typesolver.TypeRule.CouldNotResolveResultSetDataTypeException;

public class GenericRowReader<T> implements RowReader<T> {

  private static final Logger log = Logger.getLogger(GenericRowReader.class.getName());

  private LinkedHashMap<String, Expression> queryColumns;

  public GenericRowReader(final LiveSQLContext context, final LiveSQLPreparedQuery q, final ResultSet rs)
      throws SQLException {
    this.queryColumns = q.getQueryColumns();
    ResultSetMetaData rm = rs.getMetaData();
    int ordinal = 1;
    for (Entry<String, Expression> et : this.queryColumns.entrySet()) {
      Expression expr = et.getValue();
      if (Helper.getTypeHandler(expr) == null) {
        ResultSetColumnMetadata cm = ResultSetColumnMetadata.of(rm, ordinal);
        try {
          TypeHandler<?, ?> th = context.getTypeSolver().resolve(cm);
          Helper.setTypeHandler(expr, th);
        } catch (CouldNotResolveResultSetDataTypeException e) {
          throw new LiveSQLException(
              "Could not determine the application type for the column '" + et.getKey() + "' in the query", e);
        }
      }
      ordinal++;
    }
    logQueryColumns();
  }

  @SuppressWarnings("unchecked")
  @Override
  public T readRowFrom(ResultSet rs, Connection conn) throws SQLException {
    Row r = new Row();
    int i = 1;
    for (Expression qc : queryColumns.values()) {
      Object value;
      String alias = Helper.getReferenceName(qc);
      TypeHandler<?, ?> th = Helper.getTypeHandler(qc);
      if (th == null) { // No typeHandler: use the JDBC default value
        value = rs.getObject(i);
      } else if (th.getConverter() == null) { // TypeHandler with no converter: use the defined class
        value = rs.getObject(i, th.getJavaClass());
      } else { // TypeHandler with converter: read as defined class and apply converter
        Object raw = rs.getObject(i, th.getRawClass());
        TypeConverter<?, ?> converter = th.getConverter();
        value = this.decode(raw, converter, conn);
      }
      r.put(alias, value);
      i++;
    }
    return (T) r;
  }

  private Object decode(final Object raw, final TypeConverter<?, ?> converter, final Connection conn) {

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

  private void logQueryColumns() {
    int n;
    n = 1;
    for (Expression qc : this.queryColumns.values()) {
      int i = n++;
      String alias = Helper.getReferenceName(qc);
      TypeHandler<?, ?> th = Helper.getTypeHandler(qc);
      log.info("- column #" + i + " '" + alias + "': " + th);
    }
  }

}
