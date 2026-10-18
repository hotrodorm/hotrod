package org.hotrod.dynamicsql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.hotrod.dynamicsql.assembler.DynamicSQL;
import org.junit.jupiter.api.Test;

class DynamicSQLAssemblyTest {

  @Test
  void assemblesParameterAndIncludesItsValueInPreview() throws DynamicExpressionException {
    DynamicSQL dyn = new DynamicSQL();
    DynamicSelectQuery query = dyn.literal("SELECT * FROM employee WHERE id = ").parameter("id").endSelectQuery();

    Parameters params = dyn.newParameters();
    params.add("id", 42);

    PreparedSelectQuery<Row> prepared = query.prepare(params);

    assertEquals("SELECT * FROM employee WHERE id = ?", prepared.getPreview());
    assertTrue(prepared.getPreview(true).contains("JDBC Parameters (1):"));
    assertTrue(prepared.getPreview(true).contains("id: 42"));
  }

  @Test
  void includesConditionalSqlOnlyWhenExpressionIsTrue() throws DynamicExpressionException {
    DynamicSQL dyn = new DynamicSQL();
    DynamicSelectQuery query = dyn.literal("SELECT * FROM employee").if_("filterByDepartment")
        .literal(" WHERE dept_no = ").parameter("department").endif().endSelectQuery();

    Parameters params = dyn.newParameters();
    params.add("filterByDepartment", true);
    params.add("department", 7);
    assertEquals("SELECT * FROM employee WHERE dept_no = ?", query.prepare(params).getPreview());

    params.add("filterByDepartment", false);
    assertEquals("SELECT * FROM employee", query.prepare(params).getPreview());
  }

  @Test
  void chooseSelectsMatchingBranchAndSupportsOtherwise() throws DynamicExpressionException {
    DynamicSQL dyn = new DynamicSQL();
    DynamicSelectQuery query = dyn.literal("SELECT * FROM employee").choose().when("ordering == 1")
        .literal(" ORDER BY first_name").endwhen().when("ordering == 2").literal(" ORDER BY last_name").endwhen()
        .otherwise().literal(" ORDER BY salary").endotherwise().endchoose().endSelectQuery();

    Parameters params = dyn.newParameters();
    params.add("ordering", 2);
    assertEquals("SELECT * FROM employee ORDER BY last_name", query.prepare(params).getPreview());

    params.add("ordering", 3);
    assertEquals("SELECT * FROM employee ORDER BY salary", query.prepare(params).getPreview());

    DynamicSelectQuery withoutOtherwise = dyn.literal("SELECT * FROM employee").choose().when("ordering == 1")
        .literal(" ORDER BY first_name").endwhen().endchoose().endSelectQuery();

    params.add("ordering", 3);
    assertEquals("SELECT * FROM employee", withoutOtherwise.prepare(params).getPreview());
  }

  @Test
  void expandsForeachValuesIntoPlaceholders() throws DynamicExpressionException {
    DynamicSQL dyn = new DynamicSQL();
    DynamicSelectQuery query = dyn.literal("SELECT * FROM employee WHERE id IN ").foreach("id", "ids", "(", ", ", ")")
        .parameter("id").endforeach().endSelectQuery();

    Parameters params = dyn.newParameters();
    params.add("ids", Arrays.asList(101, 104, 105));

    PreparedSelectQuery<Row> prepared = query.prepare(params);

    assertEquals("SELECT * FROM employee WHERE id IN (?, ?, ?)", prepared.getPreview());
    assertTrue(prepared.getPreview(true).contains("JDBC Parameters (3):"));
  }

  @Test
  void expandsNestedForeachValues() throws DynamicExpressionException {
    DynamicSQL dyn = new DynamicSQL();
    DynamicSelectQuery query = dyn.literal("SELECT * FROM item WHERE (group_id, item_id) IN ")
        .foreach("group", "groups", "(", ", ", ")").foreach("item", "group", "[", ", ", "]").parameter("item")
        .endforeach().endforeach().endSelectQuery();

    Parameters params = dyn.newParameters();
    params.add("groups", Arrays.asList(Arrays.asList(10, 101, 102), Arrays.asList(20, 201)));

    PreparedSelectQuery<Row> prepared = query.prepare(params);

    assertEquals("SELECT * FROM item WHERE (group_id, item_id) IN ([?, ?, ?], [?, ?])", prepared.getPreview());
    assertTrue(prepared.getPreview(true).contains("JDBC Parameters (5):"));
  }

  @Test
  void injectsConfiguredIdentifierTextIntoSql() throws DynamicExpressionException {
    DynamicSQL dyn = new DynamicSQL();
    DynamicSelectQuery query = dyn.literal("SELECT * FROM ").parameterInjection("table").literal(" ORDER BY ")
        .parameterInjection("ordering").endSelectQuery();

    Parameters params = dyn.newParameters();
    params.add("table", "employee");
    params.add("ordering", "hired_on DESC");

    assertEquals("SELECT * FROM employee ORDER BY hired_on DESC", query.prepare(params).getPreview());
  }

  @Test
  void parameterInjectionAddsTextWithoutCreatingJdbcParameters() throws DynamicExpressionException {
    DynamicSQL dyn = new DynamicSQL();
    DynamicSelectQuery query = dyn.literal("SELECT * FROM ").parameterInjection("table").endSelectQuery();

    Parameters params = dyn.newParameters();
    params.add("table", "employee");

    PreparedSelectQuery<Row> prepared = query.prepare(params);

    assertEquals("SELECT * FROM employee", prepared.getPreview());
    assertTrue(prepared.getPreview(true).contains("JDBC Parameters (0):\n  N/A"));

    params.add("table", null);
    assertEquals("SELECT * FROM ", query.prepare(params).getPreview());
  }

  @Test
  void trimsOptionalFragmentsAndAddsSeparatorsOnlyBetweenIncludedFragments() throws DynamicExpressionException {
    DynamicSQL dyn = new DynamicSQL();
    DynamicSelectQuery query = dyn.literal("SELECT ").trim("", ", ", "").if_("includeFirstName").literal("first_name")
        .endif().if_("includeLastName").literal("last_name").endif().endtrim().literal(" FROM employee")
        .endSelectQuery();

    Parameters params = dyn.newParameters();
    params.add("includeFirstName", true);
    params.add("includeLastName", true);
    assertEquals("SELECT first_name, last_name FROM employee", query.prepare(params).getPreview());

    params.add("includeFirstName", false);
    assertEquals("SELECT last_name FROM employee", query.prepare(params).getPreview());
  }

  @Test
  void whereFormatsOnlyActivePredicates() throws DynamicExpressionException {
    DynamicSQL dyn = new DynamicSQL();
    DynamicSelectQuery query = dyn.literal("SELECT * FROM employee ").where("OR").if_("includeFirstName")
        .literal("first_name = ").parameter("firstName").endif().if_("includeDepartment").literal("dept_no = ")
        .parameter("department").endif().endwhere().endSelectQuery();

    Parameters params = dyn.newParameters();
    params.add("includeFirstName", true);
    params.add("includeDepartment", true);
    params.add("firstName", "Smith");
    params.add("department", 7);

    assertEquals("SELECT * FROM employee WHERE first_name = ?\n  OR dept_no = ?", query.prepare(params).getPreview());

    params.add("includeFirstName", false);
    assertEquals("SELECT * FROM employee WHERE dept_no = ?", query.prepare(params).getPreview());
  }

  @Test
  void setFormatsConditionalAssignments() throws DynamicExpressionException {
    DynamicSQL dyn = new DynamicSQL();
    DynamicModificationQuery query = dyn.literal("UPDATE employee").set().if_("updateFirstName")
        .literal("first_name = ").parameter("firstName").endif().if_("updateLastName").literal("last_name = ")
        .parameter("lastName").endif().endset().literal(" WHERE id = ").parameter("id").endModificationQuery();

    Parameters params = dyn.newParameters();
    params.add("updateFirstName", true);
    params.add("updateLastName", true);
    params.add("firstName", "Ada");
    params.add("lastName", "Lovelace");
    params.add("id", 42);

    assertEquals("UPDATE employee\nSET first_name = ?,\n    last_name = ?\n WHERE id = ?",
        query.prepare(params).getPreview());
  }

  @Test
  void bindEvaluatesAnExpressionForVariableUse() throws DynamicExpressionException {
    DynamicSQL dyn = new DynamicSQL();
    DynamicSelectQuery query = dyn.bind("pattern", "'%' + part + '%'")
        .literal("SELECT * FROM employee WHERE last_name LIKE ").variable("pattern").endSelectQuery();

    Parameters params = dyn.newParameters();
    params.add("part", "mit");

    PreparedSelectQuery<Row> prepared = query.prepare(params);

    assertEquals("SELECT * FROM employee WHERE last_name LIKE ?", prepared.getPreview());
    assertTrue(prepared.getPreview(true).contains("pattern: %mit%"));
  }

  @Test
  void parameterNullableAddsPlaceholderAndJdbcTypeToPreview() throws DynamicExpressionException {
    DynamicSQL dyn = new DynamicSQL();
    DynamicSelectQuery query = dyn.literal("SELECT * FROM employee WHERE last_name = ")
        .parameterNullable("lastName", Types.VARCHAR)
        .endSelectQuery();

    Parameters params = dyn.newParameters();
    params.add("lastName", null);

    PreparedSelectQuery<Row> prepared = query.prepare(params);

    assertEquals("SELECT * FROM employee WHERE last_name = ?", prepared.getPreview());
    assertTrue(prepared.getPreview(true).contains("lastName (VARCHAR): null"));
  }

  @Test
  void variableEvaluatesTheReferencedParameterAsJdbcValue() throws DynamicExpressionException {
    DynamicSQL dyn = new DynamicSQL();
    DynamicSelectQuery query = dyn.literal("SELECT * FROM employee WHERE last_name = ")
        .variable("lastName")
        .endSelectQuery();

    Parameters params = dyn.newParameters();
    params.add("lastName", "Smith");

    PreparedSelectQuery<Row> prepared = query.prepare(params);

    assertEquals("SELECT * FROM employee WHERE last_name = ?", prepared.getPreview());
    assertTrue(prepared.getPreview(true).contains("lastName: Smith"));
  }

  @Test
  void beginGroupsNestedSegmentsWithoutChangingTheirSql() throws DynamicExpressionException {
    DynamicSQL dyn = new DynamicSQL();
    DynamicSelectQuery query = dyn.literal("SELECT * FROM employee WHERE ")
        .begin()
        .literal("id = ")
        .parameter("id")
        .end()
        .literal(" AND active = 'Y'")
        .endSelectQuery();

    Parameters params = dyn.newParameters();
    params.add("id", 101);

    PreparedSelectQuery<Row> prepared = query.prepare(params);

    assertEquals("SELECT * FROM employee WHERE id = ? AND active = 'Y'", prepared.getPreview());
    assertTrue(prepared.getPreview(true).contains("id: 101"));
  }

  @Test
  void executesSelectOneAndMapsTheSingleRow() throws DynamicExpressionException, SQLException {
    DynamicSQL dyn = new DynamicSQL();
    DynamicSelectQuery query = dyn.literal("SELECT id FROM employee WHERE id = ").parameter("id").endSelectQuery();

    Parameters params = dyn.newParameters();
    params.add("id", 104);

    try (Connection conn = getConnection()) {
      Row row = query.prepare(params).executeOne(conn);
      assertEquals(104, row.get("ID"));
    }
  }

  @Test
  void executesSelectCursorAndReadsRowsInQueryOrder() throws DynamicExpressionException, IOException, SQLException {
    DynamicSQL dyn = new DynamicSQL();
    DynamicSelectQuery query = dyn.literal("SELECT id FROM employee WHERE active = 'Y' ORDER BY id").endSelectQuery();

    DataSource dataSource = getDataSource();
    List<Integer> ids = new ArrayList<>();
    try (Cursor<Row> cursor = query.prepare(dyn.newParameters()).executeCursor(dataSource)) {
      for (Row row : cursor) {
        ids.add((Integer) row.get("ID"));
      }
    }

    assertEquals(Arrays.asList(101, 102, 103, 105), ids);
  }

  private Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:h2:mem:ASSEMBLY_TEST;INIT=RUNSCRIPT FROM 'classpath:/schema.sql'", "", "");
  }

  private DataSource getDataSource() {
    JdbcDataSource dataSource = new JdbcDataSource();
    dataSource.setURL("jdbc:h2:mem:ASSEMBLY_CURSOR_TEST;INIT=RUNSCRIPT FROM 'classpath:/schema.sql'");
    return dataSource;
  }
}