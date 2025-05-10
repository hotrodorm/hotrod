package org.hotrod.dynamicsql.segments;

import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;

public class ParameterInjectionSegment extends DynamicContentSegment {

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
  public boolean prepare(StaticSegmentConsumer sc, Parameters context, int loopNestingLevel)
      throws DynamicExpressionException {
    String v = this.nameExpression.evaluate(context, String.class);
    if (v != null) {
      sc.consume(v);
    }
    return true;
  }

}
