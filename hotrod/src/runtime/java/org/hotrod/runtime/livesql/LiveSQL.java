package org.hotrod.runtime.livesql;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.dialects.SQLDialect;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
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
import org.hotrod.runtime.livesql.queries.select.SelectColumnsPhase;

public class LiveSQL {

  // Properties

  private SqlSession sqlSession;

  private SQLDialect sqlDialect;

  // Setters

  public void setSqlSession(final SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }

  public void setSqlDialect(final SQLDialect sqlDialect) {
    this.sqlDialect = sqlDialect;
  }

  // Select

  public SelectColumnsPhase<Map<String, Object>> select() {
    return new SelectColumnsPhase<Map<String, Object>>(this.sqlDialect, this.sqlSession, false);
  }

  public SelectColumnsPhase<Map<String, Object>> selectDistinct() {
    return new SelectColumnsPhase<Map<String, Object>>(this.sqlDialect, this.sqlSession, true);
  }

  public SelectColumnsPhase<Map<String, Object>> select(final ResultSetColumn... resultSetColumns) {
    return new SelectColumnsPhase<Map<String, Object>>(this.sqlDialect, this.sqlSession, false, resultSetColumns);
  }

  public SelectColumnsPhase<Map<String, Object>> selectDistinct(final ResultSetColumn... resultSetColumns) {
    return new SelectColumnsPhase<Map<String, Object>>(this.sqlDialect, this.sqlSession, true, resultSetColumns);
  }

  // Tuples

  public Tuple tuple(final Expression<?>... expressions) {
    return new Tuple(expressions);
  }

  // Predicates

  public Predicate not(final Expression<Boolean> a) {
    return new Not(a);
  }

  // Subquery existence

  public <R> Predicate exists(final ExecutableSelect<R> subquery) {
    return new Exists(subquery);
  }

  public <R> Predicate notExists(final ExecutableSelect<R> subquery) {
    return new NotExists(subquery);
  }

  // Enclosing queries

  // public <R> AbstractSelect<R> encloseSelect(final AbstractSelect<R>
  // select) {
  // return new EnclosedSelect<R>(sqlDialect, false, sqlSession, null, select);
  // }

  // Aggregation expressions, that are NOT window functions

  public Count count() {
    return new Count();
  }

  public CountDistinct countDistinct(final Expression<?> expression) {
    return new CountDistinct(expression);
  }

  public SumDistinct sumDistinct(final Expression<?> expression) {
    return new SumDistinct(expression);
  }

  public AvgDistinct avgDistinct(final Expression<?> expression) {
    return new AvgDistinct(expression);
  }

  public GroupConcatDistinct groupConcatDistinct(final Expression<String> expression) {
    return new GroupConcatDistinct(expression, null, null);
  }

  public GroupConcatDistinct groupConcatDistinct(final Expression<String> expression, final String separator) {
    return new GroupConcatDistinct(expression, null, val(separator));
  }

  public GroupConcatDistinct groupConcatDistinct(final Expression<String> expression, final String separator,
      final OrderingTerm... order) {
    return new GroupConcatDistinct(expression, Arrays.asList(order), val(separator));
  }

  // Aggregation expressions, that ALSO are window functions

  public Sum sum(final Expression<Number> expression) {
    return new Sum(expression);
  }

  public Avg avg(final Expression<Number> expression) {
    return new Avg(expression);
  }

  public Min min(final Expression<Number> expression) {
    return new Min(expression);
  }

  public Max max(final Expression<Number> expression) {
    return new Max(expression);
  }

  public GroupConcat groupConcat(final Expression<String> expression) {
    return new GroupConcat(expression, null, null);
  }

  public GroupConcat groupConcat(final Expression<String> expression, final String separator) {
    return new GroupConcat(expression, null, val(separator));
  }

  public GroupConcat groupConcat(final Expression<String> expression, final String separator,
      final OrderingTerm... order) {
    return new GroupConcat(expression, Arrays.asList(order), val(separator));
  }

  // Analytical functions

  public RowNumber rowNumber() {
    return new RowNumber();
  }

  public Rank rank() {
    return new Rank();
  }

  public DenseRank denseRank() {
    return new DenseRank();
  }

  public NTile ntile() {
    return new NTile();
  }

  // Positional Analytic functions

  // Lead

  public <T> Lead<T> lead(final Expression<T> expression) {
    return new Lead<T>(expression, null, null);
  }

  public <T> Lead<T> lead(final Expression<T> expression, final Number offset) {
    return new Lead<T>(expression, val(offset), null);
  }

  public <T> Lead<T> lead(final Expression<T> expression, final Number offset, final Expression<T> defaultValue) {
    return new Lead<T>(expression, val(offset), defaultValue);
  }

  public Lead<String> lead(final Expression<String> expression, final Number offset, final String defaultValue) {
    return new Lead<String>(expression, val(offset), val(defaultValue));
  }

  public Lead<String> lead(final Expression<String> expression, final Number offset, final Character defaultValue) {
    return new Lead<String>(expression, val(offset), val(defaultValue));
  }

  public Lead<Number> lead(final Expression<Number> expression, final Number offset, final Number defaultValue) {
    return new Lead<Number>(expression, val(offset), val(defaultValue));
  }

  public Lead<Date> lead(final Expression<Date> expression, final Number offset, final Date defaultValue) {
    return new Lead<Date>(expression, val(offset), val(defaultValue));
  }

  public Lead<Boolean> lead(final Expression<Boolean> expression, final Number offset, final Boolean defaultValue) {
    return new Lead<Boolean>(expression, val(offset), val(defaultValue));
  }

  public Lead<byte[]> lead(final Expression<byte[]> expression, final Number offset, final byte[] defaultValue) {
    return new Lead<byte[]>(expression, val(offset), val(defaultValue));
  }

  public Lead<Object> lead(final Expression<Object> expression, final Number offset, final Object defaultValue) {
    return new Lead<Object>(expression, val(offset), val(defaultValue));
  }

  // Lag

  public <T> Lag<T> lag(final Expression<T> expression) {
    return new Lag<T>(expression, null, null);
  }

  public <T> Lag<T> lag(final Expression<T> expression, final Number offset) {
    return new Lag<T>(expression, val(offset), null);
  }

  public <T> Lag<T> lag(final Expression<T> expression, final Number offset, final Expression<T> defaultValue) {
    return new Lag<T>(expression, val(offset), defaultValue);
  }

  public Lag<String> lag(final Expression<String> expression, final Number offset, final String defaultValue) {
    return new Lag<String>(expression, val(offset), val(defaultValue));
  }

  public Lag<String> lag(final Expression<String> expression, final Number offset, final Character defaultValue) {
    return new Lag<String>(expression, val(offset), val(defaultValue));
  }

  public Lag<Number> lag(final Expression<Number> expression, final Number offset, final Number defaultValue) {
    return new Lag<Number>(expression, val(offset), val(defaultValue));
  }

  public Lag<Date> lag(final Expression<Date> expression, final Number offset, final Date defaultValue) {
    return new Lag<Date>(expression, val(offset), val(defaultValue));
  }

  public Lag<Boolean> lag(final Expression<Boolean> expression, final Number offset, final Boolean defaultValue) {
    return new Lag<Boolean>(expression, val(offset), val(defaultValue));
  }

  public Lag<byte[]> lag(final Expression<byte[]> expression, final Number offset, final byte[] defaultValue) {
    return new Lag<byte[]>(expression, val(offset), val(defaultValue));
  }

  public Lag<Object> lag(final Expression<Object> expression, final Number offset, final Object defaultValue) {
    return new Lag<Object>(expression, val(offset), val(defaultValue));
  }

  // Case

  public CaseWhenStage<String> caseWhen(final Predicate predicate, String value) {
    return new CaseWhenStage<String>(predicate, val(value));
  }

  public CaseWhenStage<String> caseWhen(final Predicate predicate, Character value) {
    return new CaseWhenStage<String>(predicate, val(value));
  }

  public CaseWhenStage<Number> caseWhen(final Predicate predicate, Number value) {
    return new CaseWhenStage<Number>(predicate, val(value));
  }

  public CaseWhenStage<Date> caseWhen(final Predicate predicate, Date value) {
    return new CaseWhenStage<Date>(predicate, val(value));
  }

  public CaseWhenStage<Boolean> caseWhen(final Predicate predicate, Boolean value) {
    return new CaseWhenStage<Boolean>(predicate, val(value));
  }

  public CaseWhenStage<byte[]> caseWhen(final Predicate predicate, byte[] value) {
    return new CaseWhenStage<byte[]>(predicate, val(value));
  }

  public CaseWhenStage<Object> caseWhen(final Predicate predicate, Object value) {
    return new CaseWhenStage<Object>(predicate, val(value));
  }

  // Date/Time

  public DateTimeExpression currentDate() {
    return new CurrentDate();
  }

  public DateTimeExpression currentTime() {
    return new CurrentTime();
  }

  public DateTimeExpression currentDateTime() {
    return new CurrentDateTime();
  }

  public DateTimeExpression datetime(final Expression<Date> date, final Expression<Date> time) {
    return new DateTime(date, time);
  }

  // Scalar values

  public StringConstant val(final String value) {
    return new StringConstant(value);
  }

  public StringConstant val(final Character value) {
    return new StringConstant("" + value);
  }

  public NumberConstant val(final Number value) {
    return new NumberConstant(value);
  }

  public DateTimeConstant val(final Date value) {
    return new DateTimeConstant(value);
  }

  public BooleanConstant val(final Boolean value) {
    return new BooleanConstant(value);
  }

  public ByteArrayConstant val(final byte[] value) {
    return new ByteArrayConstant(value);
  }

  public ObjectConstant val(final Object value) {
    return new ObjectConstant(value);
  }

}
