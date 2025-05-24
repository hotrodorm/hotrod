package org.hotrod.livesql.expressions.binary;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.predicates.Between;
import org.hotrod.livesql.expressions.predicates.Equal;
import org.hotrod.livesql.expressions.predicates.GreaterThan;
import org.hotrod.livesql.expressions.predicates.GreaterThanOrEqualTo;
import org.hotrod.livesql.expressions.predicates.InList;
import org.hotrod.livesql.expressions.predicates.LessThan;
import org.hotrod.livesql.expressions.predicates.LessThanOrEqualTo;
import org.hotrod.livesql.expressions.predicates.NotBetween;
import org.hotrod.livesql.expressions.predicates.NotEqual;
import org.hotrod.livesql.expressions.predicates.NotInList;
import org.hotrod.livesql.expressions.predicates.Predicate;
import org.hotrod.livesql.util.BoxUtil;

public abstract class GeneralByteArrayExpression extends ComparableExpression {

  protected GeneralByteArrayExpression(final int precedence) {
    super(precedence);
  }

  // Coalesce

  public ByteArrayExpression coalesce(final GeneralByteArrayExpression a) {
    return new ByteArrayCoalesce(this, a);
  }

  public ByteArrayExpression coalesce(final byte[] a) {
    return new ByteArrayCoalesce(this, new ByteArrayConstant(a));
  }

  // NullIf

  public ByteArrayExpression nullIf(final GeneralByteArrayExpression a) {
    return new ByteArrayNullIf(this, a);
  }

  public ByteArrayExpression nullIf(final byte[] a) {
    return new ByteArrayNullIf(this, new ByteArrayConstant(a));
  }

  // Scalar comparisons

  // Equal

  public Predicate eq(final GeneralByteArrayExpression e) {
    return new Equal(this, e);
  }

  public Predicate eq(final byte[] value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public Predicate ne(final GeneralByteArrayExpression e) {
    return new NotEqual(this, e);
  }

  public Predicate ne(final byte[] value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public Predicate gt(final GeneralByteArrayExpression e) {
    return new GreaterThan(this, e);
  }

  public Predicate gt(final byte[] value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public Predicate ge(final GeneralByteArrayExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public Predicate ge(final byte[] value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public Predicate lt(final GeneralByteArrayExpression e) {
    return new LessThan(this, e);
  }

  public Predicate lt(final byte[] value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public Predicate le(final GeneralByteArrayExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public Predicate le(final byte[] value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public Predicate between(final GeneralByteArrayExpression from, final GeneralByteArrayExpression to) {
    return new Between(this, from, to);
  }

  public Predicate between(final GeneralByteArrayExpression from, final byte[] to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public Predicate between(final byte[] from, final GeneralByteArrayExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public Predicate between(final byte[] from, final byte[] to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public Predicate notBetween(final GeneralByteArrayExpression from, final GeneralByteArrayExpression to) {
    return new NotBetween<GeneralByteArrayExpression>(this, from, to);
  }

  public Predicate notBetween(final GeneralByteArrayExpression from, final byte[] to) {
    return new NotBetween<GeneralByteArrayExpression>(this, from, BoxUtil.box(to));
  }

  public Predicate notBetween(final byte[] from, final GeneralByteArrayExpression to) {
    return new NotBetween<GeneralByteArrayExpression>(this, BoxUtil.box(from), to);
  }

  public Predicate notBetween(final byte[] from, byte[] to) {
    return new NotBetween<GeneralByteArrayExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final Predicate in(final GeneralByteArrayExpression... values) {
    return new InList<GeneralByteArrayExpression>(this, Arrays.asList(values));
  }

  public final Predicate in(final byte[]... values) {
    return new InList<GeneralByteArrayExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final Predicate notIn(final GeneralByteArrayExpression... values) {
    return new NotInList<GeneralByteArrayExpression>(this, Arrays.asList(values));
  }

  public final Predicate notIn(final byte[]... values) {
    return new NotInList<GeneralByteArrayExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  // TODO: END -- implement in subclasses

}
