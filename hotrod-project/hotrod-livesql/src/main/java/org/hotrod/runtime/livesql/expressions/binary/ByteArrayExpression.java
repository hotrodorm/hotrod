package org.hotrod.runtime.livesql.expressions.binary;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Between;
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

public abstract class ByteArrayExpression extends Expression {

  protected ByteArrayExpression(final int precedence) {
    super(precedence);
  }

  // Coalesce

  public ByteArrayExpression coalesce(final ByteArrayExpression a) {
    return new ByteArrayCoalesce(this, a);
  }

  public ByteArrayExpression coalesce(final byte[] a) {
    return new ByteArrayCoalesce(this, new ByteArrayConstant(a));
  }

  // Scalar comparisons

  // Equal

  public Predicate eq(final ByteArrayExpression e) {
    return new Equal(this, e);
  }

  public Predicate eq(final byte[] value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public Predicate ne(final ByteArrayExpression e) {
    return new NotEqual(this, e);
  }

  public Predicate ne(final byte[] value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public Predicate gt(final ByteArrayExpression e) {
    return new GreaterThan(this, e);
  }

  public Predicate gt(final byte[] value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public Predicate ge(final ByteArrayExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public Predicate ge(final byte[] value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public Predicate lt(final ByteArrayExpression e) {
    return new LessThan(this, e);
  }

  public Predicate lt(final byte[] value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public Predicate le(final ByteArrayExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public Predicate le(final byte[] value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public Predicate between(final ByteArrayExpression from, final ByteArrayExpression to) {
    return new Between(this, from, to);
  }

  public Predicate between(final ByteArrayExpression from, final byte[] to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public Predicate between(final byte[] from, final ByteArrayExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public Predicate between(final byte[] from, final byte[] to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public Predicate notBetween(final ByteArrayExpression from, final ByteArrayExpression to) {
    return new NotBetween<ByteArrayExpression>(this, from, to);
  }

  public Predicate notBetween(final ByteArrayExpression from, final byte[] to) {
    return new NotBetween<ByteArrayExpression>(this, from, BoxUtil.box(to));
  }

  public Predicate notBetween(final byte[] from, final ByteArrayExpression to) {
    return new NotBetween<ByteArrayExpression>(this, BoxUtil.box(from), to);
  }

  public Predicate notBetween(final byte[] from, byte[] to) {
    return new NotBetween<ByteArrayExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final Predicate in(final ByteArrayExpression... values) {
    return new InList<ByteArrayExpression>(this, Arrays.asList(values));
  }

  public final Predicate in(final byte[]... values) {
    return new InList<ByteArrayExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final Predicate notIn(final ByteArrayExpression... values) {
    return new NotInList<ByteArrayExpression>(this, Arrays.asList(values));
  }

  public final Predicate notIn(final byte[]... values) {
    return new NotInList<ByteArrayExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  // TODO: END -- implement in subclasses

}
