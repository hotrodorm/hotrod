package org.hotrod.runtime.livesql;

import java.util.Arrays;
import java.util.Date;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.aggregations.Avg;
import org.hotrod.runtime.livesql.expressions.aggregations.AvgDistinct;
import org.hotrod.runtime.livesql.expressions.aggregations.Count;
import org.hotrod.runtime.livesql.expressions.aggregations.CountDistinct;
import org.hotrod.runtime.livesql.expressions.aggregations.GroupConcat;
import org.hotrod.runtime.livesql.expressions.aggregations.GroupConcatDistinct;
import org.hotrod.runtime.livesql.expressions.aggregations.Max;
import org.hotrod.runtime.livesql.expressions.aggregations.Min;
import org.hotrod.runtime.livesql.expressions.aggregations.Sum;
import org.hotrod.runtime.livesql.expressions.aggregations.SumDistinct;
import org.hotrod.runtime.livesql.expressions.analytics.DenseRank;
import org.hotrod.runtime.livesql.expressions.analytics.Lag;
import org.hotrod.runtime.livesql.expressions.analytics.Lead;
import org.hotrod.runtime.livesql.expressions.analytics.NTile;
import org.hotrod.runtime.livesql.expressions.analytics.Rank;
import org.hotrod.runtime.livesql.expressions.analytics.RowNumber;
import org.hotrod.runtime.livesql.expressions.binary.ByteArrayConstant;
import org.hotrod.runtime.livesql.expressions.caseclause.CaseWhenStage;
import org.hotrod.runtime.livesql.expressions.datetime.CurrentDate;
import org.hotrod.runtime.livesql.expressions.datetime.CurrentDateTime;
import org.hotrod.runtime.livesql.expressions.datetime.CurrentTime;
import org.hotrod.runtime.livesql.expressions.datetime.DateTime;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeConstant;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.general.Tuple;
import org.hotrod.runtime.livesql.expressions.numbers.NumberConstant;
import org.hotrod.runtime.livesql.expressions.object.ObjectConstant;
import org.hotrod.runtime.livesql.expressions.predicates.BooleanConstant;
import org.hotrod.runtime.livesql.expressions.predicates.Exists;
import org.hotrod.runtime.livesql.expressions.predicates.Not;
import org.hotrod.runtime.livesql.expressions.predicates.NotExists;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.expressions.strings.StringConstant;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;

public class SQL {

  // Tuples

  public static Tuple tuple(final Expression<?>... expressions) {
    return new Tuple(expressions);
  }

  // Predicates

  public static Predicate not(final Expression<Boolean> a) {
    return new Not(a);
  }

  // Subquery existence

  public static Predicate exists(final ExecutableSelect subquery) {
    return new Exists(subquery);
  }

  public static Predicate notExists(final ExecutableSelect subquery) {
    return new NotExists(subquery);
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
    return new GroupConcatDistinct(expression, null, null);
  }

  public static GroupConcatDistinct groupConcatDistinct(final Expression<String> expression, final String separator) {
    return new GroupConcatDistinct(expression, null, val(separator));
  }

  public static GroupConcatDistinct groupConcatDistinct(final Expression<String> expression, final String separator,
      final OrderingTerm... order) {
    return new GroupConcatDistinct(expression, Arrays.asList(order), val(separator));
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
    return new GroupConcat(expression, null, null);
  }

  public static GroupConcat groupConcat(final Expression<String> expression, final String separator) {
    return new GroupConcat(expression, null, val(separator));
  }

  public static GroupConcat groupConcat(final Expression<String> expression, final String separator,
      final OrderingTerm... order) {
    return new GroupConcat(expression, Arrays.asList(order), val(separator));
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

  // Lead

  public static <T> Lead<T> lead(final Expression<T> expression) {
    return new Lead<T>(expression, null, null);
  }

  public static <T> Lead<T> lead(final Expression<T> expression, final Number offset) {
    return new Lead<T>(expression, val(offset), null);
  }

  public static <T> Lead<T> lead(final Expression<T> expression, final Number offset,
      final Expression<T> defaultValue) {
    return new Lead<T>(expression, val(offset), defaultValue);
  }

  public static Lead<String> lead(final Expression<String> expression, final Number offset, final String defaultValue) {
    return new Lead<String>(expression, val(offset), val(defaultValue));
  }

  public static Lead<String> lead(final Expression<String> expression, final Number offset,
      final Character defaultValue) {
    return new Lead<String>(expression, val(offset), val(defaultValue));
  }

  public static Lead<Number> lead(final Expression<Number> expression, final Number offset, final Number defaultValue) {
    return new Lead<Number>(expression, val(offset), val(defaultValue));
  }

  public static Lead<Date> lead(final Expression<Date> expression, final Number offset, final Date defaultValue) {
    return new Lead<Date>(expression, val(offset), val(defaultValue));
  }

  public static Lead<Boolean> lead(final Expression<Boolean> expression, final Number offset,
      final Boolean defaultValue) {
    return new Lead<Boolean>(expression, val(offset), val(defaultValue));
  }

  public static Lead<byte[]> lead(final Expression<byte[]> expression, final Number offset, final byte[] defaultValue) {
    return new Lead<byte[]>(expression, val(offset), val(defaultValue));
  }

  public static Lead<Object> lead(final Expression<Object> expression, final Number offset, final Object defaultValue) {
    return new Lead<Object>(expression, val(offset), val(defaultValue));
  }

  // Lag

  public static <T> Lag<T> lag(final Expression<T> expression) {
    return new Lag<T>(expression, null, null);
  }

  public static <T> Lag<T> lag(final Expression<T> expression, final Number offset) {
    return new Lag<T>(expression, val(offset), null);
  }

  public static <T> Lag<T> lag(final Expression<T> expression, final Number offset, final Expression<T> defaultValue) {
    return new Lag<T>(expression, val(offset), defaultValue);
  }

  public static Lag<String> lag(final Expression<String> expression, final Number offset, final String defaultValue) {
    return new Lag<String>(expression, val(offset), val(defaultValue));
  }

  public static Lag<String> lag(final Expression<String> expression, final Number offset,
      final Character defaultValue) {
    return new Lag<String>(expression, val(offset), val(defaultValue));
  }

  public static Lag<Number> lag(final Expression<Number> expression, final Number offset, final Number defaultValue) {
    return new Lag<Number>(expression, val(offset), val(defaultValue));
  }

  public static Lag<Date> lag(final Expression<Date> expression, final Number offset, final Date defaultValue) {
    return new Lag<Date>(expression, val(offset), val(defaultValue));
  }

  public static Lag<Boolean> lag(final Expression<Boolean> expression, final Number offset,
      final Boolean defaultValue) {
    return new Lag<Boolean>(expression, val(offset), val(defaultValue));
  }

  public static Lag<byte[]> lag(final Expression<byte[]> expression, final Number offset, final byte[] defaultValue) {
    return new Lag<byte[]>(expression, val(offset), val(defaultValue));
  }

  public static Lag<Object> lag(final Expression<Object> expression, final Number offset, final Object defaultValue) {
    return new Lag<Object>(expression, val(offset), val(defaultValue));
  }

  // Case

  public static CaseWhenStage<String> caseWhen(final Predicate predicate, String value) {
    return new CaseWhenStage<String>(predicate, val(value));
  }

  public static CaseWhenStage<String> caseWhen(final Predicate predicate, Character value) {
    return new CaseWhenStage<String>(predicate, val(value));
  }

  public static CaseWhenStage<Number> caseWhen(final Predicate predicate, Number value) {
    return new CaseWhenStage<Number>(predicate, val(value));
  }

  public static CaseWhenStage<Date> caseWhen(final Predicate predicate, Date value) {
    return new CaseWhenStage<Date>(predicate, val(value));
  }

  public static CaseWhenStage<Boolean> caseWhen(final Predicate predicate, Boolean value) {
    return new CaseWhenStage<Boolean>(predicate, val(value));
  }

  public static CaseWhenStage<byte[]> caseWhen(final Predicate predicate, byte[] value) {
    return new CaseWhenStage<byte[]>(predicate, val(value));
  }

  public static CaseWhenStage<Object> caseWhen(final Predicate predicate, Object value) {
    return new CaseWhenStage<Object>(predicate, val(value));
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

  // Scalar values

  public static StringConstant val(final String value) {
    return new StringConstant(value);
  }

  public static StringConstant val(final Character value) {
    return new StringConstant("" + value);
  }

  public static NumberConstant val(final Number value) {
    return new NumberConstant(value);
  }

  public static DateTimeConstant val(final Date value) {
    return new DateTimeConstant(value);
  }

  public static BooleanConstant val(final Boolean value) {
    return new BooleanConstant(value);
  }

  public static ByteArrayConstant val(final byte[] value) {
    return new ByteArrayConstant(value);
  }

  public static ObjectConstant val(final Object value) {
    return new ObjectConstant(value);
  }

}
