package org.hotrod.runtime.dynamicsql.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.jexl3.JexlExpression;
import org.apache.log4j.Logger;
import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;
import org.hotrod.runtime.dynamicsql.DynamicSQLParameters;
import org.hotrod.runtime.dynamicsql.EvaluationFeedback;

public class IfExpression extends DynamicExpression {

  private static final Logger log = Logger.getLogger(IfExpression.class);

  private JexlExpression condition;

  private DynamicExpression[] expressions;

  public IfExpression(final String condition, final DynamicExpression... expressions) {
    log.debug("init");
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
        for (DynamicExpression expr : this.expressions) {
          expr.evaluate(out, variables);
        }
      }
      return new EvaluationFeedback(cond);

    } catch (ClassCastException e) {
      throw new DynamicSQLEvaluationException("The condition on the <if> tag '" + this.condition.getSourceText()
          + "' must evaluate to a boolean value, but resulted in an object of class '" + obj.getClass() + "'.");
    }
  }

  @Override
  public List<Object> getConstructorParameters() {
    List<Object> params = new ArrayList<Object>();
    List<String> stringParams = new ArrayList<String>();
    stringParams.add(this.condition.getSourceText());
    params.add(stringParams);
    params.addAll(Arrays.asList(this.expressions));
    return params;
  }

}
