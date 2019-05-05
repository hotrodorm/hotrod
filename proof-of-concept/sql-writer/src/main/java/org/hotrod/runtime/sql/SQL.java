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
import org.hotrod.runtime.sql.expressions.ResultSetColumn;
import org.hotrod.runtime.sql.expressions.Tuple;
import org.hotrod.runtime.sql.expressions.aggregations.Avg;
import org.hotrod.runtime.sql.expressions.aggregations.AvgDistinct;
import org.hotrod.runtime.sql.expressions.aggregations.Count;
import org.hotrod.runtime.sql.expressions.aggregations.CountDistinct;
import org.hotrod.runtime.sql.expressions.aggregations.GroupConcat;
import org.hotrod.runtime.sql.expressions.aggregations.GroupConcatDistinct;
import org.hotrod.runtime.sql.expressions.aggregations.Max;
import org.hotrod.runtime.sql.expressions.aggregations.Min;
import org.hotrod.runtime.sql.expressions.aggregations.Sum;
import org.hotrod.runtime.sql.expressions.aggregations.SumDistinct;
import org.hotrod.runtime.sql.expressions.analytics.DenseRank;
import org.hotrod.runtime.sql.expressions.analytics.Lag;
import org.hotrod.runtime.sql.expressions.analytics.Lead;
import org.hotrod.runtime.sql.expressions.analytics.NTile;
import org.hotrod.runtime.sql.expressions.analytics.Rank;
import org.hotrod.runtime.sql.expressions.analytics.RowNumber;
import org.hotrod.runtime.sql.expressions.datetime.CurrentDate;
import org.hotrod.runtime.sql.expressions.datetime.CurrentDateTime;
import org.hotrod.runtime.sql.expressions.datetime.CurrentTime;
import org.hotrod.runtime.sql.expressions.datetime.DateTime;
import org.hotrod.runtime.sql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.sql.expressions.predicates.And;
import org.hotrod.runtime.sql.expressions.predicates.Exists;
import org.hotrod.runtime.sql.expressions.predicates.Not;
import org.hotrod.runtime.sql.expressions.predicates.NotExists;
import org.hotrod.runtime.sql.expressions.predicates.Or;
import org.hotrod.runtime.sql.expressions.predicates.Predicate;
import org.hotrod.runtime.sql.ordering.OrderByDirectionStage;

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

  public static OrderByDirectionStage asc(final Expression<?> expression) {
    return new OrderByDirectionStage(expression, true);
  }

  public static OrderByDirectionStage desc(final Expression<?> expression) {
    return new OrderByDirectionStage(expression, false);
  }

  // Aggregation expressions, that are NOT window functions

  public static Count count() {
    return new Count();
  }

  public static CountDistinct countDistinct(final Expression<?> expression) {
    return new CountDistinct(expression);
  }

  public static SumDistinct sumDistinct(final Expression<?> expression) {
    return new SumDistinct(expression);
  }

  public static AvgDistinct avgDistinct(final Expression<?> expression) {
    return new AvgDistinct(expression);
  }

  public static GroupConcatDistinct groupConcatDistinct(final Expression<String> expression) {
    return new GroupConcatDistinct(expression, null);
  }

  public static GroupConcatDistinct groupConcatDistinct(final Expression<String> expression,
      final Expression<String> delimiter) {
    return new GroupConcatDistinct(expression, delimiter);
  }

  public static GroupConcatDistinct groupConcatDistinct(final Expression<String> expression, final String delimiter) {
    return new GroupConcatDistinct(expression, box(delimiter));
  }

  // Aggregation expressions, that ALSO are window functions

  public static Sum sum(final Expression<Number> expression) {
    return new Sum(expression);
  }

  public static Avg avg(final Expression<Number> expression) {
    return new Avg(expression);
  }

  public static Min min(final Expression<Number> expression) {
    return new Min(expression);
  }

  public static Max max(final Expression<Number> expression) {
    return new Max(expression);
  }

  public static GroupConcat groupConcat(final Expression<String> expression) {
    return new GroupConcat(expression, null);
  }

  public static GroupConcat groupConcat(final Expression<String> expression, final Expression<String> delimiter) {
    return new GroupConcat(expression, delimiter);
  }

  public static GroupConcat groupConcat(final Expression<String> expression, final String delimiter) {
    return new GroupConcat(expression, box(delimiter));
  }

  // Analytical functions

  public static RowNumber rowNumber() {
    return new RowNumber();
  }

  public static Rank rank() {
    return new Rank();
  }

  public static DenseRank denseRank() {
    return new DenseRank();
  }

  public static NTile ntile() {
    return new NTile();
  }

  // Positional Analytic functions

  public static <T> Lead<T> lead(final Expression<T> expression) {
    return new Lead<T>(expression, null, null);
  }

  public static <T> Lead<T> lead(final Expression<T> expression, final Number offset) {
    return new Lead<T>(expression, box(offset), null);
  }

  public static <T> Lead<T> lead(final Expression<T> expression, final Number offset,
      final Expression<T> defaultValue) {
    return new Lead<T>(expression, box(offset), defaultValue);
  }

  public static <T> Lead<T> lead(final Expression<T> expression, final Number offset, final T defaultValue) {
    return new Lead<T>(expression, box(offset), box(defaultValue));
  }

  public static <T> Lag<T> lag(final Expression<T> expression) {
    return new Lag<T>(expression, null, null);
  }

  public static <T> Lag<T> lag(final Expression<T> expression, final Number offset) {
    return new Lag<T>(expression, box(offset), null);
  }

  public static <T> Lag<T> lag(final Expression<T> expression, final Number offset, final Expression<T> defaultValue) {
    return new Lag<T>(expression, box(offset), defaultValue);
  }

  public static <T> Lag<T> lag(final Expression<T> expression, final Number offset, final T defaultValue) {
    return new Lag<T>(expression, box(offset), box(defaultValue));
  }

  // Case

  public static <T> CaseWhenStage<T> caseWhen(final Predicate predicate, Expression<T> value) {
    return new CaseWhenStage<T>(predicate, value);
  }

  public static <T> CaseWhenStage<T> caseWhen(final Predicate predicate, T value) {
    Constant<T> v = box(value);
    return new CaseWhenStage<T>(predicate, v);
  }

  // General functions

  @SafeVarargs
  public static <T> Coalesce<T> coalesce(final Expression<T> first, final Expression<T>... rest) {
    return new Coalesce<T>(first, rest);
  }

  // Date/Time

  public static DateTimeExpression currentDate() {
    return new CurrentDate();
  }

  public static DateTimeExpression currentTime() {
    return new CurrentTime();
  }

  public static DateTimeExpression currentDateTime() {
    return new CurrentDateTime();
  }

  public static DateTimeExpression datetime(final Expression<Date> date, final Expression<Date> time) {
    return new DateTime(date, time);
  }

//  // Arithmetic
//
//  public static Expression<Number> power(final Expression<Number> value, final Expression<Number> exponent) {
//    return new Pow(value, exponent);
//  }
//
//  public static Expression<Number> log(final Expression<Number> value, final Expression<Number> base) {
//    return new Log(value, base);
//  }
//
//  public static Expression<Number> round(final Expression<Number> value, final Expression<Number> places) {
//    return new Log(value, places);
//  }
//
//  public static Expression<Number> abs(final Expression<Number> value) {
//    return new Abs(value);
//  }
//
//  public static Expression<Number> signum(final Expression<Number> value) {
//    return new Signum(value);
//  }
//
//  public static Expression<Number> neg(final Expression<Number> value) {
//    return new Neg(value);
//  }
//  
//  // 

  // SQL translator resolver

  private static SQLDialect sqlDialect = null;

  private static SQLDialect resolveSQLDialect() {
    if (sqlDialect == null) {
      sqlDialect = new PostgreSQLDialect("PostgreSQL", "9.4.5", 9, 4);
    }
    return sqlDialect;
  }

  // Boxing scalar values

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
