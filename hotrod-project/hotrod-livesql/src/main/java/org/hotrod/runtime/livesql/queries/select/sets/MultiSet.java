package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.LinkedHashMap;
import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.QueryWriter.LiveSQLPreparedQuery;
import org.hotrod.runtime.livesql.util.PreviewRenderer;

public abstract class MultiSet<R> {

  private MultiSet<R> parent;

  public void setParent(MultiSet<R> parent) {
    this.parent = parent;
  }

  public MultiSet<R> getParent() {
    return this.parent;
  }

  public abstract void validateTableReferences(TableReferences tableReferences, AliasGenerator ag);

  public abstract void renderTo(QueryWriter w);

  // Execution

  public abstract List<R> execute(final LiveSQLContext context);

  public abstract Cursor<R> executeCursor(final LiveSQLContext context);

  public String getPreview(final LiveSQLContext context) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    return PreviewRenderer.render(q);
  }

  protected LiveSQLPreparedQuery prepareQuery(final LiveSQLContext context) {

    // Validate

    TableReferences tableReferences = new TableReferences();
    AliasGenerator ag = new AliasGenerator();
    this.validateTableReferences(tableReferences, ag);

    // Render

    QueryWriter w = new QueryWriter(context.getLiveSQLDialect());
    renderTo(w);
    return w.getPreparedQuery();
  }

  // Utils

  @SuppressWarnings("unchecked")
  protected List<R> executeLiveSQL(final LiveSQLContext context, final LiveSQLPreparedQuery q) {
    LinkedHashMap<String, Object> parameters = q.getParameters();
    parameters.put("sql", q.getSQL());
    return (List<R>) context.getLiveSQLMapper().select(parameters);
  }

  @SuppressWarnings("unchecked")
  protected Cursor<R> executeLiveSQLCursor(final LiveSQLContext context, final LiveSQLPreparedQuery q) {
    LinkedHashMap<String, Object> parameters = q.getParameters();
    parameters.put("sql", q.getSQL());
    return (Cursor<R>) context.getLiveSQLMapper().selectCursor(parameters);
  }

}
