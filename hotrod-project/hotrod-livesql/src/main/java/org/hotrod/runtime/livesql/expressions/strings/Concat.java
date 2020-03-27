package org.hotrod.runtime.livesql.expressions.strings;

import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Concat extends StringFunction {

  private List<Expression<String>> strings;

  public Concat(final List<Expression<String>> strings) {
    super();
    this.strings = strings;
  }

  @SafeVarargs
  public Concat(final Expression<String>... a) {
    super();
    this.strings = Arrays.asList(a);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().concat(w, this.strings);
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    for (Expression<String> e : this.strings) {
      e.validateTableReferences(tableReferences, ag);
    }
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    for (Expression<String> e : this.strings) {
      e.designateAliases(ag);
    }
  }

}
