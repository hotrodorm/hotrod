package org.hotrod.runtime.sql.expressions;

import org.hotrod.runtime.sql.ExecutableSelect;
import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.SQL;
import org.hotrod.runtime.sql.expressions.asymmetric.EqAll;
import org.hotrod.runtime.sql.expressions.asymmetric.EqAny;
import org.hotrod.runtime.sql.expressions.asymmetric.GeAll;
import org.hotrod.runtime.sql.expressions.asymmetric.GeAny;
import org.hotrod.runtime.sql.expressions.asymmetric.GtAll;
import org.hotrod.runtime.sql.expressions.asymmetric.GtAny;
import org.hotrod.runtime.sql.expressions.asymmetric.In;
import org.hotrod.runtime.sql.expressions.asymmetric.LeAll;
import org.hotrod.runtime.sql.expressions.asymmetric.LeAny;
import org.hotrod.runtime.sql.expressions.asymmetric.LtAll;
import org.hotrod.runtime.sql.expressions.asymmetric.LtAny;
import org.hotrod.runtime.sql.expressions.asymmetric.NeAll;
import org.hotrod.runtime.sql.expressions.asymmetric.NeAny;
import org.hotrod.runtime.sql.expressions.asymmetric.NotIn;
import org.hotrod.runtime.sql.expressions.predicates.Between;
import org.hotrod.runtime.sql.expressions.predicates.Equal;
import org.hotrod.runtime.sql.expressions.predicates.GreaterThan;
import org.hotrod.runtime.sql.expressions.predicates.GreaterThanOrEqualTo;
import org.hotrod.runtime.sql.expressions.predicates.IsNotNull;
import org.hotrod.runtime.sql.expressions.predicates.IsNull;
import org.hotrod.runtime.sql.expressions.predicates.LessThan;
import org.hotrod.runtime.sql.expressions.predicates.LessThanOrEqualTo;
import org.hotrod.runtime.sql.expressions.predicates.NotBetween;
import org.hotrod.runtime.sql.expressions.predicates.NotEqual;
import org.hotrod.runtime.sql.expressions.predicates.Predicate;
import org.hotrod.runtime.sql.ordering.OrderByDirectionStage;

public abstract class Expression<T> implements ResultSetColumn {

  public static final int PRECEDENCE_LITERAL = 1;
  public static final int PRECEDENCE_COLUMN = 1;
  public static final int PRECEDENCE_PARENTHESIS = 1;

  public static final int PRECEDENCE_CASE = 2;
  public static final int PRECEDENCE_FUNCTION = 2;
  public static final int PRECEDENCE_TUPLE = 2;

  public static final int PRECEDENCE_MULT_DIV_MOD = 3;

  public static final int PRECEDENCE_PLUS_MINUS = 4;

  public static final int PRECEDENCE_BETWEEN = 6;
  public static final int PRECEDENCE_EQ_NE_LT_LE_GT_GE = 6;
  public static final int PRECEDENCE_LIKE = 6;
  public static final int PRECEDENCE_IN = 6;
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

  // Column ordering

  public final OrderByDirectionStage asc() {
    return new OrderByDirectionStage(this, true);
  }

  public final OrderByDirectionStage desc() {
    return new OrderByDirectionStage(this, false);
  }

  // Scalar comparisons

  // Equal

  public Predicate eq(final Expression<T> e) {
    return new Equal(this, e);
  }

  public Predicate eq(final T value) {
    return new Equal(this, SQL.box(value));
  }

  // Not Equal

  public Predicate ne(final Expression<T> e) {
    return new NotEqual(this, e);
  }

  public Predicate ne(final T value) {
    return new NotEqual(this, SQL.box(value));
  }

  // Greater Than

  public Predicate gt(final Expression<T> e) {
    return new GreaterThan(this, e);
  }

  public Predicate gt(final T value) {
    return new GreaterThan(this, SQL.box(value));
  }

  // Greater Than or Equal To

  public Predicate ge(final Expression<T> e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public Predicate ge(final T value) {
    return new GreaterThanOrEqualTo(this, SQL.box(value));
  }

  // Less Than

  public Predicate lt(final Expression<T> e) {
    return new LessThan(this, e);
  }

  public Predicate lt(final T value) {
    return new LessThan(this, SQL.box(value));
  }

  // Less Than or Equal To

  public Predicate le(final Expression<T> e) {
    return new LessThanOrEqualTo(this, e);
  }

  public Predicate le(final T value) {
    return new LessThanOrEqualTo(this, SQL.box(value));
  }

  // Between

  public Predicate between(final Expression<T> from, final Expression<T> to) {
    return new Between(this, from, to);
  }

  public Predicate between(final Expression<T> from, final T to) {
    return new Between(this, from, SQL.box(to));
  }

  public Predicate between(final T from, final Expression<T> to) {
    return new Between(this, SQL.box(from), to);
  }

  public Predicate between(final T from, final T to) {
    return new Between(this, SQL.box(from), SQL.box(to));
  }

  // Not Between

  public Predicate notBetween(final Expression<T> from, final Expression<T> to) {
    return new NotBetween(this, from, to);
  }

  public Predicate notBetween(final Expression<T> from, final T to) {
    return new NotBetween(this, from, SQL.box(to));
  }

  public Predicate notBetween(final T from, final Expression<T> to) {
    return new NotBetween(this, SQL.box(from), to);
  }

  public Predicate notBetween(final T from, T to) {
    return new NotBetween(this, SQL.box(from), SQL.box(to));
  }

  // Is Null and Is Not Null

  public Predicate isNotNull() {
    return new IsNotNull(this);
  }

  public Predicate isNull() {
    return new IsNull(this);
  }

  // In

  public Predicate in(final ExecutableSelect subquery) {
    return new In(this, subquery);
  }

  public Predicate notIn(final ExecutableSelect subquery) {
    return new NotIn(this, subquery);
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
