package org.hotrod.livesql.expressions.general;

import java.util.Arrays;
import java.util.List;

import org.hotrod.livesql.exceptions.InvalidLiveSQLClauseException;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.utils.Separator;

public class TupleExpression extends ComparableExpression {

  private List<ComparableExpression> expressions;

  public TupleExpression(final ComparableExpression... expressions) {
    super(Expression.PRECEDENCE_TUPLE);
    if (expressions == null || expressions.length == 0) {
      throw new InvalidLiveSQLClauseException("A tuple cannot be empty. Please add expressions to the tuple");
    }
    this.expressions = Arrays.asList(expressions);
    this.expressions.forEach(e -> super.register(e));
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.write("(");
    Separator s = new Separator();
    this.expressions.forEach(e -> {
      w.write(s.render());
      Shield.renderTo(e, w);
    });
    w.write(")");
  }

}
