package tests;

import sql.QueryWriter;
import sql.QueryWriter.PreparedQuery;
import sql.SQL;
import sql.expressions.Constant;
import sql.expressions.predicates.Equal;
import sql.expressions.predicates.NotEqual;
import sql.expressions.predicates.PredicateBuilder;
import sql.sqldialects.PostgreSQLDialect;

public class TestPredicateBuilder {

  public static void main(final String[] args) {
    QueryWriter w = new QueryWriter(new PostgreSQLDialect());

    // ------------------------------------

    PredicateBuilder b = new PredicateBuilder(new Equal(new Constant(1), new Constant(2)));
    b.or(new Equal(new Constant(3), new Constant(4)));
    b.and(new Equal(new Constant(5), new Constant(6)));

//    b.and(new Or(new NotEqual(new Constant(20), new Constant(21)), new NotEqual(new Constant(22), new Constant(23))));

    b.and(SQL.or(new NotEqual(new Constant(20), new Constant(21)), new NotEqual(new Constant(22), new Constant(23))));

    b.or(new Equal(new Constant(9), new Constant(10)));

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
