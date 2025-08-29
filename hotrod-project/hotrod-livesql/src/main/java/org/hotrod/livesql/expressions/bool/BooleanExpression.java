package org.hotrod.livesql.expressions.bool;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.subqueries.BooleanSubqueryExpression;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.livesql.util.BoxUtil;

public abstract class BooleanExpression extends ComparableExpression {

  protected BooleanExpression(final int precedence) {
    super(precedence);
  }

  @Override
  protected Expression asSubqueryExpression(final Subquery subquery, final String alias) {
    return new BooleanSubqueryExpression(subquery, alias, this);
  }

  // Coalesce

  public BooleanSyntaxExpression coalesce(final BooleanExpression a) {
    return new BooleanCoalesce(this, a);
  }

  public BooleanSyntaxExpression coalesce(final Boolean a) {
    return new BooleanCoalesce(this, BoxUtil.box(a));
  }

  // NullIf

  public BooleanSyntaxExpression nullIf(final BooleanExpression a) {
    return new BooleanNullIf(this, a);
  }

  public BooleanSyntaxExpression nullIf(final Boolean a) {
    return new BooleanNullIf(this, BoxUtil.box(a));
  }

  // Predicate operators

  public BooleanSyntaxExpression and(final BooleanExpression p) {
    return new And(this, p);
  }

  public BooleanSyntaxExpression andNot(final BooleanExpression p) {
    return new And(this, new Not(p));
  }

  public BooleanSyntaxExpression or(final BooleanExpression p) {
    return new Or(this, p);
  }

  public BooleanSyntaxExpression orNot(final BooleanExpression p) {
    return new Or(this, new Not(p));
  }

  // Scalar comparisons

  // Equal

  public BooleanSyntaxExpression eq(final BooleanExpression e) {
    return new Equal(this, e);
  }

  public BooleanSyntaxExpression eq(final Boolean value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public BooleanSyntaxExpression ne(final BooleanExpression e) {
    return new NotEqual(this, e);
  }

  public BooleanSyntaxExpression ne(final Boolean value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public BooleanSyntaxExpression gt(final BooleanExpression e) {
    return new GreaterThan(this, e);
  }

  public BooleanSyntaxExpression gt(final Boolean value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public BooleanSyntaxExpression ge(final BooleanExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public BooleanSyntaxExpression ge(final Boolean value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public BooleanSyntaxExpression lt(final BooleanExpression e) {
    return new LessThan(this, e);
  }

  public BooleanSyntaxExpression lt(final Boolean value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public BooleanSyntaxExpression le(final BooleanExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public BooleanSyntaxExpression le(final Boolean value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public BooleanSyntaxExpression between(final BooleanExpression from, final BooleanExpression to) {
    return new Between(this, from, to);
  }

  public BooleanSyntaxExpression between(final BooleanExpression from, final Boolean to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public BooleanSyntaxExpression between(final Boolean from, final BooleanExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public BooleanSyntaxExpression between(final Boolean from, final Boolean to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public BooleanSyntaxExpression notBetween(final BooleanExpression from, final BooleanExpression to) {
    return new NotBetween<BooleanExpression>(this, from, to);
  }

  public BooleanSyntaxExpression notBetween(final BooleanExpression from, final Boolean to) {
    return new NotBetween<BooleanExpression>(this, from, BoxUtil.box(to));
  }

  public BooleanSyntaxExpression notBetween(final Boolean from, final BooleanExpression to) {
    return new NotBetween<BooleanExpression>(this, BoxUtil.box(from), to);
  }

  public BooleanSyntaxExpression notBetween(final Boolean from, Boolean to) {
    return new NotBetween<BooleanExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final BooleanSyntaxExpression in(final BooleanExpression... values) {
    return new InList<BooleanExpression>(this, Arrays.asList(values));
  }

  public final BooleanSyntaxExpression in(final Boolean... values) {
    return new InList<BooleanExpression>(this, Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final BooleanSyntaxExpression notIn(final BooleanExpression... values) {
    return new NotInList<BooleanExpression>(this, Arrays.asList(values));
  }

  public final BooleanSyntaxExpression notIn(final Boolean... values) {
    return new NotInList<BooleanExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

}
