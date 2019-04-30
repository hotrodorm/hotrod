package org.hotrod.runtime.sql.expressions.numbers;

import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.sql.QueryWriter;

import sql.util.Separator;

public abstract class NumericFunction extends NumberExpression {

  private static final int PRECEDENCE = 1;

  private String name;
  private List<NumberExpression> parameters;

  protected NumericFunction(final String name, final NumberExpression... parameters) {
    super(PRECEDENCE);
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Function name must be specified");
    }
    this.name = name;
    this.parameters = Arrays.asList(parameters);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.write(this.name);
    w.write("(");
    Separator s = new Separator();
    for (NumberExpression p : this.parameters) {
      w.write(s.render());
      p.renderTo(w);
    }
    w.write(")");
  }

}
