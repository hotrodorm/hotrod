package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.Expression;

public class GroupConcat extends StringAggregationFunction {

  private Expression<String> delimiter;

  public GroupConcat(final Expression<String> expression, final Expression<String> delimiter) {
    super("group_concat", expression);
    this.delimiter = delimiter;

  }

  @Override
  public void renderTo(final QueryWriter w) {

    super.renderHead(w);

    if (this.delimiter != null) {
      w.write(", ");
      this.delimiter.renderTo(w);
    }

    super.renderTail(w);

  }

}
