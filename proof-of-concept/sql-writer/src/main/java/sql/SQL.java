package sql;

import java.util.Date;

import sql.expressions.Constant;
import sql.expressions.Constant.JDBCType;
import sql.expressions.Expression;
import sql.expressions.OrderingTerm;
import sql.expressions.aggregations.Count;
import sql.expressions.aggregations.CountDistinct;
import sql.expressions.predicates.And;
import sql.expressions.predicates.Not;
import sql.expressions.predicates.Or;
import sql.expressions.predicates.Predicate;

/*
 * Stages:
 * 
 * - select
 * - from
 * - where
 * - groupBy
 * - having
 * - orderBy
 * - offset
 * - limit
 * 
 */

public class SQL {

  public static SelectColumns select() {
    return new SelectColumns(resolveSQLDialect());
  }

  public static SelectColumns select(final Expression... queryColumns) {
    return new SelectColumns(resolveSQLDialect(), queryColumns);
  }

  // Constants

  public static Constant constant(final String value) {
    return new Constant(value);
  }

  public static Constant constant(final Character value) {
    return new Constant(value);
  }

  public static Constant constant(final Number value) {
    return new Constant(value);
  }

  public static Constant constant(final Boolean value) {
    return new Constant(value);
  }

  public static Constant constant(final Date value) {
    return new Constant(value);
  }

  public static Constant constant(final Object value, final JDBCType type) {
    return new Constant(value, type);
  }

  // Predicates

  public static Predicate or(final Predicate a, final Predicate b) {
    return new Or(a, b);
  }

  public static Predicate and(final Predicate a, final Predicate b) {
    return new And(a, b);
  }

  public static Predicate not(final Predicate a) {
    return new Not(a);
  }

  // Ordering expressions

  public static OrderingTerm asc(final Expression expression) {
    return new OrderingTerm(expression, true);
  }

  public static OrderingTerm desc(final Expression expression) {
    return new OrderingTerm(expression, false);
  }

  // Aggregation expressions

  public static Count count() {
    return new Count();
  }

  public static CountDistinct countDistinct(final Expression... expressions) {
    return new CountDistinct(expressions);
  }

  // SQL translator resolver

  private static SQLDialect sqlDialect = null;

  private static SQLDialect resolveSQLDialect() {
    if (sqlDialect == null) {
      sqlDialect = new PostgreSQLDialect();
    }
    return sqlDialect;
  }

}
