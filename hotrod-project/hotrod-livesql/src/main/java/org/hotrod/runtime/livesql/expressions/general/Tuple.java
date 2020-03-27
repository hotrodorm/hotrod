package org.hotrod.runtime.livesql.expressions.general;

import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.livesql.exceptions.InvalidLiveSQLClauseException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrodorm.hotrod.utils.Separator;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Tuple extends Expression<Tuple> {

  private List<Expression<?>> expressions;

  public Tuple(final Expression<?>... expressions) {
    super(Expression.PRECEDENCE_TUPLE);
    if (expressions == null || expressions.length == 0) {
      throw new InvalidLiveSQLClauseException("A tuple cannot be empty. Please add expressions to the tuple");
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

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    for (Expression<?> e : this.expressions) {
      e.validateTableReferences(tableReferences, ag);
    }
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    for (Expression<?> e : this.expressions) {
      e.designateAliases(ag);
    }
  }

}
