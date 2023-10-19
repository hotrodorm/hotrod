package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

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

public abstract class SetOperator<R> {

  public static final int PRECEDENCE_INTERSECT = 1;
  public static final int PRECEDENCE_INTERSECT_ALL = 1;
  public static final int PRECEDENCE_UNION = 3;
  public static final int PRECEDENCE_UNION_ALL = 3;
  public static final int PRECEDENCE_EXCEPT = 3;
  public static final int PRECEDENCE_EXCEPT_ALL = 3;

  public abstract int getPrecedence();

  // Rendering

  protected abstract void renderTo(final QueryWriter w);

//  public LiveSQLPreparedQuery prepareQuery(final LiveSQLContext context) {
//    validateQuery();
//    QueryWriter w = new QueryWriter(context.getLiveSQLDialect());
//    renderTo(w);
//    return w.getPreparedQuery();
//  }
//
//  private void validateQuery() {
//    TableReferences tableReferences = new TableReferences();
//    AliasGenerator ag = new AliasGenerator();
//    this.children.forEach(s -> s.validateTableReferences(tableReferences, ag));
//  }

//  public void renderTo(final QueryWriter w) {
//    Iterator<XMultiSet<R>> it = this.children.iterator();
//    while (it.hasNext()) {
//      XMultiSet<R> s = it.next();
//      boolean isOperator = s instanceof SetOperator;
//
//      if (isOperator) {
//        w.enterLevel();
//        w.write("(\n");
//      }
//
//      s.renderTo(w);
//
//      if (isOperator) {
//        w.exitLevel();
//        w.write("\n)");
//      }
//
//      if (it.hasNext()) {
//        w.write("\n");
//        this.renderSetOperator(w);
//        w.write("\n");
//      }
//    }
//  }

//  public String getPreview(final LiveSQLContext context) {
//    LiveSQLPreparedQuery q = this.prepareQuery(context);
//    return PreviewRenderer.render(q);
//  }

}
