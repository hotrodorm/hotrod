package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter.LiveSQLPreparedQuery;
import org.hotrod.runtime.livesql.queries.select.MyBatisCursor;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

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

  public void renderTo(final QueryWriter w) {
    this.first.renderTo(w);
    for (SetOperatorTerm<R> t : this.combined) {
      t.getOperator().renderTo(w);
      t.getMultiset().renderTo(w);
    }
  }

  // Combining

  public CombinedSelectObject<R> prepareCombinationWith(final SetOperator<R> op) {

    int currentPrecedence = this.combined.get(0).getOperator().getPrecedence();

    if (op.getPrecedence() == currentPrecedence) {

      return this;

    } else if (op.getPrecedence() < currentPrecedence) { // e.g. INTERSECT after UNION

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
      this.combined.add(new SetOperatorTerm<>(last.getOperator(), o));
      return o;

    } else { // e.g. UNION after INTERSECT

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

      return new CombinedSelectObject<R>(this);

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

}
