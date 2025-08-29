package org.hotrod.runtime.livesql.expressions.predicates;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.bool.And;
import org.hotrod.livesql.expressions.bool.Between;
import org.hotrod.livesql.expressions.bool.BooleanCoalesce;
import org.hotrod.livesql.expressions.bool.BooleanNullIf;
import org.hotrod.livesql.expressions.bool.BooleanSyntaxExpression;
import org.hotrod.livesql.expressions.bool.Equal;
import org.hotrod.livesql.expressions.bool.GreaterThan;
import org.hotrod.livesql.expressions.bool.GreaterThanOrEqualTo;
import org.hotrod.livesql.expressions.bool.InList;
import org.hotrod.livesql.expressions.bool.LessThan;
import org.hotrod.livesql.expressions.bool.LessThanOrEqualTo;
import org.hotrod.livesql.expressions.bool.Not;
import org.hotrod.livesql.expressions.bool.NotBetween;
import org.hotrod.livesql.expressions.bool.NotEqual;
import org.hotrod.livesql.expressions.bool.NotInList;
import org.hotrod.livesql.expressions.bool.Or;
import org.hotrod.livesql.queries.subqueries.BooleanSubqueryExpression;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.livesql.util.BoxUtil;

public abstract class Predicate extends ComparableExpression {

  protected Predicate(final int precedence) {
    super(precedence);
  }

  @Override
  protected Expression asSubqueryExpression(final Subquery subquery, final String alias) {
    return new BooleanSubqueryExpression(subquery, alias, this);
  }

  // Coalesce

  public BooleanSyntaxExpression coalesce(final Predicate a) {
    return new BooleanCoalesce(this, a);
  }

  public BooleanSyntaxExpression coalesce(final Boolean a) {
    return new BooleanCoalesce(this, BoxUtil.box(a));
  }

  // NullIf

  public BooleanSyntaxExpression nullIf(final Predicate a) {
    return new BooleanNullIf(this, a);
  }

  public BooleanSyntaxExpression nullIf(final Boolean a) {
    return new BooleanNullIf(this, BoxUtil.box(a));
  }

  // Predicate operators

  public BooleanSyntaxExpression and(final Predicate p) {
    return new And(this, p);
  }

  public BooleanSyntaxExpression andNot(final Predicate p) {
    return new And(this, new Not(p));
  }

  public BooleanSyntaxExpression or(final Predicate p) {
    return new Or(this, p);
  }

  public BooleanSyntaxExpression orNot(final Predicate p) {
    return new Or(this, new Not(p));
  }

  // Scalar comparisons

  // Equal

  public BooleanSyntaxExpression eq(final Predicate e) {
    return new Equal(this, e);
  }

  public BooleanSyntaxExpression eq(final Boolean value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public BooleanSyntaxExpression ne(final Predicate e) {
    return new NotEqual(this, e);
  }

  public BooleanSyntaxExpression ne(final Boolean value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public BooleanSyntaxExpression gt(final Predicate e) {
    return new GreaterThan(this, e);
  }

  public BooleanSyntaxExpression gt(final Boolean value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public BooleanSyntaxExpression ge(final Predicate e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public BooleanSyntaxExpression ge(final Boolean value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public BooleanSyntaxExpression lt(final Predicate e) {
    return new LessThan(this, e);
  }

  public BooleanSyntaxExpression lt(final Boolean value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public BooleanSyntaxExpression le(final Predicate e) {
    return new LessThanOrEqualTo(this, e);
  }

  public BooleanSyntaxExpression le(final Boolean value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public BooleanSyntaxExpression between(final Predicate from, final Predicate to) {
    return new Between(this, from, to);
  }

  public BooleanSyntaxExpression between(final Predicate from, final Boolean to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public BooleanSyntaxExpression between(final Boolean from, final Predicate to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public BooleanSyntaxExpression between(final Boolean from, final Boolean to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public BooleanSyntaxExpression notBetween(final Predicate from, final Predicate to) {
    return new NotBetween<Predicate>(this, from, to);
  }

  public BooleanSyntaxExpression notBetween(final Predicate from, final Boolean to) {
    return new NotBetween<Predicate>(this, from, BoxUtil.box(to));
  }

  public BooleanSyntaxExpression notBetween(final Boolean from, final Predicate to) {
    return new NotBetween<Predicate>(this, BoxUtil.box(from), to);
  }

  public BooleanSyntaxExpression notBetween(final Boolean from, Boolean to) {
    return new NotBetween<Predicate>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final BooleanSyntaxExpression in(final Predicate... values) {
    return new InList<Predicate>(this, Arrays.asList(values));
  }

  public final BooleanSyntaxExpression in(final Boolean... values) {
    return new InList<Predicate>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final BooleanSyntaxExpression notIn(final Predicate... values) {
    return new NotInList<Predicate>(this, Arrays.asList(values));
  }

  public final BooleanSyntaxExpression notIn(final Boolean... values) {
    return new NotInList<Predicate>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

}
