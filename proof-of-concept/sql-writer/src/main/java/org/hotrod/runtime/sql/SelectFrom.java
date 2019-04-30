package org.hotrod.runtime.sql;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.sql.expressions.Expression;
import org.hotrod.runtime.sql.expressions.OrderingTerm;
import org.hotrod.runtime.sql.expressions.predicates.Predicate;
import org.hotrod.runtime.sql.metadata.TableOrView;

public class SelectFrom implements ExecutableSelect {

  // Properties

  private AbstractSelect select;

  // Constructor

  SelectFrom(final AbstractSelect select, final TableOrView t) {
    this.select = select;
    this.select.setBaseTable(t);
  }

  // This stage

  public SelectFrom join(final TableOrView t, final Predicate on) {
    this.select.addJoin(new InnerJoin(t, on));
    return this;
  }

  public SelectFrom leftJoin(final TableOrView t, final Predicate on) {
    this.select.addJoin(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectFrom rightJoin(final TableOrView t, final Predicate on) {
    this.select.addJoin(new RightOuterJoin(t, on));
    return this;
  }

  public SelectFrom fullOuterJoin(final TableOrView t, final Predicate on) {
    this.select.addJoin(new FullOuterJoin(t, on));
    return this;
  }

  public SelectFrom crossJoin(final TableOrView t) {
    this.select.addJoin(new CrossJoin(t));
    return this;
  }

  // Next stages

  public SelectWhere where(final Predicate predicate) {
    return new SelectWhere(this.select, predicate);
  }

  public SelectGroupBy groupBy(final Expression<?>... columns) {
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

  public List<Map<String, Object>> execute() {
    return this.select.execute();
  }

}
