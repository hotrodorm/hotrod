package org.hotrod.livesql.expressions.binary;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.bool.Between;
import org.hotrod.livesql.expressions.bool.BooleanSyntaxExpression;
import org.hotrod.livesql.expressions.bool.Equal;
import org.hotrod.livesql.expressions.bool.GreaterThan;
import org.hotrod.livesql.expressions.bool.GreaterThanOrEqualTo;
import org.hotrod.livesql.expressions.bool.InList;
import org.hotrod.livesql.expressions.bool.LessThan;
import org.hotrod.livesql.expressions.bool.LessThanOrEqualTo;
import org.hotrod.livesql.expressions.bool.NotBetween;
import org.hotrod.livesql.expressions.bool.NotEqual;
import org.hotrod.livesql.expressions.bool.NotInList;
import org.hotrod.livesql.queries.subqueries.BinarySubqueryExpression;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.livesql.util.BoxUtil;

public abstract class BinaryExpression extends ComparableExpression {

  protected BinaryExpression(final int precedence) {
    super(precedence);
  }

  @Override
  protected Expression asSubqueryExpression(final Subquery subquery, final String alias) {
    return new BinarySubqueryExpression(subquery, alias, this);
  }

  // Coalesce

  public BinarySyntaxExpression coalesce(final BinaryExpression a) {
    return new BinaryCoalesce(this, a);
  }

  public BinarySyntaxExpression coalesce(final byte[] a) {
    return new BinaryCoalesce(this, new BinaryConstant(a));
  }

  // NullIf

  public BinarySyntaxExpression nullIf(final BinaryExpression a) {
    return new BinaryNullIf(this, a);
  }

  public BinarySyntaxExpression nullIf(final byte[] a) {
    return new BinaryNullIf(this, new BinaryConstant(a));
  }

  // Scalar comparisons

  // Equal

  public BooleanSyntaxExpression eq(final BinaryExpression e) {
    return new Equal(this, e);
  }

  public BooleanSyntaxExpression eq(final byte[] value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public BooleanSyntaxExpression ne(final BinaryExpression e) {
    return new NotEqual(this, e);
  }

  public BooleanSyntaxExpression ne(final byte[] value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public BooleanSyntaxExpression gt(final BinaryExpression e) {
    return new GreaterThan(this, e);
  }

  public BooleanSyntaxExpression gt(final byte[] value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public BooleanSyntaxExpression ge(final BinaryExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public BooleanSyntaxExpression ge(final byte[] value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public BooleanSyntaxExpression lt(final BinaryExpression e) {
    return new LessThan(this, e);
  }

  public BooleanSyntaxExpression lt(final byte[] value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public BooleanSyntaxExpression le(final BinaryExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public BooleanSyntaxExpression le(final byte[] value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public BooleanSyntaxExpression between(final BinaryExpression from, final BinaryExpression to) {
    return new Between(this, from, to);
  }

  public BooleanSyntaxExpression between(final BinaryExpression from, final byte[] to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public BooleanSyntaxExpression between(final byte[] from, final BinaryExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public BooleanSyntaxExpression between(final byte[] from, final byte[] to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public BooleanSyntaxExpression notBetween(final BinaryExpression from, final BinaryExpression to) {
    return new NotBetween<BinaryExpression>(this, from, to);
  }

  public BooleanSyntaxExpression notBetween(final BinaryExpression from, final byte[] to) {
    return new NotBetween<BinaryExpression>(this, from, BoxUtil.box(to));
  }

  public BooleanSyntaxExpression notBetween(final byte[] from, final BinaryExpression to) {
    return new NotBetween<BinaryExpression>(this, BoxUtil.box(from), to);
  }

  public BooleanSyntaxExpression notBetween(final byte[] from, byte[] to) {
    return new NotBetween<BinaryExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final BooleanSyntaxExpression in(final BinaryExpression... values) {
    return new InList<BinaryExpression>(this, Arrays.asList(values));
  }

  public final BooleanSyntaxExpression in(final byte[]... values) {
    return new InList<BinaryExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final BooleanSyntaxExpression notIn(final BinaryExpression... values) {
    return new NotInList<BinaryExpression>(this, Arrays.asList(values));
  }

  public final BooleanSyntaxExpression notIn(final byte[]... values) {
    return new NotInList<BinaryExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  // TODO: END -- implement in subclasses

}
