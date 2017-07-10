package org.hotrod.runtime.dynamicsql.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;
import org.hotrod.runtime.dynamicsql.DynamicSQLParameters;
import org.hotrod.runtime.dynamicsql.EvaluationFeedback;

public class CollectionExpression extends DynamicExpression {

  private DynamicExpression[] expressions;

  public CollectionExpression(final DynamicExpression... expressions) {
    this.expressions = expressions;
  }

  @Override
  public EvaluationFeedback evaluate(final StringBuilder out, final DynamicSQLParameters variables)
      throws DynamicSQLEvaluationException {
    boolean contentRendered = false;
    for (DynamicExpression expr : this.expressions) {
      EvaluationFeedback feedback = expr.evaluate(out, variables);
      contentRendered = contentRendered || feedback.wasContentRendered();
    }
    return new EvaluationFeedback(contentRendered);
  }

  @Override
  public List<Object> getConstructorParameters() {
    List<Object> params = new ArrayList<Object>();
    params.addAll(Arrays.asList(this.expressions));
    return params;
  }

}
