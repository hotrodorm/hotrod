package org.hotrod.dynamic.segments;

import java.util.logging.Logger;

import org.hotrod.dynamic.DynamicExpression;
import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.ParameterContext;

public class ParameterInjectionSegment extends ParameterSegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(ParameterInjectionSegment.class.getName());

  private DynamicExpressionFactory factory;
  private String name;

  private DynamicExpression nameExpression;

  public ParameterInjectionSegment(DynamicExpressionFactory factory, String name) {
    this.factory = factory;
    this.name = name;
    this.nameExpression = this.factory.expression(this.name);
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, ParameterContext context, int loopNestingLevel)
      throws DynamicExpressionException {
    String v = this.nameExpression.evaluate(context, String.class);
    sc.consume(v);
    return true;
  }

  public String getName() {
    return name;
  }

  public int getSQLType() {
    return 0;
  }

  public Object getValue() {
    return null;
  }

}
