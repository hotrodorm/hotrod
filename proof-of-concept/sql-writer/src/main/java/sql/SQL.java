package sql;

import java.util.Date;

import metadata.Column;
import sql.predicates.And;
import sql.predicates.Constant;
import sql.predicates.Not;
import sql.predicates.Or;
import sql.predicates.Predicate;
import sql.predicates.Constant.JDBCType;

/*
 * Stages:
 * 
 * - select
 * - from
 * - where
 * - groupBy
 * - having
 * - orderBy
 * - limit
 * 
 */

public class SQL {

  public static SelectColumns select() {
    return new SelectColumns(resolveSQLTranslator());
  }

  public static SelectColumns select(final Column... columns) {
    return new SelectColumns(resolveSQLTranslator(), columns);
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

  // SQL translator resolver

  private static SQLDialect sqlTranslator = null;

  private static SQLDialect resolveSQLTranslator() {
    if (sqlTranslator == null) {
      sqlTranslator = new PostgreSQLDialect();
    }
    return sqlTranslator;
  }

}
