package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.Available;
import org.hotrod.runtime.livesql.dialects.Const;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

public class SelectFromPhase<R> implements ExecutableSelect<R>, CombinableSelect<R> {

  // Properties

  private AbstractSelect<R> select;

  // Constructor

  SelectFromPhase(final AbstractSelect<R> select, final TableOrView t) {
    this.select = select;
    this.select.setBaseTable(t);
  }

  // This stage

  public SelectFromPhase<R> join(final TableOrView t, final Predicate on) {
    this.select.addJoin(new InnerJoin(t, on));
    return this;
  }

  public SelectFromPhase<R> join(final TableOrView t, final Column... using) {
    this.select.addJoin(new InnerJoin(t, using));
    return this;
  }

  public SelectFromPhase<R> leftJoin(final TableOrView t, final Predicate on) {
    this.select.addJoin(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectFromPhase<R> leftJoin(final TableOrView t, final Column... using) {
    this.select.addJoin(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectFromPhase<R> rightJoin(final TableOrView t, final Predicate on) {
    this.select.addJoin(new RightOuterJoin(t, on));
    return this;
  }

  public SelectFromPhase<R> rightJoin(final TableOrView t, final Column... using) {
    this.select.addJoin(new RightOuterJoin(t, using));
    return this;
  }

  public SelectFromPhase<R> fullJoin(final TableOrView t, final Predicate on) {
    this.select.addJoin(new FullOuterJoin(t, on));
    return this;
  }

  public SelectFromPhase<R> fullJoin(final TableOrView t, final Column... using) {
    this.select.addJoin(new FullOuterJoin(t, using));
    return this;
  }

  public SelectFromPhase<R> crossJoin(final TableOrView t) {
    this.select.addJoin(new CrossJoin(t));
    return this;
  }

  public SelectFromPhase<R> naturalJoin(final TableOrView t) {
    this.select.addJoin(new NaturalInnerJoin(t));
    return this;
  }

  public SelectFromPhase<R> naturalLeftJoin(final TableOrView t) {
    this.select.addJoin(new NaturalLeftOuterJoin(t));
    return this;
  }

  public SelectFromPhase<R> naturalRightJoin(final TableOrView t) {
    this.select.addJoin(new NaturalRightOuterJoin(t));
    return this;
  }

  public SelectFromPhase<R> naturalFullJoin(final TableOrView t) {
    this.select.addJoin(new NaturalFullOuterJoin(t));
    return this;
  }

  @Available(engine = Const.HYPERSQL, since = Const.HS2)
  public SelectFromPhase<R> unionJoin(final TableOrView t) {
    this.select.addJoin(new UnionJoin(t));
    return this;
  }

  // Next stages

  public SelectWherePhase<R> where(final Predicate predicate) {
    return new SelectWherePhase<R>(this.select, predicate);
  }

  public SelectGroupByPhase<R> groupBy(final Expression... columns) {
    return new SelectGroupByPhase<R>(this.select, columns);
  }

  public SelectOrderByPhase<R> orderBy(final OrderingTerm... orderingTerms) {
    return new SelectOrderByPhase<R>(this.select, orderingTerms);
  }

  public SelectOffsetPhase<R> offset(final int offset) {
    return new SelectOffsetPhase<R>(this.select, offset);
  }

  public SelectLimitPhase<R> limit(final int limit) {
    return new SelectLimitPhase<R>(this.select, limit);
  }

  // Set operations

  // public SelectHavingPhase<R> union(final CombinableSelect<R> select) {
  // this.select.setCombinedSelect(SetOperation.UNION, select);
  // return new SelectHavingPhase<R>(this.select, null);
  // }
  //
  // public SelectHavingPhase<R> unionAll(final CombinableSelect<R> select) {
  // this.select.setCombinedSelect(SetOperation.UNION_ALL, select);
  // return new SelectHavingPhase<R>(this.select, null);
  // }
  //
  // public SelectHavingPhase<R> intersect(final CombinableSelect<R> select) {
  // this.select.setCombinedSelect(SetOperation.INTERSECT, select);
  // return new SelectHavingPhase<R>(this.select, null);
  // }
  //
  // public SelectHavingPhase<R> intersectAll(final CombinableSelect<R> select)
  // {
  // this.select.setCombinedSelect(SetOperation.INTERSECT_ALL, select);
  // return new SelectHavingPhase<R>(this.select, null);
  // }
  //
  // public SelectHavingPhase<R> except(final CombinableSelect<R> select) {
  // this.select.setCombinedSelect(SetOperation.EXCEPT, select);
  // return new SelectHavingPhase<R>(this.select, null);
  // }
  //
  // public SelectHavingPhase<R> exceptAll(final CombinableSelect<R> select) {
  // this.select.setCombinedSelect(SetOperation.EXCEPT_ALL, select);
  // return new SelectHavingPhase<R>(this.select, null);
  // }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    this.select.renderTo(w);
  }

  // Execute

  public List<R> execute() {
    return this.select.execute();
  }

  @Override
  public Cursor<R> executeCursor() {
    return this.select.executeCursor();
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    this.select.validateTableReferences(tableReferences, ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.select.assignNonDeclaredAliases(ag);
  }

  // CombinableSelect

  @Override
  public void setParent(final AbstractSelect<R> parent) {
    this.select.setParent(parent);
  }

  @Override
  public String getPreview() {
    return this.select.getPreview();
  }

}
