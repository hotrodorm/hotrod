package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.Select;

public class CombinedSelectPhase<R> implements ExecutableSelect<R> {

  private SetOperator<R> op;

  public CombinedSelectPhase(SetOperator<R> op) {
    this.op = op;
  }

  // Set phases

  public void union() {
  }

  public void unionAll() {
  }

  public void except() {
  }

  public void exceptAll() {
  }

  public void intersect() {
  }

  public void intersectAll() {
  }

  // Next phases

//  public SelectOrderByPhase<R> orderBy(final OrderingTerm... orderingTerms) {
//    return new SelectOrderByPhase<R>(this.select, orderingTerms);
//  }
//
//  public SelectOffsetPhase<R> offset(final int offset) {
//    return new SelectOffsetPhase<R>(this.select, offset);
//  }
//
//  public SelectLimitPhase<R> limit(final int limit) {
//    return new SelectLimitPhase<R>(this.select, limit);
//  }

  // ExecutableSelect

  @Override
  public void renderTo(QueryWriter w) {
    SetOperator<R> root = op.findRoot();
    // TODO Auto-generated method stub

  }

  @Override
  public List<R> execute() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Cursor<R> executeCursor() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getPreview() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Select<R> getSelect() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void validateTableReferences(TableReferences tableReferences, AliasGenerator ag) {
    // TODO Auto-generated method stub

  }

  @Override
  public List<ResultSetColumn> listColumns() throws IllegalAccessException {
    // TODO Auto-generated method stub
    return null;
  }

}
