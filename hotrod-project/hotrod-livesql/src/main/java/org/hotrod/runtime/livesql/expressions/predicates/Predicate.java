package org.hotrod.runtime.livesql.expressions.predicates;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.util.BoxUtil;

public abstract class Predicate extends Expression {

  protected Predicate(final int precedence) {
    super(precedence);
  }

  // Coalesce

  public Predicate coalesce(final Predicate a) {
    return new PredicateCoalesce(this, a);
  }

  public Predicate coalesce(final Boolean a) {
    return new PredicateCoalesce(this, new BooleanConstant(a));
  }

  // Predicate operators

  public Predicate and(final Predicate p) {
    return new And(this, p);
  }

  public Predicate andNot(final Predicate p) {
    return new And(this, new Not(p));
  }

  public Predicate or(final Predicate p) {
    return new Or(this, p);
  }

  public Predicate orNot(final Predicate p) {
    return new Or(this, new Not(p));
  }

  // TODO: implement in subclasses

  // Scalar comparisons

  // Equal

  public Predicate eq(final Predicate e) {
    return new Equal(this, e);
  }

  public Predicate eq(final Boolean value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public Predicate ne(final Predicate e) {
    return new NotEqual(this, e);
  }

  public Predicate ne(final Boolean value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public Predicate gt(final Predicate e) {
    return new GreaterThan(this, e);
  }

  public Predicate gt(final Boolean value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public Predicate ge(final Predicate e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public Predicate ge(final Boolean value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public Predicate lt(final Predicate e) {
    return new LessThan(this, e);
  }

  public Predicate lt(final Boolean value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public Predicate le(final Predicate e) {
    return new LessThanOrEqualTo(this, e);
  }

  public Predicate le(final Boolean value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public Predicate between(final Predicate from, final Predicate to) {
    return new Between(this, from, to);
  }

  public Predicate between(final Predicate from, final Boolean to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public Predicate between(final Boolean from, final Predicate to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public Predicate between(final Boolean from, final Boolean to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public Predicate notBetween(final Predicate from, final Predicate to) {
    return new NotBetween<Predicate>(this, from, to);
  }

  public Predicate notBetween(final Predicate from, final Boolean to) {
    return new NotBetween<Predicate>(this, from, BoxUtil.box(to));
  }

  public Predicate notBetween(final Boolean from, final Predicate to) {
    return new NotBetween<Predicate>(this, BoxUtil.box(from), to);
  }

  public Predicate notBetween(final Boolean from, Boolean to) {
    return new NotBetween<Predicate>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final Predicate in(final Predicate... values) {
    return new InList<Predicate>(this, Arrays.asList(values));
  }

  public final Predicate in(final Boolean... values) {
    return new InList<Predicate>(this, Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final Predicate notIn(final Predicate... values) {
    return new NotInList<Predicate>(this, Arrays.asList(values));
  }

  public final Predicate notIn(final Boolean... values) {
    return new NotInList<Predicate>(this, Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  // TODO: END -- implement in subclasses

}
