package org.hotrod.runtime.sql.expressions;

import java.util.Date;

import org.hotrod.runtime.sql.ExecutableSelect;
import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.exceptions.InvalidSQLStatementException;
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
import org.hotrod.runtime.sql.expressions.predicates.Like;
import org.hotrod.runtime.sql.expressions.predicates.NotBetween;
import org.hotrod.runtime.sql.expressions.predicates.NotEqual;
import org.hotrod.runtime.sql.expressions.predicates.NotLike;
import org.hotrod.runtime.sql.expressions.predicates.Predicate;

public abstract class Expression<T> implements ResultSetColumn {

  /**
   * <pre>
   * Precedence  Operator
   * ----------  ------------------
   *         11  and
   *          6  between
   *         --  binary operator
   *          6  =
   *          6  >
   *          6  >=
   *          6  is null
   *          6  is not null
   *          6  <
   *          6  <=
   *          6  like
   *         10  not
   *          6  not between
   *          6  <>
   *          6  not like
   *         --  operand
   *         12  or
   *         --  expression
   *          1  literal value, column
   *          2  function
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

  // Scalar comparisons

  // String
  // Character
  // Number (byte, short, int, long, float, double, BigInteger, BigDecimal)
  // java.util.Date
  // Boolean
  // Object

  // Boxing

  private Expression<T> box(final T value) {
    if (value instanceof String) {
      return new Constant<T>(value, JDBCType.VARCHAR);
    } else if (value instanceof Character) {
      return new Constant<T>(value, JDBCType.VARCHAR);
    } else if (value instanceof Number) {
      return new Constant<T>(value, JDBCType.NUMERIC);
    } else if (value instanceof Date) {
      return new Constant<T>(value, JDBCType.TIMESTAMP);
    } else if (value instanceof Boolean) {
      return new Constant<T>(value, JDBCType.BOOLEAN);
    } else {
      throw new InvalidSQLStatementException("Invalid expression type: " + value.getClass());
    }
  }

  private Expression<String> box(final String value) {
    return new Constant<String>(value, JDBCType.VARCHAR);
  }

  @SuppressWarnings("unused")
  private Expression<Character> box(final Character value) {
    return new Constant<Character>(value, JDBCType.VARCHAR);
  }

  @SuppressWarnings("unused")
  private Expression<Number> box(final Number value) {
    return new Constant<Number>(value, JDBCType.NUMERIC);
  }

  @SuppressWarnings("unused")
  private Expression<Date> box(final Date value) {
    return new Constant<Date>(value, JDBCType.TIMESTAMP);
  }

  @SuppressWarnings("unused")
  private Expression<Boolean> box(final Boolean value) {
    return new Constant<Boolean>(value, JDBCType.BOOLEAN);
  }

  // Equal

  public Predicate eq(final Expression<T> e) {
    return new Equal(this, e);
  }

  public Predicate eq(final T value) {
    return new Equal(this, box(value));
  }

  // Not Equal

  public Predicate ne(final Expression<T> e) {
    return new NotEqual(this, e);
  }

  public Predicate ne(final T value) {
    return new NotEqual(this, box(value));
  }

  // Greater Than

  public Predicate gt(final Expression<T> e) {
    return new GreaterThan(this, e);
  }

  public Predicate gt(final T value) {
    return new GreaterThan(this, box(value));
  }

  // Greater Than or Equal To

  public Predicate ge(final Expression<T> e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public Predicate ge(final T value) {
    return new GreaterThanOrEqualTo(this, box(value));
  }

  // Less Than

  public Predicate lt(final Expression<T> e) {
    return new LessThan(this, e);
  }

  public Predicate lt(final T value) {
    return new LessThan(this, box(value));
  }

  // Less Than or Equal To

  public Predicate le(final Expression<T> e) {
    return new LessThanOrEqualTo(this, e);
  }

  public Predicate le(final T value) {
    return new LessThanOrEqualTo(this, box(value));
  }

  // Between

  public Predicate between(final Expression<T> from, final Expression<T> to) {
    return new Between(this, from, to);
  }

  public Predicate between(final Expression<T> from, final T to) {
    return new Between(this, from, box(to));
  }

  public Predicate between(final T from, final Expression<T> to) {
    return new Between(this, box(from), to);
  }

  public Predicate between(final T from, final T to) {
    return new Between(this, box(from), box(to));
  }

  // Not Between

  public Predicate notBetween(final Expression<T> from, final Expression<T> to) {
    return new NotBetween(this, from, to);
  }

  public Predicate notBetween(final Expression<T> from, final T to) {
    return new NotBetween(this, from, box(to));
  }

  public Predicate notBetween(final T from, final Expression<T> to) {
    return new NotBetween(this, box(from), to);
  }

  public Predicate notBetween(final T from, T to) {
    return new NotBetween(this, box(from), box(to));
  }

  // Like

  public Predicate like(final Expression<String> e) {
    return new Like(this, e);
  }

  public Predicate like(final String value) {
    return new Like(this, box(value));
  }

  // Like escape

  public Predicate like(final Expression<String> e, final Expression<String> escape) {
    return new Like(this, e, escape);
  }

  public Predicate like(final Expression<String> e, final String escape) {
    return new Like(this, e, box(escape));
  }

  public Predicate like(final String e, final Expression<String> escape) {
    return new Like(this, box(e), escape);
  }

  public Predicate like(final String e, final String escape) {
    return new Like(this, box(e), box(escape));
  }

  // Not Like

  public Predicate notLike(final Expression<String> e) {
    return new NotLike(this, e);
  }

  public Predicate notLike(final String e) {
    return new NotLike(this, box(e));
  }

  // Not like escape

  public Predicate notLike(final Expression<T> e, final Expression<T> escape) {
    return new NotLike(this, e, escape);
  }

  public Predicate notLike(final Expression<T> e, final T escape) {
    return new NotLike(this, e, box(escape));
  }

  public Predicate notLike(final T e, final Expression<T> escape) {
    return new NotLike(this, box(e), escape);
  }

  public Predicate notLike(final T e, final T escape) {
    return new NotLike(this, box(e), box(escape));
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

  // public static List<Expression<?>> toNonEmptyList(final String errorMessage,
  // final Expression<?>... expressions) {
  // if (expressions == null) {
  // throw new InvalidSQLStatementException(errorMessage);
  // }
  // return Arrays.asList(expressions);
  // }

  // public static List<Expression<?>> toList(final Expression<?> expression) {
  // List<Expression<?>> l = new ArrayList<Expression<?>>();
  // l.add(expression);
  // return l;
  // }

}
