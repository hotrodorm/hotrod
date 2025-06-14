package org.hotrod.livesql.queries.select.tuples;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.livesql.metadata.Table;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.queries.select.LockableSelectLimitPhase;
import org.hotrod.livesql.queries.select.LockableSelectOffsetPhase;
import org.hotrod.livesql.queries.select.LockableSelectOrderByPhase;
import org.hotrod.livesql.queries.select.SHelper;
import org.hotrod.livesql.queries.select.SelectGroupByPhase;
import org.hotrod.livesql.queries.select.SelectWherePhase;
import org.hotrod.livesql.queries.select.sets.CombinedSelectObject;

public class SelectTuplesFrom1Phase<A> {

  private TuplesMetadata metadata;

  SelectTuplesFrom1Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  public <T extends Table<B>, B> SelectTuplesFrom2Phase<A, B> join(T t) {
    this.metadata.add(t);
    return new SelectTuplesFrom2Phase<A, B>(this.metadata);
  }

  // next phases
  
  public SelectWherePhase<Tuple1<A>> where(final GeneralBooleanExpression predicate) {
    return SHelper.getSelectWherePhase(this.metadata.getContext(), new TuplesSelect<Tuple1<A>>(this.metadata),
        predicate);
  }

  public SelectGroupByPhase<Tuple1<A>> groupBy(final ComparableExpression... columns) {
    return SHelper.getSelectGroupByPhase(this.metadata.getContext(), new TuplesSelect<Tuple1<A>>(this.metadata),
        columns);
  }

  public LockableSelectOrderByPhase<Tuple1<A>> orderBy(final OrderingTerm... orderingTerms) {
    return SHelper.getSelectOrderByPhase(this.metadata.getContext(), new TuplesSelect<Tuple1<A>>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple1<A>> offset(final int offset) {
    return SHelper.getSelectOffsetPhase(this.metadata.getContext(), new TuplesSelect<Tuple1<A>>(this.metadata), offset);
  }

  public LockableSelectLimitPhase<Tuple1<A>> limit(final int limit) {
    return SHelper.getSelectLimitPhase(this.metadata.getContext(), new TuplesSelect<Tuple1<A>>(this.metadata), limit);
  }

  // execute

  public List<Tuple1<A>> execute() {
    CombinedSelectObject<Tuple1<A>> combined = new CombinedSelectObject<>(new TuplesSelect<>(this.metadata));
    return combined.execute(this.metadata.getContext());
  }

  public Cursor<Tuple1<A>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple1<A>> combined = new CombinedSelectObject<>(new TuplesSelect<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext());
  }

  public Cursor<Tuple1<A>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple1<A>> combined = new CombinedSelectObject<>(new TuplesSelect<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), fetchSize);
  }

  public Tuple1<A> executeOne() throws SQLException {
    CombinedSelectObject<Tuple1<A>> combined = new CombinedSelectObject<>(new TuplesSelect<>(this.metadata));
    return combined.executeOne(this.metadata.getContext());
  }

}
