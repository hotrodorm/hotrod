package org.hotrod.livesql.queries.select.tuples;

import org.hotrod.livesql.expressions.bool.BooleanExpression;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.queries.select.CrossJoin;
import org.hotrod.livesql.queries.select.FullOuterJoin;
import org.hotrod.livesql.queries.select.InnerJoin;
import org.hotrod.livesql.queries.select.LeftOuterJoin;
import org.hotrod.livesql.queries.select.NaturalFullOuterJoin;
import org.hotrod.livesql.queries.select.NaturalInnerJoin;
import org.hotrod.livesql.queries.select.NaturalLeftOuterJoin;
import org.hotrod.livesql.queries.select.NaturalRightOuterJoin;
import org.hotrod.livesql.queries.select.RightOuterJoin;
import org.hotrod.livesql.queries.select.tuples.gen.SelectTuplesFrom1Phase;
import org.hotrod.livesql.queries.subqueries.Subquery;

public class SelectTuplesFrom0Phase {

  private TuplesMetadata metadata;

  SelectTuplesFrom0Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  public <A> SelectTuplesFrom1Phase<A> semiJoin(TableOrView<A> t, final BooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on), false);
    return new SelectTuplesFrom1Phase<>(this.metadata);
  }

  // joining TableOrView

  public <T extends TableOrView<A>, A> SelectTuplesFrom1Phase<A> join(T t, final BooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on));
    return new SelectTuplesFrom1Phase<>(this.metadata);
  }

  public <T extends TableOrView<A>, A> SelectTuplesFrom1Phase<A> join(T t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return new SelectTuplesFrom1Phase<>(this.metadata);
  }

  public <T extends TableOrView<A>, A> SelectTuplesFrom1Phase<A> leftJoin(T t, final BooleanExpression on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return new SelectTuplesFrom1Phase<>(this.metadata);
  }

  public <T extends TableOrView<A>, A> SelectTuplesFrom1Phase<A> leftJoin(T t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return new SelectTuplesFrom1Phase<>(this.metadata);
  }

  public <T extends TableOrView<A>, A> SelectTuplesFrom1Phase<A> rightJoin(T t, final BooleanExpression on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return new SelectTuplesFrom1Phase<>(this.metadata);
  }

  public <T extends TableOrView<A>, A> SelectTuplesFrom1Phase<A> rightJoin(T t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return new SelectTuplesFrom1Phase<>(this.metadata);
  }

  public <T extends TableOrView<A>, A> SelectTuplesFrom1Phase<A> fullJoin(T t, final BooleanExpression on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return new SelectTuplesFrom1Phase<>(this.metadata);
  }

  public <T extends TableOrView<A>, A> SelectTuplesFrom1Phase<A> fullJoin(T t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return new SelectTuplesFrom1Phase<>(this.metadata);
  }

  public <T extends TableOrView<A>, A> SelectTuplesFrom1Phase<A> crossJoin(T t) {
    this.metadata.join(new CrossJoin(t));
    return new SelectTuplesFrom1Phase<>(this.metadata);
  }

  public <T extends TableOrView<A>, A> SelectTuplesFrom1Phase<A> naturalJoin(T t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return new SelectTuplesFrom1Phase<>(this.metadata);
  }

  public <T extends TableOrView<A>, A> SelectTuplesFrom1Phase<A> naturalLeftJoin(T t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return new SelectTuplesFrom1Phase<>(this.metadata);
  }

  public <T extends TableOrView<A>, A> SelectTuplesFrom1Phase<A> naturalRightJoin(T t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return new SelectTuplesFrom1Phase<>(this.metadata);
  }

  public <T extends TableOrView<A>, A> SelectTuplesFrom1Phase<A> naturalFullJoin(T t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return new SelectTuplesFrom1Phase<>(this.metadata);
  }

  // joining Subquery

  public SelectTuplesFrom0Phase join(Subquery t, final BooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on));
    return this;
  }

  public SelectTuplesFrom0Phase join(Subquery t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return this;
  }

  public SelectTuplesFrom0Phase leftJoin(Subquery t, final BooleanExpression on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom0Phase leftJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom0Phase rightJoin(Subquery t, final BooleanExpression on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom0Phase rightJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom0Phase fullJoin(Subquery t, final BooleanExpression on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom0Phase fullJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom0Phase crossJoin(Subquery t) {
    this.metadata.join(new CrossJoin(t));
    return this;
  }

  public SelectTuplesFrom0Phase naturalJoin(Subquery t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return this;
  }

  public SelectTuplesFrom0Phase naturalLeftJoin(Subquery t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom0Phase naturalRightJoin(Subquery t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom0Phase naturalFullJoin(Subquery t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return this;
  }

  // next phases

  // execute

}
