package org.hotrod.livesql.expressions.predicates;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.livesql.queries.subqueries.SubqueryBooleanRefColumn;
import org.hotrod.livesql.util.BoxUtil;

public abstract class GeneralBooleanExpression extends ComparableExpression {

  protected GeneralBooleanExpression(final int precedence) {
    super(precedence);
  }

  @Override
  protected Expression asSubqueryExpression(final Subquery subquery, final String alias) {
    return new SubqueryBooleanRefColumn(subquery, alias, this);
  }

  // Coalesce

  public Predicate coalesce(final GeneralBooleanExpression a) {
    return new BooleanCoalesce(this, a);
  }

  public Predicate coalesce(final Boolean a) {
    return new BooleanCoalesce(this, BoxUtil.box(a));
  }

  // NullIf

  public Predicate nullIf(final GeneralBooleanExpression a) {
    return new BooleanNullIf(this, a);
  }

  public Predicate nullIf(final Boolean a) {
    return new BooleanNullIf(this, BoxUtil.box(a));
  }

  // Predicate operators

  public Predicate and(final GeneralBooleanExpression p) {
    return new And(this, p);
  }

  public Predicate andNot(final GeneralBooleanExpression p) {
    return new And(this, new Not(p));
  }

  public Predicate or(final GeneralBooleanExpression p) {
    return new Or(this, p);
  }

  public Predicate orNot(final GeneralBooleanExpression p) {
    return new Or(this, new Not(p));
  }

  // Scalar comparisons

  // Equal

  public Predicate eq(final GeneralBooleanExpression e) {
    return new Equal(this, e);
  }

  public Predicate eq(final Boolean value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public Predicate ne(final GeneralBooleanExpression e) {
    return new NotEqual(this, e);
  }

  public Predicate ne(final Boolean value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public Predicate gt(final GeneralBooleanExpression e) {
    return new GreaterThan(this, e);
  }

  public Predicate gt(final Boolean value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public Predicate ge(final GeneralBooleanExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public Predicate ge(final Boolean value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public Predicate lt(final GeneralBooleanExpression e) {
    return new LessThan(this, e);
  }

  public Predicate lt(final Boolean value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public Predicate le(final GeneralBooleanExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public Predicate le(final Boolean value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public Predicate between(final GeneralBooleanExpression from, final GeneralBooleanExpression to) {
    return new Between(this, from, to);
  }

  public Predicate between(final GeneralBooleanExpression from, final Boolean to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public Predicate between(final Boolean from, final GeneralBooleanExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public Predicate between(final Boolean from, final Boolean to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public Predicate notBetween(final GeneralBooleanExpression from, final GeneralBooleanExpression to) {
    return new NotBetween<GeneralBooleanExpression>(this, from, to);
  }

  public Predicate notBetween(final GeneralBooleanExpression from, final Boolean to) {
    return new NotBetween<GeneralBooleanExpression>(this, from, BoxUtil.box(to));
  }

  public Predicate notBetween(final Boolean from, final GeneralBooleanExpression to) {
    return new NotBetween<GeneralBooleanExpression>(this, BoxUtil.box(from), to);
  }

  public Predicate notBetween(final Boolean from, Boolean to) {
    return new NotBetween<GeneralBooleanExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final Predicate in(final GeneralBooleanExpression... values) {
    return new InList<GeneralBooleanExpression>(this, Arrays.asList(values));
  }

  public final Predicate in(final Boolean... values) {
    return new InList<GeneralBooleanExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final Predicate notIn(final GeneralBooleanExpression... values) {
    return new NotInList<GeneralBooleanExpression>(this, Arrays.asList(values));
  }

  public final Predicate notIn(final Boolean... values) {
    return new NotInList<GeneralBooleanExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

}
