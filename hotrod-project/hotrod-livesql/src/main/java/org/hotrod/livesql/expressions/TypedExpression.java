package org.hotrod.livesql.expressions;

import java.util.List;

import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.typesolver.TypeHandler;
import org.hotrod.livesql.queries.typesolver.TypeSource;

public class TypedExpression extends Expression {

  private Expression expr;

  public TypedExpression(Expression expr, Class<?> type) {
    super(expr);
    TypeHandler<?, ?> th = TypeHandler.forClass(type, TypeSource.RUNTIME_DESIGNATED);
    super.setTypeHandler(th);
    this.typeHandler = th;
    this.expr = expr;
  }

  @Override
  protected Expression getEmergingExpression() {
    return this.expr.getEmergingExpression();
  }

  @Override
  protected String getReferenceName() {
    return this.expr.getReferenceName();
  }

  @Override
  protected void renderTo(QueryWriter w) {
    this.expr.renderTo(w);
  }

  @Override
  protected List<Expression> expand() {
    return expr.expand();
  }

}
