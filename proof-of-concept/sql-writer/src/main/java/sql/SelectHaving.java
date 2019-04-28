package sql;

import java.util.List;

import com.sun.rowset.internal.Row;

import sql.expressions.OrderingTerm;
import sql.expressions.predicates.And;
import sql.expressions.predicates.Or;
import sql.expressions.predicates.Predicate;

public class SelectHaving implements ExecutableSelect {

  // Properties

  private AbstractSelect select;

  // Constructor

  SelectHaving(final AbstractSelect select, final Predicate predicate) {
    this.select = select;
    this.select.setHavingCondition(predicate);
  }

  // Same stage

  public SelectHaving and(final Predicate predicate) {
    this.select.setHavingCondition(new And(this.select.getHavingCondition(), this.select.getHavingCondition()));
    return this;
  }

  public SelectHaving or(final Predicate predicate) {
    this.select.setHavingCondition(new Or(this.select.getHavingCondition(), this.select.getHavingCondition()));
    return this;
  }

  // Next stages

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
