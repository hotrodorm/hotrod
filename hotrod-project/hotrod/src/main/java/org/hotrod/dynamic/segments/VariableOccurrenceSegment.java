package org.hotrod.dynamic.segments;

import java.util.logging.Logger;

import org.hotrod.dynamic.DynamicExpression;
import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.ParameterContext;

public class VariableOccurrenceSegment extends QuerySegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(VariableOccurrenceSegment.class.getName());

  private DynamicExpressionFactory factory;
  private String name;
  private DynamicExpression nameExpression;

  public VariableOccurrenceSegment(DynamicExpressionFactory factory, String name) {
    this.factory = factory;
    this.name = name;
    this.nameExpression = this.factory.expression(this.name);
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, ParameterContext context, int loopNestingLevel)
      throws DynamicExpressionException {

    Object v = this.nameExpression.evaluate(context, Object.class);
    VariableInstanceValueSegment is = new VariableInstanceValueSegment(v, this.name);
    sc.consume("?");
    sc.consume(is);
    return true;
  }

}
