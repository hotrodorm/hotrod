package org.hotrod.runtime.sql;

import org.hotrod.runtime.sql.exceptions.InvalidSQLClauseException;
import org.hotrod.runtime.sql.expressions.predicates.Predicate;
import org.hotrod.runtime.sql.metadata.TableOrView;

abstract class PredicatedJoin extends Join {

  private Predicate predicate;

  PredicatedJoin(final TableOrView table, final Predicate predicate) {
    super(table);
    if (predicate == null) {
      throw new InvalidSQLClauseException("The join predicate cannot be null");
    }
    this.predicate = predicate;
  }

  Predicate getJoinPredicate() {
    return this.predicate;
  }

}
