package sql;

import metadata.TableOrView;
import sql.expressions.predicates.Predicate;

class InnerJoin extends PredicatedJoin {

  InnerJoin(final TableOrView table, final Predicate predicate) {
    super(table, predicate);
  }

}
