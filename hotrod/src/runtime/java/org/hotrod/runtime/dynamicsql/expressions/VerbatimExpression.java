package org.hotrod.runtime.dynamicsql.expressions;

import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;
import org.hotrod.runtime.dynamicsql.DynamicSQLParameters;
import org.hotrod.runtime.dynamicsql.EvaluationFeedback;

public class VerbatimExpression extends DynamicSQLExpression {

  private String verbatim;

  public VerbatimExpression(final String verbatim) {
    this.verbatim = verbatim;
  }

  @Override
  public EvaluationFeedback evaluate(final StringBuilder out, final DynamicSQLParameters variables)
      throws DynamicSQLEvaluationException {
    out.append(this.verbatim);
    return new EvaluationFeedback(true);
  }

}
