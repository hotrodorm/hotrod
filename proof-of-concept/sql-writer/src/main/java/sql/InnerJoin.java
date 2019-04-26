package sql;

import metadata.TableOrView;
import sql.predicates.Predicate;

class InnerJoin extends PredicatedJoin {

  InnerJoin(final TableOrView table, final Predicate predicate) {
    super(table, predicate);
  }

}
