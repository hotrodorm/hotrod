package org.hotrod.livesql.queries.select.tuples;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.queries.select.LockableSelectLimitPhase;
import org.hotrod.livesql.queries.select.LockableSelectOffsetPhase;
import org.hotrod.livesql.queries.select.LockableSelectOrderByPhase;
import org.hotrod.livesql.queries.select.SHelper;
import org.hotrod.livesql.queries.select.SelectGroupByPhase;
import org.hotrod.livesql.queries.select.SelectWherePhase;
import org.hotrod.livesql.queries.select.sets.CombinedSelectObject;

public class SelectTuplesFrom3Phase<A, B, C> {

  private TuplesMetadata metadata;

  SelectTuplesFrom3Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  // next phases

  public SelectWherePhase<Tuple3<A, B, C>> where(final GeneralBooleanExpression predicate) {
    return SHelper.getSelectWherePhase(this.metadata.getContext(), new CompositeSelectObject<Tuple3<A, B, C>>(this.metadata),
        predicate);
  }

  public SelectGroupByPhase<Tuple3<A, B, C>> groupBy(final ComparableExpression... columns) {
    return SHelper.getSelectGroupByPhase(this.metadata.getContext(), new CompositeSelectObject<Tuple3<A, B, C>>(this.metadata),
        columns);
  }

  public LockableSelectOrderByPhase<Tuple3<A, B, C>> orderBy(final OrderingTerm... orderingTerms) {
    return SHelper.getSelectOrderByPhase(this.metadata.getContext(), new CompositeSelectObject<Tuple3<A, B, C>>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple3<A, B, C>> offset(final int offset) {
    return SHelper.getSelectOffsetPhase(this.metadata.getContext(), new CompositeSelectObject<Tuple3<A, B, C>>(this.metadata),
        offset);
  }

  public LockableSelectLimitPhase<Tuple3<A, B, C>> limit(final int limit) {
    return SHelper.getSelectLimitPhase(this.metadata.getContext(), new CompositeSelectObject<Tuple3<A, B, C>>(this.metadata),
        limit);
  }

  // execute

  public List<Tuple3<A, B, C>> execute() {
    CombinedSelectObject<Tuple3<A, B, C>> combined = new CombinedSelectObject<>(new CompositeSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext());
  }

  public Cursor<Tuple3<A, B, C>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple3<A, B, C>> combined = new CombinedSelectObject<>(new CompositeSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext());
  }

  public Cursor<Tuple3<A, B, C>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple3<A, B, C>> combined = new CombinedSelectObject<>(new CompositeSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), fetchSize);
  }

  public Tuple3<A, B, C> executeOne() throws SQLException {
    CombinedSelectObject<Tuple3<A, B, C>> combined = new CombinedSelectObject<>(new CompositeSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext());
  }

}
