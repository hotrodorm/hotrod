package org.hotrod.runtime.livesql.queries.select;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

public class SelectColumnsPhase<R> implements ExecutableSelect<R>, CombinableSelect<R> {

  // Properties

  private AbstractSelect<R> select;

  // Constructor

  public SelectColumnsPhase(final LiveSQLDialect sqlDialect, final SqlSession sqlSession,
      final LiveSQLMapper liveSQLMapper, final boolean distinct, final ResultSetColumn... resultSetColumns) {
    Select<R> s = new Select<R>(sqlDialect, distinct, sqlSession, liveSQLMapper, false);
    s.setResultSetColumns(Arrays.asList(resultSetColumns).stream().collect(Collectors.toList()));
    this.select = s;
  }

  public SelectColumnsPhase(final Select<R> select, final boolean distinct, final ResultSetColumn... resultSetColumns) {
    select.setResultSetColumns(Arrays.asList(resultSetColumns).stream().collect(Collectors.toList()));
    this.select = select;
  }

  // Next stages

  public SelectFromPhase<R> from(final TableExpression tableViewOrSubquery) {
    return new SelectFromPhase<R>(this.select, tableViewOrSubquery);
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

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.select.assignNonDeclaredAliases(ag);
  }

  // CombinableSelect

  @Override
  public void setParent(final AbstractSelect<R> parent) {
    this.select.setParent(parent);
  }

  @Override
  public String getPreview() {
    return this.select.getPreview();
  }

}
