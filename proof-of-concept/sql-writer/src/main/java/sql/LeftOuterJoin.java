package sql;

import sql.expressions.predicates.Predicate;
import sql.metadata.TableOrView;

public class LeftOuterJoin extends PredicatedJoin {

  LeftOuterJoin(final TableOrView table, final Predicate predicate) {
    super(table, predicate);
  }

}
