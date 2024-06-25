package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.expressions.OrderingTerm;
import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameBound;
import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameExclusion;
import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression.FrameUnit;
import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class ObjectWindowExpression extends ObjectExpression {

  // Properties

  private WindowableFunction windowableFunction;
  private WindowExpression windowExpression;

  // Constructor

  public ObjectWindowExpression(final WindowableFunction windowableFunction) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.windowableFunction = windowableFunction;
    this.windowExpression = new WindowExpression();
  }

  // Setters

  public void setPartitionBy(final List<ComparableExpression> partitionBy) {
    this.windowExpression.setPartitionBy(partitionBy);
  }

  public void setOrderBy(final List<OrderingTerm> orderBy) {
    this.windowExpression.setOrderBy(orderBy);
  }

  public void setFrameUnit(final FrameUnit frameUnit) {
    this.windowExpression.setFrameUnit(frameUnit);
  }

  public void setFrameStart(final FrameBound frameStart, final Integer offsetStart) {
    this.windowExpression.setFrameStart(frameStart, offsetStart);
  }

  public void setFrameEnd(final FrameBound frameEnd, final Integer offsetEnd) {
    this.windowExpression.setFrameEnd(frameEnd, offsetEnd);
  }

  public void setFrameExclusion(final FrameExclusion frameExclusion) {
    this.windowExpression.setFrameExclusion(frameExclusion);
  }

  // Rendering

  @Override
  protected void renderTo(final QueryWriter w) {
    Helper.renderTo(this.windowableFunction, w);
    this.windowExpression.renderTo(w);
  }

}
