package org.hotrod.dynamic.segments;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.ParameterContext;

public class LiteralSegment extends StaticSegment {

  private String literal;

  public LiteralSegment(String literal) {
    this.literal = literal;
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, ParameterContext context) throws DynamicExpressionException {
    sc.consume(this.literal);
    return true;
  }

}
