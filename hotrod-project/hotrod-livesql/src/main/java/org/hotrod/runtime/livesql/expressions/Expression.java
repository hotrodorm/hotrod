package org.hotrod.runtime.livesql.expressions;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.converter.TypeConverter;
import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.predicates.IsNotNull;
import org.hotrod.runtime.livesql.expressions.predicates.IsNull;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.Select;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrodorm.hotrod.utils.SUtil;

public abstract class Expression implements ResultSetColumn, Rendereable, OrderingTerm {

  protected static final int PRECEDENCE_LITERAL = 1;
  protected static final int PRECEDENCE_COLUMN = 1;
  protected static final int PRECEDENCE_PARENTHESIS = 1;

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
  protected Class<?> javaClass;
  protected Class<?> rawClass;
  protected TypeConverter<?, ?> converter;

  protected void setPrecedence(final int precedence) {
    this.precedence = precedence;
    this.javaClass = null;
    this.converter = null;
  }

  // Constructors

  protected Expression(final int precedence) {
    this.precedence = precedence;
  }
//
//  protected Expression(final int precedence, final Class<?> javaClass, final TypeConverter<?, ?> converter) {
//    this.precedence = precedence;
//    this.javaClass = javaClass;
//    this.converter = converter;
//  }

  // Getters

  protected int getPrecedence() {
    return precedence;
  }

//  protected Class<?> getJavaClass() {
//    return javaClass;
//  }
//
//  protected TypeConverter<?, ?> getConverter() {
//    return converter;
//  }

  // Setters

  protected void setClassConverter(final Class<?> javaClass, final Class<?> rawClass,
      final TypeConverter<?, ?> converter) {
    this.javaClass = javaClass;
    this.rawClass = rawClass;
    this.converter = converter;
  }

  // Apply aliases

  private List<Expression> expressions = new ArrayList<>();
  private List<CombinedSelectObject<?>> subqueries = new ArrayList<>();
  private List<TableOrView> tablesOrViews = new ArrayList<>();

  protected void register(final Expression expression) {
    this.expressions.add(expression);
  }

  protected void register(final Select<?> subquery) {
    this.subqueries.add(subquery.getCombinedSelect());
  }

  protected void register(final TableOrView tableOrView) {
    this.tablesOrViews.add(tableOrView);
  }

  public final void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
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
      t.validateTableReferences(tableReferences, ag);
    }
  }

  // Is Null and Is Not Null

  public Predicate isNotNull() {
    return new IsNotNull(this);
  }

  public Predicate isNull() {
    return new IsNull(this);
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

  // Aliasing

  public AliasedExpression as(final String alias) {
    if (SUtil.isEmpty(alias)) {
      throw new LiveSQLException("An alias specified with the .as() method cannot be null");
    }
    return new AliasedExpression(this, alias);
  }

}
