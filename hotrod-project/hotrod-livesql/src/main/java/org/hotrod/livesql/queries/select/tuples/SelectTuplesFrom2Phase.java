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

public class SelectTuplesFrom2Phase<A, B> {

  private TuplesMetadata metadata;

  SelectTuplesFrom2Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  public <T extends Table<C>, C> SelectTuplesFrom3Phase<A, B, C> join(T t) {
    this.metadata.add(t);
    return new SelectTuplesFrom3Phase<A, B, C>(this.metadata);
  }

  // next phases
  
  public SelectWherePhase<Tuple2<A, B>> where(final GeneralBooleanExpression predicate) {
    return SHelper.getSelectWherePhase(this.metadata.getContext(), new TuplesSelect<Tuple2<A, B>>(this.metadata),
        predicate);
  }

  public SelectGroupByPhase<Tuple2<A, B>> groupBy(final ComparableExpression... columns) {
    return SHelper.getSelectGroupByPhase(this.metadata.getContext(), new TuplesSelect<Tuple2<A, B>>(this.metadata),
        columns);
  }

  public LockableSelectOrderByPhase<Tuple2<A, B>> orderBy(final OrderingTerm... orderingTerms) {
    return SHelper.getSelectOrderByPhase(this.metadata.getContext(), new TuplesSelect<Tuple2<A, B>>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple2<A, B>> offset(final int offset) {
    return SHelper.getSelectOffsetPhase(this.metadata.getContext(), new TuplesSelect<Tuple2<A, B>>(this.metadata), offset);
  }

  public LockableSelectLimitPhase<Tuple2<A, B>> limit(final int limit) {
    return SHelper.getSelectLimitPhase(this.metadata.getContext(), new TuplesSelect<Tuple2<A, B>>(this.metadata), limit);
  }

  // execute

  public List<Tuple2<A, B>> execute() {
    CombinedSelectObject<Tuple2<A, B>> combined = new CombinedSelectObject<>(new TuplesSelect<>(this.metadata));
    return combined.execute(this.metadata.getContext());
  }

  public Cursor<Tuple2<A, B>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple2<A, B>> combined = new CombinedSelectObject<>(new TuplesSelect<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext());
  }

  public Cursor<Tuple2<A, B>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple2<A, B>> combined = new CombinedSelectObject<>(new TuplesSelect<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), fetchSize);
  }

  public Tuple2<A, B> executeOne() throws SQLException {
    CombinedSelectObject<Tuple2<A, B>> combined = new CombinedSelectObject<>(new TuplesSelect<>(this.metadata));
    return combined.executeOne(this.metadata.getContext());
  }


}
