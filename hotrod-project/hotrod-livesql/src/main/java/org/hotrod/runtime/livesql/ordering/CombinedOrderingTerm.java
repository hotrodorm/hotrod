package org.hotrod.runtime.livesql.ordering;

import org.hotrod.runtime.livesql.expressions.OrderingTerm;

public abstract class CombinedOrderingTerm extends OrderingTerm {

  protected CombinedOrderingTerm() {
    super(PRECEDENCE_ORDERING);
  }

}
