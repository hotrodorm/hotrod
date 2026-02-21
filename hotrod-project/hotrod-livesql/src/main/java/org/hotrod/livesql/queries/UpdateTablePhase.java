package org.hotrod.livesql.queries;

import java.util.Date;

import org.hotrod.livesql.expressions.NullLiteral;
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
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.util.BoxUtil;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class UpdateTablePhase {

  // Properties

  private LiveSQLContext context;
  private UpdateObject update;

  // Constructor

  public UpdateTablePhase(final LiveSQLContext context, final TableOrView<?> tableOrView) {
    this.context = context;
    this.update = new UpdateObject();
    this.update.setTableOrView(tableOrView);
  }

  // Next stages

  public UpdateSetPhase set(final NumericEntityColumn column, final NumericExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final NumericEntityColumn column, final Number n) {
    this.update.addSetter(column, BoxUtil.box(n));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final NumericEntityColumn column, final NullLiteral n) {
    this.update.addSetter(column, n);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final CharEntityColumn column, final CharExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final CharEntityColumn column, final String s) {
    this.update.addSetter(column, BoxUtil.box(s));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final CharEntityColumn column, final NullLiteral n) {
    this.update.addSetter(column, n);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final DateTimeEntityColumn column, final DateTimeExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final DateTimeEntityColumn column, final Date dt) {
    this.update.addSetter(column, BoxUtil.box(dt));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final DateTimeEntityColumn column, final NullLiteral n) {
    this.update.addSetter(column, n);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final BooleanEntityColumn column, final Predicate expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final BooleanEntityColumn column, final boolean b) {
    this.update.addSetter(column, BoxUtil.box(b));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final BooleanEntityColumn column, final NullLiteral n) {
    this.update.addSetter(column, n);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final BinaryEntityColumn column, final BinaryExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final BinaryEntityColumn column, final byte[] a) {
    this.update.addSetter(column, BoxUtil.box(a));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final BinaryEntityColumn column, final NullLiteral n) {
    this.update.addSetter(column, n);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final ObjectEntityColumn column, final ObjectExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final ObjectEntityColumn column, final Object o) {
    this.update.addSetter(column, BoxUtil.box(o));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final ObjectEntityColumn column, final NullLiteral n) {
    this.update.addSetter(column, n);
    return new UpdateSetPhase(this.context, this.update);
  }

}
