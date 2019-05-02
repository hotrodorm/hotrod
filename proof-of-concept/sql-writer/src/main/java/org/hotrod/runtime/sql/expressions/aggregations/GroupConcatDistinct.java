package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.Expression;

public class GroupConcatDistinct extends NonWindowableAggregationFunction<String> {

  private Expression<String> delimiter;

  public GroupConcatDistinct(final Expression<String> expression, final Expression<String> delimiter) {
    super("group_concat", "distinct", expression);
    this.delimiter = delimiter;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    renderHead(w);
    if (this.delimiter != null) {
      w.write(", ");
      this.delimiter.renderTo(w);
    }
    renderTail(w);
  }

}
