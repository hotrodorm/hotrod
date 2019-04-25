package sql;

import java.util.List;

import com.sun.rowset.internal.Row;

import metadata.Column;
import metadata.ColumnOrdering;
import metadata.TableOrView;
import sql.Select.Limit;
import sql.predicates.Predicate;

public class SelectFrom {

  // Properties

  private Select select;

  // Constructor

  SelectFrom(final Select select, final TableOrView t) {
    this.select = select;
    this.select.setBaseTable(t);
  }

  // This stage

  public SelectFrom join(final TableOrView t, final Predicate on) {
    this.select.addJoin(new InnerJoin(t, on));
    return this;
  }

  public SelectFrom leftJoin(final TableOrView t, final Predicate on) {
    this.select.addJoin(new LeftJoin(t, on));
    return this;
  }

  public SelectFrom rightJoin(final TableOrView t, final Predicate on) {
    this.select.addJoin(new RightJoin(t, on));
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

  public SelectGroupBy groupBy(final Column... columns) {
    return new SelectGroupBy(this.select, columns);
  }

  public SelectOrderBy orderBy(final ColumnOrdering... columnOrderings) {
    return new SelectOrderBy(this.select, columnOrderings);
  }

  public SelectLimit limit(final int limit) {
    return new SelectLimit(this.select, new Limit(0, limit));
  }

  public SelectLimit limit(final int offset, final int limit) {
    return new SelectLimit(this.select, new Limit(offset, limit));
  }

  // Execute

  public List<Row> execute() {
    return null;
  }

}
