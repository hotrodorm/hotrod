package tests;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.QueryWriter.PreparedQuery;
import org.hotrod.runtime.sql.SQL;
import org.hotrod.runtime.sql.dialects.PostgreSQLDialect;
import org.hotrod.runtime.sql.expressions.Constant;
import org.hotrod.runtime.sql.expressions.predicates.Equal;
import org.hotrod.runtime.sql.expressions.predicates.NotEqual;
import org.hotrod.runtime.sql.expressions.predicates.PredicateBuilder;

public class TestPredicateBuilder {

  public static void main(final String[] args) {
    QueryWriter w = new QueryWriter(new PostgreSQLDialect("PostgreSQL", "9.4.5", 9, 4));

    // ------------------------------------

    Constant.from(1);

    PredicateBuilder b = new PredicateBuilder(new Equal(Constant.from(1), Constant.from(2)));
    b.or(new Equal(Constant.from(3), Constant.from(4)));
    b.and(new Equal(Constant.from(5), Constant.from(6)));

    // b.and(new Or(new NotEqual(new Constant(20), new Constant(21)), new
    // NotEqual(new Constant(22), new Constant(23))));

    b.and(
        SQL.or(new NotEqual(Constant.from(20), Constant.from(21)), new NotEqual(Constant.from(22), Constant.from(23))));

    b.or(new Equal(Constant.from(9), Constant.from(10)));

    // ------------------------------------

    b.getAssembled().renderTo(w);
    PreparedQuery q = w.getPreparedQuery();

    System.out.println("--- SQL ---");
    System.out.println(q.getSQL());
    System.out.println("--- Parameters ---");
    for (String name : q.getParameters().keySet()) {
      Object value = q.getParameters().get(name);
      System.out.println(" * " + name + (value == null ? "" : " (" + value.getClass().getName() + ")") + ": " + value);
    }
    System.out.println("------------------");
  }

}
