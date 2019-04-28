package sql;

import java.util.List;

import com.sun.rowset.internal.Row;

import sql.expressions.OrderingTerm;
import sql.expressions.predicates.And;
import sql.expressions.predicates.Or;
import sql.expressions.predicates.Predicate;
import sql.metadata.Column;

public class SelectWhere implements ExecutableSelect {

  // Properties

  private AbstractSelect select;

  // Constructors

  SelectWhere(final AbstractSelect select, final Predicate predicate) {
    this.select = select;
    this.select.setWhereCondition(predicate);
  }

  // Same stage

  public SelectWhere and(final Predicate predicate) {
    this.select.setWhereCondition(new And(this.select.getWhereCondition(), predicate));
    return this;
  }

  public SelectWhere or(final Predicate predicate) {
    this.select.setWhereCondition(new Or(this.select.getWhereCondition(), predicate));
    return this;
  }

  // Next stages

  public SelectGroupBy groupBy(final Column... columns) {
    return new SelectGroupBy(this.select, columns);
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

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    this.select.renderTo(w);
  }

  // Execute

  public List<Row> execute() {
    return this.select.execute();
  }

}
