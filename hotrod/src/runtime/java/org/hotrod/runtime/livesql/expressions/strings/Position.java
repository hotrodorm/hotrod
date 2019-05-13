package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;

public class Position extends StringFunction {

  private StringExpression substring;
  private StringExpression string;
  private NumberExpression from;

  public Position(final StringExpression substring, final StringExpression string, final NumberExpression from) {
    super();
    this.substring = substring;
    this.string = string;
    this.from = from;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().locate(w, this.substring, this.string, this.from);
  }

  // Apply aliases

  @Override
  public void gatherAliases(final AliasGenerator ag) {
    this.substring.gatherAliases(ag);
    this.string.gatherAliases(ag);
    this.from.gatherAliases(ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.substring.designateAliases(ag);
    this.string.designateAliases(ag);
    this.from.designateAliases(ag);
  }

}
