package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;

public abstract class NumericAggregationFunction extends WindowableAggregationFunction<Number> {

  private static final int PRECEDENCE = 2;

  private String functionName;
  private Expression<Number> expression;

  protected NumericAggregationFunction(final String functionName, final Expression<Number> expression) {
    super(PRECEDENCE);
    this.functionName = functionName;
    this.expression = expression;
  }

  @Override
  public final void renderTo(final QueryWriter w) {

    w.write(this.functionName);

    w.write("(");

    if (this.expression != null) {
      super.renderInner(this.expression, w);
    }

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
