package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.List;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.ctes.CTE;

public class IndividualSelectPhase<R> extends AbstractSelectPhase<R> {

  public IndividualSelectPhase(final LiveSQLContext context, final List<CTE> ctes, final boolean distinct,
      final boolean doNotAliasColumns) {
    super(context, ctes, distinct, doNotAliasColumns);
  }

  public IndividualSelectPhase(final LiveSQLContext context, final CombinedSelectObject<R> cs) {
    super(context, cs);
  }

  // Inline Set Operators

  public CombinedSelectLinkingPhase<R> union() {
    return new CombinedSelectLinkingPhase<>(this.context, this.combined, new UnionOperator());
  }

  public CombinedSelectLinkingPhase<R> unionAll() {
    return new CombinedSelectLinkingPhase<>(this.context, this.combined, new UnionAllOperator());
  }

  public CombinedSelectLinkingPhase<R> except() {
    return new CombinedSelectLinkingPhase<>(this.context, this.combined, new ExceptOperator());
  }

  public CombinedSelectLinkingPhase<R> exceptAll() {
    return new CombinedSelectLinkingPhase<>(this.context, this.combined, new ExceptAllOperator());
  }

  public CombinedSelectLinkingPhase<R> intersect() {
    return new CombinedSelectLinkingPhase<>(this.context, this.combined, new IntersectOperator());
  }

  public CombinedSelectLinkingPhase<R> intersectAll() {
    return new CombinedSelectLinkingPhase<>(this.context, this.combined, new IntersectAllOperator());
  }

  // Nested Set Operators

//  public CombinedSelectLinkingPhase<R> union(final Select<R> select) {
//    CombinedSelectObject<R> cs = boxSelect();
//    SetOperator<R> op = new UnionOperator<>();
//    CombinedSelectObject<R> newCS = cs.prepareCombinationWith(op);
//
//    CombinedSelectPhase<R> ph = new CombinedSelectPhase<R><>(this.context, null, distinct, newCS,
//        resultSetColumns);
//
//    newCS.add(new SetOperatorTerm<>(this.op, ph.getSelect()));
//    System.out.println("Resulting: " + newCS.toString());
//
//    return new CombinedSelectLinkingPhase<>(this.context, boxSelect(), new UnionOperator<>());
//  }

}
