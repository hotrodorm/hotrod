package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.List;

import org.hotrod.runtime.livesql.ordering.CombinedOrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.ctes.CTE;

public class CombinedSelectPhase<R> extends AbstractSelectPhase<R> {

  public CombinedSelectPhase(final LiveSQLContext context, final List<CTE> ctes, final boolean distinct,
      final boolean doNotAliasColumns) {
    super(context, ctes, distinct, doNotAliasColumns);
  }

  public CombinedSelectPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined) {
    super(context, combined);
  }

  // Inline Set Operators

  public CombinedSelectLinkingPhase<R> union() {
    return new CombinedSelectLinkingPhase<>(this.context, this.combined, new UnionOperator());
  }

  public CombinedSelectLinkingPhase<R> unionAll() {
    return new CombinedSelectLinkingPhase<>(this.context, this.combined, new UnionAllOperator());
  }

  public CombinedSelectLinkingPhase<R> except() {
    System.out.println("- except()...: " + this.combined.toString());
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

  // Combined

  public final CombinedSelectOrderByPhase<R> orderBy(final CombinedOrderingTerm... orderingTerms) {
    return new CombinedSelectOrderByPhase<R>(this.context, this.combined, orderingTerms);
  }

  public final CombinedSelectOffsetPhase<R> offset(final int offset) {
    return new CombinedSelectOffsetPhase<R>(this.context, this.combined, offset);
  }

  public final CombinedSelectLimitPhase<R> limit(final int limit) {
    return new CombinedSelectLimitPhase<R>(this.context, this.combined, limit);
  }

}
