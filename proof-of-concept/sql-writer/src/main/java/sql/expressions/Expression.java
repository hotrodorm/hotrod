package sql.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sql.ExecutableSelect;
import sql.QueryWriter;
import sql.SelectSubquery;
import sql.exceptions.InvalidSQLStatementException;
import sql.expressions.predicates.Between;
import sql.expressions.predicates.Equal;
import sql.expressions.predicates.GreaterThan;
import sql.expressions.predicates.GreaterThanOrEqualTo;
import sql.expressions.predicates.In;
import sql.expressions.predicates.IsNotNull;
import sql.expressions.predicates.IsNull;
import sql.expressions.predicates.LessThan;
import sql.expressions.predicates.LessThanOrEqualTo;
import sql.expressions.predicates.Like;
import sql.expressions.predicates.NotBetween;
import sql.expressions.predicates.NotEqual;
import sql.expressions.predicates.NotIn;
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

  // Grammar

  public Predicate between(final Expression from, final Expression to) {
    return new Between(this, from, to);
  }

  public Predicate equals(final Expression e) {
    return new Equal(this, e);
  }

  public Predicate greaterThan(final Expression e) {
    return new GreaterThan(this, e);
  }

  public Predicate greaterThanOrEqualTo(final Expression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public Predicate isNotNull(final Expression e) {
    return new IsNotNull(this);
  }

  public Predicate isNull(final Expression e) {
    return new IsNull(this);
  }

  public Predicate lessThan(final Expression e) {
    return new LessThan(this, e);
  }

  public Predicate lessThanOrEqualTo(final Expression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public Predicate like(final Expression e) {
    return new Like(this, e);
  }

  public Predicate notBetween(final Expression from, final Expression to) {
    return new NotBetween(this, from, to);
  }

  public Predicate notEqual(final Expression e) {
    return new NotEqual(this, e);
  }

  public Predicate notLike(final Expression e) {
    return new NotLike(this, e);
  }

  public Expression in(final ExecutableSelect s) {
    return new In(this, s);
  }

  public Predicate notIn(final ExecutableSelect s) {
    return new NotIn(this, s);
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
