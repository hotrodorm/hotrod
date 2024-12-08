package org.hotrod.dynamic.segments;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.ParameterContext;
import org.hotrod.dynamic.PreparedQuery;

public class LiteralSegment extends QuerySegment {

  private String literal;

  public LiteralSegment(String literal) {
    this.literal = literal;
  }

  @Override
  public void prepare(PreparedQuery pq, ParameterContext context) throws DynamicExpressionException {
    pq.addLiteral(this.literal);
  }

}
