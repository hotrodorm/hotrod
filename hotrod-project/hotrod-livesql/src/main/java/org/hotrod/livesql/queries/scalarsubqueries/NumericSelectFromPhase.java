package org.hotrod.livesql.queries.scalarsubqueries;

import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.Available;
import org.hotrod.livesql.dialects.Const;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.queries.select.CrossJoin;
import org.hotrod.livesql.queries.select.FullOuterJoin;
import org.hotrod.livesql.queries.select.InnerJoin;
import org.hotrod.livesql.queries.select.JoinLateral;
import org.hotrod.livesql.queries.select.LeftJoinLateral;
import org.hotrod.livesql.queries.select.LeftOuterJoin;
import org.hotrod.livesql.queries.select.NaturalFullOuterJoin;
import org.hotrod.livesql.queries.select.NaturalInnerJoin;
import org.hotrod.livesql.queries.select.NaturalLeftOuterJoin;
import org.hotrod.livesql.queries.select.NaturalRightOuterJoin;
import org.hotrod.livesql.queries.select.RightOuterJoin;
import org.hotrod.livesql.queries.select.UnarySelectObject;
import org.hotrod.livesql.queries.select.TableExpression;
import org.hotrod.livesql.queries.select.UnionJoin;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class NumericSelectFromPhase extends NumericSelectExpression {

  // Constructor

  NumericSelectFromPhase(final UnarySelectObject<Row> select, final TableExpression t) {
    super(select);
    this.select.setBaseTableExpression(t);
  }

  // This stage

  public NumericSelectFromPhase join(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.select.addJoin(new InnerJoin(tableViewOrSubquery, on));
    return this;
  }

  public NumericSelectFromPhase join(final TableExpression tableViewOrSubquery, final EntityColumn... using) {
    this.select.addJoin(new InnerJoin(tableViewOrSubquery, using));
    return this;
  }

  public NumericSelectFromPhase leftJoin(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.select.addJoin(new LeftOuterJoin(tableViewOrSubquery, on));
    return this;
  }

  public NumericSelectFromPhase leftJoin(final TableExpression tableViewOrSubquery, final EntityColumn... using) {
    this.select.addJoin(new LeftOuterJoin(tableViewOrSubquery, using));
    return this;
  }

  public NumericSelectFromPhase rightJoin(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.select.addJoin(new RightOuterJoin(tableViewOrSubquery, on));
    return this;
  }

  public NumericSelectFromPhase rightJoin(final TableExpression tableViewOrSubquery, final EntityColumn... using) {
    this.select.addJoin(new RightOuterJoin(tableViewOrSubquery, using));
    return this;
  }

  public NumericSelectFromPhase fullJoin(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.select.addJoin(new FullOuterJoin(tableViewOrSubquery, on));
    return this;
  }

  public NumericSelectFromPhase fullJoin(final TableExpression tableViewOrSubquery, final EntityColumn... using) {
    this.select.addJoin(new FullOuterJoin(tableViewOrSubquery, using));
    return this;
  }

  public NumericSelectFromPhase crossJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new CrossJoin(tableViewOrSubquery));
    return this;
  }

  public NumericSelectFromPhase naturalJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new NaturalInnerJoin(tableViewOrSubquery));
    return this;
  }

  public NumericSelectFromPhase naturalLeftJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new NaturalLeftOuterJoin(tableViewOrSubquery));
    return this;
  }

  public NumericSelectFromPhase naturalRightJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new NaturalRightOuterJoin(tableViewOrSubquery));
    return this;
  }

  public NumericSelectFromPhase naturalFullJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new NaturalFullOuterJoin(tableViewOrSubquery));
    return this;
  }

  public NumericSelectFromPhase joinLateral(final Subquery subquery) {
    this.select.addJoin(new JoinLateral(subquery));
    return this;
  }

  public NumericSelectFromPhase leftJoinLateral(final Subquery subquery) {
    this.select.addJoin(new LeftJoinLateral(subquery));
    return this;
  }

  @Available(engine = Const.GENERIC, since = Const.ALL)
  @Available(engine = Const.HYPERSQL, since = Const.HS2)
  public NumericSelectFromPhase unionJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new UnionJoin(tableViewOrSubquery));
    return this;
  }

  @Available(engine = Const.POSTGRESQL, since = Const.PG15)
  public NumericSelectFromPhase unionPGJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new UnionJoin(tableViewOrSubquery));
    return this;
  }

  // Next stages

  public NumericSelectWherePhase where(final Predicate predicate) {
    return new NumericSelectWherePhase(this.select, predicate);
  }

  public NumericSelectGroupByPhase groupBy(final ComparableExpression... columns) {
    return new NumericSelectGroupByPhase(this.select, columns);
  }

  public NumericSelectOrderByPhase orderBy(final OrderingTerm... orderingTerms) {
    return new NumericSelectOrderByPhase(this.select, orderingTerms);
  }

  public NumericSelectOffsetPhase offset(final int offset) {
    return new NumericSelectOffsetPhase(this.select, offset);
  }

  public NumericSelectLimitPhase limit(final int limit) {
    return new NumericSelectLimitPhase(this.select, limit);
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
