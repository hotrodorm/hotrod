package org.hotrod.runtime.dynamicsql.expressions;

import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;
import org.hotrod.runtime.dynamicsql.DynamicSQLParameters;
import org.hotrod.runtime.dynamicsql.EvaluationFeedback;

public class CollectionExpression extends DynamicSQLExpression {

  private DynamicSQLExpression[] expressions;

  public CollectionExpression(final DynamicSQLExpression... expressions) {
    this.expressions = expressions;
  }

  @Override
  public EvaluationFeedback evaluate(final StringBuilder out, final DynamicSQLParameters variables)
      throws DynamicSQLEvaluationException {
    boolean contentRendered = false;
    for (DynamicSQLExpression expr : this.expressions) {
      EvaluationFeedback feedback = expr.evaluate(out, variables);
      contentRendered = contentRendered || feedback.wasContentRendered();
    }
    return new EvaluationFeedback(contentRendered);
  }

}
