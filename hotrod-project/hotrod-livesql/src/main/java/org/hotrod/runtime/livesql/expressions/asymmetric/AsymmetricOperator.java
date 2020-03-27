package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public abstract class AsymmetricOperator extends Predicate {

  private Expression<?> value;
  private String operator;
  private ExecutableSelect subquery;

  protected AsymmetricOperator(final Expression<?> value, final String operator, final ExecutableSelect subquery) {
    super(Expression.PRECEDENCE_ANY_ALL_EQ_NE_LT_LE_GT_GE);
    this.value = value;
    this.operator = operator;
    this.subquery = subquery;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    super.renderInner(this.value, w);
    w.write(" " + this.operator + " (\n");
    w.enterLevel();
    this.subquery.renderTo(w);
    w.exitLevel();
    w.write("\n)");
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    this.value.validateTableReferences(tableReferences, ag);
    this.subquery.validateTableReferences(tableReferences, ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.value.designateAliases(ag);
    this.subquery.designateAliases(ag);
  }

}
