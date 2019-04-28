package sql.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import sql.ExecutableSelect;
import sql.QueryWriter;
import sql.exceptions.InvalidSQLStatementException;
import sql.expressions.asymmetrical.EqAll;
import sql.expressions.asymmetrical.EqAny;
import sql.expressions.asymmetrical.GeAll;
import sql.expressions.asymmetrical.GeAny;
import sql.expressions.asymmetrical.GtAll;
import sql.expressions.asymmetrical.GtAny;
import sql.expressions.asymmetrical.In;
import sql.expressions.asymmetrical.LeAll;
import sql.expressions.asymmetrical.LeAny;
import sql.expressions.asymmetrical.LtAll;
import sql.expressions.asymmetrical.LtAny;
import sql.expressions.asymmetrical.NeAll;
import sql.expressions.asymmetrical.NeAny;
import sql.expressions.asymmetrical.NotIn;
import sql.expressions.predicates.Between;
import sql.expressions.predicates.Equal;
import sql.expressions.predicates.GreaterThan;
import sql.expressions.predicates.GreaterThanOrEqualTo;
import sql.expressions.predicates.IsNotNull;
import sql.expressions.predicates.IsNull;
import sql.expressions.predicates.LessThan;
import sql.expressions.predicates.LessThanOrEqualTo;
import sql.expressions.predicates.Like;
import sql.expressions.predicates.NotBetween;
import sql.expressions.predicates.NotEqual;
import sql.expressions.predicates.NotLike;
import sql.expressions.predicates.Predicate;

public abstract class Expression implements ResultSetColumn {

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

  // Equal

  public Predicate eq(final String e) {
    return new Equal(this, new Constant(e));
  }

  public Predicate eq(final Character e) {
    return new Equal(this, new Constant(e));
  }

  public Predicate eq(final Number e) {
    return new Equal(this, new Constant(e));
  }

  public Predicate eq(final Date e) {
    return new Equal(this, new Constant(e));
  }

  public Predicate eq(final Boolean e) {
    return new Equal(this, new Constant(e));
  }

  public Predicate eq(final Object e, final JDBCType type) {
    return new Equal(this, new Constant(e, type));
  }

  public Predicate eq(final Expression e) {
    return new Equal(this, e);
  }

  // Not Equal

  public Predicate ne(final String e) {
    return new NotEqual(this, new Constant(e));
  }

  public Predicate ne(final Character e) {
    return new NotEqual(this, new Constant(e));
  }

  public Predicate ne(final Number e) {
    return new NotEqual(this, new Constant(e));
  }

  public Predicate ne(final Date e) {
    return new NotEqual(this, new Constant(e));
  }

  public Predicate ne(final Boolean e) {
    return new NotEqual(this, new Constant(e));
  }

  public Predicate ne(final Object e, final JDBCType type) {
    return new NotEqual(this, new Constant(e, type));
  }

  public Predicate ne(final Expression e) {
    return new NotEqual(this, e);
  }

  // Greater Than

  public Predicate gt(final String e) {
    return new GreaterThan(this, new Constant(e));
  }

  public Predicate gt(final Character e) {
    return new GreaterThan(this, new Constant(e));
  }

  public Predicate gt(final Number e) {
    return new GreaterThan(this, new Constant(e));
  }

  public Predicate gt(final Date e) {
    return new GreaterThan(this, new Constant(e));
  }

  public Predicate gt(final Boolean e) {
    return new GreaterThan(this, new Constant(e));
  }

  public Predicate gt(final Object e, final JDBCType type) {
    return new GreaterThan(this, new Constant(e, type));
  }

  public Predicate gt(final Expression e) {
    return new GreaterThan(this, e);
  }

  // Greater Than or Equal To

  public Predicate ge(final String e) {
    return new GreaterThanOrEqualTo(this, new Constant(e));
  }

  public Predicate ge(final Character e) {
    return new GreaterThanOrEqualTo(this, new Constant(e));
  }

  public Predicate ge(final Number e) {
    return new GreaterThanOrEqualTo(this, new Constant(e));
  }

  public Predicate ge(final Date e) {
    return new GreaterThanOrEqualTo(this, new Constant(e));
  }

  public Predicate ge(final Boolean e) {
    return new GreaterThanOrEqualTo(this, new Constant(e));
  }

  public Predicate ge(final Object e, final JDBCType type) {
    return new GreaterThanOrEqualTo(this, new Constant(e, type));
  }

  public Predicate ge(final Expression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  // Less Than

  public Predicate lt(final String e) {
    return new LessThan(this, new Constant(e));
  }

  public Predicate lt(final Character e) {
    return new LessThan(this, new Constant(e));
  }

  public Predicate lt(final Number e) {
    return new LessThan(this, new Constant(e));
  }

  public Predicate lt(final Date e) {
    return new LessThan(this, new Constant(e));
  }

  public Predicate lt(final Boolean e) {
    return new LessThan(this, new Constant(e));
  }

  public Predicate lt(final Object e, final JDBCType type) {
    return new LessThan(this, new Constant(e, type));
  }

  public Predicate lt(final Expression e) {
    return new LessThan(this, e);
  }

  // Less Than or Equal To

  public Predicate le(final String e) {
    return new LessThanOrEqualTo(this, new Constant(e));
  }

  public Predicate le(final Character e) {
    return new LessThanOrEqualTo(this, new Constant(e));
  }

  public Predicate le(final Number e) {
    return new LessThanOrEqualTo(this, new Constant(e));
  }

  public Predicate le(final Date e) {
    return new LessThanOrEqualTo(this, new Constant(e));
  }

  public Predicate le(final Boolean e) {
    return new LessThanOrEqualTo(this, new Constant(e));
  }

  public Predicate le(final Object e, final JDBCType type) {
    return new LessThanOrEqualTo(this, new Constant(e, type));
  }

  public Predicate le(final Expression e) {
    return new LessThanOrEqualTo(this, e);
  }

  // Between

  public Predicate between(final String from, final String to) {
    return new Between(this, new Constant(from), new Constant(to));
  }

  public Predicate between(final Character from, final Character to) {
    return new Between(this, new Constant(from), new Constant(to));
  }

  public Predicate between(final Number from, final Number to) {
    return new Between(this, new Constant(from), new Constant(to));
  }

  public Predicate between(final Date from, final Date to) {
    return new Between(this, new Constant(from), new Constant(to));
  }

  public Predicate between(final Boolean from, final Boolean to) {
    return new Between(this, new Constant(from), new Constant(to));
  }

  public Predicate between(final Object from, final Object to, final JDBCType type) {
    return new Between(this, new Constant(from, type), new Constant(to, type));
  }

  public Predicate between(final Expression from, final Expression to) {
    return new Between(this, from, to);
  }

  // Not Between

  public Predicate notBetween(final String from, final String to) {
    return new NotBetween(this, new Constant(from), new Constant(to));
  }

  public Predicate notBetween(final Character from, final Character to) {
    return new NotBetween(this, new Constant(from), new Constant(to));
  }

  public Predicate notBetween(final Number from, final Number to) {
    return new NotBetween(this, new Constant(from), new Constant(to));
  }

  public Predicate notBetween(final Date from, final Date to) {
    return new NotBetween(this, new Constant(from), new Constant(to));
  }

  public Predicate notBetween(final Boolean from, final Boolean to) {
    return new NotBetween(this, new Constant(from), new Constant(to));
  }

  public Predicate notBetween(final Object from, final Object to, final JDBCType type) {
    return new NotBetween(this, new Constant(from, type), new Constant(to, type));
  }

  public Predicate notBetween(final Expression from, final Expression to) {
    return new NotBetween(this, from, to);
  }

  // Like

  public Predicate like(final String e) {
    return new Like(this, new Constant(e));
  }

  public Predicate like(final Expression e) {
    return new Like(this, e);
  }

  // Not Like

  public Predicate notLike(final String e) {
    return new NotLike(this, new Constant(e));
  }

  public Predicate notLike(final Expression e) {
    return new NotLike(this, e);
  }

  // Is Null and Is Not Null

  public Predicate isNotNull() {
    return new IsNotNull(this);
  }

  public Predicate isNull() {
    return new IsNull(this);
  }

  // In

  public Expression in(final ExecutableSelect s) {
    return new In(this, s);
  }

  public Predicate notIn(final ExecutableSelect s) {
    return new NotIn(this, s);
  }

  // Any

  public Expression eqAny(final ExecutableSelect s) {
    return new EqAny(this, s);
  }

  public Expression neAny(final ExecutableSelect s) {
    return new NeAny(this, s);
  }

  public Expression ltAny(final ExecutableSelect s) {
    return new LtAny(this, s);
  }

  public Expression leAny(final ExecutableSelect s) {
    return new LeAny(this, s);
  }

  public Expression gtAny(final ExecutableSelect s) {
    return new GtAny(this, s);
  }

  public Expression geAny(final ExecutableSelect s) {
    return new GeAny(this, s);
  }

  // All

  public Expression eqAll(final ExecutableSelect s) {
    return new EqAll(this, s);
  }

  public Expression neAll(final ExecutableSelect s) {
    return new NeAll(this, s);
  }

  public Expression ltAll(final ExecutableSelect s) {
    return new LtAll(this, s);
  }

  public Expression leAll(final ExecutableSelect s) {
    return new LeAll(this, s);
  }

  public Expression gtAll(final ExecutableSelect s) {
    return new GtAll(this, s);
  }

  public Expression geAll(final ExecutableSelect s) {
    return new GeAll(this, s);
  }

  // Aliasing

  public AliasedExpression as(final String alias) {
    return new AliasedExpression(this, alias);
  }

  // Rendering

  protected void renderInner(final Expression inner, final QueryWriter w) {
    boolean parenthesis = inner.getPrecedence() >= this.precedence;
    if (parenthesis) {
      w.write("(");
    }
    inner.renderTo(w);
    if (parenthesis) {
      w.write(")");
    }
  }

  public abstract void renderTo(final QueryWriter w);

  public static List<Expression> toNonEmptyList(final String errorMessage, final Expression... expressions) {
    if (expressions == null) {
      throw new InvalidSQLStatementException(errorMessage);
    }
    return Arrays.asList(expressions);
  }

  public static List<Expression> toList(final Expression expression) {
    List<Expression> l = new ArrayList<Expression>();
    l.add(expression);
    return l;
  }

}
