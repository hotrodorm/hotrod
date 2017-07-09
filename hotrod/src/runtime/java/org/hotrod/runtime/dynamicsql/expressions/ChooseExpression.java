package org.hotrod.runtime.dynamicsql.expressions;

import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;
import org.hotrod.runtime.dynamicsql.DynamicSQLParameters;
import org.hotrod.runtime.dynamicsql.EvaluationFeedback;

public class ChooseExpression extends DynamicSQLExpression {

  private CollectionExpression otherwise;

  private WhenExpression[] whens;

  public ChooseExpression(final CollectionExpression otherwise, final WhenExpression... whens) {
    this.otherwise = otherwise;
    this.whens = whens;
  }

  @Override
  public EvaluationFeedback evaluate(final StringBuilder out, final DynamicSQLParameters variables)
      throws DynamicSQLEvaluationException {

    for (WhenExpression w : this.whens) {
      EvaluationFeedback feedback = w.evaluate(out, variables);
      if (feedback.wasContentRendered()) {
        return new EvaluationFeedback(true);
      }
    }

    if (this.otherwise != null) {
      this.otherwise.evaluate(out, variables);
    }

    return new EvaluationFeedback(this.otherwise != null);
  }

}
