package org.hotrod.livesql.queries;

import java.util.Date;

import org.hotrod.livesql.expressions.NullLiteral;
import org.hotrod.livesql.expressions.binary.GeneralByteArrayExpression;
import org.hotrod.livesql.expressions.datetime.GeneralDateTimeExpression;
import org.hotrod.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.livesql.expressions.object.GeneralObjectExpression;
import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.livesql.expressions.strings.GeneralStringExpression;
import org.hotrod.livesql.metadata.BooleanEntityColumn;
import org.hotrod.livesql.metadata.ByteArrayEntityColumn;
import org.hotrod.livesql.metadata.DateTimeEntityColumn;
import org.hotrod.livesql.metadata.NumberEntityColumn;
import org.hotrod.livesql.metadata.ObjectEntityColumn;
import org.hotrod.livesql.metadata.StringEntityColumn;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.util.BoxUtil;

public class UpdateTablePhase {

  // Properties

  private LiveSQLContext context;
  private UpdateObject update;

  // Constructor

  public UpdateTablePhase(final LiveSQLContext context, final TableOrView tableOrView) {
    this.context = context;
    this.update = new UpdateObject();
    this.update.setTableOrView(tableOrView);
  }

  // Next stages

  public UpdateSetPhase set(final NumberEntityColumn column, final GeneralNumberExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final NumberEntityColumn column, final Number n) {
    this.update.addSetter(column, BoxUtil.box(n));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final NumberEntityColumn column, final NullLiteral n) {
    this.update.addSetter(column, n);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final StringEntityColumn column, final GeneralStringExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final StringEntityColumn column, final String s) {
    this.update.addSetter(column, BoxUtil.box(s));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final StringEntityColumn column, final NullLiteral n) {
    this.update.addSetter(column, n);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final DateTimeEntityColumn column, final GeneralDateTimeExpression expression) {
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

  public UpdateSetPhase set(final BooleanEntityColumn column, final GeneralBooleanExpression expression) {
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

  public UpdateSetPhase set(final ByteArrayEntityColumn column, final GeneralByteArrayExpression expression) {
    this.update.addSetter(column, expression);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final ByteArrayEntityColumn column, final byte[] a) {
    this.update.addSetter(column, BoxUtil.box(a));
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final ByteArrayEntityColumn column, final NullLiteral n) {
    this.update.addSetter(column, n);
    return new UpdateSetPhase(this.context, this.update);
  }

  public UpdateSetPhase set(final ObjectEntityColumn column, final GeneralObjectExpression expression) {
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
