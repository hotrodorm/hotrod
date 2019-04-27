package sql;

import java.util.Arrays;
import java.util.List;

import com.sun.rowset.internal.Row;

import sql.expressions.Expression;
import sql.expressions.OrderingTerm;
import sql.expressions.predicates.Predicate;

public class SelectGroupBy {

  // Properties

  private Select select;

  // Constructor

  SelectGroupBy(final Select select, final Expression... expressions) {
    this.select = select;
    this.select.setGroupBy(Arrays.asList(expressions));
  }

  // Next stages

  public SelectHaving having(final Predicate predicate) {
    return new SelectHaving(this.select, predicate);
  }

  public SelectOrderBy orderBy(final OrderingTerm... orderingTerms) {
    return new SelectOrderBy(this.select, orderingTerms);
  }

  public SelectOffset offset(final int offset) {
    return new SelectOffset(this.select, offset);
  }

  public SelectLimit limit(final int limit) {
    return new SelectLimit(this.select, limit);
  }

  // Execute

  public List<Row> execute() {
    return this.select.execute();
  }

}
