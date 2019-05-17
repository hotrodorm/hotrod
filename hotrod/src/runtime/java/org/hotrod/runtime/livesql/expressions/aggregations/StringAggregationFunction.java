package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public abstract class StringAggregationFunction extends WindowableAggregationFunction<String> {

  private String functionName;
  protected Expression<String> expression;

  protected StringAggregationFunction(final String functionName, final Expression<String> expression) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.functionName = functionName;
    this.expression = expression;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    renderHead(w);
    renderTail(w);
  }

  protected void renderTail(final QueryWriter w) {
    w.write(")");
  }

  protected void renderHead(final QueryWriter w) {
    w.write(this.functionName);
    w.write("(");
    this.expression.renderTo(w);
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    this.expression.validateTableReferences(tableReferences, ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.expression.designateAliases(ag);
  }

}
