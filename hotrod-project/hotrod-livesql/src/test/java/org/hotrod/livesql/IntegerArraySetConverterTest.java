package org.hotrod.livesql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.hotrod.converter.TypeConverter;
import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.dialects.H2Dialect;
import org.hotrod.livesql.metadata.AllColumns;
import org.hotrod.livesql.metadata.Name;
import org.hotrod.livesql.metadata.Table;
import org.hotrod.livesql.expressions.bool.converter.CShield;
import org.hotrod.livesql.expressions.bool.converter.ConvertedEntityColumn;
import org.hotrod.livesql.expressions.bool.converter.ConvertedEntityColumnMetaData;
import org.hotrod.livesql.queries.ConvertedValue;
import org.hotrod.livesql.queries.SShield;
import org.hotrod.livesql.queries.UpdateSetPhase;
import org.hotrod.livesql.queries.UpdateTablePhase;
import org.hotrod.livesql.queries.UpdateObject;
import org.hotrod.livesql.queries.typesolver.TypeHandler;
import org.hotrod.livesql.queries.typesolver.TypeRule;
import org.hotrod.livesql.queries.typesolver.TypeSource;
import org.junit.jupiter.api.Test;

class IntegerArraySetConverterTest {

//  @Test
  void convertsIntegerArraysAsSetsInWhereAndUpdateSetClauses() throws Exception {
    JdbcDataSource dataSource = newDataSource();
    createTableAndInsertRow(dataSource);

    LiveSQL liveSQL = newLiveSQL(dataSource);
    IntegerArrayTable table = new IntegerArrayTable();
    Set<Integer> original = setOf(10, 20, 30);

    List<Row> selected = liveSQL.select(table.numbers).from(table).where(table.numbers.eq(original)).execute();
    assertEquals(1, selected.size());
    assertEquals(original, selected.get(0).get("numbers"));

    Set<Integer> replacement = setOf(40, 50);
    int updated = liveSQL.update(table).set(table.numbers, replacement).where(table.numbers.eq(original)).execute();
    assertEquals(1, updated);

    List<Row> afterUpdate = liveSQL.select(table.numbers).from(table).where(table.numbers.eq(replacement)).execute();
    assertEquals(1, afterUpdate.size());
    assertEquals(replacement, afterUpdate.get(0).get("numbers"));
  }

  private JdbcDataSource newDataSource() {
    JdbcDataSource dataSource = new JdbcDataSource();
    dataSource.setURL("jdbc:h2:mem:integer_array_converter;DB_CLOSE_DELAY=-1");
    return dataSource;
  }

  private void createTableAndInsertRow(DataSource dataSource) throws Exception {
    try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
      statement.execute("CREATE TABLE integer_array_values (id INTEGER PRIMARY KEY, numbers INTEGER ARRAY)");
      try (PreparedStatement insert = connection
          .prepareStatement("INSERT INTO integer_array_values (id, numbers) VALUES (?, ?)");) {
        insert.setInt(1, 1);
        insert.setArray(2, connection.createArrayOf("INTEGER", new Integer[] { 10, 20, 30 }));
        insert.executeUpdate();
      }
    }
  }

  private LiveSQL newLiveSQL(DataSource dataSource) {
    return new LiveSQL(new H2Dialect(false, "H2", "2.2.224", 2, 2), dataSource, null, new LayerConfiguration() {
      @Override
      public List<TypeRule> getRuntimeTypeSolverRules() {
        return Collections.emptyList();
      }

      @Override
      public String[] getAllowedPackages() {
        return new String[0];
      }
    });
  }

  private Set<Integer> setOf(Integer... values) {
    return new LinkedHashSet<>(Arrays.asList(values));
  }

  private static class IntegerArraySetConverter implements TypeConverter<Array, Set<Integer>> {

    @Override
    public Set<Integer> decode(Array raw, Connection connection) {
      try {
        Object[] values = (Object[]) raw.getArray();
        Set<Integer> result = new LinkedHashSet<>();
        for (Object value : values) {
          result.add(((Number) value).intValue());
        }
        return result;
      } catch (Exception e) {
        throw new IllegalStateException("Could not decode INTEGER ARRAY", e);
      }
    }

    @Override
    public Array encode(Set<Integer> domain, Connection connection) {
      try {
        return connection.createArrayOf("INTEGER", domain.toArray(new Integer[0]));
      } catch (Exception e) {
        throw new IllegalStateException("Could not encode INTEGER ARRAY", e);
      }
    }
  }

  private static class IntegerArrayTable extends Table<Object, IntegerArrayUpdateTablePhase> {

    private final ConvertedEntityColumn<Array, Set<Integer>> numbers;

    private IntegerArrayTable() {
      super(null, null, Name.of("integer_array_values", false), "TABLE", null, Object.class, Object.class);
      IntegerArraySetConverter converter = new IntegerArraySetConverter();
      TypeHandler<Array, Set<Integer>> handler = TypeHandler.forConverter(converter, TypeSource.RUNTIME_DESIGNATED, null);
      ConvertedEntityColumnMetaData<Array, Set<Integer>> metadata = new ConvertedEntityColumnMetaData<>(
          Name.of("numbers", false), "numbers", "INTEGER ARRAY", null, null, handler, converter);
      this.numbers = new ConvertedEntityColumn<>(this, metadata);
      super.add(this.numbers);
    }

    @Override
    protected IntegerArrayUpdateTablePhase getUpdatePhase(org.hotrod.livesql.queries.LiveSQLContext context) {
      return new IntegerArrayUpdateTablePhase(context, this);
    }

    @Override
    protected org.hotrod.livesql.metadata.SQLMetaExpression star() {
      return new AllColumns(this.numbers);
    }
  }

  private static class IntegerArrayUpdateTablePhase extends UpdateTablePhase {

    private IntegerArrayUpdateTablePhase(org.hotrod.livesql.queries.LiveSQLContext context, IntegerArrayTable table) {
      super(context, table);
    }

    private IntegerArrayUpdateSetPhase set(ConvertedEntityColumn<Array, Set<Integer>> column, Set<Integer> value) {
      ConvertedValue convertedValue = ConvertedValue.of(value, CShield.getConverter(column));
      SShield.addSetter(this.update, column, convertedValue);
      return new IntegerArrayUpdateSetPhase(this.context, this.update);
    }
  }

  private static class IntegerArrayUpdateSetPhase extends UpdateSetPhase {

    private IntegerArrayUpdateSetPhase(org.hotrod.livesql.queries.LiveSQLContext context, UpdateObject update) {
      super(context, update);
    }

    private IntegerArrayUpdateSetPhase set(ConvertedEntityColumn<Array, Set<Integer>> column, Set<Integer> value) {
      ConvertedValue convertedValue = ConvertedValue.of(value, CShield.getConverter(column));
      SShield.addSetter(this.update, column, convertedValue);
      return new IntegerArrayUpdateSetPhase(this.context, this.update);
    }
  }
}
