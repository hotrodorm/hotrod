package org.hotrod.runtime.livesql.ordering;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public abstract class OrderingExpression implements OrderingTerm {

  protected abstract void renderTo(final QueryWriter w);

}
