package org.hotrod.dynamicsql.segments;

import java.util.List;

import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;

public class WhenSegment extends ControlSegment {

  private String test;
  private List<QuerySegment> segments;

  private DynamicExpression testExpression;

  public WhenSegment(String test, List<QuerySegment> segments, DynamicExpressionFactory factory) {
    this.test = test;
    this.segments = segments;
    this.testExpression = factory.expression(this.test);
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, Parameters context, int loopNestingLevel)
      throws DynamicExpressionException {

    // 1. Evaluate the test condition

    Boolean condObj = null;
    boolean cond = false;
    try {
      condObj = this.testExpression.evaluate(context, Boolean.class);
      if (condObj == null) {
        throw new DynamicExpressionException("The dynamic test condition '" + this.test
            + "' evaluated to null, but must evaluate to either true or false.");
      } else {
        cond = condObj;
      }
    } catch (RuntimeException e) {
      throw new DynamicExpressionException(
          "Could not evaluate the test condition '" + this.test + "' on Dynamic SQL WHEN segment", e);
    }

    // 2. Include the inner segments as needed

    if (cond) {
      for (QuerySegment s : this.segments) {
        s.prepare(sc, context, loopNestingLevel);
      }
    }

    return cond;

  }

}
