package sql;

import sql.exceptions.InvalidSQLClauseException;
import sql.expressions.predicates.Predicate;
import sql.metadata.TableOrView;

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