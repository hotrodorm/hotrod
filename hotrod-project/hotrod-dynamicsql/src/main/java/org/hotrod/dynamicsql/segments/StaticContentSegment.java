package org.hotrod.dynamicsql.segments;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.Parameters;

public class StaticContentSegment extends ContentSegment {

  private String literal;

  public StaticContentSegment(String literal) {
    this.literal = literal;
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, Parameters context, int loopNestingLevel)
      throws DynamicExpressionException {
    sc.consume(this.literal);
    return true;
  }

}
