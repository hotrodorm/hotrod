package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.AbstractSelect.TableReferencesValidator;
import org.hotrod.runtime.livesql.QueryWriter;

public class CurrentTime extends DateTimeFunction {

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().currentTime(w);
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferencesValidator tableReferences, final AliasGenerator ag) {
    // nothing to do
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    // nothing to do
  }

}
