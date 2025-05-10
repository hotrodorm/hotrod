package org.hotrod.dynamicsql.segments;

import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.dynamicsql.Utl;

public class BindSegment extends ControlSegment {

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

      if (Utl.isEmpty(this.name)) {
        throw new DynamicExpressionException("The 'name' property of a Dynamic BIND cannot be empty.");
      }

      if (Utl.isEmpty(this.value)) {
        throw new DynamicExpressionException("The 'value' property of a Dynamic BIND cannot be empty.");
      }

      this.validated = true;
    }
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, Parameters context, int loopNestingLevel)
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
