package org.hotrod.runtime.livesql.expressions.binary;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Between;
import org.hotrod.runtime.livesql.expressions.predicates.BooleanFreeExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Equal;
import org.hotrod.runtime.livesql.expressions.predicates.GreaterThan;
import org.hotrod.runtime.livesql.expressions.predicates.GreaterThanOrEqualTo;
import org.hotrod.runtime.livesql.expressions.predicates.InList;
import org.hotrod.runtime.livesql.expressions.predicates.LessThan;
import org.hotrod.runtime.livesql.expressions.predicates.LessThanOrEqualTo;
import org.hotrod.runtime.livesql.expressions.predicates.NotBetween;
import org.hotrod.runtime.livesql.expressions.predicates.NotEqual;
import org.hotrod.runtime.livesql.expressions.predicates.NotInList;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.util.BoxUtil;

public abstract class ByteArrayExpression extends ComparableExpression {

  protected ByteArrayExpression(final int precedence) {
    super(precedence);
  }

  // Coalesce

  public ByteArrayFreeExpression coalesce(final ByteArrayExpression a) {
    return new ByteArrayCoalesce(this, a);
  }

  public ByteArrayFreeExpression coalesce(final byte[] a) {
    return new ByteArrayCoalesce(this, new ByteArrayConstant(a));
  }

  // NullIf

  public ByteArrayFreeExpression nullIf(final ByteArrayExpression a) {
    return new ByteArrayNullIf(this, a);
  }

  public ByteArrayFreeExpression nullIf(final byte[] a) {
    return new ByteArrayNullIf(this, new ByteArrayConstant(a));
  }

  // Scalar comparisons

  // Equal

  public BooleanFreeExpression eq(final ByteArrayExpression e) {
    return new Equal(this, e);
  }

  public BooleanFreeExpression eq(final byte[] value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public BooleanFreeExpression ne(final ByteArrayExpression e) {
    return new NotEqual(this, e);
  }

  public BooleanFreeExpression ne(final byte[] value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public BooleanFreeExpression gt(final ByteArrayExpression e) {
    return new GreaterThan(this, e);
  }

  public BooleanFreeExpression gt(final byte[] value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public BooleanFreeExpression ge(final ByteArrayExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public BooleanFreeExpression ge(final byte[] value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public BooleanFreeExpression lt(final ByteArrayExpression e) {
    return new LessThan(this, e);
  }

  public BooleanFreeExpression lt(final byte[] value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public BooleanFreeExpression le(final ByteArrayExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public BooleanFreeExpression le(final byte[] value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public BooleanFreeExpression between(final ByteArrayExpression from, final ByteArrayExpression to) {
    return new Between(this, from, to);
  }

  public BooleanFreeExpression between(final ByteArrayExpression from, final byte[] to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public BooleanFreeExpression between(final byte[] from, final ByteArrayExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public Predicate between(final byte[] from, final byte[] to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public BooleanFreeExpression notBetween(final ByteArrayExpression from, final ByteArrayExpression to) {
    return new NotBetween<ByteArrayExpression>(this, from, to);
  }

  public BooleanFreeExpression notBetween(final ByteArrayExpression from, final byte[] to) {
    return new NotBetween<ByteArrayExpression>(this, from, BoxUtil.box(to));
  }

  public BooleanFreeExpression notBetween(final byte[] from, final ByteArrayExpression to) {
    return new NotBetween<ByteArrayExpression>(this, BoxUtil.box(from), to);
  }

  public BooleanFreeExpression notBetween(final byte[] from, byte[] to) {
    return new NotBetween<ByteArrayExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final BooleanFreeExpression in(final ByteArrayExpression... values) {
    return new InList<ByteArrayExpression>(this, Arrays.asList(values));
  }

  public final BooleanFreeExpression in(final byte[]... values) {
    return new InList<ByteArrayExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final BooleanFreeExpression notIn(final ByteArrayExpression... values) {
    return new NotInList<ByteArrayExpression>(this, Arrays.asList(values));
  }

  public final BooleanFreeExpression notIn(final byte[]... values) {
    return new NotInList<ByteArrayExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  // TODO: END -- implement in subclasses

}
