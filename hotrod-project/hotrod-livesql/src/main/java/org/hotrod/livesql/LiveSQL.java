package org.hotrod.livesql;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.NullLiteral;
import org.hotrod.livesql.expressions.SQLExpression;
import org.hotrod.livesql.expressions.aggregations.Avg;
import org.hotrod.livesql.expressions.aggregations.AvgDistinct;
import org.hotrod.livesql.expressions.aggregations.BinaryMax;
import org.hotrod.livesql.expressions.aggregations.BinaryMin;
import org.hotrod.livesql.expressions.aggregations.BooleanMax;
import org.hotrod.livesql.expressions.aggregations.BooleanMin;
import org.hotrod.livesql.expressions.aggregations.CharMax;
import org.hotrod.livesql.expressions.aggregations.CharMin;
import org.hotrod.livesql.expressions.aggregations.CountDistinct;
import org.hotrod.livesql.expressions.aggregations.CountRows;
import org.hotrod.livesql.expressions.aggregations.CountValues;
import org.hotrod.livesql.expressions.aggregations.DateTimeMax;
import org.hotrod.livesql.expressions.aggregations.DateTimeMin;
import org.hotrod.livesql.expressions.aggregations.GroupConcat;
import org.hotrod.livesql.expressions.aggregations.GroupConcatDistinct;
import org.hotrod.livesql.expressions.aggregations.NumericMax;
import org.hotrod.livesql.expressions.aggregations.NumericMin;
import org.hotrod.livesql.expressions.aggregations.ObjectMax;
import org.hotrod.livesql.expressions.aggregations.ObjectMin;
import org.hotrod.livesql.expressions.aggregations.Sum;
import org.hotrod.livesql.expressions.aggregations.SumDistinct;
import org.hotrod.livesql.expressions.analytics.BinaryLag;
import org.hotrod.livesql.expressions.analytics.BinaryLead;
import org.hotrod.livesql.expressions.analytics.BooleanLag;
import org.hotrod.livesql.expressions.analytics.BooleanLead;
import org.hotrod.livesql.expressions.analytics.CharLag;
import org.hotrod.livesql.expressions.analytics.CharLead;
import org.hotrod.livesql.expressions.analytics.DateTimeLag;
import org.hotrod.livesql.expressions.analytics.DateTimeLead;
import org.hotrod.livesql.expressions.analytics.DenseRank;
import org.hotrod.livesql.expressions.analytics.NTile;
import org.hotrod.livesql.expressions.analytics.NumericLag;
import org.hotrod.livesql.expressions.analytics.NumericLead;
import org.hotrod.livesql.expressions.analytics.ObjectLag;
import org.hotrod.livesql.expressions.analytics.ObjectLead;
import org.hotrod.livesql.expressions.analytics.Rank;
import org.hotrod.livesql.expressions.analytics.RowNumber;
import org.hotrod.livesql.expressions.binary.BinaryConstant;
import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.livesql.expressions.binary.BinarySyntaxExpression;
import org.hotrod.livesql.expressions.binary.EnclosedBinaryExpression;
import org.hotrod.livesql.expressions.bool.BooleanConstant;
import org.hotrod.livesql.expressions.bool.BooleanLiteral;
import org.hotrod.livesql.expressions.bool.BooleanSyntaxExpression;
import org.hotrod.livesql.expressions.bool.EnclosedBooleanExpression;
import org.hotrod.livesql.expressions.bool.Exists;
import org.hotrod.livesql.expressions.bool.Not;
import org.hotrod.livesql.expressions.bool.NotExists;
import org.hotrod.livesql.expressions.caseclause.BinaryCaseWhenStage;
import org.hotrod.livesql.expressions.caseclause.BooleanCaseWhenStage;
import org.hotrod.livesql.expressions.caseclause.CharCaseWhenStage;
import org.hotrod.livesql.expressions.caseclause.DateTimeCaseWhenStage;
import org.hotrod.livesql.expressions.caseclause.NumericCaseWhenStage;
import org.hotrod.livesql.expressions.caseclause.ObjectCaseWhenStage;
import org.hotrod.livesql.expressions.character.CharConstant;
import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.expressions.character.CharLiteral;
import org.hotrod.livesql.expressions.character.CharSyntaxExpression;
import org.hotrod.livesql.expressions.character.EnclosedCharExpression;
import org.hotrod.livesql.expressions.datetime.CurrentDate;
import org.hotrod.livesql.expressions.datetime.CurrentDateTime;
import org.hotrod.livesql.expressions.datetime.CurrentTime;
import org.hotrod.livesql.expressions.datetime.DateTime;
import org.hotrod.livesql.expressions.datetime.DateTimeConstant;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeSyntaxExpression;
import org.hotrod.livesql.expressions.datetime.EnclosedDateTimeExpression;
import org.hotrod.livesql.expressions.datetime.literals.LocalDateLiteral;
import org.hotrod.livesql.expressions.datetime.literals.LocalTimeLiteral;
import org.hotrod.livesql.expressions.datetime.literals.LocalTimestampLiteral;
import org.hotrod.livesql.expressions.datetime.literals.OffsetTimeLiteral;
import org.hotrod.livesql.expressions.datetime.literals.OffsetTimestampLiteral;
import org.hotrod.livesql.expressions.general.TupleExpression;
import org.hotrod.livesql.expressions.numeric.DecimalLiteral;
import org.hotrod.livesql.expressions.numeric.EnclosedNumericExpression;
import org.hotrod.livesql.expressions.numeric.IntegerLiteral;
import org.hotrod.livesql.expressions.numeric.NumericConstant;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.numeric.NumericSyntaxExpression;
import org.hotrod.livesql.expressions.object.EnclosedObjectExpression;
import org.hotrod.livesql.expressions.object.ObjectConstant;
import org.hotrod.livesql.expressions.object.ObjectExpression;
import org.hotrod.livesql.expressions.object.ObjectSyntaxExpression;
import org.hotrod.livesql.metadata.Table;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.ordering.AliasOrderingTerm;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.ordering.OrdinalOrderingTerm;
import org.hotrod.livesql.queries.DeleteFromPhase;
import org.hotrod.livesql.queries.InsertIntoPhase;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.UpdateTablePhase;
import org.hotrod.livesql.queries.ctes.CTE;
import org.hotrod.livesql.queries.ctes.CTEHeaderPhase;
import org.hotrod.livesql.queries.ctes.RecursiveCTE;
import org.hotrod.livesql.queries.scalarsubqueries.BinarySelectColumnsPhase;
import org.hotrod.livesql.queries.scalarsubqueries.BooleanSelectColumnsPhase;
import org.hotrod.livesql.queries.scalarsubqueries.CharSelectColumnsPhase;
import org.hotrod.livesql.queries.scalarsubqueries.DateTimeSelectColumnsPhase;
import org.hotrod.livesql.queries.scalarsubqueries.NumericSelectColumnsPhase;
import org.hotrod.livesql.queries.scalarsubqueries.ObjectSelectColumnsPhase;
import org.hotrod.livesql.queries.select.EnclosedSelectPhase;
import org.hotrod.livesql.queries.select.NonLockableSelectColumnsPhase;
import org.hotrod.livesql.queries.select.NonLockableSelectDistinctOnPhase;
import org.hotrod.livesql.queries.select.SShield;
import org.hotrod.livesql.queries.select.Select;
import org.hotrod.livesql.queries.select.SelectCTEPhase;
import org.hotrod.livesql.queries.select.SelectColumnsPhase;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.livesql.queries.subqueries.SubqueryColumnsPhase;
import org.hotrod.livesql.queries.typesolver.RuntimeTypeSolver;
import org.hotrod.livesql.sysobjects.DualTable;
import org.hotrod.livesql.sysobjects.SysDummy1Table;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class LiveSQL {

  private static final Logger log = Logger.getLogger(LiveSQL.class.getName());

  public final Table<?> DUAL = new DualTable();
  public final Table<?> SYSDUMMY1 = new SysDummy1Table();

  // Properties

  private LiveSQLContext context = null;
  private DataSource dataSource;
  private LiveSQLDialect liveSQLDialect;

  private RuntimeTypeSolver typeSolver;
  @SuppressWarnings("unused")
  private String qualifier = null;

  // Constructor

  public LiveSQL(LiveSQLDialect liveSQLDialect, DataSource dataSource, String qualifier,
      LayerConfiguration layerConfiguration) {
    log.info("Initializing LiveSQL for qualifier: " + (qualifier == null ? "(main)" : qualifier)
        + (" - runtime rules: " + layerConfiguration.getRuntimeTypeSolverRules().size()));
    this.liveSQLDialect = liveSQLDialect;
    this.dataSource = dataSource;
    this.qualifier = qualifier;
    this.typeSolver = new RuntimeTypeSolver(layerConfiguration.getRuntimeTypeSolverRules(), this.liveSQLDialect);
    this.context = new LiveSQLContext(liveSQLDialect, this.dataSource, this.typeSolver);

  }

  // Shielded methods

  DataSource getDataSource() {
    return dataSource;
  }

  LiveSQLDialect getLiveSQLDialect() {
    return liveSQLDialect;
  }

  // Select

  public SelectColumnsPhase<Row> select() {
    return new SelectColumnsPhase<Row>(this.context, null, false);
  }

  public NonLockableSelectColumnsPhase<Row> selectDistinct() {
    return new NonLockableSelectColumnsPhase<Row>(this.context, null, true);
  }

  public SelectColumnsPhase<Row> select(final SQLExpression... sqlExpressions) {
    return new SelectColumnsPhase<Row>(this.context, null, false, sqlExpressions);
  }

  public NonLockableSelectColumnsPhase<Row> selectDistinct(final SQLExpression... sqlExpressions) {
    return new NonLockableSelectColumnsPhase<Row>(this.context, null, true, sqlExpressions);
  }

  public NonLockableSelectDistinctOnPhase<Row> selectDistinctOn(final Expression... expressions) {
    return new NonLockableSelectDistinctOnPhase<Row>(this.context, null, expressions);
  }

  // Subqueries

  public Subquery subquery(final String alias, final Select<?> select) {
    return new Subquery(alias, null, select);
  }

  public SubqueryColumnsPhase subquery(final String alias, final String... columns) {
    return new SubqueryColumnsPhase(alias, columns);
  }

  // Scalar subqueries

  public NumericSelectColumnsPhase selectScalar(final NumericExpression expression) {
    return new NumericSelectColumnsPhase(null, false, expression);
  }

  public CharSelectColumnsPhase selectScalar(final CharExpression expression) {
    return new CharSelectColumnsPhase(null, false, expression);
  }

  public BooleanSelectColumnsPhase selectScalar(final Predicate expression) {
    return new BooleanSelectColumnsPhase(null, false, expression);
  }

  public DateTimeSelectColumnsPhase selectScalar(final DateTimeExpression expression) {
    return new DateTimeSelectColumnsPhase(null, false, expression);
  }

  public BinarySelectColumnsPhase selectScalar(final BinaryExpression expression) {
    return new BinarySelectColumnsPhase(null, false, expression);
  }

  public ObjectSelectColumnsPhase selectScalar(final ObjectExpression expression) {
    return new ObjectSelectColumnsPhase(null, false, expression);
  }

  // CTEs

  public CTE cte(final String name, final Select<Row> select) {
    return new CTE(name, select);
  }

  public CTEHeaderPhase cte(final String name, final String... columns) {
    return new CTEHeaderPhase(name, columns);
  }

  public RecursiveCTE recursiveCTE(final String name, final String... columns) {
    return new RecursiveCTE(name, columns);
  }

  public SelectCTEPhase<Row> with(final CTE... ctes) {
    return new SelectCTEPhase<Row>(this.context, ctes);
  }

  // Insert

  public InsertIntoPhase insert(final TableOrView<?> into) {
    return new InsertIntoPhase(this.context, into);
  }

  // Update

  public UpdateTablePhase update(final TableOrView<?> tableOrView) {
    return new UpdateTablePhase(this.context, tableOrView);
  }

  // Delete

  public DeleteFromPhase delete(final TableOrView<?> from) {
    return new DeleteFromPhase(this.context, from);
  }

  // Tuples

  public TupleExpression tuple(final ComparableExpression... expressions) {
    return new TupleExpression(expressions);
  }

  // Predicates

  public BooleanSyntaxExpression not(final Predicate a) {
    return new Not(a);
  }

  // Subquery existence

  public <R> BooleanSyntaxExpression exists(final Select<?> subquery) {
    return new Exists(subquery);
  }

  public <R> BooleanSyntaxExpression notExists(final Select<?> subquery) {
    return new NotExists(subquery);
  }

  // Enclosing queries

  public <R> EnclosedSelectPhase<R> enclose(final Select<R> select) {
    return new EnclosedSelectPhase<>(this.context, SShield.getCombinedSelect(select));
  }

  // Aggregation expressions, that are NOT window functions

  public CountRows count() {
    return new CountRows();
  }

  public CountValues count(final ComparableExpression expression) {
    return new CountValues(expression);
  }

  public CountDistinct countDistinct(final ComparableExpression expression) {
    return new CountDistinct(expression);
  }

  public SumDistinct sumDistinct(final NumericExpression expression) {
    return new SumDistinct(expression);
  }

  public AvgDistinct avgDistinct(final NumericExpression expression) {
    return new AvgDistinct(expression);
  }

  public GroupConcatDistinct groupConcatDistinct(final CharExpression expression) {
    return new GroupConcatDistinct(expression, null, null);
  }

  public GroupConcatDistinct groupConcatDistinct(final CharExpression expression, final String separator) {
    return new GroupConcatDistinct(expression, null, val(separator));
  }

  public GroupConcatDistinct groupConcatDistinct(final CharExpression expression, final String separator,
      final OrderingTerm... order) {
    return new GroupConcatDistinct(expression, Arrays.asList(order), val(separator));
  }

  // Aggregation expressions, that ALSO are window functions

  public Sum sum(final NumericExpression expression) {
    return new Sum(expression);
  }

  public Avg avg(final NumericExpression expression) {
    return new Avg(expression);
  }

  public GroupConcat groupConcat(final CharExpression expression) {
    return new GroupConcat(expression, null, null);
  }

  public GroupConcat groupConcat(final CharExpression expression, final String separator) {
    return new GroupConcat(expression, null, val(separator));
  }

  public GroupConcat groupConcat(final CharExpression expression, final String separator, final OrderingTerm... order) {
    return new GroupConcat(expression, Arrays.asList(order), val(separator));
  }

  // Max -- Aggregation expressions, that ALSO are window functions

  public NumericMax max(final NumericExpression expression) {
    return new NumericMax(expression);
  }

  public CharMax max(final CharExpression expression) {
    return new CharMax(expression);
  }

  public DateTimeMax max(final DateTimeExpression expression) {
    return new DateTimeMax(expression);
  }

  public BooleanMax max(final Predicate expression) {
    return new BooleanMax(expression);
  }

  public BinaryMax max(final BinaryExpression expression) {
    return new BinaryMax(expression);
  }

  public ObjectMax max(final ObjectExpression expression) {
    return new ObjectMax(expression);
  }

  // Min -- Aggregation expressions, that ALSO are window functions

  public NumericMin min(final NumericExpression expression) {
    return new NumericMin(expression);
  }

  public CharMin min(final CharExpression expression) {
    return new CharMin(expression);
  }

  public DateTimeMin min(final DateTimeExpression expression) {
    return new DateTimeMin(expression);
  }

  public BooleanMin min(final Predicate expression) {
    return new BooleanMin(expression);
  }

  public BinaryMin min(final BinaryExpression expression) {
    return new BinaryMin(expression);
  }

  public ObjectMin min(final ObjectExpression expression) {
    return new ObjectMin(expression);
  }

  // Analytical functions

  public RowNumber rowNumber() {
    return new RowNumber();
  }

  public Rank rank(final ComparableExpression expression) {
    return new Rank(expression);
  }

  public DenseRank denseRank(final ComparableExpression expression) {
    return new DenseRank(expression);
  }

  public NTile ntile(final ComparableExpression expression) {
    return new NTile(expression);
  }

  // Positional Analytic functions

  // === Lead Number ===

  public NumericLead lead(final NumericExpression expression) {
    return new NumericLead(expression);
  }

  public NumericLead lead(final NumericExpression expression, final Number offset) {
    return new NumericLead(expression, val(offset));
  }

  public NumericLead lead(final NumericExpression expression, final NumericExpression offset) {
    return new NumericLead(expression, offset);
  }

  public NumericLead lead(final NumericExpression expression, final Number offset, final Number defaultValue) {
    return new NumericLead(expression, val(offset), val(defaultValue));
  }

  public NumericLead lead(final NumericExpression expression, final NumericExpression offset,
      final Number defaultValue) {
    return new NumericLead(expression, offset, val(defaultValue));
  }

  public NumericLead lead(final NumericExpression expression, final Number offset,
      final NumericExpression defaultValue) {
    return new NumericLead(expression, val(offset), defaultValue);
  }

  public NumericLead lead(final NumericExpression expression, final NumericExpression offset,
      final NumericExpression defaultValue) {
    return new NumericLead(expression, offset, defaultValue);
  }

  // === Lead String ===

  public CharLead lead(final CharExpression expression) {
    return new CharLead(expression);
  }

  public CharLead lead(final CharExpression expression, final Number offset) {
    return new CharLead(expression, val(offset));
  }

  public CharLead lead(final CharExpression expression, final NumericExpression offset) {
    return new CharLead(expression, offset);
  }

  public CharLead lead(final CharExpression expression, final Number offset, final String defaultValue) {
    return new CharLead(expression, val(offset), val(defaultValue));
  }

  public CharLead lead(final CharExpression expression, final NumericExpression offset, final String defaultValue) {
    return new CharLead(expression, offset, val(defaultValue));
  }

  public CharLead lead(final CharExpression expression, final Number offset, final CharExpression defaultValue) {
    return new CharLead(expression, val(offset), defaultValue);
  }

  public CharLead lead(final CharExpression expression, final NumericExpression offset,
      final CharExpression defaultValue) {
    return new CharLead(expression, offset, defaultValue);
  }

  // === Lead DateTime ===

  public DateTimeLead lead(final DateTimeExpression expression) {
    return new DateTimeLead(expression);
  }

  public DateTimeLead lead(final DateTimeExpression expression, final Number offset) {
    return new DateTimeLead(expression, val(offset));
  }

  public DateTimeLead lead(final DateTimeExpression expression, final NumericExpression offset) {
    return new DateTimeLead(expression, offset);
  }

  public DateTimeLead lead(final DateTimeExpression expression, final Number offset, final Date defaultValue) {
    return new DateTimeLead(expression, val(offset), val(defaultValue));
  }

  public DateTimeLead lead(final DateTimeExpression expression, final NumericExpression offset,
      final Date defaultValue) {
    return new DateTimeLead(expression, offset, val(defaultValue));
  }

  public DateTimeLead lead(final DateTimeExpression expression, final Number offset,
      final DateTimeExpression defaultValue) {
    return new DateTimeLead(expression, val(offset), defaultValue);
  }

  public DateTimeLead lead(final DateTimeExpression expression, final NumericExpression offset,
      final DateTimeExpression defaultValue) {
    return new DateTimeLead(expression, offset, defaultValue);
  }

  // === Lead Boolean ===

  public BooleanLead lead(final Predicate expression) {
    return new BooleanLead(expression);
  }

  public BooleanLead lead(final Predicate expression, final Number offset) {
    return new BooleanLead(expression, val(offset));
  }

  public BooleanLead lead(final Predicate expression, final NumericExpression offset) {
    return new BooleanLead(expression, offset);
  }

  public BooleanLead lead(final Predicate expression, final Number offset, final Boolean defaultValue) {
    return new BooleanLead(expression, val(offset), val(defaultValue));
  }

  public BooleanLead lead(final Predicate expression, final NumericExpression offset, final Boolean defaultValue) {
    return new BooleanLead(expression, offset, val(defaultValue));
  }

  public BooleanLead lead(final Predicate expression, final Number offset, final Predicate defaultValue) {
    return new BooleanLead(expression, val(offset), defaultValue);
  }

  public BooleanLead lead(final Predicate expression, final NumericExpression offset, final Predicate defaultValue) {
    return new BooleanLead(expression, offset, defaultValue);
  }

  // === Lead ByteArray ===

  public BinaryLead lead(final BinaryExpression expression) {
    return new BinaryLead(expression);
  }

  public BinaryLead lead(final BinaryExpression expression, final Number offset) {
    return new BinaryLead(expression, val(offset));
  }

  public BinaryLead lead(final BinaryExpression expression, final NumericExpression offset) {
    return new BinaryLead(expression, offset);
  }

  public BinaryLead lead(final BinaryExpression expression, final Number offset, final byte[] defaultValue) {
    return new BinaryLead(expression, val(offset), val(defaultValue));
  }

  public BinaryLead lead(final BinaryExpression expression, final NumericExpression offset, final byte[] defaultValue) {
    return new BinaryLead(expression, offset, val(defaultValue));
  }

  public BinaryLead lead(final BinaryExpression expression, final Number offset, final BinaryExpression defaultValue) {
    return new BinaryLead(expression, val(offset), defaultValue);
  }

  public BinaryLead lead(final BinaryExpression expression, final NumericExpression offset,
      final BinaryExpression defaultValue) {
    return new BinaryLead(expression, offset, defaultValue);
  }

  // === Lead Object ===

  public ObjectLead lead(final ObjectExpression expression) {
    return new ObjectLead(expression);
  }

  public ObjectLead lead(final ObjectExpression expression, final Number offset) {
    return new ObjectLead(expression, val(offset));
  }

  public ObjectLead lead(final ObjectExpression expression, final NumericExpression offset) {
    return new ObjectLead(expression, offset);
  }

  public ObjectLead lead(final ObjectExpression expression, final Number offset, final Object defaultValue) {
    return new ObjectLead(expression, val(offset), val(defaultValue));
  }

  public ObjectLead lead(final ObjectExpression expression, final NumericExpression offset, final Object defaultValue) {
    return new ObjectLead(expression, offset, val(defaultValue));
  }

  public ObjectLead lead(final ObjectExpression expression, final Number offset, final ObjectExpression defaultValue) {
    return new ObjectLead(expression, val(offset), defaultValue);
  }

  public ObjectLead lead(final ObjectExpression expression, final NumericExpression offset,
      final ObjectExpression defaultValue) {
    return new ObjectLead(expression, offset, defaultValue);
  }

  // === Lag Number ===

  public NumericLag lag(final NumericExpression expression) {
    return new NumericLag(expression);
  }

  public NumericLag lag(final NumericExpression expression, final Number offset) {
    return new NumericLag(expression, val(offset));
  }

  public NumericLag lag(final NumericExpression expression, final NumericExpression offset) {
    return new NumericLag(expression, offset);
  }

  public NumericLag lag(final NumericExpression expression, final Number offset, final Number defaultValue) {
    return new NumericLag(expression, val(offset), val(defaultValue));
  }

  public NumericLag lag(final NumericExpression expression, final NumericExpression offset, final Number defaultValue) {
    return new NumericLag(expression, offset, val(defaultValue));
  }

  public NumericLag lag(final NumericExpression expression, final Number offset, final NumericExpression defaultValue) {
    return new NumericLag(expression, val(offset), defaultValue);
  }

  public NumericLag lag(final NumericExpression expression, final NumericExpression offset,
      final NumericExpression defaultValue) {
    return new NumericLag(expression, offset, defaultValue);
  }

  // === Lag String ===

  public CharLag lag(final CharExpression expression) {
    return new CharLag(expression);
  }

  public CharLag lag(final CharExpression expression, final Number offset) {
    return new CharLag(expression, val(offset));
  }

  public CharLag lag(final CharExpression expression, final NumericExpression offset) {
    return new CharLag(expression, offset);
  }

  public CharLag lag(final CharExpression expression, final Number offset, final String defaultValue) {
    return new CharLag(expression, val(offset), val(defaultValue));
  }

  public CharLag lag(final CharExpression expression, final NumericExpression offset, final String defaultValue) {
    return new CharLag(expression, offset, val(defaultValue));
  }

  public CharLag lag(final CharExpression expression, final Number offset, final CharExpression defaultValue) {
    return new CharLag(expression, val(offset), defaultValue);
  }

  public CharLag lag(final CharExpression expression, final NumericExpression offset,
      final CharExpression defaultValue) {
    return new CharLag(expression, offset, defaultValue);
  }

  // === Lag DateTime ===

  public DateTimeLag lag(final DateTimeExpression expression) {
    return new DateTimeLag(expression);
  }

  public DateTimeLag lag(final DateTimeExpression expression, final Number offset) {
    return new DateTimeLag(expression, val(offset));
  }

  public DateTimeLag lag(final DateTimeExpression expression, final NumericExpression offset) {
    return new DateTimeLag(expression, offset);
  }

  public DateTimeLag lag(final DateTimeExpression expression, final Number offset, final Date defaultValue) {
    return new DateTimeLag(expression, val(offset), val(defaultValue));
  }

  public DateTimeLag lag(final DateTimeExpression expression, final NumericExpression offset, final Date defaultValue) {
    return new DateTimeLag(expression, offset, val(defaultValue));
  }

  public DateTimeLag lag(final DateTimeExpression expression, final Number offset,
      final DateTimeExpression defaultValue) {
    return new DateTimeLag(expression, val(offset), defaultValue);
  }

  public DateTimeLag lag(final DateTimeExpression expression, final NumericExpression offset,
      final DateTimeExpression defaultValue) {
    return new DateTimeLag(expression, offset, defaultValue);
  }

  // === Lag Boolean ===

  public BooleanLag lag(final Predicate expression) {
    return new BooleanLag(expression);
  }

  public BooleanLag lag(final Predicate expression, final Number offset) {
    return new BooleanLag(expression, val(offset));
  }

  public BooleanLag lag(final Predicate expression, final NumericExpression offset) {
    return new BooleanLag(expression, offset, null);
  }

  public BooleanLag lag(final Predicate expression, final Number offset, final Boolean defaultValue) {
    return new BooleanLag(expression, val(offset), val(defaultValue));
  }

  public BooleanLag lag(final Predicate expression, final NumericExpression offset, final Boolean defaultValue) {
    return new BooleanLag(expression, offset, val(defaultValue));
  }

  public BooleanLag lag(final Predicate expression, final Number offset, final Predicate defaultValue) {
    return new BooleanLag(expression, val(offset), defaultValue);
  }

  public BooleanLag lag(final Predicate expression, final NumericExpression offset, final Predicate defaultValue) {
    return new BooleanLag(expression, offset, defaultValue);
  }

  // === Lag ByteArray ===

  public BinaryLag lag(final BinaryExpression expression) {
    return new BinaryLag(expression);
  }

  public BinaryLag lag(final BinaryExpression expression, final Number offset) {
    return new BinaryLag(expression, val(offset));
  }

  public BinaryLag lag(final BinaryExpression expression, final NumericExpression offset) {
    return new BinaryLag(expression, offset);
  }

  public BinaryLag lag(final BinaryExpression expression, final Number offset, final byte[] defaultValue) {
    return new BinaryLag(expression, val(offset), val(defaultValue));
  }

  public BinaryLag lag(final BinaryExpression expression, final NumericExpression offset, final byte[] defaultValue) {
    return new BinaryLag(expression, offset, val(defaultValue));
  }

  public BinaryLag lag(final BinaryExpression expression, final Number offset, final BinaryExpression defaultValue) {
    return new BinaryLag(expression, val(offset), defaultValue);
  }

  public BinaryLag lag(final BinaryExpression expression, final NumericExpression offset,
      final BinaryExpression defaultValue) {
    return new BinaryLag(expression, offset, defaultValue);
  }

  // === Lag Object ===

  public ObjectLag lag(final ObjectExpression expression) {
    return new ObjectLag(expression);
  }

  public ObjectLag lag(final ObjectExpression expression, final Number offset) {
    return new ObjectLag(expression, val(offset));
  }

  public ObjectLag lag(final ObjectExpression expression, final NumericExpression offset) {
    return new ObjectLag(expression, offset);
  }

  public ObjectLag lag(final ObjectExpression expression, final Number offset, final Object defaultValue) {
    return new ObjectLag(expression, val(offset), val(defaultValue));
  }

  public ObjectLag lag(final ObjectExpression expression, final NumericExpression offset, final Object defaultValue) {
    return new ObjectLag(expression, offset, val(defaultValue));
  }

  public ObjectLag lag(final ObjectExpression expression, final Number offset, final ObjectExpression defaultValue) {
    return new ObjectLag(expression, val(offset), defaultValue);
  }

  public ObjectLag lag(final ObjectExpression expression, final NumericExpression offset,
      final ObjectExpression defaultValue) {
    return new ObjectLag(expression, offset, defaultValue);
  }

  // Case

  public NumericCaseWhenStage caseWhen(final Predicate predicate, final Number value) {
    return new NumericCaseWhenStage(predicate, val(value));
  }

  public NumericCaseWhenStage caseWhen(final Predicate predicate, final NumericExpression value) {
    return new NumericCaseWhenStage(predicate, value);
  }

  public CharCaseWhenStage caseWhen(final Predicate predicate, final String value) {
    return new CharCaseWhenStage(predicate, val(value));
  }

  public CharCaseWhenStage caseWhen(final Predicate predicate, final CharExpression value) {
    return new CharCaseWhenStage(predicate, value);
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

  public BinaryCaseWhenStage caseWhen(final Predicate predicate, final byte[] value) {
    return new BinaryCaseWhenStage(predicate, val(value));
  }

  public BinaryCaseWhenStage caseWhen(final Predicate predicate, final BinaryExpression value) {
    return new BinaryCaseWhenStage(predicate, value);
  }

  public ObjectCaseWhenStage caseWhen(final Predicate predicate, final Object value) {
    return new ObjectCaseWhenStage(predicate, val(value));
  }

  public ObjectCaseWhenStage caseWhen(final Predicate predicate, final ObjectExpression value) {
    return new ObjectCaseWhenStage(predicate, value);
  }

  // Date/Time

  public DateTimeSyntaxExpression currentDate() {
    return new CurrentDate();
  }

  public DateTimeSyntaxExpression currentTime() {
    return new CurrentTime();
  }

  public DateTimeSyntaxExpression currentDateTime() {
    return new CurrentDateTime();
  }

  public DateTimeSyntaxExpression datetime(final DateTimeExpression date, final DateTimeExpression time) {
    return new DateTime(date, time);
  }

  // Boxing of scalar values

  public CharConstant val(final String value) {
    return new CharConstant(value);
  }

  public CharConstant val(final Character value) {
    return new CharConstant("" + value);
  }

  public NumericConstant val(final Number value) {
    return new NumericConstant(value);
  }

  public DateTimeConstant val(final Date value) {
    return new DateTimeConstant(value);
  }

  public BooleanConstant val(final Boolean value) {
    return new BooleanConstant(value);
  }

  public BinaryConstant val(final byte[] value) {
    return new BinaryConstant(value);
  }

  public ObjectConstant val(final Object value) {
    return new ObjectConstant(value);
  }

  // Literal NULL

  public final NullLiteral NULL = new NullLiteral();

  // Literals (numbers)

  public final IntegerLiteral ZERO = IntegerLiteral.getZero();
  public final IntegerLiteral ONE = IntegerLiteral.getOne();

  // Covers: byte, short, int, and long
  public IntegerLiteral literal(final long value) {
    return new IntegerLiteral(value);
  }

  public IntegerLiteral literal(final BigInteger value) {
    return new IntegerLiteral(value);
  }

  // Covers: float and double
  public DecimalLiteral literal(final double value, final int precision) {
    return new DecimalLiteral(value, precision);
  }

  public DecimalLiteral literal(final BigDecimal value, final int precision) {
    return new DecimalLiteral(value, precision);
  }

  // Literals (String)

  public CharLiteral literal(final String value) {
    return new CharLiteral(value);
  }

  // Literals (DateTime)

  public LocalDateLiteral literal(final LocalDate value) {
    return new LocalDateLiteral(this.context, value);
  }

  public LocalTimeLiteral literal(final LocalTime value, final int precision) {
    return new LocalTimeLiteral(this.context, value, precision);
  }

  public LocalTimestampLiteral literal(final LocalDateTime value, final int precision) {
    return new LocalTimestampLiteral(this.context, value, precision);
  }

  public OffsetTimeLiteral literal(final OffsetTime value, final int precision) {
    return new OffsetTimeLiteral(this.context, value, precision);
  }

  public OffsetTimestampLiteral literal(final OffsetDateTime value, final int precision) {
    return new OffsetTimestampLiteral(this.context, value, precision);
  }

  // Literals (Boolean)

  public final BooleanLiteral FALSE = BooleanLiteral.getFalse();
  public final BooleanLiteral TRUE = BooleanLiteral.getTrue();

  // Parenthesis

  public CharSyntaxExpression enclose(final CharExpression value) {
    return new EnclosedCharExpression(value);
  }

  public NumericSyntaxExpression enclose(final NumericExpression value) {
    return new EnclosedNumericExpression(value);
  }

  public DateTimeSyntaxExpression enclose(final DateTimeExpression value) {
    return new EnclosedDateTimeExpression(value);
  }

  public BooleanSyntaxExpression enclose(final Predicate value) {
    return new EnclosedBooleanExpression(value);
  }

  public BinarySyntaxExpression enclose(final BinaryExpression value) {
    return new EnclosedBinaryExpression(value);
  }

  public ObjectSyntaxExpression enclose(final ObjectExpression value) {
    return new EnclosedObjectExpression(value);
  }

  // Ordering Terms

  public AliasOrderingTerm ordering(final String column) {
    return new AliasOrderingTerm(column);
  }

  public OrdinalOrderingTerm ordering(final int column) {
    return new OrdinalOrderingTerm(column);
  }

}
