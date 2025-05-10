package manual;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.hotrod.data.Row;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicSelectQuery;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.dynamicsql.PreparedSelectQuery;
import org.hotrod.dynamicsql.assembler.DynamicSQL;
import org.hotrod.dynamicsql.tuples.Tuple3;
import org.junit.jupiter.api.Assertions;

public class TestParameterTesting {

//  @Test
  public void test1() throws DynamicExpressionException, SQLException {

    DynamicSQL dyn = new DynamicSQL();

    List<Integer> codes = Arrays.asList(12, 14, 16);
    List<Integer> offsets = Arrays.asList(7, 17, 27);

//    DynamicSelectQuery q = b.literal("SELECT * FROM t WHERE a >= ").parameter("id").endSelectQuery();
    DynamicSelectQuery q = dyn.literal("SELECT * FROM t WHERE a in ") //
        .foreach("v", "codes", "(", ", ", ")", dyn.parameter("v")). //
        endSelectQuery();

    Parameters ctx = dyn.newParameters();
    ctx.add("id", 12);
    ctx.add("name", "ghi");
    ctx.add("cost", 105.43);
    ctx.add("codes", codes);
    ctx.add("offsets", offsets);

    PreparedSelectQuery<Row> pq = q.prepare(ctx);

    String preview = pq.getPreview(true);

    System.out.println("query:\n" + preview);

    try (Connection conn = DriverManager
        .getConnection("jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1", "", "")) {
      System.out.println("-- Connected --");
      List<Row> rows = pq.execute(conn);
      rows.forEach(r -> System.out.println("r: " + r));
    }

    Assertions.assertTrue(true);

  }

//  @Test
  public void test2() throws DynamicExpressionException, SQLException, IOException {

    DynamicSQL dyn = new DynamicSQL();

    List<Integer> codes = Arrays.asList(12, 14, 16);
    List<Integer> offsets = Arrays.asList(7, 17, 27);

//    DynamicSelectQuery q = b.literal("SELECT * FROM t WHERE a >= ").parameter("id").endSelectQuery();
    DynamicSelectQuery q = dyn.literal("SELECT a, a*a as b, b ||'...' as c FROM t WHERE a in ") //
        .foreach("v", "codes", "(", ", ", ")", dyn.parameter("v")). //
        endSelectQuery();

    Parameters ctx = dyn.newParameters();
    ctx.add("id", 12);
    ctx.add("name", "ghi");
    ctx.add("cost", 105.43);
    ctx.add("codes", codes);
    ctx.add("offsets", offsets);

//    PreparedSelectQuery<Integer> pq = q.prepare(ctx, new RowReader<Integer>() {
//
//      @Override
//      public Integer readRowFrom(ResultSet rs, Connection conn) throws SQLException {
//        int v = rs.getInt(1);
//        return rs.wasNull() ? null : v;
//      }
//
//    });

//    PreparedSelectQuery<Integer> pq = q.prepare(ctx, Integer.class);
    PreparedSelectQuery<Tuple3<Integer, Integer, String>> pq = q.prepare(ctx, Integer.class, Integer.class,
        String.class);

    String preview = pq.getPreview(true);

    System.out.println("query:\n" + preview);

    try (Connection conn = DriverManager
        .getConnection("jdbc:h2:mem:EXAMPLEDB;INIT=runscript from './schema.sql';DB_CLOSE_DELAY=-1", "", "")) {
      System.out.println("-- Connected --");

      List<Tuple3<Integer, Integer, String>> rows = pq.execute(conn);
      rows.forEach(r -> System.out.println("r: " + r.getA() + " - " + r.getB() + " - " + r.getC()));

//      Tuple3<Integer, Integer, String> r = pq.executeOne(conn);
//      System.out.println("r: " + r.getA() + " - " + r.getB() + " - " + r.getC());

//      try (Cursor<Tuple3<Integer, Integer, String>> rows = pq.executeCursor(conn)) {
//        rows.forEach(r -> System.out.println("r: " + r.getA() + " - " + r.getB() + " - " + r.getC()));
//      }

    }

    Assertions.assertTrue(true);

  }

}
