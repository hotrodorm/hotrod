package org.hotrod.runtime.livesql.ordering;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public interface OrderingTerm {

  void renderTo(QueryWriter w);

}
