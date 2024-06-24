package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.dialects.PaginationRenderer.PaginationType;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.ordering.CombinedOrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.QueryWriter.LiveSQLPreparedQuery;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;
import org.hotrod.runtime.livesql.queries.select.SelectObject;
import org.hotrod.runtime.livesql.util.IdUtil;

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

public class CombinedSelectObject<R> extends MultiSet<R> {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(CombinedSelectObject.class.getName());

  private boolean forceParenthesis;
  private MultiSet<R> first;
  private List<SetOperatorTerm<R>> combined;
  private SelectObject<R> lastSelect; // TODO: Remove?

  private List<CombinedOrderingTerm> orderingTerms = null;
  private Integer offset = null;
  private Integer limit = null;

  public CombinedSelectObject(final MultiSet<R> first) {
    initialize(first, false);
  }

  public CombinedSelectObject(final MultiSet<R> first, final boolean forceParenthesis) {
    initialize(first, forceParenthesis);
  }

  private void initialize(final MultiSet<R> first, final boolean forceParenthesis) {
    this.forceParenthesis = forceParenthesis;
    this.first = first;
    this.combined = new ArrayList<>();
    this.lastSelect = null;
    first.setParent(this);
  }

  public CombinedSelectObject(final SelectObject<R> first) {
    this.forceParenthesis = false;
    this.first = first;
    this.combined = new ArrayList<>();
    this.lastSelect = first;
    first.setParent(this);
  }

  public void add(final SetOperator operator, final MultiSet<R> multiset) {
    SetOperatorTerm<R> term = new SetOperatorTerm<>(operator, multiset);
    this.combined.add(term);
    multiset.setParent(this);
  }

  public void add(final SetOperator operator, final SelectObject<R> select) {
    SetOperatorTerm<R> term = new SetOperatorTerm<>(operator, select);
    this.combined.add(term);
    this.lastSelect = select;
    select.setParent(this);
  }

  public void setColumnOrderings(final List<CombinedOrderingTerm> orderingTerms) {
    this.orderingTerms = orderingTerms;
  }

  public void setOffset(final Integer offset) {
    this.offset = offset;
  }

  public void setLimit(final Integer limit) {
    this.limit = limit;
  }

  public boolean forcesParenthesis() {
    return forceParenthesis;
  }

  // tree: [4f [1 s2, u/s3], ]

  @Override
  public void flatten() {
    if (this.combined.isEmpty()) {
      try {
        CombinedSelectObject<R> nestedFirst = (CombinedSelectObject<R>) this.first;
        if (!nestedFirst.forceParenthesis) {
          this.first = nestedFirst.first;
          this.combined = nestedFirst.combined;
        }
      } catch (ClassCastException e) {
        // Nothing to do
      }
    }

    this.first.flatten();
    this.combined.forEach(c -> c.getMultiset().flatten());

  }

  // Rendering

  public void renderTo(final QueryWriter w) {
    this.renderTo(w, false);
  }

  public void renderTo(final QueryWriter w, final boolean inline) {

    boolean orderedSelect = this.orderingTerms != null && !this.orderingTerms.isEmpty();

    LiveSQLDialect liveSQLDialect = w.getSQLDialect();
    PaginationType paginationType = liveSQLDialect.getPaginationRenderer().getPaginationType(orderedSelect, this.offset,
        this.limit);

    // Entering level

    if (inline) {
      w.write(" ");
    }

    // Entering level when no forced parenthesis

    if (!this.forceParenthesis && this.getParent() != null) {
      w.write("(\n");
      w.enterLevel();
    }

    // Enclosing pagination - begin

    if ((this.offset != null || this.limit != null) && paginationType == PaginationType.ENCLOSE) {
      liveSQLDialect.getPaginationRenderer().renderBeginEnclosingPagination(this.offset, this.limit, w);
    }

    // Entering level when forced parenthesis

    if (this.forceParenthesis) {
      w.write("(\n");
      w.enterLevel();
    }

    // Sub-queries

    this.first.renderTo(w, false);

    for (SetOperatorTerm<R> t : this.combined) {
      w.write("\n");
      t.getOperator().renderTo(w);
      t.getMultiset().renderTo(w, true);
    }

    // Exiting level when forced parenthesis

    if (this.forceParenthesis) {
      w.exitLevel();
      w.write("\n)");
    }

    // ORDER BY

    if (orderedSelect) {
      w.write("\nORDER BY ");
      boolean first = true;
      for (CombinedOrderingTerm term : this.orderingTerms) {
        if (first) {
          first = false;
        } else {
          w.write(", ");
        }
        term.renderTo(w);
      }
    }

    // Bottom OFFSET and LIMIT

    if ((this.offset != null || this.limit != null) && paginationType == PaginationType.BOTTOM) {
      liveSQLDialect.getPaginationRenderer().renderBottomPagination(this.offset, this.limit, w);
    }

    // Enclosing pagination - end

    if ((this.offset != null || this.limit != null) && paginationType == PaginationType.ENCLOSE) {
      liveSQLDialect.getPaginationRenderer().renderEndEnclosingPagination(this.offset, this.limit, w);
    }

    // Exiting level when not forced parenthesis

    if (!this.forceParenthesis && this.getParent() != null) {
      w.exitLevel();
      w.write("\n)");
    }

  }

  // Combining

  public CombinedSelectObject<R> prepareCombinationWith(final SetOperator op) {

    if (this.combined.isEmpty()) {
//      System.out.println("// 0 no precedence yet");
      return this;
    }

    int currentPrecedence = this.combined.get(0).getOperator().getPrecedence();

    if (op.getPrecedence() == currentPrecedence) {
//      System.out.println("// 1 equal precedence");

      return this;

    } else if (op.getPrecedence() < currentPrecedence) { // e.g. INTERSECT after UNION
//      System.out.println("// 2 higher precedence");

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
//      System.out.println("// 3 lower precedence");

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

  // Combining

  public final SelectObject<R> getLastSelect() {
    return this.lastSelect;
  }

  @Override
  protected List<Expression> assembleColumns() {
    List<Expression> cols = this.first.assembleColumns();
    for (SetOperatorTerm<R> o : this.combined) {
      o.getMultiset().assembleColumns();
    }
    return cols;
  }

//  private List<ResultSetColumn> columns = null;

//  public List<ResultSetColumn> listColumns() {
//    log.info(
//        "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ listColumns()");
//    log.info("$$$ " + TUtil.compactStackTrace());
//    if (this.columns == null) {
//      this.columns = this.first.listColumns();
//    }
//    return this.columns;
//  }

  // Rendering

//  @Override
//  protected List<Expression> assembleColumns() {
//    List<Expression> cols = this.first.assembleColumns();
//    for (SetOperatorTerm<?> t : this.combined) {
//      t.getMultiset().assembleColumns();
//    }
//    return cols;
//  }
//
//  @Deprecated
//  @Override
//  protected void computeQueryColumns() {
//    this.first.computeQueryColumns();
//    for (SetOperatorTerm<?> t : this.combined) {
//      t.getMultiset().computeQueryColumns();
//    }
//  }

//  @Override
//  protected LinkedHashMap<String, QueryColumn> getQueryColumns() {
//    return this.first.getQueryColumns();
//  }

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

  @Override
  public R executeOne(final LiveSQLContext context) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    return executeLiveSQLOne(context, q);
  }

  public final String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(
        "[" + IdUtil.id(this) + (this.forceParenthesis ? "f" : "") + (this.orderingTerms != null ? "o" : "") + " ");
    sb.append(this.first.toString());
    sb.append(", ");
    sb.append(this.combined.stream().map(c -> c.toString()).collect(Collectors.joining(", ")));
    sb.append("]");
    return sb.toString();
  }

}
