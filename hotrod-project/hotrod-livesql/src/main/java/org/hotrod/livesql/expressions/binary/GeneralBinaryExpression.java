package org.hotrod.livesql.expressions.binary;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.bool.Between;
import org.hotrod.livesql.expressions.bool.Equal;
import org.hotrod.livesql.expressions.bool.GreaterThan;
import org.hotrod.livesql.expressions.bool.GreaterThanOrEqualTo;
import org.hotrod.livesql.expressions.bool.InList;
import org.hotrod.livesql.expressions.bool.LessThan;
import org.hotrod.livesql.expressions.bool.LessThanOrEqualTo;
import org.hotrod.livesql.expressions.bool.NotBetween;
import org.hotrod.livesql.expressions.bool.NotEqual;
import org.hotrod.livesql.expressions.bool.NotInList;
import org.hotrod.livesql.expressions.bool.Predicate;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.livesql.queries.subqueries.SubqueryBinaryRefColumn;
import org.hotrod.livesql.util.BoxUtil;

public abstract class GeneralBinaryExpression extends ComparableExpression {

  protected GeneralBinaryExpression(final int precedence) {
    super(precedence);
  }

  @Override
  protected Expression asSubqueryExpression(final Subquery subquery, final String alias) {
    return new SubqueryBinaryRefColumn(subquery, alias, this);
  }

  // Coalesce

  public BinaryExpression coalesce(final GeneralBinaryExpression a) {
    return new BinaryCoalesce(this, a);
  }

  public BinaryExpression coalesce(final byte[] a) {
    return new BinaryCoalesce(this, new BinaryConstant(a));
  }

  // NullIf

  public BinaryExpression nullIf(final GeneralBinaryExpression a) {
    return new BinaryNullIf(this, a);
  }

  public BinaryExpression nullIf(final byte[] a) {
    return new BinaryNullIf(this, new BinaryConstant(a));
  }

  // Scalar comparisons

  // Equal

  public Predicate eq(final GeneralBinaryExpression e) {
    return new Equal(this, e);
  }

  public Predicate eq(final byte[] value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public Predicate ne(final GeneralBinaryExpression e) {
    return new NotEqual(this, e);
  }

  public Predicate ne(final byte[] value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public Predicate gt(final GeneralBinaryExpression e) {
    return new GreaterThan(this, e);
  }

  public Predicate gt(final byte[] value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public Predicate ge(final GeneralBinaryExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public Predicate ge(final byte[] value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public Predicate lt(final GeneralBinaryExpression e) {
    return new LessThan(this, e);
  }

  public Predicate lt(final byte[] value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public Predicate le(final GeneralBinaryExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public Predicate le(final byte[] value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public Predicate between(final GeneralBinaryExpression from, final GeneralBinaryExpression to) {
    return new Between(this, from, to);
  }

  public Predicate between(final GeneralBinaryExpression from, final byte[] to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public Predicate between(final byte[] from, final GeneralBinaryExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public Predicate between(final byte[] from, final byte[] to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public Predicate notBetween(final GeneralBinaryExpression from, final GeneralBinaryExpression to) {
    return new NotBetween<GeneralBinaryExpression>(this, from, to);
  }

  public Predicate notBetween(final GeneralBinaryExpression from, final byte[] to) {
    return new NotBetween<GeneralBinaryExpression>(this, from, BoxUtil.box(to));
  }

  public Predicate notBetween(final byte[] from, final GeneralBinaryExpression to) {
    return new NotBetween<GeneralBinaryExpression>(this, BoxUtil.box(from), to);
  }

  public Predicate notBetween(final byte[] from, byte[] to) {
    return new NotBetween<GeneralBinaryExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final Predicate in(final GeneralBinaryExpression... values) {
    return new InList<GeneralBinaryExpression>(this, Arrays.asList(values));
  }

  public final Predicate in(final byte[]... values) {
    return new InList<GeneralBinaryExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final Predicate notIn(final GeneralBinaryExpression... values) {
    return new NotInList<GeneralBinaryExpression>(this, Arrays.asList(values));
  }

  public final Predicate notIn(final byte[]... values) {
    return new NotInList<GeneralBinaryExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  // TODO: END -- implement in subclasses

}
