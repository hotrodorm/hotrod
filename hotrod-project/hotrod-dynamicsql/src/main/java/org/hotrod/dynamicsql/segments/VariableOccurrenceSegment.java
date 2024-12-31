package org.hotrod.dynamicsql.segments;

import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.ParameterContext;

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
