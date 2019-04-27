package sql;

import sql.expressions.predicates.Predicate;
import sql.metadata.TableOrView;

public class FullOuterJoin extends PredicatedJoin {

  FullOuterJoin(final TableOrView table, final Predicate predicate) {
    super(table, predicate);
  }

}
