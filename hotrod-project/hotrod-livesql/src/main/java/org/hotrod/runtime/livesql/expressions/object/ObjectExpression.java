package org.hotrod.runtime.livesql.expressions.object;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
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

public abstract class ObjectExpression extends ComparableExpression {

  protected ObjectExpression(final int precedence) {
    super(precedence);
  }

  // Coalesce

  public ObjectExpression coalesce(final ObjectExpression a) {
    return new ObjectCoalesce(this, a);
  }

  public ObjectExpression coalesce(final Object a) {
    return new ObjectCoalesce(this, BoxUtil.box(a));
  }

  // Scalar comparisons

  // Equal

  public Predicate eq(final ObjectExpression e) {
    return new Equal(this, e);
  }

  public Predicate eq(final Object value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public Predicate ne(final ObjectExpression e) {
    return new NotEqual(this, e);
  }

  public Predicate ne(final Object value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public Predicate gt(final ObjectExpression e) {
    return new GreaterThan(this, e);
  }

  public Predicate gt(final Object value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public Predicate ge(final ObjectExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public Predicate ge(final Object value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public Predicate lt(final ObjectExpression e) {
    return new LessThan(this, e);
  }

  public Predicate lt(final Object value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public Predicate le(final ObjectExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public Predicate le(final Object value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public Predicate between(final ObjectExpression from, final ObjectExpression to) {
    return new Between(this, from, to);
  }

  public Predicate between(final ObjectExpression from, final Object to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public Predicate between(final Object from, final ObjectExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public Predicate between(final Object from, final Object to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public Predicate notBetween(final ObjectExpression from, final ObjectExpression to) {
    return new NotBetween<ObjectExpression>(this, from, to);
  }

  public Predicate notBetween(final ObjectExpression from, final Object to) {
    return new NotBetween<ObjectExpression>(this, from, BoxUtil.box(to));
  }

  public Predicate notBetween(final Object from, final ObjectExpression to) {
    return new NotBetween<ObjectExpression>(this, BoxUtil.box(from), to);
  }

  public Predicate notBetween(final Object from, Object to) {
    return new NotBetween<ObjectExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final Predicate in(final ObjectExpression... values) {
    return new InList<ObjectExpression>(this, Arrays.asList(values));
  }

  public final Predicate in(final Object... values) {
    return new InList<ObjectExpression>(this, Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final Predicate notIn(final ObjectExpression... values) {
    return new NotInList<ObjectExpression>(this, Arrays.asList(values));
  }

  public final Predicate notIn(final Object... values) {
    return new NotInList<ObjectExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

}
