package org.hotrod.dynamicsql.segments;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.ParameterContext;

public class SetSegment extends DynamicSegment {

  private String column;
  private ParameterOccurrenceSegment value;

  public SetSegment(String column, ParameterOccurrenceSegment value, DynamicExpressionFactory factory) {
    this.column = column;
    this.value = value;
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, ParameterContext context, int loopNestingLevel)
      throws DynamicExpressionException {
    sc.consume(this.column + " = ");
    this.value.prepare(sc, context, loopNestingLevel);
    return true;
  }

}
