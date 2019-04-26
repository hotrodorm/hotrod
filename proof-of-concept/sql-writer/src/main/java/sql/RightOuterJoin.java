package sql;

import metadata.TableOrView;
import sql.predicates.Predicate;

class RightOuterJoin extends PredicatedJoin {

  RightOuterJoin(final TableOrView table, final Predicate on) {
    super(table, on);
  }

}
