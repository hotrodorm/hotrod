package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.queries.QueryWriter;

public abstract class BinaryNumericExpression<T extends ComparableExpression> extends NumericSyntaxExpression {

  private T left;
  private String operator;
  private T right;

  protected BinaryNumericExpression(final T left, final String operator, final T right, final int operatorPrecedence) {
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
  protected void renderTo(final QueryWriter w) {
    super.renderInner(this.left, w);
    w.write(" ");
    w.write(this.operator);
    w.write(" ");
    super.renderInner(this.right, w);
  }

}
