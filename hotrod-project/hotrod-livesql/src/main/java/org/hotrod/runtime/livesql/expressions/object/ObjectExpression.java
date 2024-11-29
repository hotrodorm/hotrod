package org.hotrod.runtime.livesql.expressions.object;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.AliasedExpression;
import org.hotrod.runtime.livesql.expressions.TypedExpression;
import org.hotrodorm.hotrod.utils.SUtil;

public abstract class ObjectExpression extends GeneralObjectExpression {

  // TypeHandler setter

  protected ObjectExpression(int precedence) {
    super(precedence);
  }

  public TypedExpression type(final Class<?> type) {
    return new TypedExpression(this, type);
  }

  // Aliasing

  public AliasedExpression as(final String alias) {
    if (SUtil.isEmpty(alias)) {
      throw new LiveSQLException("An alias specified with the .as() method cannot be null");
    }
    return new AliasedExpression(this, alias);
  }

}
