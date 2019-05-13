package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;

public class Log extends NumericFunction {

  private Expression<Number> value;
  private Expression<Number> base;

  public Log(final Expression<Number> value, final Expression<Number> base) {
    super();
    this.value = value;
    this.base = base;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().logarithm(w, this.value, this.base);
  }

  // Apply aliases

  @Override
  public void gatherAliases(final AliasGenerator ag) {
    this.value.gatherAliases(ag);
    this.base.gatherAliases(ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.value.designateAliases(ag);
    this.base.designateAliases(ag);
  }

}
