package org.hotrod.livesql.queries.select.tuples.gen;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.bool.GeneralBooleanExpression;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.queries.select.CrossJoin;
import org.hotrod.livesql.queries.select.FullOuterJoin;
import org.hotrod.livesql.queries.select.InnerJoin;
import org.hotrod.livesql.queries.select.LeftOuterJoin;
import org.hotrod.livesql.queries.select.LockableSelectLimitPhase;
import org.hotrod.livesql.queries.select.LockableSelectOffsetPhase;
import org.hotrod.livesql.queries.select.LockableSelectOrderByPhase;
import org.hotrod.livesql.queries.select.NaturalFullOuterJoin;
import org.hotrod.livesql.queries.select.NaturalInnerJoin;
import org.hotrod.livesql.queries.select.NaturalLeftOuterJoin;
import org.hotrod.livesql.queries.select.NaturalRightOuterJoin;
import org.hotrod.livesql.queries.select.RightOuterJoin;
import org.hotrod.livesql.queries.select.SShield;
import org.hotrod.livesql.queries.select.SelectGroupByPhase;
import org.hotrod.livesql.queries.select.SelectWherePhase;
import org.hotrod.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.livesql.queries.select.tuples.TuplesMetadata;
import org.hotrod.livesql.queries.select.tuples.TuplesSelectObject;
import org.hotrod.livesql.queries.subqueries.Subquery;

public class SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> {

  private TuplesMetadata metadata;

  SelectTuplesFrom9Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  // joining TableOrView

  public <TV extends TableOrView<J>, J> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> join(TV t, final GeneralBooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on));
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<J>, J> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> join(TV t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<J>, J> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> leftJoin(TV t,
      final GeneralBooleanExpression on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<J>, J> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> leftJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<J>, J> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> rightJoin(TV t,
      final GeneralBooleanExpression on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<J>, J> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> rightJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<J>, J> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> fullJoin(TV t,
      final GeneralBooleanExpression on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<J>, J> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> fullJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<J>, J> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> crossJoin(TV t) {
    this.metadata.join(new CrossJoin(t));
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<J>, J> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> naturalJoin(TV t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<J>, J> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> naturalLeftJoin(TV t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<J>, J> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> naturalRightJoin(TV t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<J>, J> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> naturalFullJoin(TV t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  // joining Subquery

  public SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> join(Subquery t, final GeneralBooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on));
    return this;
  }

  public SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> join(Subquery t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return this;
  }

  public SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> leftJoin(Subquery t, final GeneralBooleanExpression on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> leftJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> rightJoin(Subquery t, final GeneralBooleanExpression on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> rightJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> fullJoin(Subquery t, final GeneralBooleanExpression on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> fullJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> crossJoin(Subquery t) {
    this.metadata.join(new CrossJoin(t));
    return this;
  }

  public SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> naturalJoin(Subquery t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return this;
  }

  public SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> naturalLeftJoin(Subquery t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> naturalRightJoin(Subquery t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> naturalFullJoin(Subquery t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return this;
  }

  // next phases

  public SelectWherePhase<Tuple9<A, B, C, D, E, F, G, H, I>> where(final GeneralBooleanExpression predicate) {
    return SShield.getSelectWherePhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), predicate);
  }

  public SelectGroupByPhase<Tuple9<A, B, C, D, E, F, G, H, I>> groupBy(final ComparableExpression... columns) {
    return SShield.getSelectGroupByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), columns);
  }

  public LockableSelectOrderByPhase<Tuple9<A, B, C, D, E, F, G, H, I>> orderBy(final OrderingTerm... orderingTerms) {
    return SShield.getSelectOrderByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple9<A, B, C, D, E, F, G, H, I>> offset(final int offset) {
    return SShield.getSelectOffsetPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), offset);
  }

  public LockableSelectLimitPhase<Tuple9<A, B, C, D, E, F, G, H, I>> limit(final int limit) {
    return SShield.getSelectLimitPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), limit);
  }

  // execute

  public List<Tuple9<A, B, C, D, E, F, G, H, I>> execute() {
    CombinedSelectObject<Tuple9<A, B, C, D, E, F, G, H, I>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext());
  }

  public Cursor<Tuple9<A, B, C, D, E, F, G, H, I>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple9<A, B, C, D, E, F, G, H, I>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext());
  }

  public Cursor<Tuple9<A, B, C, D, E, F, G, H, I>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple9<A, B, C, D, E, F, G, H, I>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), fetchSize);
  }

  public Tuple9<A, B, C, D, E, F, G, H, I> executeOne() throws SQLException {
    CombinedSelectObject<Tuple9<A, B, C, D, E, F, G, H, I>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext());
  }

}
