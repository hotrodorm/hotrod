package sql;

import sql.expressions.predicates.Predicate;
import sql.metadata.TableOrView;

public class InnerJoin extends PredicatedJoin {

  InnerJoin(final TableOrView table, final Predicate predicate) {
    super(table, predicate);
  }

}
