package org.hotrod.dynamic.segments;

import org.hotrod.dynamic.DynamicExpression;
import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.ParameterContext;
import org.hotrod.utils.SUtil;

public class BindSegment extends DynamicSegment {

  private String name;
  private String value;
  private DynamicExpressionFactory factory;

  private DynamicExpression valueExpression;

  public BindSegment(String name, String value, DynamicExpressionFactory factory) {
    this.name = name == null ? null : name.trim();
    this.value = value;
    this.factory = factory;
    this.valueExpression = this.factory.expression(this.value);
  }

  private boolean validated = false;

  private synchronized void validate() throws DynamicExpressionException {
    if (!this.validated) {

      if (SUtil.isEmpty(this.name)) {
        throw new DynamicExpressionException("The 'name' property of a Dynamic BIND cannot be empty.");
      }

      if (SUtil.isEmpty(this.value)) {
        throw new DynamicExpressionException("The 'value' property of a Dynamic BIND cannot be empty.");
      }

      this.validated = true;
    }
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, ParameterContext context, int loopNestingLevel)
      throws DynamicExpressionException {

    if (!this.validated) {
      this.validate();
    }

    if (context.hasParameter(this.name)) {
      throw new DynamicExpressionException("The variable '" + this.name
          + "' defined by the 'name' property of a Dynamic SQL BIND already exists. Cannot shadow an existing variable");
    }

    Object obj = this.valueExpression.evaluate(context);
    context.bind(this.name, obj);

    return true;

  }

}
