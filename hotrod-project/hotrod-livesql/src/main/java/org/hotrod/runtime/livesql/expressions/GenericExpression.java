package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.converter.TypeConverter;
import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.predicates.IsNotNull;
import org.hotrod.runtime.livesql.expressions.predicates.IsNull;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.typesolver.TypeHandler;
import org.hotrodorm.hotrod.utils.SUtil;

public abstract class GenericExpression extends Expression implements OrderingTerm {

  protected GenericExpression(int precedence) {
    super(precedence);
  }

  // TypeHandler setter

  public TypedExpression type(final Class<?> type) {
    super.setTypeHandler(TypeHandler.of(type));
    return new TypedExpression(this);
  }

  public TypedExpression type(final Class<?> type, final Class<?> raw, final TypeConverter<?, ?> converter) {
    super.setTypeHandler(TypeHandler.of(type, raw, converter));
    return new TypedExpression(this);
  }

  // Is Null and Is Not Null

  public Predicate isNotNull() {
    return new IsNotNull(this);
  }

  public Predicate isNull() {
    return new IsNull(this);
  }

// Aliasing

  public AliasedExpression as(final String alias) {
    if (SUtil.isEmpty(alias)) {
      throw new LiveSQLException("An alias specified with the .as() method cannot be null");
    }
    return new AliasedExpression(this, alias);
  }

}
