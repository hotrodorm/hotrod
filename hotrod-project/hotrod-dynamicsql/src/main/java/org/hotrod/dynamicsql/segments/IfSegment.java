package org.hotrod.dynamicsql.segments;

import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.dynamicsql.assembler.Shield;
import org.hotrod.dynamicsql.assembler.Sentence;

public class IfSegment extends ControlSegment {

  private String test;
  private Sentence sentence;

  private DynamicExpression testExpression;

  public IfSegment(String test, Sentence sentence, DynamicExpressionFactory factory) {
    this.test = test;
    this.sentence = sentence;
    this.testExpression = factory.expression(this.test);
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, Parameters context, int loopNestingLevel)
      throws DynamicExpressionException {

    // 1. Evaluate the test condition

    Boolean cond = null;
    try {
      cond = this.testExpression.evaluate(context, Boolean.class);
      if (cond == null) {
        throw new DynamicExpressionException("The dynamic test condition '" + this.test
            + "' evaluated to null, but must evaluate to either true or false.");
      }
    } catch (Throwable e) {
      throw new DynamicExpressionException(
          "Could not evaluate the test condition '" + this.test + "' on Dynamic SQL IF segment", e);
    }

    // 2. Include the inner segments as needed

    if (cond) {
      for (QuerySegment s : Shield.getSegments(this.sentence)) {
        s.prepare(sc, context, loopNestingLevel);
      }
    }

    return cond;

  }

}
