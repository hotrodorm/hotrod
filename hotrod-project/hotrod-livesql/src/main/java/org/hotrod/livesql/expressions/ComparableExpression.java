package org.hotrod.livesql.expressions;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.asymmetric.GeAll;
import org.hotrod.livesql.expressions.asymmetric.GeAny;
import org.hotrod.livesql.expressions.asymmetric.GtAll;
import org.hotrod.livesql.expressions.asymmetric.GtAny;
import org.hotrod.livesql.expressions.asymmetric.LeAll;
import org.hotrod.livesql.expressions.asymmetric.LeAny;
import org.hotrod.livesql.expressions.asymmetric.LtAll;
import org.hotrod.livesql.expressions.asymmetric.LtAny;
import org.hotrod.livesql.expressions.predicates.Predicate;
import org.hotrod.livesql.queries.select.Select;

public abstract class ComparableExpression extends EquatableExpression {

  // Constructor

  protected ComparableExpression(final int precedence) {
    super(precedence);
  }

  // Any

  public Predicate ltAny(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new LtAny(this, subquery);
  }

  public Predicate leAny(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new LeAny(this, subquery);
  }

  public Predicate gtAny(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new GtAny(this, subquery);
  }

  public Predicate geAny(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new GeAny(this, subquery);
  }

  // All

  public Predicate ltAll(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new LtAll(this, subquery);
  }

  public Predicate leAll(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new LeAll(this, subquery);
  }

  public Predicate gtAll(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new GtAll(this, subquery);
  }

  public Predicate geAll(final Select<?> subquery) {
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    return new GeAll(this, subquery);
  }

}
