package org.hotrod.dynamic.segments;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.ParameterContext;

public class LiteralSegment extends StaticSegment {

  private String literal;

  public LiteralSegment(String literal) {
    this.literal = literal;
  }

  @Override
  public void prepare(StaticSegmentConsumer sc, ParameterContext context) throws DynamicExpressionException {
    sc.consume(this.literal);
  }

//  // Static Segment
//
//  @Override
//  public String getLiteral() {
//    return this.literal;
//  }
//
//  @Override
//  public ParameterSegment getParameter() {
//    return null;
//  }

}
