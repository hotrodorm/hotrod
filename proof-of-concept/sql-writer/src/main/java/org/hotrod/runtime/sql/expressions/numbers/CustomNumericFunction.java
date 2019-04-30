package org.hotrod.runtime.sql.expressions.numbers;

import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.sql.QueryWriter;

import sql.util.Separator;

public abstract class CustomNumericFunction extends NumberExpression {

  private static final int PRECEDENCE = 1;

  private List<NumberExpression> parameters;

  protected CustomNumericFunction(final NumberExpression... parameters) {
    super(PRECEDENCE);
    this.parameters = Arrays.asList(parameters);
  }

  protected abstract String getName(final QueryWriter w);

  @Override
  public void renderTo(final QueryWriter w) {
    w.write(this.getName(w));
    w.write("(");
    Separator s = new Separator();
    for (NumberExpression p : this.parameters) {
      w.write(s.render());
      p.renderTo(w);
    }
    w.write(")");
  }

}
