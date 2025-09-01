package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.QueryWriter;

public class Count extends NonWindowableAggregationFunction<Number> {

  public Count() {
    super("count", null, null);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    renderHead(w);
    w.write("*");
    renderTail(w);
  }

}
