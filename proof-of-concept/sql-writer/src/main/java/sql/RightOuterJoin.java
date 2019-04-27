package sql;

import sql.expressions.predicates.Predicate;
import sql.metadata.TableOrView;

public class RightOuterJoin extends PredicatedJoin {

  RightOuterJoin(final TableOrView table, final Predicate on) {
    super(table, on);
  }

}
