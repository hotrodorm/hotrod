package org.hotrod.runtime.livesql.expressions.general;

import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.livesql.exceptions.InvalidLiveSQLClauseException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrodorm.hotrod.utils.Separator;

public class Tuple extends Expression<Tuple> {

  private List<Expression<?>> expressions;

  public Tuple(final Expression<?>... expressions) {
    super(Expression.PRECEDENCE_TUPLE);
    if (expressions == null || expressions.length == 0) {
      throw new InvalidLiveSQLClauseException("A tuple cannot be empty. Please add expressions to the tuple");
    }
    this.expressions = Arrays.asList(expressions);
    this.expressions.forEach(e -> super.register(e));
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

}
