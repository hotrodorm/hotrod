package org.hotrod.runtime.dynamicsql.expressions;

import org.apache.commons.jexl3.JexlExpression;
import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;
import org.hotrod.runtime.dynamicsql.DynamicSQLParameters;
import org.hotrod.runtime.dynamicsql.EvaluationFeedback;

public class BindExpression extends DynamicSQLExpression {

  private String name;
  private JexlExpression value;

  public BindExpression(final String name, final String valueExpr) {
    this.name = name;
    this.value = JEXL_ENGINE.createExpression(valueExpr);
  }

  @Override
  public EvaluationFeedback evaluate(final StringBuilder out, final DynamicSQLParameters variables)
      throws DynamicSQLEvaluationException {
    Object v = null;
    try {
      v = this.value.evaluate(variables);
    } catch (Exception e) {
      throw new DynamicSQLEvaluationException(
          "Could not evaluate the value '" + this.value.getSourceText() + "' on the <bind> tag: " + e.getMessage());
    }
    variables.set(this.name, v);
    return new EvaluationFeedback(false);
  }

}
