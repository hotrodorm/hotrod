package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public abstract class BinaryNumberExpression<T extends Expression> extends NumberExpression {

  private T left;
  private String operator;
  private T right;

  protected BinaryNumberExpression(final T left, final String operator, final T right, final int operatorPrecedence) {
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

    super.register(this.left);
    super.register(this.right);

  }

  @Override
  public void renderTo(final QueryWriter w) {
    super.renderInner(this.left, w);
    w.write(" ");
    w.write(this.operator);
    w.write(" ");
    super.renderInner(this.right, w);
  }

}
