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

public class SelectTuplesFrom7Phase<A, B, C, D, E, F, G> {

  private TuplesMetadata metadata;

  public SelectTuplesFrom7Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  // joining TableOrView

  public <TV extends TableOrView<H>, H> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> join(TV t, final BooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on));
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> join(TV t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> leftJoin(TV t,
      final BooleanExpression on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> leftJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> rightJoin(TV t,
      final BooleanExpression on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> rightJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> fullJoin(TV t,
      final BooleanExpression on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> fullJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> crossJoin(TV t) {
    this.metadata.join(new CrossJoin(t));
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> naturalJoin(TV t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> naturalLeftJoin(TV t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> naturalRightJoin(TV t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> naturalFullJoin(TV t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  // semi-joining TableOrView

  public <TV extends TableOrView<H>, H> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> semiJoin(TV t, final BooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on), false);
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> semiJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using), false);
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> semiLeftJoin(TV t, final BooleanExpression on) {
    this.metadata.join(new LeftOuterJoin(t, on), false);
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> semiLeftJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using), false);
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> semiRightJoin(TV t, final BooleanExpression on) {
    this.metadata.join(new RightOuterJoin(t, on), false);
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> semiRightJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using), false);
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> semiFullJoin(TV t, final BooleanExpression on) {
    this.metadata.join(new FullOuterJoin(t, on), false);
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> semiFullJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using), false);
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> semiCrossJoin(TV t) {
    this.metadata.join(new CrossJoin(t), false);
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> semiNaturalJoin(TV t) {
    this.metadata.join(new NaturalInnerJoin(t), false);
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> semiNaturalLeftJoin(TV t) {
    this.metadata.join(new NaturalLeftOuterJoin(t), false);
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> semiNaturalRightJoin(TV t) {
    this.metadata.join(new NaturalRightOuterJoin(t), false);
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <TV extends TableOrView<H>, H> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> semiNaturalFullJoin(TV t) {
    this.metadata.join(new NaturalFullOuterJoin(t), false);
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  // joining Subquery

  public SelectTuplesFrom7Phase<A, B, C, D, E, F, G> join(Subquery t, final BooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on));
    return this;
  }

  public SelectTuplesFrom7Phase<A, B, C, D, E, F, G> join(Subquery t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return this;
  }

  public SelectTuplesFrom7Phase<A, B, C, D, E, F, G> leftJoin(Subquery t, final BooleanExpression on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom7Phase<A, B, C, D, E, F, G> leftJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom7Phase<A, B, C, D, E, F, G> rightJoin(Subquery t, final BooleanExpression on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom7Phase<A, B, C, D, E, F, G> rightJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom7Phase<A, B, C, D, E, F, G> fullJoin(Subquery t, final BooleanExpression on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom7Phase<A, B, C, D, E, F, G> fullJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom7Phase<A, B, C, D, E, F, G> crossJoin(Subquery t) {
    this.metadata.join(new CrossJoin(t));
    return this;
  }

  public SelectTuplesFrom7Phase<A, B, C, D, E, F, G> naturalJoin(Subquery t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return this;
  }

  public SelectTuplesFrom7Phase<A, B, C, D, E, F, G> naturalLeftJoin(Subquery t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom7Phase<A, B, C, D, E, F, G> naturalRightJoin(Subquery t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom7Phase<A, B, C, D, E, F, G> naturalFullJoin(Subquery t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return this;
  }

  // next phases

  public SelectWherePhase<Tuple7<A, B, C, D, E, F, G>> where(final BooleanExpression predicate) {
    return SShield.getSelectWherePhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), predicate);
  }

  public SelectGroupByPhase<Tuple7<A, B, C, D, E, F, G>> groupBy(final ComparableExpression... columns) {
    return SShield.getSelectGroupByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), columns);
  }

  public LockableSelectOrderByPhase<Tuple7<A, B, C, D, E, F, G>> orderBy(final OrderingTerm... orderingTerms) {
    return SShield.getSelectOrderByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple7<A, B, C, D, E, F, G>> offset(final int offset) {
    return SShield.getSelectOffsetPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), offset);
  }

  public LockableSelectLimitPhase<Tuple7<A, B, C, D, E, F, G>> limit(final int limit) {
    return SShield.getSelectLimitPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), limit);
  }

  // execute

  public List<Tuple7<A, B, C, D, E, F, G>> execute() {
    CombinedSelectObject<Tuple7<A, B, C, D, E, F, G>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext());
  }

  public Cursor<Tuple7<A, B, C, D, E, F, G>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple7<A, B, C, D, E, F, G>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext());
  }

  public Cursor<Tuple7<A, B, C, D, E, F, G>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple7<A, B, C, D, E, F, G>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), fetchSize);
  }

  public Tuple7<A, B, C, D, E, F, G> executeOne() throws SQLException {
    CombinedSelectObject<Tuple7<A, B, C, D, E, F, G>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext());
  }

}
