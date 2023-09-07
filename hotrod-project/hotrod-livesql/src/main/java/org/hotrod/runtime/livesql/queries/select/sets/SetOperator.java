package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.QueryWriter.LiveSQLPreparedQuery;
import org.hotrod.runtime.livesql.util.PreviewRenderer;

//                Union-2
//                  / \
//                 /   \
//           Union-1   SELECT-c
//            / \
//           /   \
//    SELECT-a   SELECT-b
//
//
//           Union-1   
//            / \
//           /   \
//    SELECT-a   Intersect-2
//                 /  \
//                /    \
//          SELECT-b  SELECT-c

public abstract class SetOperator<R> implements MultiSet<R> {

  public static final int PRECEDENCE_INTERSECT = 1;
  public static final int PRECEDENCE_INTERSECT_ALL = 1;
  public static final int PRECEDENCE_UNION = 3;
  public static final int PRECEDENCE_UNION_ALL = 3;
  public static final int PRECEDENCE_EXCEPT = 3;
  public static final int PRECEDENCE_EXCEPT_ALL = 3;

  private SetOperator<R> parent;
  private List<MultiSet<R>> children;

  public SetOperator() {
    this.parent = null;
    this.children = new ArrayList<>();
  }

  @Override
  public void setParentOperator(final SetOperator<R> parent) {
    this.parent = parent;
  }

  @Override
  public SetOperator<R> getParentOperator() {
    return this.parent;
  }

  public void add(final MultiSet<R> child) {
    this.children.add(child);
    child.setParentOperator(this);
  }

  public abstract int getPrecedence();

  public SetOperator<R> findRoot() {
    SetOperator<R> root = this;
    while (root.parent != null) {
      root = root.parent;
    }
    return root;
  }

  // Execute

  public List<R> execute(LiveSQLContext context) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    return executeLiveSQL(context, q);
  }

  @SuppressWarnings("unchecked")
  private List<R> executeLiveSQL(final LiveSQLContext context, final LiveSQLPreparedQuery q) {
    LinkedHashMap<String, Object> parameters = q.getParameters();
    parameters.put("sql", q.getSQL());
    return (List<R>) context.getLiveSQLMapper().select(parameters);
  }

  // Execute Cursor

  public Cursor<R> executeCursor(final LiveSQLContext context) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    return executeLiveSQLCursor(context, q);
  }

  @SuppressWarnings("unchecked")
  private Cursor<R> executeLiveSQLCursor(final LiveSQLContext context, final LiveSQLPreparedQuery q) {
    LinkedHashMap<String, Object> parameters = q.getParameters();
    parameters.put("sql", q.getSQL());
    return (Cursor<R>) context.getLiveSQLMapper().selectCursor(parameters);
  }

  // Rendering

  protected abstract void renderSetOperator(final QueryWriter w);

  public LiveSQLPreparedQuery prepareQuery(final LiveSQLContext context) {
    validateQuery();
    QueryWriter w = new QueryWriter(context.getLiveSQLDialect());
    renderTo(w);
    return w.getPreparedQuery();
  }

  private void validateQuery() {
    TableReferences tableReferences = new TableReferences();
    AliasGenerator ag = new AliasGenerator();
    this.children.forEach(s -> s.validateTableReferences(tableReferences, ag));
  }

  public void renderTo(final QueryWriter w) {
    Iterator<MultiSet<R>> it = this.children.iterator();
    while (it.hasNext()) {
      MultiSet<R> s = it.next();
      s.renderTo(w);
      if (it.hasNext()) {
        w.write("\n");
        this.renderSetOperator(w);
        w.write("\n");
      }
    }
  }

  public String getPreview(final LiveSQLContext context) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    return PreviewRenderer.render(q);
  }

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    this.children.forEach(s -> s.validateTableReferences(tableReferences, ag));
  }

}
