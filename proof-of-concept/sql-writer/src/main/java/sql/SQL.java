package sql;

import java.util.Date;

import metadata.Column;
import sql.predicates.Constant;
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
    return new SelectColumns();
  }

  public static SelectColumns select(final Column... columns) {
    return new SelectColumns(columns);
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

}
