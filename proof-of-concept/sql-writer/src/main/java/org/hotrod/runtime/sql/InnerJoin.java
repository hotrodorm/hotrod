package org.hotrod.runtime.sql;

import org.hotrod.runtime.sql.expressions.predicates.Predicate;
import org.hotrod.runtime.sql.metadata.TableOrView;

public class InnerJoin extends PredicatedJoin {

  InnerJoin(final TableOrView table, final Predicate predicate) {
    super(table, predicate);
  }

}
