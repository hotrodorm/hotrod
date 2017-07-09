package org.hotrod.runtime.dynamicsql.expressions;

import org.apache.commons.jexl3.JexlExpression;
import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;
import org.hotrod.runtime.dynamicsql.DynamicSQLParameters;
import org.hotrod.runtime.dynamicsql.EvaluationFeedback;

public class IfExpression extends DynamicSQLExpression {

  private JexlExpression condition;

  private DynamicSQLExpression[] expressions;

  public IfExpression(final String condition, final DynamicSQLExpression... expressions) {
    this.condition = JEXL_ENGINE.createExpression(condition);
    this.expressions = expressions;
  }

  @Override
  public EvaluationFeedback evaluate(final StringBuilder out, final DynamicSQLParameters variables)
      throws DynamicSQLEvaluationException {
    Object obj = null;
    try {
      obj = this.condition.evaluate(variables);
    } catch (Exception e) {
      throw new DynamicSQLEvaluationException("Could not evaluate the condition '" + this.condition.getSourceText()
          + "' on the <if> tag: " + e.getMessage());
    }
    if (obj == null) {
      throw new DynamicSQLEvaluationException("The condition of the <if> tag '" + this.condition.getSourceText()
          + "' evaluated to null, but must evaluate to either true or false.");
    }
    try {
      Boolean cond = (Boolean) obj;
      if (cond) {
        for (DynamicSQLExpression expr : this.expressions) {
          expr.evaluate(out, variables);
        }
      }
      return new EvaluationFeedback(cond);

    } catch (ClassCastException e) {
      throw new DynamicSQLEvaluationException("The condition on the <if> tag '" + this.condition.getSourceText()
          + "' must evaluate to a boolean value, but resulted in an object of class '" + obj.getClass() + "'.");
    }
  }

}
