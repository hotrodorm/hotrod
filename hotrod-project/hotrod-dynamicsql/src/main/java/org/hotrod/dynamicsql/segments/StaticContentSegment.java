package org.hotrod.dynamicsql.segments;

import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.Parameters;

public class StaticContentSegment extends ContentSegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(StaticContentSegment.class.getName());

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
