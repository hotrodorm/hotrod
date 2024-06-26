package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.predicates.IsNotNull;
import org.hotrod.runtime.livesql.expressions.predicates.IsNull;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrodorm.hotrod.utils.SUtil;

public abstract class GenericExpression extends Expression implements OrderingTerm {

  protected GenericExpression(int precedence) {
    super(precedence);
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
