package org.hotrod.livesql.queries;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.object.ObjectExpression;
import org.hotrod.livesql.metadata.BinaryEntityColumn;
import org.hotrod.livesql.metadata.BooleanEntityColumn;
import org.hotrod.livesql.metadata.CharEntityColumn;
import org.hotrod.livesql.metadata.DateTimeEntityColumn;
import org.hotrod.livesql.metadata.EntityColumnMetaData;
import org.hotrod.livesql.metadata.NumericEntityColumn;
import org.hotrod.livesql.metadata.ObjectEntityColumn;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.queries.select.SShield;
import org.hotrod.livesql.queries.select.Select;
import org.hotrod.livesql.util.BoxUtil;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class InsertIntoPhase<I extends InsertSetPhase<?>> implements DMLQuery {

  private static final Logger log = Logger.getLogger(InsertIntoPhase.class.getName());

  // Properties

  protected LiveSQLContext context;
  protected InsertObject insert;

  // Constructor

  public InsertIntoPhase(final LiveSQLContext context, final TableOrView<?, ?, ?> into) {
    log.finer("init");
    this.context = context;
    this.insert = new InsertObject();
    this.insert.setInto(into);
  }

  @SuppressWarnings("unchecked")
  protected I getNextPhase() {
    return (I) new InsertSetPhase<>(this.context, this.insert);
  }

  // Current phase

  public I value(final NumericEntityColumn column, final NumericExpression expression) {
    this.insert.addSetter(column, expression);
    return this.getNextPhase();
  }

  public I value(final NumericEntityColumn column, final Number n) {
    this.insert.addSetter(column, BoxUtil.box(n));
    I nextPhase = this.getNextPhase();
    return nextPhase;
  }

  public I value(final CharEntityColumn column, final CharExpression expression) {
    this.insert.addSetter(column, expression);
    return this.getNextPhase();
  }

  public I value(final CharEntityColumn column, final String s) {
    this.insert.addSetter(column, BoxUtil.box(s));
    return this.getNextPhase();
  }

  public I value(final DateTimeEntityColumn column, final DateTimeExpression expression) {
    this.insert.addSetter(column, expression);
    return this.getNextPhase();
  }

  public I value(final DateTimeEntityColumn column, final Date dt) {
    this.insert.addSetter(column, BoxUtil.box(dt));
    return this.getNextPhase();
  }

  public I value(final BooleanEntityColumn column, final Predicate expression) {
    this.insert.addSetter(column, expression);
    return this.getNextPhase();
  }

  public I value(final BooleanEntityColumn column, final boolean b) {
    this.insert.addSetter(column, BoxUtil.box(b));
    return this.getNextPhase();
  }

  public I value(final BinaryEntityColumn column, final BinaryExpression expression) {
    this.insert.addSetter(column, expression);
    return this.getNextPhase();
  }

  public I value(final BinaryEntityColumn column, final byte[] a) {
    this.insert.addSetter(column, BoxUtil.box(a));
    return this.getNextPhase();
  }

  public I value(final ObjectEntityColumn column, final ObjectExpression expression) {
    this.insert.addSetter(column, expression);
    return this.getNextPhase();
  }

  public I value(final ObjectEntityColumn column, final Object o) {
    this.insert.addSetter(column, BoxUtil.box(o));
    return this.getNextPhase();
  }

  // Next stages

  public InsertColumnsPhase columns(final EntityColumnMetaData... columns) {
    this.insert.setColumns(Arrays.asList(columns));
    return new InsertColumnsPhase(this.context, this.insert);
  }

  @Deprecated
  public InsertColumnsPhase columns(final List<EntityColumnMetaData> columns) {
    this.insert.setColumns(columns);
    return new InsertColumnsPhase(this.context, this.insert);
  }

  /**
   * @deprecated Use set(Column, Value) instead.
   */
  @Deprecated
  public InsertValuesPhase values(final ComparableExpression... values) {
    this.insert.setValues(Arrays.asList(values));
    return new InsertValuesPhase(this.context, this.insert);
  }

  public InsertSelectPhase select(final Select<?> select) {
    this.insert.setSelect(SShield.getCombinedSelect(select).getLastSelect());
    return new InsertSelectPhase(this.context, this.insert);
  }

  // Preview

  @Override
  public String getPreview() {
    return this.insert.getPreview(this.context, false);
  }

  @Override
  public String getPreview(boolean includeParameters) {
    return this.insert.getPreview(this.context, includeParameters);
  }

  // Execute

  @Override
  public int execute() {
    return this.insert.execute(this.context);
  }

  @Override
  public int execute(LiveSQLLogging loggingAdapter) {
    return this.insert.execute(this.context, loggingAdapter);
  }

}
