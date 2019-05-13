package org.hotrod.runtime.livesql.expressions;

import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.exceptions.InvalidSQLClauseException;
import org.hotrod.runtime.livesql.util.Separator;

public class Tuple extends Expression<Tuple> {

  private static final int PRECEDENCE = 2;

  private List<Expression<?>> expressions;

  public Tuple(final Expression<?>... expressions) {
    super(PRECEDENCE);
    if (expressions == null || expressions.length == 0) {
      throw new InvalidSQLClauseException("A tuple cannot be empty. Please add expressions to the tuple");
    }
    this.expressions = Arrays.asList(expressions);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.write("(");
    Separator s = new Separator();
    for (Expression<?> expr : this.expressions) {
      w.write(s.render());
      expr.renderTo(w);
    }
    w.write(")");
  }

  // Apply aliases

  @Override
  public void gatherAliases(final AliasGenerator ag) {
    for (Expression<?> e : this.expressions) {
      e.gatherAliases(ag);
    }
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    for (Expression<?> e : this.expressions) {
      e.designateAliases(ag);
    }
  }

}
