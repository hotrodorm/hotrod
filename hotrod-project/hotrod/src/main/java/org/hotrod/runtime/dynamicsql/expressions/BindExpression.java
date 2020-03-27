package org.hotrod.runtime.dynamicsql.expressions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jexl3.JexlException;
import org.apache.commons.jexl3.JexlExpression;
import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;
import org.hotrod.runtime.dynamicsql.DynamicSQLParameters;
import org.hotrod.runtime.dynamicsql.EvaluationFeedback;
import org.hotrod.runtime.exceptions.InvalidJexlExpressionException;

public class BindExpression extends DynamicExpression {

  private String name;
  private JexlExpression value;

  public BindExpression(final String name, final String valueDefinition) {
    this.name = name;
    try {
      this.value = JEXL_ENGINE.createExpression(valueDefinition);
    } catch (JexlException e) {
      throw new InvalidJexlExpressionException(
          "Invalid expression on attribute value: " + valueDefinition + " (" + e.getMessage() + ")");
    }
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

  @Override
  public List<Object> getConstructorParameters() {
    List<Object> params = new ArrayList<Object>();
    List<String> stringParams = new ArrayList<String>();
    stringParams.add(this.name);
    stringParams.add(this.value.getSourceText());
    params.add(stringParams);
    return params;
  }

}
