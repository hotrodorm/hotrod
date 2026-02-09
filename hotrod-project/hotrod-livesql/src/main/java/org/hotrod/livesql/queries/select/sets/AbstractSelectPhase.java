package org.hotrod.livesql.queries.select.sets;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.ctes.CTE;
import org.hotrod.livesql.queries.select.Select;
import org.hotrod.livesql.queries.select.UnarySelectObject;

public class AbstractSelectPhase<R> extends Select<R> {

  private static final Logger log = Logger.getLogger(AbstractSelectPhase.class.getName());

  // Properties

  protected LiveSQLContext context;
  protected CombinedSelectObject<R> combined;

  public AbstractSelectPhase(final LiveSQLContext context, final List<CTE> ctes, final boolean distinct,
      final boolean doNotAliasColumns) {
    this.context = context;
    UnarySelectObject<R> s = new UnarySelectObject<>(ctes, distinct, doNotAliasColumns);
    this.combined = new CombinedSelectObject<>(s);
    s.setParent(this.combined);
  }

  public AbstractSelectPhase(final LiveSQLContext context, final List<CTE> ctes, final Expression[] distinctOn,
      final boolean doNotAliasColumns) {
    this.context = context;
    UnarySelectObject<R> s = new UnarySelectObject<>(ctes, distinctOn, doNotAliasColumns, null);
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

  protected BaseSelectObject<R> getLastSelect() {
    return this.combined.getLastSelect();
  }

  // Execute

  @Override
  public final List<R> execute() {
    LiveSQLLogging la = null;
    return this.combined.execute(this.context, la);
  }

  @Override
  public final Cursor<R> executeCursor() throws SQLException {
    return this.combined.executeCursor(this.context, null);
  }

  @Override
  public final Cursor<R> executeCursor(int fetchSize) throws SQLException {
    LiveSQLLogging la = null;
    return this.combined.executeCursor(this.context, la, fetchSize);
  }

  @Override
  public final R executeOne() {
    LiveSQLLogging la = null;
    return this.combined.executeOne(this.context, la);
  }

  @Override
  public final List<R> execute(LiveSQLLogging loggingAdapter) {
    return this.combined.execute(this.context, loggingAdapter);
  }

  @Override
  public final Cursor<R> executeCursor(LiveSQLLogging loggingAdapter) throws SQLException {
    return this.combined.executeCursor(this.context, loggingAdapter);
  }

  @Override
  public final Cursor<R> executeCursor(int fetchSize, LiveSQLLogging loggingAdapter) throws SQLException {
    return this.combined.executeCursor(this.context, loggingAdapter, fetchSize);
  }

  @Override
  public final R executeOne(LiveSQLLogging loggingAdapter) {
    return this.combined.executeOne(this.context, loggingAdapter);
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
