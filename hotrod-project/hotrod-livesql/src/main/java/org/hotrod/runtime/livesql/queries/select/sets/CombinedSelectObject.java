package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.QueryWriter.LiveSQLPreparedQuery;
import org.hotrod.runtime.livesql.util.IdUtil;

public class CombinedSelectObject<R> extends MultiSet<R> {

  /**
   * <pre>
   * 
         CombinedSelectObject (extends MultiSet)
         /              \
        /                \
     select              List(SetOperatorTerm)
     (extends MultiSet)    +SetOperator
                           +MultiSet
   * </pre>
   */

  private MultiSet<R> first;
  private List<SetOperatorTerm<R>> combined;

  public CombinedSelectObject(final MultiSet<R> first) {
    this.first = first;
    this.combined = new ArrayList<>();
  }

  public void add(final SetOperatorTerm<R> term) {
    this.combined.add(term);
  }

  // Rendering

  public void renderTo(final QueryWriter w, final boolean inline) {

    System.out.println(" --- level: " + w.getLevel() + "  inline=" + inline + " -- parent=" + this.getParent());

    if (inline) {
      w.write(" ");
    }

    if (this.getParent() != null) {
      w.write("(\n");
    }

    if (this.getParent() != null) {
      w.enterLevel();
    }

    this.first.renderTo(w, false);

    for (SetOperatorTerm<R> t : this.combined) {
      w.write("\n");
      t.getOperator().renderTo(w);
      t.getMultiset().renderTo(w, true);
    }

    if (this.getParent() != null) {
      w.exitLevel();
    }

    if (this.getParent() != null) {
      w.write("\n)");
    }
  }

  // Combining

  public CombinedSelectObject<R> prepareCombinationWith(final SetOperator<R> op) {

    if (this.combined.isEmpty()) {
      System.out.println("// 0 no precedence yet");
      return this;
    }

    int currentPrecedence = this.combined.get(0).getOperator().getPrecedence();

    if (op.getPrecedence() == currentPrecedence) {
      System.out.println("// 1 equal precedence");

      return this;

    } else if (op.getPrecedence() < currentPrecedence) { // e.g. INTERSECT after UNION
      System.out.println("// 2 higher precedence");

      /**
       * <pre>
       
        cm1        cm1
        / \        / \
       s1  u/s2   s1  u/cm2
                      /  \
                     s2  i/s3
       * 
       * </pre>
       */

      SetOperatorTerm<R> last = this.combined.remove(this.combined.size() - 1);
      CombinedSelectObject<R> o = new CombinedSelectObject<R>(last.getMultiset());
      o.setParent(this);
      this.combined.add(new SetOperatorTerm<>(last.getOperator(), o));
      return o;

    } else { // e.g. UNION after INTERSECT
      System.out.println("// 3 lower precedence");

      /**
       * <pre>
       *
                     cm2
                     /  \
        cm1        cm1 u/s3
        / \        /  \
       s1  i/s2   s1  i/s2
       * 
       * </pre>
       */

      CombinedSelectObject<R> cm2;
      if (this.getParent() == null) {
        cm2 = new CombinedSelectObject<R>(this);
        this.setParent(cm2);
      } else {
        cm2 = this.getParent();
      }

      return cm2;

    }

  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    this.first.validateTableReferences(tableReferences, ag);
    this.combined.forEach(s -> s.getMultiset().validateTableReferences(tableReferences, ag));
  }

  // MultiSet execution

  @Override
  public List<R> execute(final LiveSQLContext context) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    return executeLiveSQL(context, q);
  }

  @Override
  public Cursor<R> executeCursor(final LiveSQLContext context) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    return executeLiveSQLCursor(context, q);
  }

  public final String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[" + IdUtil.id(this) + " ");
    sb.append(this.first.toString());
    sb.append(", ");
    sb.append(this.combined.stream().map(c -> c.toString()).collect(Collectors.joining(", ")));
    sb.append("]");
    return sb.toString();
  }

}
