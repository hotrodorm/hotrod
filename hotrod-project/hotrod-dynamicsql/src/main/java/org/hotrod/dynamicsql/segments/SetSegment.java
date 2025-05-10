package org.hotrod.dynamicsql.segments;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;

public class SetSegment extends ControlSegment {

  private String column;
  private ParameterNullableSegment value;

  public SetSegment(String column, ParameterNullableSegment value, DynamicExpressionFactory factory) {
    this.column = column;
    this.value = value;
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, Parameters context, int loopNestingLevel)
      throws DynamicExpressionException {
    sc.consume(this.column + " = ");
    this.value.prepare(sc, context, loopNestingLevel);
    return true;
  }

}
