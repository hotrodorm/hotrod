package sql.expressions.aggregations;

import java.util.List;

import sql.QueryWriter;
import sql.expressions.Expression;

public abstract class AggregationFunction extends Expression {

  private static final int PRECEDENCE = 2;

  private String function;
  private String decorator;
  private List<Expression> expressions;

  protected AggregationFunction(final String function, final String decorator, final List<Expression> expressions) {
    super(PRECEDENCE);
    this.function = function;
    this.decorator = decorator;
    this.expressions = expressions;
  }

  @Override
  public final void renderTo(final QueryWriter w) {

    w.write(this.function);

    w.write("(");

    if (this.decorator != null) {
      w.write(this.decorator);
      w.write(" ");
    }

    boolean first = true;
    for (Expression expr : this.expressions) {
      if (first) {
        first = false;
      } else {
        w.write(", ");
      }
      super.renderInner(expr, w);
    }

    w.write(")");

  }

  // Literal

  static class LiteralString extends Expression {

    private static final int PRECEDENCE = 1;

    private String verbatim;

    protected LiteralString(final String verbatim) {
      super(PRECEDENCE);
      this.verbatim = verbatim;
    }

    @Override
    public void renderTo(final QueryWriter w) {
      w.write(this.verbatim);
    }

  }

}
