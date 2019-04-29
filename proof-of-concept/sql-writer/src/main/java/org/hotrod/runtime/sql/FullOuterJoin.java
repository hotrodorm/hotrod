package org.hotrod.runtime.sql;

import org.hotrod.runtime.sql.expressions.predicates.Predicate;
import org.hotrod.runtime.sql.metadata.TableOrView;

public class FullOuterJoin extends PredicatedJoin {

  FullOuterJoin(final TableOrView table, final Predicate predicate) {
    super(table, predicate);
  }

}
