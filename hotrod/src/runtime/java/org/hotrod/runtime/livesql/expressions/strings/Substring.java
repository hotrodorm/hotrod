package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;

public class Substring extends StringFunction {

  private Expression<String> string;
  private Expression<Number> from;
  private Expression<Number> length;

  public Substring(final Expression<String> string, final Expression<Number> from, final Expression<Number> length) {
    super();
    this.string = string;
    this.from = from;
    this.length = length;
  }

  public Substring(final Expression<String> string, final Expression<Number> from) {
    super();
    this.string = string;
    this.from = from;
    this.length = null;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().substr(w, this.string, this.from, this.length);
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    this.string.validateTableReferences(tableReferences, ag);
    this.from.validateTableReferences(tableReferences, ag);
    this.length.validateTableReferences(tableReferences, ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.string.designateAliases(ag);
    this.from.designateAliases(ag);
    this.length.designateAliases(ag);
  }

}
