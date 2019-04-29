package org.hotrod.runtime.sql;

import org.hotrod.runtime.sql.expressions.predicates.Predicate;
import org.hotrod.runtime.sql.metadata.TableOrView;

public class LeftOuterJoin extends PredicatedJoin {

  LeftOuterJoin(final TableOrView table, final Predicate predicate) {
    super(table, predicate);
  }

}
