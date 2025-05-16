package examples;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicInsertQuery;
import org.hotrod.dynamicsql.DynamicModificationQuery;
import org.hotrod.dynamicsql.DynamicSelectQuery;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.dynamicsql.PreparedModificationQuery;
import org.hotrod.dynamicsql.PreparedSelectQuery;
import org.hotrod.dynamicsql.Row;
import org.hotrod.dynamicsql.RowReader;
import org.hotrod.dynamicsql.assembler.DynamicSQL;
import org.hotrod.dynamicsql.insert.PreparedInsertQuery;
import org.hotrod.dynamicsql.insert.PrimaryKeyRetrievalMode;
import org.hotrod.dynamicsql.tuples.Tuple3;

public class TestExamples {

  // Example 1: Basic example with no parameters and no result
//  @Test
  public void example1() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicModificationQuery q = dyn.literal("UPDATE employee SET salary = salary + 10").endModificationQuery();

      int count = q.execute(conn);
      System.out.println("Updated rows: " + count);
    }
  }

  // Example 1.1: Previewing the assembled query (and parameters, if any)
//  @Test
  public void example1_1() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicModificationQuery q = dyn.literal("UPDATE employee SET salary = salary + 10").endModificationQuery();

      PreparedModificationQuery p = q.prepare();
      System.out.println("Dynamic Query:\n" + p.getPreview());
      int count = p.execute(conn);
      System.out.println("Updated rows: " + count);
    }
  }

  // Example 2: Applying parameters
//  @Test
  public void example2() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicModificationQuery q = dyn //
          .literal("UPDATE employee SET salary = salary + ") //
          .parameter("salaryIncrease") //
          .literal(" WHERE hired_on <= ") //
          .parameter("lastDate") //
          .endModificationQuery();

      Parameters params = dyn.newParameters();
      params.add("salaryIncrease", 15);
      params.add("lastDate", LocalDate.of(2022, 12, 31));

      PreparedModificationQuery p = q.prepare(params);
      System.out.println("Dynamic Query:\n" + p.getPreview());
      int count = p.execute(conn);
      System.out.println("Updated rows: " + count);
    }
  }

  // Example 3: Applying parameters: bean syntax (JEXL)

  public class Department {
    public Department(int minSalary) {
      this.minSalary = minSalary;
    }

    public int minSalary;
  }

  public class Branch {
    public Branch(int id, Department[] dept) {
      this.id = id;
      this.dept = dept;
    }

    public int id;
    public Department[] dept;
  }

//  @Test
  public void example3() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicModificationQuery q = dyn //
          .literal("UPDATE employee SET salary = salary + ") //
          .parameter("salaryIncrease") //
          .literal(" WHERE salary < ").parameter("branch.dept[1].minSalary") //
          .endModificationQuery();

      Parameters params = dyn.newParameters();
      params.add("salaryIncrease", 20);
      Department[] depts = { new Department(90), new Department(105), new Department(101) };
      Branch b = new Branch(1001, depts);
      params.add("branch", b);

      PreparedModificationQuery p = q.prepare(params);
      System.out.println("Dynamic Query:\n" + p.getPreview());
      int count = p.execute(conn);
      System.out.println("Updated rows: " + count);
    }
  }

  // Example 12. Injecting Parameters
//  @Test
  public void example12() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicSelectQuery q = dyn //
          .literal("SELECT * FROM ") //
          .parameterInjection("table") //
          .literal(" ORDER BY ") //
          .parameterInjection("ordering") //
          .endSelectQuery();

      Parameters params = dyn.newParameters();
      params.add("table", "employee");
      params.add("ordering", "hired_on DESC");

      PreparedSelectQuery<Row> p = q.prepare(params);
      System.out.println("Dynamic Query:\n" + p.getPreview());
      List<Row> rows = p.execute(conn);
      for (Row r : rows) {
        System.out.println(r);
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example 4. Making it dynamic: Dynamic SQL IF
//  @Test
  public void example4() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicModificationQuery q = dyn //
          .literal("UPDATE employee SET salary = salary + ") //
          .parameter("salaryIncrease") //
          .if_("!empty dept").literal(" WHERE dept_no = ").parameter("dept").endif() //
          .endModificationQuery();

      Parameters params = dyn.newParameters();
      params.add("salaryIncrease", 20);
      params.add("dept", 5); // updates 2 rows; if this line is commented out it updates all rows

      PreparedModificationQuery p = q.prepare(params);
      System.out.println("Dynamic Query:\n" + p.getPreview());
      int count = p.execute(conn);
      System.out.println("Updated rows: " + count);
    }
  }

  // Example 5. Selecting data: Single column
//  @Test
  public void example5() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicSelectQuery q = dyn //
          .literal("SELECT first_name FROM employee WHERE active = 'Y'") //
          .endSelectQuery();

      Parameters params = dyn.newParameters();

      PreparedSelectQuery<String> p = q.prepare(params, String.class);
      System.out.println("Dynamic Query:\n" + p.getPreview());
      List<String> names = p.execute(conn);
      for (String name : names) {
        System.out.println("Name: " + name);
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example 5. Selecting data: Tuples (two to six columns)
//  @Test
  public void example6() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicSelectQuery q = dyn //
          .literal("SELECT first_name, hired_on, salary FROM employee WHERE active = 'Y'") //
          .endSelectQuery();

      Parameters params = dyn.newParameters();

      PreparedSelectQuery<Tuple3<String, LocalDate, Integer>> p = q.prepare(params, String.class, LocalDate.class,
          Integer.class);
      System.out.println("Dynamic Query:\n" + p.getPreview());
      List<Tuple3<String, LocalDate, Integer>> names = p.execute(conn);
      for (Tuple3<String, LocalDate, Integer> r : names) {
        System.out.println("Name: " + r.getA() + " -- Hired: " + r.getB() + " -- Salary: " + r.getC());
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example 6. Selecting data: Generic Row
//  @Test
  public void example7() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicSelectQuery q = dyn //
          .literal("SELECT *, salary * 1.31 as gross_salary FROM employee WHERE active = 'Y'") //
          .endSelectQuery();

      Parameters params = dyn.newParameters();

      PreparedSelectQuery<Row> p = q.prepare(params);
      System.out.println("Dynamic Query:\n" + p.getPreview());
      List<Row> rows = p.execute(conn);
      for (Row r : rows) {
        System.out.println(r);
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example 8. Selecting data: Custom RowReader

  class MyEmployee {

    private String firstName;
    private LocalDate hired;
    private Double grossSalary;

    public MyEmployee(String firstName, LocalDate hired, Double grossSalary) {
      this.firstName = firstName;
      this.hired = hired;
      this.grossSalary = grossSalary;
    }

    public final String getFirstName() {
      return firstName;
    }

    public final LocalDate getHired() {
      return hired;
    }

    public final Double getGrossSalary() {
      return grossSalary;
    }

  }

//  @Test
  public void example8() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicSelectQuery q = dyn //
          .literal("SELECT first_name, hired_on, salary * 1.31 as gross_salary FROM employee WHERE active = 'Y'") //
          .endSelectQuery();

      Parameters params = dyn.newParameters();

      RowReader<MyEmployee> rr = new RowReader<MyEmployee>() {
        @Override
        public MyEmployee readRowFrom(ResultSet rs, Connection conn) throws SQLException {
          String fn = rs.getString(1);
          LocalDate ho = rs.getObject(2, LocalDate.class);
          Double gs = rs.getDouble(3);
          if (rs.wasNull())
            gs = null;
          return new MyEmployee(fn, ho, gs);
        }
      };

      PreparedSelectQuery<MyEmployee> p = q.prepare(params, rr);
      System.out.println("Dynamic Query:\n" + p.getPreview());
      List<MyEmployee> rows = p.execute(conn);
      for (MyEmployee r : rows) {
        System.out.println(
            "Name: " + r.getFirstName() + " -- Hired: " + r.getHired() + " -- Gross Salary: " + r.getGrossSalary());
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example X1. Selecting a single row
//  @Test
  public void exampleX1() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicSelectQuery q = dyn //
          .literal("SELECT * FROM employee WHERE id = ") //
          .parameter("id") //
          .endSelectQuery();

      Parameters params = dyn.newParameters();
      params.add("id", 104);

      PreparedSelectQuery<Row> p = q.prepare(params);
      System.out.println("Dynamic Query:\n" + p.getPreview());
      Row row = p.executeOne(conn);
      System.out.println(row);

    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example X2. Selecting using a Cursor
//  @Test
  public void exampleX2() throws DynamicExpressionException, SQLException, IOException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicSelectQuery q = dyn //
          .literal("SELECT * FROM employee WHERE last_name = ") //
          .parameter("last") //
          .literal(" ORDER BY hired_on DESC").endSelectQuery();

      Parameters params = dyn.newParameters();
      params.add("last", "Smith");

      PreparedSelectQuery<Row> p = q.prepare(params);
      System.out.println("Dynamic Query:\n" + p.getPreview());

      try (Cursor<Row> rows = p.executeCursor(conn)) {
        for (Row r : rows) {
          System.out.println(r);
        }
      }

    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example 8.1 DynamicSQL IF, nested
//@Test
  public void example81() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicSelectQuery q = dyn //
          .literal("SELECT * FROM employee WHERE active = 'Y'") //
          .if_("filter != null").if_("filter.firstName != null").literal(" AND first_name = ")
          .parameter("filter.firstName").endif().if_("filter.lastName != null").literal(" AND last_name = ")
          .parameter("filter.lastName").endif().if_("filter.firstSSN != null").literal(" AND last_ssn = ")
          .parameter("filter.lastSSN").endif().endif().endSelectQuery();

      Parameters params = dyn.newParameters();
      Map<String, Object> filter = new HashMap<>();
      filter.put("firstName", null);
      filter.put("lastName", "Andersson");
      filter.put("lastSSN", null);
      params.add("filter", filter);

      PreparedSelectQuery<Row> p = q.prepare(params);
      System.out.println("Dynamic Query:\n" + p.getPreview());
      List<Row> rows = p.execute(conn);
      for (Row r : rows) {
        System.out.println(r);
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example 9. Dynamic SQL: CHOOSE
//  @Test
  public void example9() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicSelectQuery q = dyn //
          .literal("SELECT *, salary * 1.31 as gross_salary FROM employee WHERE active = 'Y'") //
          .choose() //
          .when("ordering == 1").literal(" ORDER BY first_name").endwhen() //
          .when("ordering == 2").literal(" ORDER BY last_name").endwhen() //
          .when("ordering == 3").literal(" ORDER BY hired_on DESC").endwhen() //
          .otherwise().literal(" ORDER BY salary").endotherwise() //
          .endchoose() //
          .endSelectQuery();

      Parameters params = dyn.newParameters();
      params.add("ordering", 2);

      PreparedSelectQuery<Row> p = q.prepare(params);
      System.out.println("Dynamic Query:\n" + p.getPreview());
      List<Row> rows = p.execute(conn);
      for (Row r : rows) {
        System.out.println(r);
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example 9. Dynamic SQL: FOREACH
//  @Test
  public void example10() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicSelectQuery q = dyn //
          .literal("SELECT * FROM employee WHERE id in ") //
          .foreach("v", "ids", "(", ", ", ")") //
          .parameter("v") //
          .endforeach() //
          .endSelectQuery();

      Parameters params = dyn.newParameters();
      params.add("ids", Arrays.asList(101, 104, 105, 144));

      PreparedSelectQuery<Row> p = q.prepare(params);
      System.out.println("Dynamic Query:\n" + p.getPreview());
      List<Row> rows = p.execute(conn);
      for (Row r : rows) {
        System.out.println(r);
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example 11. Dynamic SQL: BIND
//  @Test
  public void example11() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicSelectQuery q = dyn //
          .bind("pattern", "'%' + part + '%'") //
          .literal("SELECT * FROM employee WHERE last_name like ") //
          .variable("pattern") //
          .endSelectQuery();

      Parameters params = dyn.newParameters();
      params.add("part", "mit");

      PreparedSelectQuery<Row> p = q.prepare(params);
      System.out.println("Dynamic Query:\n" + p.getPreview());
      List<Row> rows = p.execute(conn);
      for (Row r : rows) {
        System.out.println(r);
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example 13. Dynamic SQL: TRIM
//  @Test
  public void example13() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicSelectQuery q = dyn //
          .literal("SELECT ") //
          .trim("", ", ", "") //
          .if_("getFirstName").literal("first_name").endif() //
          .if_("getLastName").literal("last_name").endif() //
          .if_("getHiredDate").literal("hired_on").endif() //
          .endtrim() //
          .literal(" FROM employee") //
          .endSelectQuery();

      Parameters params = dyn.newParameters();
      params.add("getFirstName", true);
      params.add("getLastName", false);
      params.add("getHiredDate", true);

      PreparedSelectQuery<Row> p = q.prepare(params);
      System.out.println("Dynamic Query:\n" + p.getPreview());
      List<Row> rows = p.execute(conn);
      for (Row r : rows) {
        System.out.println(r);
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example 14. Dynamic SQL: WHERE

  public class Filter {
    public String first;
    public String last;
    public LocalDate hiredDate;
  }

//  @Test
  public void example14() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicSelectQuery q = dyn //
          .literal("SELECT * FROM employee") //
          .where("OR").if_("f.first != null").literal("first_name = ").parameter("f.first").endif() //
          .if_("f.last != null").literal("last_name = ").parameter("f.last").endif() //
          .if_("f.hiredDate != null").literal("hired_on = ").parameter("f.hiredDate").endif() //
          .endwhere() //
          .endSelectQuery();

      Parameters params = dyn.newParameters();
      Filter filter = new Filter();
      filter.first = null;
      filter.last = "Smith";
      filter.hiredDate = LocalDate.of(2023, 12, 22);
      params.add("f", filter);

      PreparedSelectQuery<Row> p = q.prepare(params);
      System.out.println("Dynamic Query:\n" + p.getPreview());
      List<Row> rows = p.execute(conn);
      for (Row r : rows) {
        System.out.println(r);
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example 15. Dynamic SQL: SET

  public class NewValues {
    public String first;
    public String last;
    public LocalDate hiredDate;
  }

//  @Test
  public void example15() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicModificationQuery q = dyn //
          .literal("UPDATE employee") //
          .set() //
          .if_("nv.first != null").literal("first_name = ").parameter("nv.first").endif() //
          .if_("nv.last != null").literal("last_name = ").parameter("nv.last").endif() //
          .if_("nv.hiredDate != null").literal("hired_on = ").parameter("nv.hiredDate").endif() //
          .endset() //
          .literal("WHERE id = ").parameter("id") //
          .endModificationQuery();

      Parameters params = dyn.newParameters();
      params.add("id", 103);
      NewValues newValues = new NewValues();
      newValues.first = "Leila";
      newValues.last = null;
      newValues.hiredDate = LocalDate.of(2023, 12, 25);
      params.add("nv", newValues);

      PreparedModificationQuery p = q.prepare(params);
      System.out.println("Dynamic Query:\n" + p.getPreview());
      int count = p.execute(conn);
      System.out.println("Updated rows: " + count);

    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example 20. Dynamic SQL: UPDATE
//  @Test
  public void example20() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicModificationQuery q = dyn //
          .literal("update employee set active = 'Y' where last_name = ") //
          .parameter("lastName") //
          .endModificationQuery();

      Parameters params = dyn.newParameters();
      params.add("lastName", "Smith");

      PreparedModificationQuery p = q.prepare(params);
      System.out.println("Dynamic Query:\n" + p.getPreview());
      int count = p.execute(conn);
      System.out.println("Updated " + count + " row(s).");
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example 21. Dynamic SQL: DELETE
//  @Test
  public void example21() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicModificationQuery q = dyn //
          .literal("delete from employee where last_name = ") //
          .parameter("lastName") //
          .endModificationQuery();

      Parameters params = dyn.newParameters();
      params.add("lastName", "Smith");

      PreparedModificationQuery p = q.prepare(params);
      System.out.println("Dynamic Query:\n" + p.getPreview());
      int count = p.execute(conn);
      System.out.println("Deleted " + count + " row(s).");
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example 22. Dynamic SQL: Insert w/o recovering the inserted primary key
//  @Test
  public void example22() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicInsertQuery q = dyn //
          .literal("insert into app_log (message, recorded_at) values (") //
          .parameter("msg") //
          .literal(", ") //
          .parameter("ts") //
          .literal(")") //
          .endInsertQuery(PrimaryKeyRetrievalMode.NO_RETRIEVAL);

      Parameters params = dyn.newParameters();
      params.add("msg", "This is normal log.");
      params.add("ts", LocalDateTime.now());

      PreparedInsertQuery p = q.prepare(params);
      System.out.println("Dynamic Query:\n" + p.getPreview());
      p.execute(conn);
      System.out.println("Inserted");
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example 30. Insert with identities
//  @Test
  public void example30() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicInsertQuery q = dyn //
          .literal("insert into employee (first_name, last_name) values (") //
          .parameter("firstName") //
          .literal(", ") //
          .parameter("lastName") //
          .literal(")") //
          .endInsertQuery(PrimaryKeyRetrievalMode.IDENTITY_INLINE_KEYS_RESULTSET);
//        .endInsertQuery(PrimaryKeyRetrievalMode.IDENTITY_INLINE_KEYS_RESULTSET, null, null, "id");

      Parameters params = dyn.newParameters();
      params.add("firstName", "Johnny");
      params.add("lastName", "Doe");

      PreparedInsertQuery p = q.prepare(params);
      System.out.println("Dynamic Query:\n" + p.getPreview());
      Long pk = p.execute(conn);
      System.out.println("Inserted -- new id=" + pk);
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example 30. Insert with Sequences
//@Test
public void example31() throws DynamicExpressionException, SQLException {
  try (Connection conn = getConnection()) {
    DynamicSQL dyn = new DynamicSQL();

    DynamicInsertQuery q = dyn //
        .literal("insert into office (name) values (") //
        .parameter("officeName") //
        .literal(")") //
        .endInsertQuery(PrimaryKeyRetrievalMode.SEQUENCE_INLINE_KEYS_RESULTSET);

    Parameters params = dyn.newParameters();
    params.add("officeName", "Downtown VIP");

    PreparedInsertQuery p = q.prepare(params);
    System.out.println("Dynamic Query:\n" + p.getPreview());
    Long pk = p.execute(conn);
    System.out.println("Inserted -- new id=" + pk);
  } catch (RuntimeException e) {
    e.printStackTrace();
    throw e;
  }
}


  Connection getConnection() throws SQLException {
    return DriverManager
        .getConnection("jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './src/test/schema.sql';DB_CLOSE_DELAY=-1", "", "");
  }

}
