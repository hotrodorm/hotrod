package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.SelectObject;

public class IndividualSelectPhase<R> extends AbstractSelectPhase<R> {

  public IndividualSelectPhase(final LiveSQLContext context, final SelectObject<R> select) {
    super(context, select);
  }

  // Inline Set Operators

  public CombinedSelectLinkingPhase<R> union() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select, new UnionOperator<>());
  }

  public CombinedSelectLinkingPhase<R> unionAll() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select, new UnionAllOperator<>());
  }

  public CombinedSelectLinkingPhase<R> except() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select, new ExceptOperator<>());
  }

  public CombinedSelectLinkingPhase<R> exceptAll() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select, new ExceptAllOperator<>());
  }

  public CombinedSelectLinkingPhase<R> intersect() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select, new IntersectOperator<>());
  }

  public CombinedSelectLinkingPhase<R> intersectAll() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select, new IntersectAllOperator<>());
  }

}
