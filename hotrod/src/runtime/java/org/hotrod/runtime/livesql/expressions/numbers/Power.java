package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;

public class Power extends NumericFunction {

  private Expression<Number> value;
  private Expression<Number> exponent;

  public Power(final Expression<Number> value, final Expression<Number> exponent) {
    super();
    this.value = value;
    this.exponent = exponent;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().power(w, this.value, this.exponent);
  }

  // Apply aliases

  @Override
  public void gatherAliases(final AliasGenerator ag) {
    this.value.gatherAliases(ag);
    this.exponent.gatherAliases(ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.value.designateAliases(ag);
    this.exponent.designateAliases(ag);
  }

}
