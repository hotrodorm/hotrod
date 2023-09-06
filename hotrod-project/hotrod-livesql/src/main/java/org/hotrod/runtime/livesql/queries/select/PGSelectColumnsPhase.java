package org.hotrod.runtime.livesql.queries.select;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.ctes.CTE;

public class PGSelectColumnsPhase<R> implements Select<R> {

  // Properties

  private LiveSQLContext context;
  private SelectObject<R> select;

  // Constructor

  public PGSelectColumnsPhase(final LiveSQLContext context, final List<CTE> ctes, final boolean distinct,
      final ResultSetColumn... resultSetColumns) {
    this.context = context;
    this.select = new SelectObject<R>(ctes, distinct, false);
    this.select.setResultSetColumns(Arrays.asList(resultSetColumns).stream().collect(Collectors.toList()));
  }

  public PGSelectColumnsPhase(final SelectObject<R> select, final boolean distinct,
      final ResultSetColumn... resultSetColumns) {
    select.setResultSetColumns(Arrays.asList(resultSetColumns).stream().collect(Collectors.toList()));
    this.select = select;
  }

  // Next stages

  public SelectFromPhase<R> from(final TableOrView t) {
    return new SelectFromPhase<R>(this.context, this.select, t);
  }

  // Set operations

  // public SelectHavingPhase<R> union(final CombinableSelect<R> select) {
  // this.select.setCombinedSelect(SetOperation.UNION, select);
  // return new SelectHavingPhase<R>(this.select, null);
  // }
  //
  // public SelectHavingPhase<R> unionAll(final CombinableSelect<R> select) {
  // this.select.setCombinedSelect(SetOperation.UNION_ALL, select);
  // return new SelectHavingPhase<R>(this.select, null);
  // }
  //
  // public SelectHavingPhase<R> intersect(final CombinableSelect<R> select) {
  // this.select.setCombinedSelect(SetOperation.INTERSECT, select);
  // return new SelectHavingPhase<R>(this.select, null);
  // }
  //
  // public SelectHavingPhase<R> intersectAll(final CombinableSelect<R> select)
  // {
  // this.select.setCombinedSelect(SetOperation.INTERSECT_ALL, select);
  // return new SelectHavingPhase<R>(this.select, null);
  // }
  //
  // public SelectHavingPhase<R> except(final CombinableSelect<R> select) {
  // this.select.setCombinedSelect(SetOperation.EXCEPT, select);
  // return new SelectHavingPhase<R>(this.select, null);
  // }
  //
  // public SelectHavingPhase<R> exceptAll(final CombinableSelect<R> select) {
  // this.select.setCombinedSelect(SetOperation.EXCEPT_ALL, select);
  // return new SelectHavingPhase<R>(this.select, null);
  // }

  // Execute

  public List<R> execute() {
    return this.select.execute(this.context);
  }

  @Override
  public Cursor<R> executeCursor() {
    return this.select.executeCursor(this.context);
  }

  // Validation

  @Override
  public String getPreview() {
    return this.select.getPreview(this.context);
  }

  // Executable Select

  @Override
  public SelectObject<R> getSelect() {
    return this.select;
  }

}
