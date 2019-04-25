package sql;

import metadata.TableOrView;
import sql.exceptions.InvalidSQLClauseException;
import sql.predicates.Predicate;

class LeftJoin extends AbstractJoin {

  private Predicate on;

  LeftJoin(final TableOrView table, final Predicate on) {
    super(table);
    if (on == null) {
      throw new InvalidSQLClauseException("The predicate for a left outer join cannot be null");
    }
    this.on = on;
  }

  Predicate getOn() {
    return on;
  }

}
