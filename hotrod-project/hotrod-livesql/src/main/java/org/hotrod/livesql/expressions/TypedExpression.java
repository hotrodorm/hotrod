package org.hotrod.livesql.expressions;

import java.util.List;

import org.hotrod.converter.TypeConverter;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.typesolver.TypeHandler;
import org.hotrod.livesql.queries.typesolver.TypeSource;

public class TypedExpression extends Expression {

  private Expression referencedExpression;
  protected TypeHandler<?, ?> typeHandler;

  public TypedExpression(Expression expr, Class<?> type) {
    super(expr.getPrecedence());
    TypeHandler<?, ?> th = TypeHandler.forClass(type, TypeSource.RUNTIME_DESIGNATED, null);
    this.typeHandler = th;
    this.referencedExpression = expr;
  }

  public TypedExpression(Expression expr, TypeConverter<?, ?> converter) {
    super(expr.getPrecedence());
    TypeHandler<?, ?> th = TypeHandler.forConverter(converter, TypeSource.RUNTIME_DESIGNATED, null);
    this.typeHandler = th;
    this.referencedExpression = expr;
  }

  @Override
  protected TypeHandler<?, ?> getTypeHandler() {
    return this.typeHandler;
  }

  @Override
  protected Expression getEmergingExpression() {
    return this.referencedExpression.getEmergingExpression();
  }

  @Override
  protected String getReferenceName() {
    return this.referencedExpression.getReferenceName();
  }

  @Override
  protected String getProperty() {
    return this.referencedExpression.getProperty();
  }

  @Override
  protected void renderTo(QueryWriter w) {
    this.referencedExpression.renderTo(w);
  }

  @Override
  protected List<Expression> expand() {
    return referencedExpression.expand();
  }

}
