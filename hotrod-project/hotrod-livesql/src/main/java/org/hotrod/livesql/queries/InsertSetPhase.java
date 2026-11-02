package org.hotrod.livesql.queries;

import java.util.Date;

import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.object.ObjectExpression;
import org.hotrod.livesql.metadata.BinaryEntityColumn;
import org.hotrod.livesql.metadata.BooleanEntityColumn;
import org.hotrod.livesql.metadata.CharEntityColumn;
import org.hotrod.livesql.metadata.DateTimeEntityColumn;
import org.hotrod.livesql.metadata.NumericEntityColumn;
import org.hotrod.livesql.metadata.ObjectEntityColumn;
import org.hotrod.livesql.util.BoxUtil;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class InsertSetPhase<I extends InsertSetPhase<?>> implements DMLQuery {

  // Properties

  protected LiveSQLContext context;
  protected InsertObject insert;

  // Constructor

  public InsertSetPhase(final LiveSQLContext context, final InsertObject insert) {
    this.context = context;
    this.insert = insert;
  }

  @SuppressWarnings("unchecked")
  private I getCurrentPhase() {
    return (I) this;
  }
  
  // Current phase

  public I set(final NumericEntityColumn column, final NumericExpression expression) {
    this.insert.addSetter(column, expression);
    return this.getCurrentPhase();
  }

  public I set(final NumericEntityColumn column, final Number n) {
    this.insert.addSetter(column, BoxUtil.box(n));
    return this.getCurrentPhase();
  }

  public I set(final CharEntityColumn column, final CharExpression expression) {
    this.insert.addSetter(column, expression);
    return this.getCurrentPhase();
  }

  public I set(final CharEntityColumn column, final String s) {
    this.insert.addSetter(column, BoxUtil.box(s));
    return this.getCurrentPhase();
  }

  public I set(final DateTimeEntityColumn column, final DateTimeExpression expression) {
    this.insert.addSetter(column, expression);
    return this.getCurrentPhase();
  }

  public I set(final DateTimeEntityColumn column, final Date dt) {
    this.insert.addSetter(column, BoxUtil.box(dt));
    return this.getCurrentPhase();
  }

  public I set(final BooleanEntityColumn column, final Predicate expression) {
    this.insert.addSetter(column, expression);
    return this.getCurrentPhase();
  }

  public I set(final BooleanEntityColumn column, final boolean b) {
    this.insert.addSetter(column, BoxUtil.box(b));
    return this.getCurrentPhase();
  }

  public I set(final BinaryEntityColumn column, final BinaryExpression expression) {
    this.insert.addSetter(column, expression);
    return this.getCurrentPhase();
  }

  public I set(final BinaryEntityColumn column, final byte[] a) {
    this.insert.addSetter(column, BoxUtil.box(a));
    return this.getCurrentPhase();
  }

  public I set(final ObjectEntityColumn column, final ObjectExpression expression) {
    this.insert.addSetter(column, expression);
    return this.getCurrentPhase();
  }

  public I set(final ObjectEntityColumn column, final Object o) {
    this.insert.addSetter(column, BoxUtil.box(o));
    return this.getCurrentPhase();
  }

  // Next phases

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
