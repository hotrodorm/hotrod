package org.hotrod.dynamicsql.existing.expressions;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamicsql.existing.DynamicSQLEvaluationException;
import org.hotrod.dynamicsql.existing.DynamicSQLParameters;
import org.hotrod.dynamicsql.existing.EvaluationFeedback;
import org.hotrod.utils.SUtil;

public class LiteralExpression extends OldDynamicExpression {

  // Properties

  private String verbatim;

  private boolean isBlanks;

  // Constructor

  public LiteralExpression(final String verbatim) {
    this.verbatim = verbatim;
    this.isBlanks = SUtil.isEmpty(this.verbatim);
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
