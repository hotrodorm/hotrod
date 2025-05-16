package org.hotrod.runtime.livesql.queries.select.sets;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.ctes.CTE;
import org.hotrod.runtime.livesql.queries.select.Select;
import org.hotrod.runtime.livesql.queries.select.SelectObject;

public class AbstractSelectPhase<R> extends Select<R> {

  // Properties

  protected LiveSQLContext context;
  protected CombinedSelectObject<R> combined;

  public AbstractSelectPhase(final LiveSQLContext context, final List<CTE> ctes, final boolean distinct,
      final boolean doNotAliasColumns) {
    this.context = context;
    SelectObject<R> s = new SelectObject<>(ctes, distinct, doNotAliasColumns);
    this.combined = new CombinedSelectObject<>(s);
    s.setParent(this.combined);
  }

  public AbstractSelectPhase(final LiveSQLContext context, final List<CTE> ctes, final Expression[] distinctOn,
      final boolean doNotAliasColumns) {
    this.context = context;
    SelectObject<R> s = new SelectObject<R>(ctes, distinctOn, doNotAliasColumns, null);
    this.combined = new CombinedSelectObject<>(s);
    s.setParent(this.combined);
  }

  public AbstractSelectPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined) {
    this.context = context;
    this.combined = combined;
  }

  // Getters

  @Override
  protected CombinedSelectObject<R> getCombinedSelect() {
    return this.combined;
  }

  protected SelectObject<R> getLastSelect() {
    return this.combined.getLastSelect();
  }

  // Execute

  @Override
  public final List<R> execute() {
    return this.combined.execute(this.context);
  }

  @Override
  public final Cursor<R> executeCursor() throws SQLException {
    return this.combined.executeCursor(this.context);
  }

  @Override
  public final Cursor<R> executeCursor(int fetchSize) throws SQLException {
    return this.combined.executeCursor(this.context, fetchSize);
  }

  @Override
  public final R executeOne() {
    return this.combined.executeOne(this.context);
  }

  // Utilities

  @Override
  public final String getPreview() {
    return this.combined.getPreview(this.context, false);
  }

  @Override
  public final String getPreview(boolean includeParameters) {
    return this.combined.getPreview(this.context, includeParameters);
  }

}
