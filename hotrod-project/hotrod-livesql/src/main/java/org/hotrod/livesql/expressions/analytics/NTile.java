package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.numeric.NumericFunction;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.util.BoxUtil;

public class NTile extends NumericFunction implements AnalyticFunction {

  private NumericExpression expression;

  public NTile(final NumericExpression expression) {
    super(expression);
    this.expression = expression;
  }

  public NTile(final Number tiles) {
    super(BoxUtil.box(tiles));
    this.expression = BoxUtil.box(tiles);
  }

  public NumericWindowFunctionOverStage over() {
    return new NumericWindowFunctionOverStage(new NumericWindowExpression(this));
  }

  @Override
  protected void renderTo(QueryWriter w) {
    w.write("ntile(");
    w.write(this.expression);
    w.write(")");
  }

}
