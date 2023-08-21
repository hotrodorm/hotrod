package org.hotrod.runtime.livesql.expressions;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.asymmetric.EqAll;
import org.hotrod.runtime.livesql.expressions.asymmetric.EqAny;
import org.hotrod.runtime.livesql.expressions.asymmetric.GeAll;
import org.hotrod.runtime.livesql.expressions.asymmetric.GeAny;
import org.hotrod.runtime.livesql.expressions.asymmetric.GtAll;
import org.hotrod.runtime.livesql.expressions.asymmetric.GtAny;
import org.hotrod.runtime.livesql.expressions.asymmetric.InSubquery;
import org.hotrod.runtime.livesql.expressions.asymmetric.LeAll;
import org.hotrod.runtime.livesql.expressions.asymmetric.LeAny;
import org.hotrod.runtime.livesql.expressions.asymmetric.LtAll;
import org.hotrod.runtime.livesql.expressions.asymmetric.LtAny;
import org.hotrod.runtime.livesql.expressions.asymmetric.NeAll;
import org.hotrod.runtime.livesql.expressions.asymmetric.NeAny;
import org.hotrod.runtime.livesql.expressions.asymmetric.NotInSubquery;
import org.hotrod.runtime.livesql.expressions.predicates.IsNotNull;
import org.hotrod.runtime.livesql.expressions.predicates.IsNull;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.ordering.OrderByDirectionStage;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrodorm.hotrod.utils.SUtil;

public abstract class Expression implements ResultSetColumn, Rendereable {

//  private static final Logger log = LogManager.getLogger(Expression.class);

  public static final int PRECEDENCE_LITERAL = 1;
  public static final int PRECEDENCE_COLUMN = 1;
  public static final int PRECEDENCE_PARENTHESIS = 1;

  public static final int PRECEDENCE_CASE = 2;
  public static final int PRECEDENCE_FUNCTION = 2;
  public static final int PRECEDENCE_TUPLE = 2;
  public static final int PRECEDENCE_UNARY_MINUS = 2;

  public static final int PRECEDENCE_MULT_DIV_MOD = 3;

  public static final int PRECEDENCE_PLUS_MINUS = 4;

  public static final int PRECEDENCE_BETWEEN = 6;
  public static final int PRECEDENCE_EQ_NE_LT_LE_GT_GE = 6;
  public static final int PRECEDENCE_LIKE = 6;
  public static final int PRECEDENCE_IS_NULL = 6;
  public static final int PRECEDENCE_IN = 6;
  public static final int PRECEDENCE_EXISTS = 6;
  public static final int PRECEDENCE_ANY_ALL_EQ_NE_LT_LE_GT_GE = 6;

  public static final int PRECEDENCE_NOT = 10;
  public static final int PRECEDENCE_AND = 11;
  public static final int PRECEDENCE_OR = 12;

  /**
   * <pre>
   * Precedence  Operator
   * ----------  ------------------
   *          1  literal value, column, parenthesis
   *          2  case
   *          2  function (aggregation, analytic, math, other)
   *          2  tuple
   *          3  * / %
   *          4  + -
   *          6  between, not between
   *          6  = > >= < <= <>
   *          6  is null, is not null
   *          6  like, not like
   *          6  in, not in
   *          6  = > >= < <= <> any/all
   *         10  not
   *         11  and
   *         12  or
   * </pre>
   */

  private int precedence;

  protected void setPrecedence(final int precedence) {
    this.precedence = precedence;
  }

  // Constructor

  protected Expression(final int precedence) {
    this.precedence = precedence;
  }

  // Getters

  public int getPrecedence() {
    return precedence;
  }

  // Apply aliases

  private List<Expression> expressions = new ArrayList<Expression>();
  private List<ExecutableSelect<?>> subqueries = new ArrayList<ExecutableSelect<?>>();
  private List<TableOrView> tablesOrViews = new ArrayList<TableOrView>();

  protected void register(final Expression expression) {
    this.expressions.add(expression);
  }

  protected void register(final ExecutableSelect<?> subquery) {
    this.subqueries.add(subquery);
  }

  protected void register(final TableOrView tableOrView) {
    this.tablesOrViews.add(tableOrView);
  }

  public final void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    for (Expression e : this.expressions) {
      e.validateTableReferences(tableReferences, ag);
    }
    for (ExecutableSelect<?> s : this.subqueries) {
      if (s == null) {
        throw new LiveSQLException("Subquery cannot be null.", null);
      }
      s.validateTableReferences(tableReferences, ag);
    }
    for (TableOrView t : this.tablesOrViews) {
      if (t == null) {
        throw new LiveSQLException("Table referenced in query cannot be null.", null);
      }
      t.validateTableReferences(tableReferences, ag);
    }
  }

  // Column ordering

  public final OrderByDirectionStage asc() {
    return new OrderByDirectionStage(this, true);
  }

  public final OrderByDirectionStage desc() {
    return new OrderByDirectionStage(this, false);
  }

  // Is Null and Is Not Null

  public Predicate isNotNull() {
    return new IsNotNull(this);
  }

  public Predicate isNull() {
    return new IsNull(this);
  }

  // In subquery

  public Predicate in(final ExecutableSelect<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new InSubquery(this, subquery);
  }

  public Predicate notIn(final ExecutableSelect<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new NotInSubquery(this, subquery);
  }

  // Any

  public Predicate eqAny(final ExecutableSelect<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new EqAny(this, subquery);
  }

  public Predicate neAny(final ExecutableSelect<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new NeAny(this, subquery);
  }

  public Predicate ltAny(final ExecutableSelect<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new LtAny(this, subquery);
  }

  public Predicate leAny(final ExecutableSelect<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new LeAny(this, subquery);
  }

  public Predicate gtAny(final ExecutableSelect<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new GtAny(this, subquery);
  }

  public Predicate geAny(final ExecutableSelect<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new GeAny(this, subquery);
  }

  // All

  public Predicate eqAll(final ExecutableSelect<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new EqAll(this, subquery);
  }

  public Predicate neAll(final ExecutableSelect<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new NeAll(this, subquery);
  }

  public Predicate ltAll(final ExecutableSelect<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new LtAll(this, subquery);
  }

  public Predicate leAll(final ExecutableSelect<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new LeAll(this, subquery);
  }

  public Predicate gtAll(final ExecutableSelect<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new GtAll(this, subquery);
  }

  public Predicate geAll(final ExecutableSelect<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new GeAll(this, subquery);
  }

  // Aliasing

  public AliasedExpression as(final String alias) {
    if (SUtil.isEmpty(alias)) {
      throw new LiveSQLException("An alias specified with the .as() method cannot be null");
    }
    return new AliasedExpression(this, alias);
  }

  // Rendering

  protected void renderInner(final Expression inner, final QueryWriter w) {
    boolean parenthesis = inner.getPrecedence() > this.precedence;
    if (parenthesis) {
      w.write("(");
    }
    inner.renderTo(w);
    if (parenthesis) {
      w.write(")");
    }
  }

  public abstract void renderTo(final QueryWriter w);

  public String renderTree() {
    StringBuilder sb = new StringBuilder();
    this.renderTree(sb, 0);
    return sb.toString();
  }

  public void renderTree(final StringBuilder sb, final int level) {
    sb.append(SUtil.getFiller(". ", level) + "+ [" + this.precedence + "] " + this.getClass().getName() + "\n");
    this.expressions.forEach(e -> e.renderTree(sb, level + 1));
    this.subqueries.forEach(e -> e.renderTree(sb, level + 1));
    this.tablesOrViews.forEach(e -> e.renderTree(sb, level + 1));
  }

}
