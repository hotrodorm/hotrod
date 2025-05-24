package org.hotrod.livesql.expressions;

import java.util.List;

import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.typesolver.TypeHandler;
import org.hotrod.livesql.queries.typesolver.TypeSource;

public class TypedExpression extends Expression {

  private Expression expr;

  public TypedExpression(Expression expr, Class<?> type) {
    super(expr);
    super.setTypeHandler(TypeHandler.forClass(type, TypeSource.DESIGNATED_IN_LIVESQL));
    this.expr = expr;
  }

  protected String getReferenceName() {
    return this.expr.getReferenceName();
  }

  @Override
  protected void renderTo(QueryWriter w) {
    this.expr.renderTo(w);
  }

  protected List<Expression> unwrap() {
    return expr.unwrap();
  }

}
