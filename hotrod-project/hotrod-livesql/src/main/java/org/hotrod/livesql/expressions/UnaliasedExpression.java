package org.hotrod.livesql.expressions;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.utils.SUtil;

public abstract class UnaliasedExpression extends Expression {

  protected UnaliasedExpression(int precedence) {
    super(precedence);
  }

  // Aliasing

  public final AliasedExpression as(final String alias) {
    if (SUtil.isEmpty(alias)) {
      throw new LiveSQLException("An alias specified with the .as() method cannot be null");
    }
    return new AliasedExpression(this, alias);
  }

}
