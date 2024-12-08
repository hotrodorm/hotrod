package org.hotrod.dynamic.segments;

import java.util.List;

import org.hotrod.dynamic.DynamicExpression;
import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.DynamicSegment;
import org.hotrod.dynamic.ParameterContext;
import org.hotrod.dynamic.PreparedQuery;

public class IfSegment extends DynamicSegment {

  private String test;
  private List<QuerySegment> segments;

  private DynamicExpression testExpression;

  public IfSegment(String test, List<QuerySegment> segments, DynamicExpressionFactory factory) {
    this.test = test;
    this.segments = segments;
    this.testExpression = factory.create(this.test);
  }

  @Override
  public void prepare(PreparedQuery pq, ParameterContext context) throws DynamicExpressionException {

    // 1. Evaluate the test condition

    Boolean cond = null;
    try {
      Boolean o = this.testExpression.evaluate(context, Boolean.class);
      if (o == null) {
        throw new DynamicExpressionException("The dynamic test condition '" + this.test
            + "' evaluated to null, but must evaluate to either true or false.");
      }
//      try {
//        cond = (Boolean) o;
//      } catch (ClassCastException e) {
//        throw new DynamicExpressionException("The dynamic test condition '" + this.test
//            + "' must evaluate to a boolean value (boolean or Boolean), but resulted in an object of class '"
//            + o.getClass() + "'.");
//      }
    } catch (Throwable e) {
      throw new DynamicExpressionException(
          "Could not evaluate the test condition '" + this.test + "' on conditional segment.");
    }

    // 2. Include the inner segments as needed

    if (cond) {
      for (QuerySegment s : this.segments) {
        s.prepare(pq, context);
      }
    }

  }

}
