package org.hotrod.runtime.dynamicsql.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;
import org.hotrod.runtime.dynamicsql.DynamicSQLParameters;
import org.hotrod.runtime.dynamicsql.EvaluationFeedback;

public class ChooseExpression extends DynamicExpression {

  private OtherwiseExpression otherwise;

  private WhenExpression[] whens;

  public ChooseExpression(final OtherwiseExpression otherwise, final WhenExpression... whens) {
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

  @Override
  public List<Object> getConstructorParameters() {
    List<Object> params = new ArrayList<Object>();
    params.add(this.otherwise);
    params.addAll(Arrays.asList(this.whens));
    return params;
  }

}
