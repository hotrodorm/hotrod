package org.hotrod.dynamicsql.existing.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.jexl3.JexlException;
import org.apache.commons.jexl3.JexlExpression;
import org.hotrod.dynamicsql.existing.DynamicSQLEvaluationException;
import org.hotrod.dynamicsql.existing.DynamicSQLParameters;
import org.hotrod.dynamicsql.existing.EvaluationFeedback;
import org.hotrod.exceptions.InvalidJexlExpressionException;

public class WhenExpression extends OldDynamicExpression {

  private JexlExpression test;

  private OldDynamicExpression[] expressions;

  public WhenExpression(final String test, final OldDynamicExpression... expressions) {
    try {
      this.test = OLD_JEXL_ENGINE.createExpression(test);
    } catch (JexlException e) {
      throw new InvalidJexlExpressionException("Invalid test expression: " + test + " (" + e.getMessage() + ")");
    }

    this.expressions = expressions;
  }

  @Override
  public EvaluationFeedback evaluate(final StringBuilder out, final DynamicSQLParameters variables)
      throws DynamicSQLEvaluationException {
    Object obj = null;
    try {
      obj = this.test.evaluate(variables);
    } catch (Exception e) {
      throw new DynamicSQLEvaluationException("Could not evaluate the test condition '" + this.test.getSourceText()
          + "' on the <when> tag: " + e.getMessage());
    }
    if (obj == null) {
      throw new DynamicSQLEvaluationException("The test condition of the <when> tag '" + this.test.getSourceText()
          + "' evaluated to null, but must evaluate to either true or false.");
    }
    try {
      Boolean cond = (Boolean) obj;
      if (cond) {
        for (OldDynamicExpression expr : this.expressions) {
          expr.evaluate(out, variables);
        }
      }
      return new EvaluationFeedback(cond);

    } catch (ClassCastException e) {
      throw new DynamicSQLEvaluationException("The test condition on the <when> tag '" + this.test.getSourceText()
          + "' must evaluate to a boolean value, but resulted in an object of class '" + obj.getClass() + "'.");
    }
  }

  @Override
  public List<Object> getConstructorParameters() {
    List<Object> params = new ArrayList<Object>();
    List<String> stringParams = new ArrayList<String>();
    stringParams.add(this.test.getSourceText());
    params.add(stringParams);
    params.addAll(Arrays.asList(this.expressions));
    return params;
  }

}
