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
   *         15  case
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

  // public Expression<T> box(final T value) {
  // if (value instanceof String) {
  // return new Constant<T>(value, JDBCType.VARCHAR);
  // } else if (value instanceof Character) {
  // return new Constant<T>(value, JDBCType.VARCHAR);
  // } else if (value instanceof Number) {
  // return new Constant<T>(value, JDBCType.NUMERIC);
  // } else if (value instanceof Date) {
  // return new Constant<T>(value, JDBCType.TIMESTAMP);
  // } else if (value instanceof Boolean) {
  // return new Constant<T>(value, JDBCType.BOOLEAN);
  // } else {
  // throw new InvalidSQLStatementException("Invalid expression type: " +
  // value.getClass());
  // }
  // }

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
