package org.hotrod.runtime.livesql.expressions.aggregations;

import java.util.Date;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public abstract class DateTimeAggregationFunction extends WindowableAggregationFunction<Date> {

  private String functionName;
  private Expression<Date> expression;

  protected DateTimeAggregationFunction(final String functionName, final Expression<Date> expression) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.functionName = functionName;
    this.expression = expression;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    renderHead(w);
    renderTail(w);
  }

  private void renderHead(final QueryWriter w) {
    w.write(this.functionName);
    w.write("(");
    if (this.expression != null) {
      this.expression.renderTo(w);
    }
  }

  private void renderTail(final QueryWriter w) {
    w.write(")");
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