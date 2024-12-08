package org.hotrod.dynamicsql.existing.expressions;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamicsql.existing.DynamicSQLEvaluationException;
import org.hotrod.dynamicsql.existing.DynamicSQLParameters;
import org.hotrod.dynamicsql.existing.EvaluationFeedback;

public class VariableExpression extends OldDynamicExpression {

  private String name;

  public VariableExpression(final String name) {
    this.name = name;
  }

  @Override
  public EvaluationFeedback evaluate(final StringBuilder out, final DynamicSQLParameters variables)
      throws DynamicSQLEvaluationException {
    out.append(variables.get(name));
    return new EvaluationFeedback(true);
  }

  @Override
  public List<Object> getConstructorParameters() {
    List<Object> params = new ArrayList<Object>();
    List<String> stringParams = new ArrayList<String>();
    stringParams.add(this.name);
    params.add(stringParams);
    return params;
  }

}
