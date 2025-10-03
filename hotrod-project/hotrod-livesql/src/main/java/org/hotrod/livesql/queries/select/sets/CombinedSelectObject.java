package org.hotrod.livesql.queries.select.sets;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.dynamicsql.RowReader;
import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.dialects.PaginationRenderer.PaginationType;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.ordering.CombinedOrderingTerm;
import org.hotrod.livesql.ordering.OHelper;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.LiveSQLPreparedQuery;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.select.TableReferences;
import org.hotrod.livesql.queries.select.UnarySelectObject;
import org.hotrod.livesql.queries.select.UnarySelectObject.AliasGenerator;
import org.hotrod.livesql.util.IdUtil;
import org.hotrod.livesql.util.ToString;

/**
 * <pre>
 * 
       CombinedSelectObject (extends SelectObject)
       /              \
      /                \
   select              List(SetOperatorTerm)
   (SelectObject)        +SetOperator
                         +SelectObject
 * </pre>
 */

public class CombinedSelectObject<T> extends SelectObject<T> {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(CombinedSelectObject.class.getName());

  private boolean forceParenthesis;
  private SelectObject<T> anchor;
  private List<SetOperatorTerm<T>> combined;
  private BaseSelectObject<T> lastSelect; // TODO: Remove?

  private List<CombinedOrderingTerm> orderingTerms = null;
  private Integer offset = null;
  private Integer limit = null;

  public CombinedSelectObject(final SelectObject<T> anchor) {
    initialize(anchor, false);
  }

  public CombinedSelectObject(final SelectObject<T> anchor, final boolean forceParenthesis) {
    initialize(anchor, forceParenthesis);
  }

  private void initialize(final SelectObject<T> anchor, final boolean forceParenthesis) {
    this.forceParenthesis = forceParenthesis;
    this.anchor = anchor;
    this.combined = new ArrayList<>();
    this.lastSelect = null;
    anchor.setParent(this);
  }

  public CombinedSelectObject(final BaseSelectObject<T> anchor) {
    this.forceParenthesis = false;
    this.anchor = anchor;
    this.combined = new ArrayList<>();
    this.lastSelect = anchor;
    anchor.setParent(this);
  }

  public void add(final SetOperator operator, final SelectObject<T> multiset) {
    SetOperatorTerm<T> term = new SetOperatorTerm<>(operator, multiset);
    this.combined.add(term);
    multiset.setParent(this);
  }

  public void add(final SetOperator operator, final UnarySelectObject<T> select) {
    SetOperatorTerm<T> term = new SetOperatorTerm<>(operator, select);
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
        CombinedSelectObject<T> nestedFirst = (CombinedSelectObject<T>) this.anchor;
        if (!nestedFirst.forceParenthesis) {
          this.anchor = nestedFirst.anchor;
          this.combined = nestedFirst.combined;
        }
      } catch (ClassCastException e) {
        // Nothing to do
      }
    }

    this.anchor.flatten();
    this.combined.forEach(c -> c.getMultiset().flatten());

  }

  @Override
  public RowReader<T> getRowReader() {
    // No default Row Reader for the combined query
    return null;
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

    // Single Selects

    this.anchor.renderTo(w, false);

    for (SetOperatorTerm<T> t : this.combined) {
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
        OHelper.renderTo(term, w);
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

  public CombinedSelectObject<T> prepareCombinationWith(final SetOperator op) {

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

      SetOperatorTerm<T> last = this.combined.remove(this.combined.size() - 1);
      CombinedSelectObject<T> o = new CombinedSelectObject<T>(last.getMultiset());
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

      CombinedSelectObject<T> cm2;
      if (this.getParent() == null) {
        cm2 = new CombinedSelectObject<T>(this);
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
    this.anchor.validateTableReferences(tableReferences, ag);
    this.combined.forEach(s -> s.getMultiset().validateTableReferences(tableReferences, ag));
  }

  // Combining

  public final BaseSelectObject<T> getLastSelect() {
    return this.lastSelect;
  }

  @Override
  public boolean excludeTuplesFromUniqueNames() {
    return this.anchor.excludeTuplesFromUniqueNames();
  }

  @Override
  protected void prepareColumnCompilation() {
    this.anchor.prepareColumnCompilation();
    for (SetOperatorTerm<T> o : this.combined) {
      o.getMultiset().prepareColumnCompilation();
    }
  }

  @Override
  protected void computeColumnsCompilation() {
    this.anchor.computeColumnsCompilation();
    for (SetOperatorTerm<T> o : this.combined) {
      o.getMultiset().computeColumnsCompilation();
    }
  }

  @Override
  public List<Expression> getCompiledColumns() {
    return this.anchor.getCompiledColumns();
  }

  // MultiSet execution

  @Override
  public List<T> execute(final LiveSQLContext context) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    RowReader<T> rowReader = this.anchor.getRowReader();
    return executeLiveSQL(context, q, rowReader);
  }

  @Override
  public List<T> execute(final LiveSQLContext context, final RowReader<T> rowReader) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    return super.executeLiveSQL(context, q, rowReader);
  }

  @Override
  public T executeOne(final LiveSQLContext context) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    RowReader<T> rowReader = this.anchor.getRowReader();
    T row = super.executeLiveSQLOne(context, q, rowReader);
    return row;
  }

  @Override
  public T executeOne(LiveSQLContext context, RowReader<T> rowReader) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    T row = super.executeLiveSQLOne(context, q, rowReader);
    return row;
  }

  @Override
  public Cursor<T> executeCursor(final LiveSQLContext context) throws SQLException {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    RowReader<T> rowReader = this.anchor.getRowReader();
    return super.executeLiveSQLCursor(context, q, rowReader, null);
  }

  @Override
  public Cursor<T> executeCursor(final LiveSQLContext context, Integer fetchSize) throws SQLException {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    RowReader<T> rowReader = this.anchor.getRowReader();
    return super.executeLiveSQLCursor(context, q, rowReader, fetchSize);
  }

  @Override
  public Cursor<T> executeCursor(final LiveSQLContext context, final RowReader<T> rowReader, final Integer fetchSize)
      throws SQLException {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    return super.executeLiveSQLCursor(context, q, rowReader, fetchSize);
  }

  public final String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(
        "[" + IdUtil.id(this) + (this.forceParenthesis ? "f" : "") + (this.orderingTerms != null ? "o" : "") + " ");
    sb.append(this.anchor.toString());
    sb.append(", ");
    sb.append(this.combined.stream().map(c -> c.toString()).collect(Collectors.joining(", ")));
    sb.append("]");
    return sb.toString();
  }

  @Override
  protected void log(ToString t) {
    t.printObject(this, "combined");
    t.indent();
    this.anchor.log(t);
    t.unindent();
  }

}
