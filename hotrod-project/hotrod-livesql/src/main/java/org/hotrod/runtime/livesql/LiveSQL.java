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
import org.hotrod.runtime.livesql.expressions.aggregations.BooleanMax;
import org.hotrod.runtime.livesql.expressions.aggregations.BooleanMin;
import org.hotrod.runtime.livesql.expressions.aggregations.ByteArrayMax;
import org.hotrod.runtime.livesql.expressions.aggregations.ByteArrayMin;
import org.hotrod.runtime.livesql.expressions.aggregations.CountDistinct;
import org.hotrod.runtime.livesql.expressions.aggregations.CountRows;
import org.hotrod.runtime.livesql.expressions.aggregations.DateTimeMax;
import org.hotrod.runtime.livesql.expressions.aggregations.DateTimeMin;
import org.hotrod.runtime.livesql.expressions.aggregations.GroupConcat;
import org.hotrod.runtime.livesql.expressions.aggregations.GroupConcatDistinct;
import org.hotrod.runtime.livesql.expressions.aggregations.NumberMax;
import org.hotrod.runtime.livesql.expressions.aggregations.NumberMin;
import org.hotrod.runtime.livesql.expressions.aggregations.ObjectMax;
import org.hotrod.runtime.livesql.expressions.aggregations.ObjectMin;
import org.hotrod.runtime.livesql.expressions.aggregations.StringMax;
import org.hotrod.runtime.livesql.expressions.aggregations.StringMin;
import org.hotrod.runtime.livesql.expressions.aggregations.Sum;
import org.hotrod.runtime.livesql.expressions.aggregations.SumDistinct;
import org.hotrod.runtime.livesql.expressions.analytics.BooleanLag;
import org.hotrod.runtime.livesql.expressions.analytics.BooleanLead;
import org.hotrod.runtime.livesql.expressions.analytics.ByteArrayLag;
import org.hotrod.runtime.livesql.expressions.analytics.ByteArrayLead;
import org.hotrod.runtime.livesql.expressions.analytics.DateTimeLag;
import org.hotrod.runtime.livesql.expressions.analytics.DateTimeLead;
import org.hotrod.runtime.livesql.expressions.analytics.DenseRank;
import org.hotrod.runtime.livesql.expressions.analytics.NTile;
import org.hotrod.runtime.livesql.expressions.analytics.NumberLag;
import org.hotrod.runtime.livesql.expressions.analytics.NumberLead;
import org.hotrod.runtime.livesql.expressions.analytics.ObjectLag;
import org.hotrod.runtime.livesql.expressions.analytics.ObjectLead;
import org.hotrod.runtime.livesql.expressions.analytics.Rank;
import org.hotrod.runtime.livesql.expressions.analytics.RowNumber;
import org.hotrod.runtime.livesql.expressions.analytics.StringLag;
import org.hotrod.runtime.livesql.expressions.analytics.StringLead;
import org.hotrod.runtime.livesql.expressions.binary.ByteArrayConstant;
import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;
import org.hotrod.runtime.livesql.expressions.caseclause.BooleanCaseWhenStage;
import org.hotrod.runtime.livesql.expressions.caseclause.ByteArrayCaseWhenStage;
import org.hotrod.runtime.livesql.expressions.caseclause.DateTimeCaseWhenStage;
import org.hotrod.runtime.livesql.expressions.caseclause.NumberCaseWhenStage;
import org.hotrod.runtime.livesql.expressions.caseclause.ObjectCaseWhenStage;
import org.hotrod.runtime.livesql.expressions.caseclause.StringCaseWhenStage;
import org.hotrod.runtime.livesql.expressions.datetime.CurrentDate;
import org.hotrod.runtime.livesql.expressions.datetime.CurrentDateTime;
import org.hotrod.runtime.livesql.expressions.datetime.CurrentTime;
import org.hotrod.runtime.livesql.expressions.datetime.DateTime;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeConstant;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.general.TupleExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberConstant;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.object.ObjectConstant;
import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;
import org.hotrod.runtime.livesql.expressions.predicates.BooleanConstant;
import org.hotrod.runtime.livesql.expressions.predicates.Exists;
import org.hotrod.runtime.livesql.expressions.predicates.Not;
import org.hotrod.runtime.livesql.expressions.predicates.NotExists;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.expressions.strings.StringConstant;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.SelectColumnsPhase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LiveSQL {

  // Properties

  @Autowired
  private SqlSession sqlSession;

  @Autowired
  private SQLDialect sqlDialect;

  @Autowired
  private LiveSQLMapper liveSQLMapper;

  // Setters

  public void setSqlSession(final SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }

  public void setSqlDialect(final SQLDialect sqlDialect) {
    this.sqlDialect = sqlDialect;
  }

  // Select

  public SelectColumnsPhase<Map<String, Object>> select() {
    return new SelectColumnsPhase<Map<String, Object>>(this.sqlDialect, this.sqlSession, this.liveSQLMapper, false);
  }

  public SelectColumnsPhase<Map<String, Object>> selectDistinct() {
    return new SelectColumnsPhase<Map<String, Object>>(this.sqlDialect, this.sqlSession, this.liveSQLMapper, true);
  }

  public SelectColumnsPhase<Map<String, Object>> select(final ResultSetColumn... resultSetColumns) {
    return new SelectColumnsPhase<Map<String, Object>>(this.sqlDialect, this.sqlSession, this.liveSQLMapper, false,
        resultSetColumns);
  }

  public SelectColumnsPhase<Map<String, Object>> selectDistinct(final ResultSetColumn... resultSetColumns) {
    return new SelectColumnsPhase<Map<String, Object>>(this.sqlDialect, this.sqlSession, this.liveSQLMapper, true,
        resultSetColumns);
  }

  // Tuples

  public TupleExpression tuple(final Expression... expressions) {
    return new TupleExpression(expressions);
  }

  // Predicates

  public Predicate not(final Predicate a) {
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

  public CountRows count() {
    return new CountRows();
  }

  public CountDistinct countDistinct(final Expression expression) {
    return new CountDistinct(expression);
  }

  public SumDistinct sumDistinct(final NumberExpression expression) {
    return new SumDistinct(expression);
  }

  public AvgDistinct avgDistinct(final NumberExpression expression) {
    return new AvgDistinct(expression);
  }

  public GroupConcatDistinct groupConcatDistinct(final StringExpression expression) {
    return new GroupConcatDistinct(expression, null, null);
  }

  public GroupConcatDistinct groupConcatDistinct(final StringExpression expression, final String separator) {
    return new GroupConcatDistinct(expression, null, val(separator));
  }

  public GroupConcatDistinct groupConcatDistinct(final StringExpression expression, final String separator,
      final OrderingTerm... order) {
    return new GroupConcatDistinct(expression, Arrays.asList(order), val(separator));
  }

  // Aggregation expressions, that ALSO are window functions

  public Sum sum(final NumberExpression expression) {
    return new Sum(expression);
  }

  public Avg avg(final NumberExpression expression) {
    return new Avg(expression);
  }

  public GroupConcat groupConcat(final StringExpression expression) {
    return new GroupConcat(expression, null, null);
  }

  public GroupConcat groupConcat(final StringExpression expression, final String separator) {
    return new GroupConcat(expression, null, val(separator));
  }

  public GroupConcat groupConcat(final StringExpression expression, final String separator,
      final OrderingTerm... order) {
    return new GroupConcat(expression, Arrays.asList(order), val(separator));
  }

  // Max -- Aggregation expressions, that ALSO are window functions

  public NumberMax max(final NumberExpression expression) {
    return new NumberMax(expression);
  }

  public StringMax max(final StringExpression expression) {
    return new StringMax(expression);
  }

  public DateTimeMax max(final DateTimeExpression expression) {
    return new DateTimeMax(expression);
  }

  public BooleanMax max(final Predicate expression) {
    return new BooleanMax(expression);
  }

  public ByteArrayMax max(final ByteArrayExpression expression) {
    return new ByteArrayMax(expression);
  }

  public ObjectMax max(final ObjectExpression expression) {
    return new ObjectMax(expression);
  }

  // Min -- Aggregation expressions, that ALSO are window functions

  public NumberMin min(final NumberExpression expression) {
    return new NumberMin(expression);
  }

  public StringMin min(final StringExpression expression) {
    return new StringMin(expression);
  }

  public DateTimeMin min(final DateTimeExpression expression) {
    return new DateTimeMin(expression);
  }

  public BooleanMin min(final Predicate expression) {
    return new BooleanMin(expression);
  }

  public ByteArrayMin min(final ByteArrayExpression expression) {
    return new ByteArrayMin(expression);
  }

  public ObjectMin min(final ObjectExpression expression) {
    return new ObjectMin(expression);
  }

  // Analytical functions

  public RowNumber rowNumber() {
    return new RowNumber();
  }

  public Rank rank(final Expression expression) {
    return new Rank(expression);
  }

  public DenseRank denseRank(final Expression expression) {
    return new DenseRank(expression);
  }

  public NTile ntile(final Expression expression) {
    return new NTile(expression);
  }

  // Positional Analytic functions

  // === Lead Number ===

  public NumberLead lead(final NumberExpression expression) {
    return new NumberLead(expression, null, null);
  }

  public NumberLead lead(final NumberExpression expression, final Number offset) {
    return new NumberLead(expression, val(offset), null);
  }

  public NumberLead lead(final NumberExpression expression, final NumberExpression offset) {
    return new NumberLead(expression, offset, null);
  }

  public NumberLead lead(final NumberExpression expression, final Number offset, final Number defaultValue) {
    return new NumberLead(expression, val(offset), val(defaultValue));
  }

  public NumberLead lead(final NumberExpression expression, final NumberExpression offset, final Number defaultValue) {
    return new NumberLead(expression, offset, val(defaultValue));
  }

  public NumberLead lead(final NumberExpression expression, final Number offset, final NumberExpression defaultValue) {
    return new NumberLead(expression, val(offset), defaultValue);
  }

  public NumberLead lead(final NumberExpression expression, final NumberExpression offset,
      final NumberExpression defaultValue) {
    return new NumberLead(expression, offset, defaultValue);
  }

  // === Lead String ===

  public StringLead lead(final StringExpression expression) {
    return new StringLead(expression, null, null);
  }

  public StringLead lead(final StringExpression expression, final Number offset) {
    return new StringLead(expression, val(offset), null);
  }

  public StringLead lead(final StringExpression expression, final NumberExpression offset) {
    return new StringLead(expression, offset, null);
  }

  public StringLead lead(final StringExpression expression, final Number offset, final String defaultValue) {
    return new StringLead(expression, val(offset), val(defaultValue));
  }

  public StringLead lead(final StringExpression expression, final NumberExpression offset, final String defaultValue) {
    return new StringLead(expression, offset, val(defaultValue));
  }

  public StringLead lead(final StringExpression expression, final Number offset, final StringExpression defaultValue) {
    return new StringLead(expression, val(offset), defaultValue);
  }

  public StringLead lead(final StringExpression expression, final NumberExpression offset,
      final StringExpression defaultValue) {
    return new StringLead(expression, offset, defaultValue);
  }

  // === Lead DateTime ===

  public DateTimeLead lead(final DateTimeExpression expression) {
    return new DateTimeLead(expression, null, null);
  }

  public DateTimeLead lead(final DateTimeExpression expression, final Number offset) {
    return new DateTimeLead(expression, val(offset), null);
  }

  public DateTimeLead lead(final DateTimeExpression expression, final NumberExpression offset) {
    return new DateTimeLead(expression, offset, null);
  }

  public DateTimeLead lead(final DateTimeExpression expression, final Number offset, final Date defaultValue) {
    return new DateTimeLead(expression, val(offset), val(defaultValue));
  }

  public DateTimeLead lead(final DateTimeExpression expression, final NumberExpression offset,
      final Date defaultValue) {
    return new DateTimeLead(expression, offset, val(defaultValue));
  }

  public DateTimeLead lead(final DateTimeExpression expression, final Number offset,
      final DateTimeExpression defaultValue) {
    return new DateTimeLead(expression, val(offset), defaultValue);
  }

  public DateTimeLead lead(final DateTimeExpression expression, final NumberExpression offset,
      final DateTimeExpression defaultValue) {
    return new DateTimeLead(expression, offset, defaultValue);
  }

  // === Lead Boolean ===

  public BooleanLead lead(final Predicate expression) {
    return new BooleanLead(expression, null, null);
  }

  public BooleanLead lead(final Predicate expression, final Number offset) {
    return new BooleanLead(expression, val(offset), null);
  }

  public BooleanLead lead(final Predicate expression, final NumberExpression offset) {
    return new BooleanLead(expression, offset, null);
  }

  public BooleanLead lead(final Predicate expression, final Number offset, final Boolean defaultValue) {
    return new BooleanLead(expression, val(offset), val(defaultValue));
  }

  public BooleanLead lead(final Predicate expression, final NumberExpression offset, final Boolean defaultValue) {
    return new BooleanLead(expression, offset, val(defaultValue));
  }

  public BooleanLead lead(final Predicate expression, final Number offset, final Predicate defaultValue) {
    return new BooleanLead(expression, val(offset), defaultValue);
  }

  public BooleanLead lead(final Predicate expression, final NumberExpression offset, final Predicate defaultValue) {
    return new BooleanLead(expression, offset, defaultValue);
  }

  // === Lead ByteArray ===

  public ByteArrayLead lead(final ByteArrayExpression expression) {
    return new ByteArrayLead(expression, null, null);
  }

  public ByteArrayLead lead(final ByteArrayExpression expression, final Number offset) {
    return new ByteArrayLead(expression, val(offset), null);
  }

  public ByteArrayLead lead(final ByteArrayExpression expression, final NumberExpression offset) {
    return new ByteArrayLead(expression, offset, null);
  }

  public ByteArrayLead lead(final ByteArrayExpression expression, final Number offset, final byte[] defaultValue) {
    return new ByteArrayLead(expression, val(offset), val(defaultValue));
  }

  public ByteArrayLead lead(final ByteArrayExpression expression, final NumberExpression offset,
      final byte[] defaultValue) {
    return new ByteArrayLead(expression, offset, val(defaultValue));
  }

  public ByteArrayLead lead(final ByteArrayExpression expression, final Number offset,
      final ByteArrayExpression defaultValue) {
    return new ByteArrayLead(expression, val(offset), defaultValue);
  }

  public ByteArrayLead lead(final ByteArrayExpression expression, final NumberExpression offset,
      final ByteArrayExpression defaultValue) {
    return new ByteArrayLead(expression, offset, defaultValue);
  }

  // === Lead Object ===

  public ObjectLead lead(final ObjectExpression expression) {
    return new ObjectLead(expression, null, null);
  }

  public ObjectLead lead(final ObjectExpression expression, final Number offset) {
    return new ObjectLead(expression, val(offset), null);
  }

  public ObjectLead lead(final ObjectExpression expression, final NumberExpression offset) {
    return new ObjectLead(expression, offset, null);
  }

  public ObjectLead lead(final ObjectExpression expression, final Number offset, final Object defaultValue) {
    return new ObjectLead(expression, val(offset), val(defaultValue));
  }

  public ObjectLead lead(final ObjectExpression expression, final NumberExpression offset, final Object defaultValue) {
    return new ObjectLead(expression, offset, val(defaultValue));
  }

  public ObjectLead lead(final ObjectExpression expression, final Number offset, final ObjectExpression defaultValue) {
    return new ObjectLead(expression, val(offset), defaultValue);
  }

  public ObjectLead lead(final ObjectExpression expression, final NumberExpression offset,
      final ObjectExpression defaultValue) {
    return new ObjectLead(expression, offset, defaultValue);
  }

  // === Lag Number ===

  public NumberLag lag(final NumberExpression expression) {
    return new NumberLag(expression, null, null);
  }

  public NumberLag lag(final NumberExpression expression, final Number offset) {
    return new NumberLag(expression, val(offset), null);
  }

  public NumberLag lag(final NumberExpression expression, final NumberExpression offset) {
    return new NumberLag(expression, offset, null);
  }

  public NumberLag lag(final NumberExpression expression, final Number offset, final Number defaultValue) {
    return new NumberLag(expression, val(offset), val(defaultValue));
  }

  public NumberLag lag(final NumberExpression expression, final NumberExpression offset, final Number defaultValue) {
    return new NumberLag(expression, offset, val(defaultValue));
  }

  public NumberLag lag(final NumberExpression expression, final Number offset, final NumberExpression defaultValue) {
    return new NumberLag(expression, val(offset), defaultValue);
  }

  public NumberLag lag(final NumberExpression expression, final NumberExpression offset,
      final NumberExpression defaultValue) {
    return new NumberLag(expression, offset, defaultValue);
  }

  // === Lag String ===

  public StringLag lag(final StringExpression expression) {
    return new StringLag(expression, null, null);
  }

  public StringLag lag(final StringExpression expression, final Number offset) {
    return new StringLag(expression, val(offset), null);
  }

  public StringLag lag(final StringExpression expression, final NumberExpression offset) {
    return new StringLag(expression, offset, null);
  }

  public StringLag lag(final StringExpression expression, final Number offset, final String defaultValue) {
    return new StringLag(expression, val(offset), val(defaultValue));
  }

  public StringLag lag(final StringExpression expression, final NumberExpression offset, final String defaultValue) {
    return new StringLag(expression, offset, val(defaultValue));
  }

  public StringLag lag(final StringExpression expression, final Number offset, final StringExpression defaultValue) {
    return new StringLag(expression, val(offset), defaultValue);
  }

  public StringLag lag(final StringExpression expression, final NumberExpression offset,
      final StringExpression defaultValue) {
    return new StringLag(expression, offset, defaultValue);
  }

  // === Lag DateTime ===

  public DateTimeLag lag(final DateTimeExpression expression) {
    return new DateTimeLag(expression, null, null);
  }

  public DateTimeLag lag(final DateTimeExpression expression, final Number offset) {
    return new DateTimeLag(expression, val(offset), null);
  }

  public DateTimeLag lag(final DateTimeExpression expression, final NumberExpression offset) {
    return new DateTimeLag(expression, offset, null);
  }

  public DateTimeLag lag(final DateTimeExpression expression, final Number offset, final Date defaultValue) {
    return new DateTimeLag(expression, val(offset), val(defaultValue));
  }

  public DateTimeLag lag(final DateTimeExpression expression, final NumberExpression offset, final Date defaultValue) {
    return new DateTimeLag(expression, offset, val(defaultValue));
  }

  public DateTimeLag lag(final DateTimeExpression expression, final Number offset,
      final DateTimeExpression defaultValue) {
    return new DateTimeLag(expression, val(offset), defaultValue);
  }

  public DateTimeLag lag(final DateTimeExpression expression, final NumberExpression offset,
      final DateTimeExpression defaultValue) {
    return new DateTimeLag(expression, offset, defaultValue);
  }

  // === Lag Boolean ===

  public BooleanLag lag(final Predicate expression) {
    return new BooleanLag(expression, null, null);
  }

  public BooleanLag lag(final Predicate expression, final Number offset) {
    return new BooleanLag(expression, val(offset), null);
  }

  public BooleanLag lag(final Predicate expression, final NumberExpression offset) {
    return new BooleanLag(expression, offset, null);
  }

  public BooleanLag lag(final Predicate expression, final Number offset, final Boolean defaultValue) {
    return new BooleanLag(expression, val(offset), val(defaultValue));
  }

  public BooleanLag lag(final Predicate expression, final NumberExpression offset, final Boolean defaultValue) {
    return new BooleanLag(expression, offset, val(defaultValue));
  }

  public BooleanLag lag(final Predicate expression, final Number offset, final Predicate defaultValue) {
    return new BooleanLag(expression, val(offset), defaultValue);
  }

  public BooleanLag lag(final Predicate expression, final NumberExpression offset, final Predicate defaultValue) {
    return new BooleanLag(expression, offset, defaultValue);
  }

  // === Lag ByteArray ===

  public ByteArrayLag lag(final ByteArrayExpression expression) {
    return new ByteArrayLag(expression, null, null);
  }

  public ByteArrayLag lag(final ByteArrayExpression expression, final Number offset) {
    return new ByteArrayLag(expression, val(offset), null);
  }

  public ByteArrayLag lag(final ByteArrayExpression expression, final NumberExpression offset) {
    return new ByteArrayLag(expression, offset, null);
  }

  public ByteArrayLag lag(final ByteArrayExpression expression, final Number offset, final byte[] defaultValue) {
    return new ByteArrayLag(expression, val(offset), val(defaultValue));
  }

  public ByteArrayLag lag(final ByteArrayExpression expression, final NumberExpression offset,
      final byte[] defaultValue) {
    return new ByteArrayLag(expression, offset, val(defaultValue));
  }

  public ByteArrayLag lag(final ByteArrayExpression expression, final Number offset,
      final ByteArrayExpression defaultValue) {
    return new ByteArrayLag(expression, val(offset), defaultValue);
  }

  public ByteArrayLag lag(final ByteArrayExpression expression, final NumberExpression offset,
      final ByteArrayExpression defaultValue) {
    return new ByteArrayLag(expression, offset, defaultValue);
  }

  // === Lag Object ===

  public ObjectLag lag(final ObjectExpression expression) {
    return new ObjectLag(expression, null, null);
  }

  public ObjectLag lag(final ObjectExpression expression, final Number offset) {
    return new ObjectLag(expression, val(offset), null);
  }

  public ObjectLag lag(final ObjectExpression expression, final NumberExpression offset) {
    return new ObjectLag(expression, offset, null);
  }

  public ObjectLag lag(final ObjectExpression expression, final Number offset, final Object defaultValue) {
    return new ObjectLag(expression, val(offset), val(defaultValue));
  }

  public ObjectLag lag(final ObjectExpression expression, final NumberExpression offset, final Object defaultValue) {
    return new ObjectLag(expression, offset, val(defaultValue));
  }

  public ObjectLag lag(final ObjectExpression expression, final Number offset, final ObjectExpression defaultValue) {
    return new ObjectLag(expression, val(offset), defaultValue);
  }

  public ObjectLag lag(final ObjectExpression expression, final NumberExpression offset,
      final ObjectExpression defaultValue) {
    return new ObjectLag(expression, offset, defaultValue);
  }

  // Case

  public NumberCaseWhenStage caseWhen(final Predicate predicate, final Number value) {
    return new NumberCaseWhenStage(predicate, val(value));
  }

  public NumberCaseWhenStage caseWhen(final Predicate predicate, final NumberExpression value) {
    return new NumberCaseWhenStage(predicate, value);
  }

  public StringCaseWhenStage caseWhen(final Predicate predicate, final String value) {
    return new StringCaseWhenStage(predicate, val(value));
  }

  public StringCaseWhenStage caseWhen(final Predicate predicate, final StringExpression value) {
    return new StringCaseWhenStage(predicate, value);
  }

  public DateTimeCaseWhenStage caseWhen(final Predicate predicate, final Date value) {
    return new DateTimeCaseWhenStage(predicate, val(value));
  }

  public DateTimeCaseWhenStage caseWhen(final Predicate predicate, final DateTimeExpression value) {
    return new DateTimeCaseWhenStage(predicate, value);
  }

  public BooleanCaseWhenStage caseWhen(final Predicate predicate, final Boolean value) {
    return new BooleanCaseWhenStage(predicate, val(value));
  }

  public BooleanCaseWhenStage caseWhen(final Predicate predicate, final Predicate value) {
    return new BooleanCaseWhenStage(predicate, value);
  }

  public ByteArrayCaseWhenStage caseWhen(final Predicate predicate, final byte[] value) {
    return new ByteArrayCaseWhenStage(predicate, val(value));
  }

  public ByteArrayCaseWhenStage caseWhen(final Predicate predicate, final ByteArrayExpression value) {
    return new ByteArrayCaseWhenStage(predicate, value);
  }

  public ObjectCaseWhenStage caseWhen(final Predicate predicate, final Object value) {
    return new ObjectCaseWhenStage(predicate, val(value));
  }

  public ObjectCaseWhenStage caseWhen(final Predicate predicate, final ObjectExpression value) {
    return new ObjectCaseWhenStage(predicate, value);
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

  public DateTimeExpression datetime(final DateTimeExpression date, final DateTimeExpression time) {
    return new DateTime(date, time);
  }

  // Boxing of scalar values

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
