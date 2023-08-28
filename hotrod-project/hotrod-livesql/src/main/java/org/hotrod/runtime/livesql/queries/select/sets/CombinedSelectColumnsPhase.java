package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.Select;
import org.hotrod.runtime.livesql.queries.select.TableExpression;

public class CombinedSelectColumnsPhase<R> implements ExecutableSelect<R> {

  // Properties

  private Select<R> select;

  // Constructor

  public CombinedSelectColumnsPhase(final LiveSQLDialect sqlDialect, final SqlSession sqlSession,
      final LiveSQLMapper liveSQLMapper, final boolean distinct, final ResultSetColumn... resultSetColumns) {
    Select<R> s = new Select<R>(sqlDialect, distinct, sqlSession, liveSQLMapper, false);
    for (ResultSetColumn c : resultSetColumns) {
      if (c == null) {
        throw new LiveSQLException("Select column cannot be null.");
      }
    }
    s.setResultSetColumns(Arrays.asList(resultSetColumns).stream().collect(Collectors.toList()));
    this.select = s;
  }

  public CombinedSelectColumnsPhase(final Select<R> select, final boolean distinct, final ResultSetColumn... resultSetColumns) {
    select.setResultSetColumns(Arrays.asList(resultSetColumns).stream().collect(Collectors.toList()));
    this.select = select;
  }

  // Next stages

  public CombinedSelectFromPhase<R> from(final TableExpression tableViewOrSubquery) {
    return new CombinedSelectFromPhase<R>(this.select, tableViewOrSubquery);
  }

  // Set operations

  // .select() .selectDistinct()
  public CombinedSelectLinkingPhase<R> union() {
    UnionOperator<R> op = new UnionOperator<R>(this.select);
    return new CombinedSelectLinkingPhase<R>(op);
  }

  // .union()
  public CombinedSelectPhase<R> union(final ExecutableSelect<R> select) {
    UnionOperator<R> op = new UnionOperator<R>(this.select);
    op.setRight(select.getSelect());
    return new CombinedSelectPhase<R>(op);
  }

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

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    this.select.renderTo(w);
  }

  // Execute

  public List<R> execute() {
    return this.select.execute();
  }

  @Override
  public Cursor<R> executeCursor() {
    return this.select.executeCursor();
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    this.select.validateTableReferences(tableReferences, ag);
  }

  // Utilities

  @Override
  public String getPreview() {
    return this.select.getPreview();
  }

  @Override
  public List<ResultSetColumn> listColumns() throws IllegalAccessException {
    return this.select.listColumns();
  }

  // Executable Select

  @Override
  public Select<R> getSelect() {
    return this.select;
  }

}
