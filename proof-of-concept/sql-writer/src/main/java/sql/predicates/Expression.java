package sql.predicates;

import sql.QueryWriter;

public abstract class Expression {

  /**
   * <pre>
   * Precedence  Operator
   * ----------  ------------------
   *         11  and
   *          6  between
   *         --  binary operator
   *          6  =
   *          6  >
   *          6  >=
   *          6  is null
   *          6  is not null
   *          6  <
   *          6  <=
   *          6  like
   *          2  not
   *          6  not between
   *          6  <>
   *          6  not like
   *          --  operand
   *          12  or
   *          1  parenthesis
   *          --  expression
   *          20  value
   * </pre>
   */

  private int precedence;

  // Constructor

  protected Expression(final int precedence) {
    this.precedence = precedence;
  }

  // Getters

  protected int getPrecedence() {
    return precedence;
  }

  // Rendering

  protected void renderInner(final Expression inner, final QueryWriter pq) {
    boolean parenthesis = inner.getPrecedence() >= this.precedence;
    if (parenthesis) {
      pq.write("(");
    }
    inner.renderTo(pq);
    if (parenthesis) {
      pq.write(")");
    }
  }

  public abstract void renderTo(final QueryWriter pq);

}
