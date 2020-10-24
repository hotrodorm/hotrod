package org.hotrod.runtime.livesql.expressions.aggregations;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.analytics.StringWindowFunctionOverStage;
import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression;
import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringFunction;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class GroupConcat extends StringFunction implements WindowableAggregationFunction {

  private List<OrderingTerm> ordering;
  private StringExpression separator;
  private StringExpression expression;

  public GroupConcat(final StringExpression expression, final List<OrderingTerm> ordering,
      final StringExpression separator) {
    super("group_concat", expression);
    this.ordering = ordering;
    this.separator = separator;
  }

  public StringWindowFunctionOverStage over() {
    return new StringWindowFunctionOverStage(new WindowExpression(this));
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().groupConcat(w, false, this.expression, this.ordering, this.separator);
  }

}
