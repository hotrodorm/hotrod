package sql;

import metadata.TableOrView;
import sql.expressions.predicates.Predicate;

class FullOuterJoin extends PredicatedJoin {

  FullOuterJoin(final TableOrView table, final Predicate predicate) {
    super(table, predicate);
  }

}
