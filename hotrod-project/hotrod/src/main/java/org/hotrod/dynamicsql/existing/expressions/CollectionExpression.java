package org.hotrod.dynamicsql.existing.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hotrod.dynamicsql.existing.DynamicSQLEvaluationException;
import org.hotrod.dynamicsql.existing.DynamicSQLParameters;
import org.hotrod.dynamicsql.existing.EvaluationFeedback;

public class CollectionExpression extends OldDynamicExpression {

  private OldDynamicExpression[] expressions;

  public CollectionExpression(final OldDynamicExpression... expressions) {
    this.expressions = expressions;
  }

  @Override
  public EvaluationFeedback evaluate(final StringBuilder out, final DynamicSQLParameters variables)
      throws DynamicSQLEvaluationException {
    boolean contentRendered = false;
    for (OldDynamicExpression expr : this.expressions) {
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
