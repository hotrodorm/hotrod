package org.hotrod.runtime.livesql.expressions.predicates;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.util.BoxUtil;

public abstract class Predicate extends ComparableExpression {

  protected Predicate(final int precedence) {
    super(precedence);
  }

  // Coalesce

  public BooleanFreeExpression coalesce(final Predicate a) {
    return new BooleanCoalesce(this, a);
  }

  public BooleanFreeExpression coalesce(final Boolean a) {
    return new BooleanCoalesce(this, BoxUtil.box(a));
  }

  // NullIf

  public BooleanFreeExpression nullIf(final Predicate a) {
    return new BooleanNullIf(this, a);
  }

  public BooleanFreeExpression nullIf(final Boolean a) {
    return new BooleanNullIf(this, BoxUtil.box(a));
  }

  // Predicate operators

  public BooleanFreeExpression and(final Predicate p) {
    return new And(this, p);
  }

  public BooleanFreeExpression andNot(final Predicate p) {
    return new And(this, new Not(p));
  }

  public BooleanFreeExpression or(final Predicate p) {
    return new Or(this, p);
  }

  public BooleanFreeExpression orNot(final Predicate p) {
    return new Or(this, new Not(p));
  }

  // Scalar comparisons

  // Equal

  public BooleanFreeExpression eq(final Predicate e) {
    return new Equal(this, e);
  }

  public BooleanFreeExpression eq(final Boolean value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public BooleanFreeExpression ne(final Predicate e) {
    return new NotEqual(this, e);
  }

  public BooleanFreeExpression ne(final Boolean value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public BooleanFreeExpression gt(final Predicate e) {
    return new GreaterThan(this, e);
  }

  public BooleanFreeExpression gt(final Boolean value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public BooleanFreeExpression ge(final Predicate e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public BooleanFreeExpression ge(final Boolean value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public BooleanFreeExpression lt(final Predicate e) {
    return new LessThan(this, e);
  }

  public BooleanFreeExpression lt(final Boolean value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public BooleanFreeExpression le(final Predicate e) {
    return new LessThanOrEqualTo(this, e);
  }

  public BooleanFreeExpression le(final Boolean value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public BooleanFreeExpression between(final Predicate from, final Predicate to) {
    return new Between(this, from, to);
  }

  public BooleanFreeExpression between(final Predicate from, final Boolean to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public BooleanFreeExpression between(final Boolean from, final Predicate to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public BooleanFreeExpression between(final Boolean from, final Boolean to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public BooleanFreeExpression notBetween(final Predicate from, final Predicate to) {
    return new NotBetween<Predicate>(this, from, to);
  }

  public BooleanFreeExpression notBetween(final Predicate from, final Boolean to) {
    return new NotBetween<Predicate>(this, from, BoxUtil.box(to));
  }

  public BooleanFreeExpression notBetween(final Boolean from, final Predicate to) {
    return new NotBetween<Predicate>(this, BoxUtil.box(from), to);
  }

  public BooleanFreeExpression notBetween(final Boolean from, Boolean to) {
    return new NotBetween<Predicate>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final BooleanFreeExpression in(final Predicate... values) {
    return new InList<Predicate>(this, Arrays.asList(values));
  }

  public final BooleanFreeExpression in(final Boolean... values) {
    return new InList<Predicate>(this, Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final BooleanFreeExpression notIn(final Predicate... values) {
    return new NotInList<Predicate>(this, Arrays.asList(values));
  }

  public final BooleanFreeExpression notIn(final Boolean... values) {
    return new NotInList<Predicate>(this, Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

}
