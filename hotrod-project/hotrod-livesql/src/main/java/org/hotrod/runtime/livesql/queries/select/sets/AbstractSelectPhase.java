package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.ctes.CTE;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.SelectObject;

@SuppressWarnings("deprecation")
public class AbstractSelectPhase<R> implements ExecutableSelect<R> {

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
  public CombinedSelectObject<R> getCombinedSelect() {
    return this.combined;
  }

  public SelectObject<R> getLastSelect() {
    return this.combined.getLastSelect();
  }

  // Execute

  @Override
  public final List<R> execute() {
    return this.combined.execute(this.context);
  }

  @Override
  public final Cursor<R> executeCursor() {
    return this.combined.executeCursor(this.context);
  }

  @Override
  public final R executeOne() {
    return this.combined.executeOne(this.context);
  }

  // Utilities

  @Override
  public final String getPreview() {
    return this.combined.getPreview(this.context);
  }

}
