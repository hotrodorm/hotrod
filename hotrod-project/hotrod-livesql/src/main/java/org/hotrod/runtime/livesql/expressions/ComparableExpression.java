package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.asymmetric.EqAll;
import org.hotrod.runtime.livesql.expressions.asymmetric.EqAny;
import org.hotrod.runtime.livesql.expressions.asymmetric.GeAll;
import org.hotrod.runtime.livesql.expressions.asymmetric.GeAny;
import org.hotrod.runtime.livesql.expressions.asymmetric.GtAll;
import org.hotrod.runtime.livesql.expressions.asymmetric.GtAny;
import org.hotrod.runtime.livesql.expressions.asymmetric.InSubquery;
import org.hotrod.runtime.livesql.expressions.asymmetric.LeAll;
import org.hotrod.runtime.livesql.expressions.asymmetric.LeAny;
import org.hotrod.runtime.livesql.expressions.asymmetric.LtAll;
import org.hotrod.runtime.livesql.expressions.asymmetric.LtAny;
import org.hotrod.runtime.livesql.expressions.asymmetric.NeAll;
import org.hotrod.runtime.livesql.expressions.asymmetric.NeAny;
import org.hotrod.runtime.livesql.expressions.asymmetric.NotInSubquery;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.ordering.OrderByDirectionPhase;
import org.hotrod.runtime.livesql.queries.select.Select;

public abstract class ComparableExpression extends GenericExpression {

  // Constructor

  protected ComparableExpression(final int precedence) {
    super(precedence);
  }

  // Column ordering

  public final OrderByDirectionPhase asc() {
    return new OrderByDirectionPhase(this, true);
  }

  public final OrderByDirectionPhase desc() {
    return new OrderByDirectionPhase(this, false);
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
