package org.hotrod.runtime.livesql.expressions;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.metadata.MDHelper;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;
import org.hotrod.runtime.livesql.queries.select.SHelper;
import org.hotrod.runtime.livesql.queries.select.Select;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.runtime.livesql.queries.subqueries.EmergingColumn;
import org.hotrod.runtime.livesql.queries.subqueries.QHelper;
import org.hotrod.runtime.livesql.queries.subqueries.Subquery;

public abstract class Expression extends ResultSetColumn {

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
  private String alias;
  private TypeHandler typeHandler;

  protected void setPrecedence(final int precedence) {
    this.precedence = precedence;
  }

  // Constructors

  protected Expression(final int precedence) {
    this.precedence = precedence;
    this.alias = null;
    this.typeHandler = null;
  }

  // ResultSetColumn

  protected Expression getExpression() {
    return this;
  }

  protected final List<Expression> unwrap() {
    return null;
  }

  // Getters & Setters

  protected String getAlias() {
    return alias;
  }

  protected int getPrecedence() {
    return precedence;
  }

  protected void setAlias(String alias) {
    this.alias = alias;
  }

  protected void setTypeHandler(TypeHandler typeHandler) {
    this.typeHandler = typeHandler;
  }

  // Getters

  protected TypeHandler getTypeHandler() {
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
    this.subqueries.add(SHelper.getCombinedSelect(subquery));
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
      SHelper.validateTableReferences(t, tableReferences, ag);
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

  public EmergingColumn asEmergingColumnOf(final Subquery s) {
    return new EmergingColumn(QHelper.getName(s), this.alias, this.typeHandler);
  }

  public EmergingColumn asEmergingColumnOf(final TableOrView tov) {
    return new EmergingColumn(MDHelper.getAliasName(tov), this.alias, this.typeHandler);
  }

}
