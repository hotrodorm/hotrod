package org.hotrod.runtime.livesql.queries.scalarsubqueries;

import org.hotrod.runtime.livesql.Available;
import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.dialects.Const;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject;
import org.hotrod.runtime.livesql.queries.select.CrossJoin;
import org.hotrod.runtime.livesql.queries.select.FullOuterJoin;
import org.hotrod.runtime.livesql.queries.select.InnerJoin;
import org.hotrod.runtime.livesql.queries.select.JoinLateral;
import org.hotrod.runtime.livesql.queries.select.LeftJoinLateral;
import org.hotrod.runtime.livesql.queries.select.LeftOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalFullOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalInnerJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalLeftOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalRightOuterJoin;
import org.hotrod.runtime.livesql.queries.select.RightOuterJoin;
import org.hotrod.runtime.livesql.queries.select.TableExpression;
import org.hotrod.runtime.livesql.queries.select.UnionJoin;
import org.hotrod.runtime.livesql.queries.subqueries.Subquery;

public class BooleanSelectFromPhase extends BooleanSelectExpression {

  // Constructor

  BooleanSelectFromPhase(final AbstractSelectObject<Row> select, final TableExpression t) {
    super(select);
    this.select.setBaseTableExpression(t);
  }

  // This stage

  public BooleanSelectFromPhase join(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.select.addJoin(new InnerJoin(tableViewOrSubquery, on));
    return this;
  }

  public BooleanSelectFromPhase join(final TableExpression tableViewOrSubquery, final Column... using) {
    this.select.addJoin(new InnerJoin(tableViewOrSubquery, using));
    return this;
  }

  public BooleanSelectFromPhase leftJoin(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.select.addJoin(new LeftOuterJoin(tableViewOrSubquery, on));
    return this;
  }

  public BooleanSelectFromPhase leftJoin(final TableExpression tableViewOrSubquery, final Column... using) {
    this.select.addJoin(new LeftOuterJoin(tableViewOrSubquery, using));
    return this;
  }

  public BooleanSelectFromPhase rightJoin(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.select.addJoin(new RightOuterJoin(tableViewOrSubquery, on));
    return this;
  }

  public BooleanSelectFromPhase rightJoin(final TableExpression tableViewOrSubquery, final Column... using) {
    this.select.addJoin(new RightOuterJoin(tableViewOrSubquery, using));
    return this;
  }

  public BooleanSelectFromPhase fullJoin(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.select.addJoin(new FullOuterJoin(tableViewOrSubquery, on));
    return this;
  }

  public BooleanSelectFromPhase fullJoin(final TableExpression tableViewOrSubquery, final Column... using) {
    this.select.addJoin(new FullOuterJoin(tableViewOrSubquery, using));
    return this;
  }

  public BooleanSelectFromPhase crossJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new CrossJoin(tableViewOrSubquery));
    return this;
  }

  public BooleanSelectFromPhase naturalJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new NaturalInnerJoin(tableViewOrSubquery));
    return this;
  }

  public BooleanSelectFromPhase naturalLeftJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new NaturalLeftOuterJoin(tableViewOrSubquery));
    return this;
  }

  public BooleanSelectFromPhase naturalRightJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new NaturalRightOuterJoin(tableViewOrSubquery));
    return this;
  }

  public BooleanSelectFromPhase naturalFullJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new NaturalFullOuterJoin(tableViewOrSubquery));
    return this;
  }

  public BooleanSelectFromPhase joinLateral(final Subquery subquery) {
    this.select.addJoin(new JoinLateral(subquery));
    return this;
  }

  public BooleanSelectFromPhase leftJoinLateral(final Subquery subquery) {
    this.select.addJoin(new LeftJoinLateral(subquery));
    return this;
  }

  @Available(engine = Const.GENERIC, since = Const.ALL)
  @Available(engine = Const.HYPERSQL, since = Const.HS2)
  public BooleanSelectFromPhase unionJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new UnionJoin(tableViewOrSubquery));
    return this;
  }

  @Available(engine = Const.POSTGRESQL, since = Const.PG15)
  public BooleanSelectFromPhase unionPGJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new UnionJoin(tableViewOrSubquery));
    return this;
  }

  // Next stages

  public BooleanSelectWherePhase where(final Predicate predicate) {
    return new BooleanSelectWherePhase(this.select, predicate);
  }

  public BooleanSelectGroupByPhase groupBy(final Expression... columns) {
    return new BooleanSelectGroupByPhase(this.select, columns);
  }

  public BooleanSelectOrderByPhase orderBy(final OrderingTerm... orderingTerms) {
    return new BooleanSelectOrderByPhase(this.select, orderingTerms);
  }

  public BooleanSelectOffsetPhase offset(final int offset) {
    return new BooleanSelectOffsetPhase(this.select, offset);
  }

  public BooleanSelectLimitPhase limit(final int limit) {
    return new BooleanSelectLimitPhase(this.select, limit);
  }

  // Set operations

  // public SelectHavingPhase union(final CombinableSelect select) {
  // this.select.setCombinedSelect(SetOperation.UNION, select);
  // return new SelectHavingPhase(this.select, null);
  // }
  //
  // public SelectHavingPhase unionAll(final CombinableSelect select) {
  // this.select.setCombinedSelect(SetOperation.UNION_ALL, select);
  // return new SelectHavingPhase(this.select, null);
  // }
  //
  // public SelectHavingPhase intersect(final CombinableSelect select) {
  // this.select.setCombinedSelect(SetOperation.INTERSECT, select);
  // return new SelectHavingPhase(this.select, null);
  // }
  //
  // public SelectHavingPhase intersectAll(final CombinableSelect select)
  // {
  // this.select.setCombinedSelect(SetOperation.INTERSECT_ALL, select);
  // return new SelectHavingPhase(this.select, null);
  // }
  //
  // public SelectHavingPhase except(final CombinableSelect select) {
  // this.select.setCombinedSelect(SetOperation.EXCEPT, select);
  // return new SelectHavingPhase(this.select, null);
  // }
  //
  // public SelectHavingPhase exceptAll(final CombinableSelect select) {
  // this.select.setCombinedSelect(SetOperation.EXCEPT_ALL, select);
  // return new SelectHavingPhase(this.select, null);
  // }

//  // Execute
//
//  public List execute() {
//    return this.select.execute();
//  }
//
//  @Override
//  public Cursor executeCursor() {
//    return this.select.executeCursor();
//  }
//
//  // Validation
//
//  @Override
//  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
//    this.select.validateTableReferences(tableReferences, ag);
//  }
//
//  @Override
//  public void designateAliases(final AliasGenerator ag) {
//    this.select.assignNonDeclaredAliases(ag);
//  }
//
//  // CombinableSelect
//
//  @Override
//  public void setParent(final AbstractSelect parent) {
//    this.select.setParent(parent);
//  }
//
//  @Override
//  public String getPreview() {
//    return this.select.getPreview();
//  }
//
//  @Override
//  public List<ResultSetColumn> listColumns() throws IllegalAccessException {
//    return this.select.listColumns();
//  }

}
