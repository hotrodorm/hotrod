package org.hotrod.runtime.livesql.expressions.binary;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.AliasedExpression;
import org.hotrod.runtime.livesql.expressions.TypedExpression;
import org.hotrod.utils.SUtil;

public abstract class ByteArrayExpression extends GeneralByteArrayExpression {

  protected ByteArrayExpression(int precedence) {
    super(precedence);
  }

  // TypeHandler setter

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
