package org.hotrod.runtime.sql.ordering;

import org.hotrod.runtime.sql.QueryWriter;

public interface OrderingTerm {

  void renderTo(QueryWriter w);

}
