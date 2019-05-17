package org.hotrod.runtime.livesql.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.hotrod.runtime.livesql.expressions.general.Coalesce;
import org.hotrod.runtime.livesql.expressions.general.Constant;
import org.hotrod.runtime.livesql.expressions.predicates.Between;
import org.hotrod.runtime.livesql.expressions.predicates.Equal;
import org.hotrod.runtime.livesql.expressions.predicates.GreaterThan;
import org.hotrod.runtime.livesql.expressions.predicates.GreaterThanOrEqualTo;
import org.hotrod.runtime.livesql.expressions.predicates.InList;
import org.hotrod.runtime.livesql.expressions.predicates.IsNotNull;
import org.hotrod.runtime.livesql.expressions.predicates.IsNull;
import org.hotrod.runtime.livesql.expressions.predicates.LessThan;
import org.hotrod.runtime.livesql.expressions.predicates.LessThanOrEqualTo;
import org.hotrod.runtime.livesql.expressions.predicates.NotBetween;
import org.hotrod.runtime.livesql.expressions.predicates.NotEqual;
import org.hotrod.runtime.livesql.expressions.predicates.NotInList;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.ordering.OrderByDirectionStage;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.util.BoxUtil;

public abstract class Expression<T> implements ResultSetColumn {

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

  // Constructor

  protected Expression(final int precedence) {
    this.precedence = precedence;
  }

  // Getters

  protected int getPrecedence() {
    return precedence;
  }

  // Apply aliases

  public abstract void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag);

  public abstract void designateAliases(final AliasGenerator ag);

  // Column ordering

  public final OrderByDirectionStage asc() {
    return new OrderByDirectionStage(this, true);
  }

  public final OrderByDirectionStage desc() {
    return new OrderByDirectionStage(this, false);
  }

  // General

  public Expression<T> coalesce(final Expression<T> a) {
    @SuppressWarnings("unchecked")
    Coalesce<T> coalesce = new Coalesce<T>(this, a);
    return coalesce;
  }

  public Expression<T> coalesce(final T a) {
    @SuppressWarnings("unchecked")
    Coalesce<T> coalesce = new Coalesce<T>(this, new Constant<T>(a));
    return coalesce;
  }

  // Scalar comparisons

  // Equal

  public Predicate eq(final Expression<T> e) {
    return new Equal(this, e);
  }

  public Predicate eq(final T value) {
    return new Equal(this, BoxUtil.boxTyped(value));
  }

  // Not Equal

  public Predicate ne(final Expression<T> e) {
    return new NotEqual(this, e);
  }

  public Predicate ne(final T value) {
    return new NotEqual(this, BoxUtil.boxTyped(value));
  }

  // Greater Than

  public Predicate gt(final Expression<T> e) {
    return new GreaterThan(this, e);
  }

  public Predicate gt(final T value) {
    return new GreaterThan(this, BoxUtil.boxTyped(value));
  }

  // Greater Than or Equal To

  public Predicate ge(final Expression<T> e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public Predicate ge(final T value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.boxTyped(value));
  }

  // Less Than

  public Predicate lt(final Expression<T> e) {
    return new LessThan(this, e);
  }

  public Predicate lt(final T value) {
    return new LessThan(this, BoxUtil.boxTyped(value));
  }

  // Less Than or Equal To

  public Predicate le(final Expression<T> e) {
    return new LessThanOrEqualTo(this, e);
  }

  public Predicate le(final T value) {
    return new LessThanOrEqualTo(this, BoxUtil.boxTyped(value));
  }

  // Between

  public Predicate between(final Expression<T> from, final Expression<T> to) {
    return new Between(this, from, to);
  }

  public Predicate between(final Expression<T> from, final T to) {
    return new Between(this, from, BoxUtil.boxTyped(to));
  }

  public Predicate between(final T from, final Expression<T> to) {
    return new Between(this, BoxUtil.boxTyped(from), to);
  }

  public Predicate between(final T from, final T to) {
    return new Between(this, BoxUtil.boxTyped(from), BoxUtil.boxTyped(to));
  }

  // Not Between

  public Predicate notBetween(final Expression<T> from, final Expression<T> to) {
    return new NotBetween(this, from, to);
  }

  public Predicate notBetween(final Expression<T> from, final T to) {
    return new NotBetween(this, from, BoxUtil.boxTyped(to));
  }

  public Predicate notBetween(final T from, final Expression<T> to) {
    return new NotBetween(this, BoxUtil.boxTyped(from), to);
  }

  public Predicate notBetween(final T from, T to) {
    return new NotBetween(this, BoxUtil.boxTyped(from), BoxUtil.boxTyped(to));
  }

  // Is Null and Is Not Null

  public Predicate isNotNull() {
    return new IsNotNull(this);
  }

  public Predicate isNull() {
    return new IsNull(this);
  }

  // In list

  public Predicate in(final Expression<T>... values) {
    return new InList<T>(this, Arrays.asList(values));
  }

  public Predicate in(final T... values) {
    List<Expression<T>> list = new ArrayList<Expression<T>>();
    for (T t : values) {
      list.add(BoxUtil.boxTyped(t));
    }
    return new InList<T>(this, list);
  }

  public Predicate notIn(final Expression<T>... values) {
    return new NotInList<T>(this, Arrays.asList(values));
  }

  public Predicate notIn(final T... values) {
    List<Expression<T>> list = new ArrayList<Expression<T>>();
    for (T t : values) {
      list.add(BoxUtil.boxTyped(t));
    }
    return new NotInList<T>(this, list);
  }

  // In subquery

  public Predicate in(final ExecutableSelect subquery) {
    return new InSubquery(this, subquery);
  }

  public Predicate notIn(final ExecutableSelect subquery) {
    return new NotInSubquery(this, subquery);
  }

  // Any

  public Predicate eqAny(final ExecutableSelect subquery) {
    return new EqAny(this, subquery);
  }

  public Predicate neAny(final ExecutableSelect subquery) {
    return new NeAny(this, subquery);
  }

  public Predicate ltAny(final ExecutableSelect subquery) {
    return new LtAny(this, subquery);
  }

  public Predicate leAny(final ExecutableSelect subquery) {
    return new LeAny(this, subquery);
  }

  public Predicate gtAny(final ExecutableSelect subquery) {
    return new GtAny(this, subquery);
  }

  public Predicate geAny(final ExecutableSelect subquery) {
    return new GeAny(this, subquery);
  }

  // All

  public Predicate eqAll(final ExecutableSelect subquery) {
    return new EqAll(this, subquery);
  }

  public Predicate neAll(final ExecutableSelect subquery) {
    return new NeAll(this, subquery);
  }

  public Predicate ltAll(final ExecutableSelect subquery) {
    return new LtAll(this, subquery);
  }

  public Predicate leAll(final ExecutableSelect subquery) {
    return new LeAll(this, subquery);
  }

  public Predicate gtAll(final ExecutableSelect subquery) {
    return new GtAll(this, subquery);
  }

  public Predicate geAll(final ExecutableSelect subquery) {
    return new GeAll(this, subquery);
  }

  // Aliasing

  public AliasedExpression as(final String alias) {
    return new AliasedExpression(this, alias);
  }

  // Rendering

  protected void renderInner(final Expression<?> inner, final QueryWriter w) {
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

}
