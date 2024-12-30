package org.hotrod.dynamicsql.segments;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.ParameterContext;

public class LiteralSegment extends StaticSegment {

  private String literal;

  public LiteralSegment(String literal) {
    this.literal = literal;
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, ParameterContext context, int loopNestingLevel)
      throws DynamicExpressionException {
    sc.consume(this.literal);
    return true;
  }

}
