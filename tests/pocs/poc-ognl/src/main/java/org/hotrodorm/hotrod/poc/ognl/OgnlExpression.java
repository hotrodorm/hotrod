package org.hotrodorm.hotrod.poc.ognl;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

public class OgnlExpression {

  private Object ognlExpression;

  public OgnlExpression(final String expression) throws OgnlException {
    this.ognlExpression = Ognl.parseExpression(expression);
  }

  public Object getValue(final OgnlContext context, final Object rootObject) throws OgnlException {
    return Ognl.getValue(this.ognlExpression, context, rootObject);
  }

  public void setValue(final OgnlContext context, final Object rootObject, final Object value) throws OgnlException {
    Ognl.setValue(this.ognlExpression, context, rootObject, value);
  }

}
