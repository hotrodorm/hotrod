package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.QueryWriter;

public class Trim extends StringFunction {

  private StringExpression string;

  public Trim(final StringExpression string) {
    super();
    this.string = string;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().trim(w, this.string);
  }

  // Apply aliases

  @Override
  public void gatherAliases(final AliasGenerator ag) {
    this.string.gatherAliases(ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.string.designateAliases(ag);
  }

}
