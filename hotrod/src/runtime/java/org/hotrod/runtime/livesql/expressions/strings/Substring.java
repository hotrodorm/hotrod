package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;

public class Substring extends StringFunction {

  private StringExpression string;
  private NumberExpression from;
  private NumberExpression length;

  public Substring(final StringExpression string, final NumberExpression from, final NumberExpression length) {
    super();
    this.string = string;
    this.from = from;
    this.length = length;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().substr(w, this.string, this.from, this.length);
  }

  // Apply aliases

  @Override
  public void gatherAliases(final AliasGenerator ag) {
    this.string.gatherAliases(ag);
    this.from.gatherAliases(ag);
    this.length.gatherAliases(ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.string.designateAliases(ag);
    this.from.designateAliases(ag);
    this.length.designateAliases(ag);
  }

}
