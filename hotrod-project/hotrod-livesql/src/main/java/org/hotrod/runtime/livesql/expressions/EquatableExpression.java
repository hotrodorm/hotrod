package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.asymmetric.EqAll;
import org.hotrod.runtime.livesql.expressions.asymmetric.EqAny;
import org.hotrod.runtime.livesql.expressions.asymmetric.InSubquery;
import org.hotrod.runtime.livesql.expressions.asymmetric.NeAll;
import org.hotrod.runtime.livesql.expressions.asymmetric.NeAny;
import org.hotrod.runtime.livesql.expressions.asymmetric.NotInSubquery;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.queries.select.Select;

public abstract class EquatableExpression extends SortableExpression {

  // Constructor

  protected EquatableExpression(final int precedence) {
    super(precedence);
  }

  // In subquery

  public Predicate in(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new InSubquery(this, subquery);
  }

  public Predicate notIn(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new NotInSubquery(this, subquery);
  }

  // Any

  public Predicate eqAny(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new EqAny(this, subquery);
  }

  public Predicate neAny(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new NeAny(this, subquery);
  }

  // All

  public Predicate eqAll(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new EqAll(this, subquery);
  }

  public Predicate neAll(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new NeAll(this, subquery);
  }

}
