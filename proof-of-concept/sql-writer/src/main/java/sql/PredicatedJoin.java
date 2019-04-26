package sql;

import metadata.TableOrView;
import sql.exceptions.InvalidSQLClauseException;
import sql.predicates.Predicate;

abstract class PredicatedJoin extends Join {

  private TableOrView table;
  private Predicate predicate;

  PredicatedJoin(final TableOrView table, final Predicate predicate) {
    super(table);
    if (predicate == null) {
      throw new InvalidSQLClauseException("The join predicate cannot be null");
    }
    this.predicate = predicate;
  }

  TableOrView getTable() {
    return this.table;
  }

  Predicate getJoinPredicate() {
    return this.predicate;
  }

}
