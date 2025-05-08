package org.hotrod.dynamicsql.segments;

import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.ParameterContext;
import org.hotrod.dynamicsql.parameters.ParameterInstance;
import org.hotrod.dynamicsql.parameters.VariableInstance;

public class VariableSegment extends DynamicContentSegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(VariableSegment.class.getName());

  private DynamicExpressionFactory factory;
  private String name;
  private DynamicExpression nameExpression;

  public VariableSegment(DynamicExpressionFactory factory, String name) {
    this.factory = factory;
    this.name = name;
    this.nameExpression = this.factory.expression(this.name);
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, ParameterContext context, int loopNestingLevel)
      throws DynamicExpressionException {
    Object v = this.nameExpression.evaluate(context, Object.class);
    Integer index = ParameterInstance.getCounterAndIncrement(this, loopNestingLevel);
    VariableInstance is = new VariableInstance(this.name, index, v);
    sc.consume("?");
    sc.consume(is);
    return true;
  }

}
