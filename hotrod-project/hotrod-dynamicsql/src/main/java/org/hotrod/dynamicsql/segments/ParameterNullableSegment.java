package org.hotrod.dynamicsql.segments;

import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.dynamicsql.parameters.ParameterInstance;
import org.hotrod.dynamicsql.parameters.ParameterNullableInstance;

public class ParameterNullableSegment extends DynamicContentSegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(ParameterNullableSegment.class.getName());

  private DynamicExpressionFactory factory;
  private String name;
  private int sqlType;

  private DynamicExpression nameExpression;

  public ParameterNullableSegment(DynamicExpressionFactory factory, String name, int sqlType) {
    this.factory = factory;
    this.name = name;
    this.sqlType = sqlType;
    this.nameExpression = this.factory.expression(this.name);
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, Parameters context, int loopNestingLevel)
      throws DynamicExpressionException {
    Object v = this.nameExpression.evaluate(context, Object.class);
    Integer index = ParameterInstance.getCounterAndIncrement(this, loopNestingLevel);
    ParameterNullableInstance is = new ParameterNullableInstance(this.sqlType, this.name, index, v);
    sc.consume("?");
    sc.consume(is);
    return true;
  }

}
