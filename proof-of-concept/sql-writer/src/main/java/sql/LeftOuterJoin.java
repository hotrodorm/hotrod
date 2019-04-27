package sql;

import metadata.TableOrView;
import sql.expressions.predicates.Predicate;

class LeftOuterJoin extends PredicatedJoin {

  LeftOuterJoin(final TableOrView table, final Predicate predicate) {
    super(table, predicate);
  }

}
