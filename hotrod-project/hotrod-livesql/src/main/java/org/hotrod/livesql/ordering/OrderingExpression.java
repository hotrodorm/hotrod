package org.hotrod.livesql.ordering;

import org.hotrod.livesql.queries.QueryWriter;

public abstract class OrderingExpression implements OrderingTerm {

  protected abstract void renderTo(final QueryWriter w);

}
