package org.hotrod.runtime.dynamicsql.expressions;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;
import org.hotrod.runtime.dynamicsql.DynamicSQLParameters;
import org.hotrod.runtime.dynamicsql.EvaluationFeedback;
import org.hotrod.runtime.util.SUtils;

public class LiteralExpression extends DynamicExpression {

  // Properties

  private String verbatim;

  private boolean isBlanks;

  // Constructor

  public LiteralExpression(final String verbatim) {
    this.verbatim = verbatim;
    this.isBlanks = SUtils.isEmpty(this.verbatim);
  }

  // Behavior

  @Override
  public EvaluationFeedback evaluate(final StringBuilder out, final DynamicSQLParameters variables)
      throws DynamicSQLEvaluationException {
    out.append(this.verbatim);
    return new EvaluationFeedback(!this.isBlanks);
  }

  @Override
  public List<Object> getConstructorParameters() {
    List<Object> params = new ArrayList<Object>();
    List<String> stringParams = new ArrayList<String>();
    stringParams.add(this.verbatim);
    params.add(stringParams);
    return params;
  }

  public LiteralExpression concat(final LiteralExpression other) {
    return new LiteralExpression(this.verbatim + other.verbatim);
  }

}
