package org.hotrod.livesql.queries.select.tuples.gen;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.bool.BooleanExpression;
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

public class SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> {

  private TuplesMetadata metadata;

  public SelectTuplesFrom10Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  // joining TableOrView

  public <TV extends TableOrView<K>, K> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> join(TV t, final BooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on));
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> join(TV t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> leftJoin(TV t,
      final BooleanExpression on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> leftJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> rightJoin(TV t,
      final BooleanExpression on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> rightJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> fullJoin(TV t,
      final BooleanExpression on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> fullJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> crossJoin(TV t) {
    this.metadata.join(new CrossJoin(t));
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> naturalJoin(TV t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> naturalLeftJoin(TV t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> naturalRightJoin(TV t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> naturalFullJoin(TV t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  // semi-joining TableOrView

  public <TV extends TableOrView<K>, K> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> semiJoin(TV t, final BooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on), false);
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> semiJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using), false);
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> semiLeftJoin(TV t, final BooleanExpression on) {
    this.metadata.join(new LeftOuterJoin(t, on), false);
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> semiLeftJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using), false);
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> semiRightJoin(TV t, final BooleanExpression on) {
    this.metadata.join(new RightOuterJoin(t, on), false);
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> semiRightJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using), false);
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> semiFullJoin(TV t, final BooleanExpression on) {
    this.metadata.join(new FullOuterJoin(t, on), false);
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> semiFullJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using), false);
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> semiCrossJoin(TV t) {
    this.metadata.join(new CrossJoin(t), false);
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> semiNaturalJoin(TV t) {
    this.metadata.join(new NaturalInnerJoin(t), false);
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> semiNaturalLeftJoin(TV t) {
    this.metadata.join(new NaturalLeftOuterJoin(t), false);
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> semiNaturalRightJoin(TV t) {
    this.metadata.join(new NaturalRightOuterJoin(t), false);
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  public <TV extends TableOrView<K>, K> SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> semiNaturalFullJoin(TV t) {
    this.metadata.join(new NaturalFullOuterJoin(t), false);
    return new SelectTuplesFrom10Phase<>(this.metadata);
  }

  // joining Subquery

  public SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> join(Subquery t, final BooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on));
    return this;
  }

  public SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> join(Subquery t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return this;
  }

  public SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> leftJoin(Subquery t, final BooleanExpression on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> leftJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> rightJoin(Subquery t, final BooleanExpression on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> rightJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> fullJoin(Subquery t, final BooleanExpression on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> fullJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> crossJoin(Subquery t) {
    this.metadata.join(new CrossJoin(t));
    return this;
  }

  public SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> naturalJoin(Subquery t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return this;
  }

  public SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> naturalLeftJoin(Subquery t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> naturalRightJoin(Subquery t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom10Phase<A, B, C, D, E, F, G, H, I, J> naturalFullJoin(Subquery t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return this;
  }

  // next phases

  public SelectWherePhase<Tuple10<A, B, C, D, E, F, G, H, I, J>> where(final BooleanExpression predicate) {
    return SShield.getSelectWherePhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), predicate);
  }

  public SelectGroupByPhase<Tuple10<A, B, C, D, E, F, G, H, I, J>> groupBy(final ComparableExpression... columns) {
    return SShield.getSelectGroupByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), columns);
  }

  public LockableSelectOrderByPhase<Tuple10<A, B, C, D, E, F, G, H, I, J>> orderBy(final OrderingTerm... orderingTerms) {
    return SShield.getSelectOrderByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple10<A, B, C, D, E, F, G, H, I, J>> offset(final int offset) {
    return SShield.getSelectOffsetPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), offset);
  }

  public LockableSelectLimitPhase<Tuple10<A, B, C, D, E, F, G, H, I, J>> limit(final int limit) {
    return SShield.getSelectLimitPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), limit);
  }

  // execute

  public List<Tuple10<A, B, C, D, E, F, G, H, I, J>> execute() {
    CombinedSelectObject<Tuple10<A, B, C, D, E, F, G, H, I, J>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext());
  }

  public Cursor<Tuple10<A, B, C, D, E, F, G, H, I, J>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple10<A, B, C, D, E, F, G, H, I, J>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext());
  }

  public Cursor<Tuple10<A, B, C, D, E, F, G, H, I, J>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple10<A, B, C, D, E, F, G, H, I, J>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), fetchSize);
  }

  public Tuple10<A, B, C, D, E, F, G, H, I, J> executeOne() throws SQLException {
    CombinedSelectObject<Tuple10<A, B, C, D, E, F, G, H, I, J>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext());
  }

}
