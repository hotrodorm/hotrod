package org.hotrod.livesql.expressions.object;

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
import org.hotrod.livesql.queries.subqueries.ObjectSubqueryExpression;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.livesql.util.BoxUtil;

public abstract class ObjectExpression extends ComparableExpression {

  protected ObjectExpression(final int precedence) {
    super(precedence);
  }

  @Override
  protected Expression asSubqueryExpression(final Subquery subquery, final String alias) {
    return new ObjectSubqueryExpression(subquery, alias, this);
  }

  // Coalesce

  public ObjectSyntaxExpression coalesce(final ObjectExpression a) {
    return new ObjectCoalesce(this, a);
  }

  public ObjectSyntaxExpression coalesce(final Object a) {
    return new ObjectCoalesce(this, BoxUtil.box(a));
  }

  // NullIf

  public ObjectSyntaxExpression nullIf(final ObjectExpression a) {
    return new ObjectNullIf(this, a);
  }

  public ObjectSyntaxExpression nullIf(final Object a) {
    return new ObjectNullIf(this, BoxUtil.box(a));
  }

  // Scalar comparisons

  // Equal

  public BooleanSyntaxExpression eq(final ObjectExpression e) {
    return new Equal(this, e);
  }

  public BooleanSyntaxExpression eq(final Object value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public BooleanSyntaxExpression ne(final ObjectExpression e) {
    return new NotEqual(this, e);
  }

  public BooleanSyntaxExpression ne(final Object value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public BooleanSyntaxExpression gt(final ObjectExpression e) {
    return new GreaterThan(this, e);
  }

  public BooleanSyntaxExpression gt(final Object value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public BooleanSyntaxExpression ge(final ObjectExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public BooleanSyntaxExpression ge(final Object value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public BooleanSyntaxExpression lt(final ObjectExpression e) {
    return new LessThan(this, e);
  }

  public BooleanSyntaxExpression lt(final Object value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public BooleanSyntaxExpression le(final ObjectExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public BooleanSyntaxExpression le(final Object value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public BooleanSyntaxExpression between(final ObjectExpression from, final ObjectExpression to) {
    return new Between(this, from, to);
  }

  public BooleanSyntaxExpression between(final ObjectExpression from, final Object to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public BooleanSyntaxExpression between(final Object from, final ObjectExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public BooleanSyntaxExpression between(final Object from, final Object to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public BooleanSyntaxExpression notBetween(final ObjectExpression from, final ObjectExpression to) {
    return new NotBetween<ObjectExpression>(this, from, to);
  }

  public BooleanSyntaxExpression notBetween(final ObjectExpression from, final Object to) {
    return new NotBetween<ObjectExpression>(this, from, BoxUtil.box(to));
  }

  public BooleanSyntaxExpression notBetween(final Object from, final ObjectExpression to) {
    return new NotBetween<ObjectExpression>(this, BoxUtil.box(from), to);
  }

  public BooleanSyntaxExpression notBetween(final Object from, Object to) {
    return new NotBetween<ObjectExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final BooleanSyntaxExpression in(final ObjectExpression... values) {
    return new InList<ObjectExpression>(this, Arrays.asList(values));
  }

  public final BooleanSyntaxExpression in(final Object... values) {
    return new InList<ObjectExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final BooleanSyntaxExpression notIn(final ObjectExpression... values) {
    return new NotInList<ObjectExpression>(this, Arrays.asList(values));
  }

  public final BooleanSyntaxExpression notIn(final Object... values) {
    return new NotInList<ObjectExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

}
