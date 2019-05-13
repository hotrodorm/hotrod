package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.QueryWriter;

public class CurrentDate extends DateTimeFunction {

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().currentDate(w);
  }

  // Apply aliases

  @Override
  public void gatherAliases(final AliasGenerator ag) {
    // nothing to do
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    // nothing to do
  }

}
