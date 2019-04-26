package sql;

import metadata.TableOrView;
import sql.predicates.Predicate;

class LeftOuterJoin extends PredicatedJoin {

  LeftOuterJoin(final TableOrView table, final Predicate predicate) {
    super(table, predicate);
  }

}
