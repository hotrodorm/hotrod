package org.hotrod.dynamic.segments;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.ParameterContext;

public class SetSegment extends DynamicSegment {

  private String column;
  private ParameterSegment value;

  public SetSegment(String column, ParameterSegment value, DynamicExpressionFactory factory) {
    this.column = column;
    this.value = value;
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, ParameterContext context) throws DynamicExpressionException {
    sc.consume(this.column + " = ");
    sc.consume(this.value);
    return true;
  }

}
