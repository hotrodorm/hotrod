package sql.predicates;

import sql.QueryWriter;

public abstract class BinaryBooleanOperator extends Predicate {

  private Expression left;
  private String operator;
  private Expression right;

  protected BinaryBooleanOperator(final Expression left, final String operator, final Expression right,
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
  public void renderTo(final QueryWriter pq) {
    super.renderInner(this.left, pq);
    pq.write(" ");
    pq.write(this.operator);
    pq.write(" ");
    super.renderInner(this.right, pq);
  }

}
