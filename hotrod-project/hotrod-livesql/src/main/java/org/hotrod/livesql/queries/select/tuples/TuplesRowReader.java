package org.hotrod.livesql.queries.select.tuples;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.RowReader;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.metadata.EntityInstanceColumn;
import org.hotrod.livesql.metadata.MDShield;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.queries.select.tuples.gen.TupleClassFactory;
import org.hotrod.livesql.queries.typesolver.TypeHandler;
import org.hotrod.livesql.util.ColumnReader;

public class TuplesRowReader<T> implements RowReader<T> {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(TuplesRowReader.class.getName());

  private Map<TableOrView<?>, ModelInstance> modelInstances = new HashMap<>();
  private List<UnboundColumnRetriever> unboundColumnRetrievers = new ArrayList<>();

  private static class ModelInstance {

    private String tupleProperty;
    private Class<?> layoutClass;
    private Class<?> modelClass;
    private Map<String, ModelColumnRetriever> columnRetrievers;

    public ModelInstance(String tupleProperty, Class<?> layoutClass, Class<?> modelClass) {
      this.tupleProperty = tupleProperty;
      this.layoutClass = layoutClass;
      this.modelClass = modelClass;
      this.columnRetrievers = new HashMap<>();
    }

    public final String getTupleProperty() {
      return tupleProperty;
    }

    public final Class<?> getLayoutClass() {
      return layoutClass;
    }

    public final Class<?> getModelClass() {
      return modelClass;
    }

    public void addColumnRetriever(String property, ModelColumnRetriever r) {
      this.columnRetrievers.put(property, r);
    }

    public final Map<String, ModelColumnRetriever> getColumnRetrievers() {
      return columnRetrievers;
    }

  }

  private static class ModelColumnRetriever {

    private int ordinal;
    private TypeHandler<?, ?> th;
    private Field field;

    public ModelColumnRetriever(int ordinal, TypeHandler<?, ?> th, Field field) {
      this.ordinal = ordinal;
      this.th = th;
      this.field = field;
    }

    public void read(Object modelObject, ResultSet rs, Connection conn)
        throws SQLException, IllegalArgumentException, IllegalAccessException {
      Object modelValue = ColumnReader.read(rs, this.ordinal, this.th, conn);
      this.field.set(modelObject, modelValue);
    }

  }

  private static class UnboundColumnRetriever {

    private int ordinal;
    private TypeHandler<?, ?> th;
    private String name;

    public UnboundColumnRetriever(int ordinal, TypeHandler<?, ?> th, String name) {
      super();
      this.ordinal = ordinal;
      this.th = th;
      this.name = name;
    }

    public void read(Map<String, Object> unboundColumns, ResultSet rs, Connection conn)
        throws SQLException, IllegalArgumentException, IllegalAccessException {
      Object value = ColumnReader.read(rs, this.ordinal, this.th, conn);
      unboundColumns.put(this.name, value);
    }

  }

  public TuplesRowReader(final List<Expression> columns, final List<TableOrView<?>> tuples) {

//    Set<String> usedNS = new HashSet<>();
//    int ord = 1;
//    for (Expression c : columns) {
//      try {
//        EntityColumn ec = (EntityColumn) c;
//        // It's an entity column; nothing to do
//      } catch (ClassCastException e) {
//        String prop = Shield.getReferenceName(c);
//        if (prop == null) {
//          throw new LiveSQLException("Column #" + ord
//              + " does not declare an explicit alias; columns that do not belong to specific tuples must be aliased using .alias(\"name\")");
//        }
//        int colon = prop.indexOf(':');
//        if (colon != -1) {
//          usedNS.add(prop.substring(0, colon));
//        }
//      }
//      ord++;
//    }
//
//    log.info("$$$ [" + columns.size() + "] usedNS=" + usedNS);
//    NSSequence seq = NSUtil.sequence();

    char tupleProperty = 'a';
    for (TableOrView<?> t : tuples) {
      Class<?> layoutClass = MDShield.getLayoutClass(t);
      Class<?> modelClass = MDShield.getModelClass(t);
//      log.info("+ " + layoutClass.getName() + " <- " + modelClass.getName() + " t=" + OUtil.hc(t));
      this.modelInstances.put(t, new ModelInstance("" + tupleProperty, layoutClass, modelClass));
      tupleProperty++;
    }

    // Columns

    int ordinal = 1;
    for (Expression c : columns) {
//      log.info("$$ c=" + c);
      EntityInstanceColumn ec;
      ModelInstance mi;
      try {
        ec = (EntityInstanceColumn) c;
        // Could be an entity column from the main model instances, or from a subquery
        mi = this.modelInstances.get(ec.getObjectInstance());
        // It's an entity column from the main model instances
      } catch (ClassCastException e) {
        ec = null;
        mi = null;
      }

      TypeHandler<?, ?> th = Shield.getTypeHandler(c);
      if (mi == null) { // An unbound column

        String name = Shield.getReferenceName(c);
        UnboundColumnRetriever r = new UnboundColumnRetriever(ordinal, th, name);
        this.unboundColumnRetrievers.add(r);

      } else { // A model instance column

        Class<?> layout = mi.getLayoutClass();
        String property = ec.getProperty();
        Field field;
        try {
//          Field[] fields = layout.getFields();
//          log.info("### Class " + layout.getName() + " [" + fields.length + "]");
//          for (Field f : fields) {
//            log.info("### f=" + f.getName());
//          }
          field = layout.getDeclaredField(property);
        } catch (NoSuchFieldException | SecurityException e) {
          throw new RuntimeException("Could not find field '" + property + "' in model class '" + layout.getName()
              + "': " + e.getClass().getName());
        }
        field.setAccessible(true);
        ModelColumnRetriever r = new ModelColumnRetriever(ordinal, th, field);
        mi.addColumnRetriever(property, r);

      }

      ordinal++;
    }

  }

  @SuppressWarnings("unchecked")
  @Override
  public T readRowFrom(ResultSet rs, Connection conn) throws SQLException {
    Class<?> tc = TupleClassFactory.getTuplesClass(this.modelInstances.size());
    Object to;
    try {
      Constructor<?> c = tc.getDeclaredConstructor();
      c.setAccessible(true);
      to = c.newInstance();
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | NoSuchMethodException | SecurityException e) {
      throw new SQLException("Could not read tuple; could not instantiate tuple", e);
    }

    for (ModelInstance mi : this.modelInstances.values()) {

      // 1. Instantiate the model object

      Object modelObject;
      try {
        modelObject = mi.getModelClass().getConstructor().newInstance();
      } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
          | NoSuchMethodException | SecurityException e) {
        throw new SQLException("Could not read tuple; could not instantiate model class", e);
      }

      // 2. Set the model object to the tuple

      Field tf;
      try {
        tf = tc.getDeclaredField(mi.getTupleProperty());
      } catch (NoSuchFieldException | SecurityException e) {
        throw new SQLException("Could not read tuple; could not find tuple field", e);
      }
      tf.setAccessible(true);
      try {
        tf.set(to, modelObject);
      } catch (IllegalArgumentException | IllegalAccessException e) {
        throw new SQLException("Could not read tuple; could not set tuple field", e);
      }

      // 3. Read all columns in this model object

      for (ModelColumnRetriever r : mi.getColumnRetrievers().values()) {
        try {
          r.read(modelObject, rs, conn);
        } catch (IllegalAccessException | IllegalArgumentException e) {
          throw new SQLException("Could not read tuple column", e);
        }
      }

    }

    // 4. Set the unbound columns

    Map<String, Object> unboundColumns = new HashMap<>();
    Field tf;
    try {
      tf = tc.getDeclaredField("unbound");
    } catch (NoSuchFieldException | SecurityException e) {
      throw new SQLException("Could not read tuple; could not find unbound property", e);
    }
    tf.setAccessible(true);
    try {
      tf.set(to, unboundColumns);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw new SQLException("Could not read tuple; could not set unbound field", e);
    }

    for (UnboundColumnRetriever r : this.unboundColumnRetrievers) {
      try {
        r.read(unboundColumns, rs, conn);
      } catch (IllegalArgumentException | IllegalAccessException e) {
        throw new SQLException("Could not read unbound columns", e);
      }
    }

    return (T) to;
  }

}
