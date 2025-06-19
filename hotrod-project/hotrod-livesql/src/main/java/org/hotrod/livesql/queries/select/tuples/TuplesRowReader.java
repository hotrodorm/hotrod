package org.hotrod.livesql.queries.select.tuples;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.RowReader;
import org.hotrod.livesql.expressions.ResultSetColumn;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.metadata.MDShield;
import org.hotrod.livesql.metadata.TableOrView;

public class TuplesRowReader<T> implements RowReader<T> {

  private static final Logger log = Logger.getLogger(TuplesRowReader.class.getName());

  // Tuples:
  // - List: Model:
  // - - List: alias, getter, setter
  // - unbound:
  // - - alias, getter

  private List<ModelClass> modelClasses;
  private List<UnboundColumn> unboundColumns;

  private static class ModelClass {

    private String alias;
    private Class<?> c;
    private List<ModelColumn> columns;

    public ModelClass(String alias, Class<?> c, List<ModelColumn> columns) {
      super();
      this.alias = alias;
      this.c = c;
      this.columns = columns;
    }

  }

  private static class ModelColumn {

    private String sqlName;
    private Field field;

    public ModelColumn(String sqlName, Field field) {
      this.sqlName = sqlName;
      this.field = field;
    }

  }

  private static class UnboundColumn {
    private String name;
//    private Getter getter;
  }

  public TuplesRowReader(final List<ResultSetColumn> resultSetColumns, final List<TableOrView<?>> tuples) {

    this.modelClasses = new ArrayList<>();
    for (TableOrView<?> t : tuples) {
      log.info("$$ t: " + System.identityHashCode(t));
      Class<?> modelClass = MDShield.getModelClass(t);
      Field[] fields = modelClass.getDeclaredFields();
      List<ModelColumn> modelColumns = new ArrayList<>();
      for (Field f : fields) {
        String sqlName = t.getAlias() + ":" + f.getName();
        f.setAccessible(true);
        ModelColumn mc = new ModelColumn(sqlName, f);
        modelColumns.add(mc);
      }
      this.modelClasses.add(new ModelClass(t.getAlias(), modelClass, modelColumns));
    }

    // Columns

    for (ResultSetColumn c : resultSetColumns) {
      log.info("$$ c=" + c);
//      EntityColumn ec = (EntityColumn) c;
//      AliasedExpression ae = (AliasedExpression) c;
    }
//    TableOrView<?> tv = e.getObjectInstance();

  }

  @Override
  public T readRowFrom(ResultSet rs, Connection conn) throws SQLException {
    ResultSetMetaData rm = rs.getMetaData();
    int cols = rm.getColumnCount();
    log.info("=== Tuples Result Set (" + cols + " columns) ===");
    for (int i = 1; i <= cols; i++) {
      String name = rm.getColumnName(i);
      log.info("@ col #" + i + ": " + name);
    }

//    if (this.joins.size() == 2) {
//      for (TableOrView<?> tv : joins) {
//        Type[] types = TableOrView.class.getGenericInterfaces();
//        log.info("tv: " + tv + " -- types=" + types.length);
//        for (Type tp : types) {
//          log.info(">> type: " + tp);
//        }
//      }
//
////      Tuple2<Integer, String> row = new Tuple2<Integer, String>(null, null, null);
////      return (T) row;
//    }

    return null;
  }

}
