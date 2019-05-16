package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;

public class Locate extends NumberExpression {

  private Expression<String> substring;
  private Expression<String> string;
  private Expression<Number> from;

  public Locate(final Expression<String> substring, final Expression<String> string, final Expression<Number> from) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.substring = substring;
    this.string = string;
    this.from = from;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().locate(w, this.substring, this.string, this.from);
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    this.substring.validateTableReferences(tableReferences, ag);
    this.string.validateTableReferences(tableReferences, ag);
    this.from.validateTableReferences(tableReferences, ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.substring.designateAliases(ag);
    this.string.designateAliases(ag);
    this.from.designateAliases(ag);
  }

}
