package org.hotrod.livesql.expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.select.SShield;
import org.hotrod.livesql.queries.select.Select;
import org.hotrod.livesql.queries.select.TableReferences;
import org.hotrod.livesql.queries.select.UnarySelectObject.AliasGenerator;
import org.hotrod.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.livesql.queries.typesolver.TypeHandler;
import org.hotrod.utils.SUtil;

public abstract class Expression extends ResultSetColumn {

  private static final Logger log = Logger.getLogger(Expression.class.getName());

  protected static final int PRECEDENCE_LITERAL = 1;
  protected static final int PRECEDENCE_COLUMN = 1;
  protected static final int PRECEDENCE_PARENTHESIS = 1;
  protected static final int PRECEDENCE_ALIAS = 1;

  protected static final int PRECEDENCE_CASE = 2;
  protected static final int PRECEDENCE_FUNCTION = 2;
  protected static final int PRECEDENCE_TUPLE = 2;
  protected static final int PRECEDENCE_UNARY_MINUS = 2;

  protected static final int PRECEDENCE_MULT_DIV_MOD = 3;

  protected static final int PRECEDENCE_PLUS_MINUS = 4;

  protected static final int PRECEDENCE_BETWEEN = 6;
  protected static final int PRECEDENCE_EQ_NE_LT_LE_GT_GE = 6;
  protected static final int PRECEDENCE_LIKE = 6;
  protected static final int PRECEDENCE_IS_NULL = 6;
  protected static final int PRECEDENCE_IN = 6;
  protected static final int PRECEDENCE_EXISTS = 6;
  protected static final int PRECEDENCE_ANY_ALL_EQ_NE_LT_LE_GT_GE = 6;

  protected static final int PRECEDENCE_NOT = 10;
  protected static final int PRECEDENCE_AND = 11;
  protected static final int PRECEDENCE_OR = 12;

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
  protected TypeHandler typeHandler;

  protected void setPrecedence(final int precedence) {
    this.precedence = precedence;
  }

  // Constructors

  protected Expression(final int precedence) {
    this.precedence = precedence;
    this.typeHandler = null;
  }

  protected Expression(final Expression expr) {
    this.expressions = expr.expressions;
    this.precedence = expr.precedence;
    this.subqueries = expr.subqueries;
    this.tablesOrViews = expr.tablesOrViews;
    this.typeHandler = expr.typeHandler;
  }

//  protected abstract Expression asSubqueryExpression(final Subquery subquery);
  protected Expression asSubqueryExpression(final Subquery subquery, final String alias) {
    log.info("Expr: " + this);
    throw new UnsupportedOperationException();
  }

  // Aliasing

  public final AliasedExpression as(final String alias) {
    if (SUtil.isEmpty(alias)) {
      throw new LiveSQLException("An alias specified with the .as() method cannot be null");
    }
    return new AliasedExpression(this, alias);
  }

  // Shielded getters

  protected String getReferenceName() {
    return null; // Only Entity columns, AliasedExpressions, and SubqueryColumns return names.
  }

  protected String getProperty() {
    return null; // Only Entity columns and SubqueryColumns return names.
  }

  // ResultSetColumn

  protected Expression getExpression() {
    return this;
  }

  protected Expression getEmergingExpression() {
    return this;
  }

  protected List<Expression> expand() {
    return null;
  }

  // Getters & Setters

  protected int getPrecedence() {
    return precedence;
  }

  protected void setTypeHandler(TypeHandler typeHandler) {
    this.typeHandler = typeHandler;
//    log.info("set type(" + typeHandler + "): " + this + " -- th=" + this.typeHandler);
//    if (typeHandler == null) {
//      log.info("Stack: " + TUtil.compactStackTrace());
//    }
  }

  // Getters

  protected TypeHandler getTypeHandler() {
    log.info(
        "TYPEHANDLER " + this + " (" + this.getProperty() + "/" + this.getReferenceName() + "): " + this.typeHandler);
    return typeHandler;
  }

  // Apply aliases

  private List<Expression> expressions = new ArrayList<>();
  private List<CombinedSelectObject<?>> subqueries = new ArrayList<>();
  private List<TableOrView> tablesOrViews = new ArrayList<>();

  protected void register(final Expression expression) {
    this.expressions.add(expression);
  }

  protected void register(final Select<?> subquery) {
    this.subqueries.add(SShield.getCombinedSelect(subquery));
  }

  protected void register(final TableOrView tableOrView) {
    this.tablesOrViews.add(tableOrView);
  }

  protected final void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    for (Expression e : this.expressions) {
      e.validateTableReferences(tableReferences, ag);
    }
    for (CombinedSelectObject<?> s : this.subqueries) {
      if (s == null) {
        throw new LiveSQLException("Subquery cannot be null.", null);
      }
      s.validateTableReferences(tableReferences, ag);
    }
    for (TableOrView t : this.tablesOrViews) {
      if (t == null) {
        throw new LiveSQLException("Table referenced in query cannot be null.", null);
      }
      SShield.validateTableReferences(t, tableReferences, ag);
    }
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

  protected abstract void renderTo(final QueryWriter w);

  protected String render() {
    return this.getClass().getSimpleName() + "@" + System.identityHashCode(this) + ": typeHandler=" + this.typeHandler;
  }

}
