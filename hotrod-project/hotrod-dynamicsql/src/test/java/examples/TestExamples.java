package examples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.hotrod.data.Row;
import org.hotrod.data.RowReader;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicModificationQuery;
import org.hotrod.dynamicsql.DynamicSelectQuery;
import org.hotrod.dynamicsql.ParameterContext;
import org.hotrod.dynamicsql.PreparedModificationQuery;
import org.hotrod.dynamicsql.PreparedSelectQuery;
import org.hotrod.dynamicsql.assembler.DynamicSQL;
import org.hotrod.dynamicsql.tuples.Tuple3;
import org.junit.jupiter.api.Test;

public class TestExamples {

  // Example 1: Basic example with no parameters and no result
//  @Test
  public void example1() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicModificationQuery q = dyn.literal("UPDATE employee SET salary = salary + 10").endModificationQuery();

      PreparedModificationQuery pq = q.prepare();
      System.out.println("Dynamic Query:\n" + pq.getPreview());
      int count = pq.execute(conn);
      System.out.println("Updated rows: " + count);
    }
  }

  // Example 2: Adding parameters
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

      ParameterContext ctx = dyn.newParameterContext();
      ctx.add("salaryIncrease", 15);
      ctx.add("lastDate", LocalDate.of(2022, 12, 31));

      PreparedModificationQuery pq = q.prepare(ctx);
      System.out.println("Dynamic Query:\n" + pq.getPreview());
      int count = pq.execute(conn);
      System.out.println("Updated rows: " + count);
    }
  }

  // Example 3: Resolving parameters using JEXL syntax, no result set

  public class Department {
    public int minSalary;
  }

  public class Branch {
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
          .literal(" WHERE salary < ") //
          .parameter("branch.dept[1].minSalary") //
          .endModificationQuery();

      ParameterContext ctx = dyn.newParameterContext();
      ctx.add("salaryIncrease", 20);
      Branch b = new Branch();
      b.id = 1001;
      b.dept = new Department[3];
      b.dept[1] = new Department();
      b.dept[1].minSalary = 105;
      ctx.add("branch", b);

      PreparedModificationQuery pq = q.prepare(ctx);
      System.out.println("Dynamic Query:\n" + pq.getPreview());
      int count = pq.execute(conn);
      System.out.println("Updated rows: " + count);
    }
  }

  // Example 4. Dynamic IF
//  @Test
  public void example4() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicModificationQuery q = dyn //
          .literal("UPDATE employee SET salary = salary + ") //
          .parameter("salaryIncrease") //
          .if_("!empty dept", dyn.literal(" WHERE dept_no = ").parameter("dept").end()) //
          .endModificationQuery();

      ParameterContext ctx = dyn.newParameterContext();
      ctx.add("salaryIncrease", 20);
      ctx.add("dept", 5); // updates 2 rows; if this line is commented out it updates all rows

      PreparedModificationQuery pq = q.prepare(ctx);
      System.out.println("Dynamic Query:\n" + pq.getPreview());
      int count = pq.execute(conn);
      System.out.println("Updated rows: " + count);
    }
  }

  // Example 5. Selecting data (single column)
//  @Test
  public void example5() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicSelectQuery q = dyn //
          .literal("SELECT first_name FROM employee WHERE active = 'Y'") //
          .endSelectQuery();

      ParameterContext ctx = dyn.newParameterContext();

      PreparedSelectQuery<String> pq = q.prepare(ctx, String.class);
      System.out.println("Dynamic Query:\n" + pq.getPreview());
      List<String> names = pq.execute(conn);
      for (String name : names) {
        System.out.println("Name: " + name);
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example 5. Selecting data (two to six columns)
//  @Test
  public void example6() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicSelectQuery q = dyn //
          .literal("SELECT first_name, hired_on, salary FROM employee WHERE active = 'Y'") //
          .endSelectQuery();

      ParameterContext ctx = dyn.newParameterContext();

      PreparedSelectQuery<Tuple3<String, LocalDate, Integer>> pq = q.prepare(ctx, String.class, LocalDate.class,
          Integer.class);
      System.out.println("Dynamic Query:\n" + pq.getPreview());
      List<Tuple3<String, LocalDate, Integer>> names = pq.execute(conn);
      for (Tuple3<String, LocalDate, Integer> r : names) {
        System.out.println("Name: " + r.getA() + " -- Hired: " + r.getB() + " -- Salary: " + r.getC());
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example 6. Selecting data to a Map
//  @Test
  public void example7() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicSelectQuery q = dyn //
          .literal("SELECT *, salary * 1.31 as gross_salary FROM employee WHERE active = 'Y'") //
          .endSelectQuery();

      ParameterContext ctx = dyn.newParameterContext();

      PreparedSelectQuery<Row> pq = q.prepare(ctx);
      System.out.println("Dynamic Query:\n" + pq.getPreview());
      List<Row> rows = pq.execute(conn);
      for (Row r : rows) {
        System.out.println(r);
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example 8. Selecting data using a custom RowReader

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

      ParameterContext ctx = dyn.newParameterContext();

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

      PreparedSelectQuery<MyEmployee> pq = q.prepare(ctx, rr);
      System.out.println("Dynamic Query:\n" + pq.getPreview());
      List<MyEmployee> rows = pq.execute(conn);
      for (MyEmployee r : rows) {
        System.out.println(
            "Name: " + r.getFirstName() + " -- Hired: " + r.getHired() + " -- Gross Salary: " + r.getGrossSalary());
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  // Example 9. Dynamic CHOOSE
  @Test
  public void example9() throws DynamicExpressionException, SQLException {
    try (Connection conn = getConnection()) {
      DynamicSQL dyn = new DynamicSQL();

      DynamicSelectQuery q = dyn //
          .literal("SELECT *, salary * 1.31 as gross_salary FROM employee WHERE active = 'Y'") //
          .choose() //
          .when("ordering == 1", dyn.literal(" ORDER BY last_name").end()) //
          .when("ordering == 2", dyn.literal(" ORDER BY hired_on DESC").end()) //
          .otherwise(dyn.literal(" ORDER BY salary").end()) //
          .endSelectQuery();

      ParameterContext ctx = dyn.newParameterContext();
      ctx.add("ordering", null);

      PreparedSelectQuery<Row> pq = q.prepare(ctx);
      System.out.println("Dynamic Query:\n" + pq.getPreview());
      List<Row> rows = pq.execute(conn);
      for (Row r : rows) {
        System.out.println(r);
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
  }

  Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1", "",
        "");
  }

}
