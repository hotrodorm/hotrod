package sql;

import metadata.TableOrView;
import sql.exceptions.InvalidSQLClauseException;
import sql.predicates.Predicate;

class InnerJoin extends AbstractJoin {

  private Predicate on;

  InnerJoin(final TableOrView table, final Predicate on) {
    super(table);
    if (on == null) {
      throw new InvalidSQLClauseException("The predicate for an inner join cannot be null");
    }
    this.on = on;
  }

  Predicate getOn() {
    return on;
  }

}
