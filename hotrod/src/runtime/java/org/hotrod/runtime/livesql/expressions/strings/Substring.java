package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.AbstractSelect.TableReferencesValidator;
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

  // Validation

  @Override
  public void validateTableReferences(final TableReferencesValidator tableReferences, final AliasGenerator ag) {
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
