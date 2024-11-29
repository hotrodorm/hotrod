package org.hotrod.runtime.livesql.expressions.object;

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
import org.hotrod.runtime.livesql.util.BoxUtil;

public abstract class ObjectExpression extends ComparableExpression {

  protected ObjectExpression(final int precedence) {
    super(precedence);
  }

  // Coalesce

  public ObjectFreeExpression coalesce(final ObjectExpression a) {
    return new ObjectCoalesce(this, a);
  }

  public ObjectFreeExpression coalesce(final Object a) {
    return new ObjectCoalesce(this, BoxUtil.box(a));
  }

  // NullIf

  public ObjectFreeExpression nullIf(final ObjectExpression a) {
    return new ObjectNullIf(this, a);
  }

  public ObjectFreeExpression nullIf(final Object a) {
    return new ObjectNullIf(this, BoxUtil.box(a));
  }

  // Scalar comparisons

  // Equal

  public BooleanFreeExpression eq(final ObjectExpression e) {
    return new Equal(this, e);
  }

  public BooleanFreeExpression eq(final Object value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public BooleanFreeExpression ne(final ObjectExpression e) {
    return new NotEqual(this, e);
  }

  public BooleanFreeExpression ne(final Object value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public BooleanFreeExpression gt(final ObjectExpression e) {
    return new GreaterThan(this, e);
  }

  public BooleanFreeExpression gt(final Object value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public BooleanFreeExpression ge(final ObjectExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public BooleanFreeExpression ge(final Object value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public BooleanFreeExpression lt(final ObjectExpression e) {
    return new LessThan(this, e);
  }

  public BooleanFreeExpression lt(final Object value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public BooleanFreeExpression le(final ObjectExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public BooleanFreeExpression le(final Object value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public BooleanFreeExpression between(final ObjectExpression from, final ObjectExpression to) {
    return new Between(this, from, to);
  }

  public BooleanFreeExpression between(final ObjectExpression from, final Object to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public BooleanFreeExpression between(final Object from, final ObjectExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public BooleanFreeExpression between(final Object from, final Object to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public BooleanFreeExpression notBetween(final ObjectExpression from, final ObjectExpression to) {
    return new NotBetween<ObjectExpression>(this, from, to);
  }

  public BooleanFreeExpression notBetween(final ObjectExpression from, final Object to) {
    return new NotBetween<ObjectExpression>(this, from, BoxUtil.box(to));
  }

  public BooleanFreeExpression notBetween(final Object from, final ObjectExpression to) {
    return new NotBetween<ObjectExpression>(this, BoxUtil.box(from), to);
  }

  public BooleanFreeExpression notBetween(final Object from, Object to) {
    return new NotBetween<ObjectExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final BooleanFreeExpression in(final ObjectExpression... values) {
    return new InList<ObjectExpression>(this, Arrays.asList(values));
  }

  public final BooleanFreeExpression in(final Object... values) {
    return new InList<ObjectExpression>(this, Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final BooleanFreeExpression notIn(final ObjectExpression... values) {
    return new NotInList<ObjectExpression>(this, Arrays.asList(values));
  }

  public final BooleanFreeExpression notIn(final Object... values) {
    return new NotInList<ObjectExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

}
