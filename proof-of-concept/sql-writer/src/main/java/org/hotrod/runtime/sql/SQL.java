package org.hotrod.runtime.sql;

import java.util.Date;

import org.hotrod.runtime.sql.caseclause.CaseWhenStage;
import org.hotrod.runtime.sql.dialects.PostgreSQLDialect;
import org.hotrod.runtime.sql.dialects.SQLDialect;
import org.hotrod.runtime.sql.exceptions.InvalidSQLStatementException;
import org.hotrod.runtime.sql.expressions.Coalesce;
import org.hotrod.runtime.sql.expressions.Constant;
import org.hotrod.runtime.sql.expressions.Expression;
import org.hotrod.runtime.sql.expressions.JDBCType;
import org.hotrod.runtime.sql.expressions.OrderingTerm;
import org.hotrod.runtime.sql.expressions.ResultSetColumn;
import org.hotrod.runtime.sql.expressions.Tuple;
import org.hotrod.runtime.sql.expressions.aggregations.Count;
import org.hotrod.runtime.sql.expressions.aggregations.CountDistinct;
import org.hotrod.runtime.sql.expressions.predicates.And;
import org.hotrod.runtime.sql.expressions.predicates.Exists;
import org.hotrod.runtime.sql.expressions.predicates.Not;
import org.hotrod.runtime.sql.expressions.predicates.NotExists;
import org.hotrod.runtime.sql.expressions.predicates.Or;
import org.hotrod.runtime.sql.expressions.predicates.Predicate;

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

  // select

  public static SelectColumns createSelect() {
    return new SelectColumns(resolveSQLDialect(), false);
  }

  public static SelectColumns createSelect(final ResultSetColumn... resultSetColumns) {
    return new SelectColumns(resolveSQLDialect(), false, resultSetColumns);
  }

  public static SelectColumns createSubquery() {
    return new SelectColumns(resolveSQLDialect(), false);
  }

  public static SelectColumns createSubquery(final ReferenceableExpression... referenceableExpressions) {
    return new SelectColumns(resolveSQLDialect(), false, referenceableExpressions);
  }

  // select distinct

  public static SelectColumns createSelectDistinct() {
    return new SelectColumns(resolveSQLDialect(), true);
  }

  public static SelectColumns createSelectDistinct(final ResultSetColumn... resultSetColumns) {
    return new SelectColumns(resolveSQLDialect(), true, resultSetColumns);
  }

  public static SelectColumns createSubqueryDistinct() {
    return new SelectColumns(resolveSQLDialect(), true);
  }

  public static SelectColumns createSubqueryDistinct(final ReferenceableExpression... referenceableExpressions) {
    return new SelectColumns(resolveSQLDialect(), true, referenceableExpressions);
  }

  // Tuples

  public static Tuple tuple(final Expression<?>... expressions) {
    return new Tuple(expressions);
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

  // Subquery existence

  public static Predicate exists(final ExecutableSelect subquery) {
    return new Exists(subquery);
  }

  public static Predicate notExists(final ExecutableSelect subquery) {
    return new NotExists(subquery);
  }

  // Ordering expressions

  public static OrderingTerm asc(final Expression<?> expression) {
    return new OrderingTerm(expression, true);
  }

  public static OrderingTerm desc(final Expression<?> expression) {
    return new OrderingTerm(expression, false);
  }

  // Aggregation expressions

  public static Count count() {
    return new Count();
  }

  public static CountDistinct countDistinct(final Expression<?>... expressions) {
    return new CountDistinct(expressions);
  }

  // Case

  public static <T> CaseWhenStage<T> caseWhen(Predicate predicate, Expression<T> value) {
    return new CaseWhenStage<T>(predicate, value);
  }

  public static <T> CaseWhenStage<T> caseWhen(Predicate predicate, T value) {
    Constant<T> v = box(value);
    return new CaseWhenStage<T>(predicate, v);
  }

  // General functions

  @SafeVarargs
  public static <T> Coalesce<T> coalesce(final Expression<T> first, final Expression<T>... rest) {
    return new Coalesce<T>(first, rest);
  }

  // SQL translator resolver

  private static SQLDialect sqlDialect = null;

  private static SQLDialect resolveSQLDialect() {
    if (sqlDialect == null) {
      sqlDialect = new PostgreSQLDialect();
    }
    return sqlDialect;
  }

  // Utilities

  public static <T> Constant<T> box(final T value) {
    Constant<T> v;
    if (value instanceof String) {
      v = new Constant<T>(value, JDBCType.VARCHAR);
    } else if (value instanceof Character) {
      v = new Constant<T>(value, JDBCType.VARCHAR);
    } else if (value instanceof Number) {
      v = new Constant<T>(value, JDBCType.NUMERIC);
    } else if (value instanceof Date) {
      v = new Constant<T>(value, JDBCType.TIMESTAMP);
    } else if (value instanceof Boolean) {
      v = new Constant<T>(value, JDBCType.BOOLEAN);
    } else {
      throw new InvalidSQLStatementException("Invalid expression type: " + value.getClass());
    }
    return v;
  }

}
