package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;

public abstract class BinaryPredicate extends Predicate {

  private Expression<?> left;
  private String operator;
  private Expression<?> right;

  protected <T> BinaryPredicate(final Expression<T> left, final String operator, final Expression<T> right,
      final int operatorPrecedence) {
    super(operatorPrecedence);
    if (operator == null || operator.trim().isEmpty()) {
      throw new IllegalArgumentException("Operator must be specified");
    }
    this.operator = operator;
    if (left == null) {
      throw new IllegalArgumentException("Left argument of the binary operator (" + this.operator + ") cannot be null");
    }
    this.left = left;
    if (right == null) {
      throw new IllegalArgumentException(
          "Right argument of the binary operator (" + this.operator + ") cannot be null");
    }
    this.right = right;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    super.renderInner(this.left, w);
    w.write(" ");
    w.write(this.operator);
    w.write(" ");
    super.renderInner(this.right, w);
  }

  // Apply aliases

  @Override
  public void gatherAliases(final AliasGenerator ag) {
    this.left.gatherAliases(ag);
    this.right.gatherAliases(ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.left.designateAliases(ag);
    this.right.designateAliases(ag);
  }

}
