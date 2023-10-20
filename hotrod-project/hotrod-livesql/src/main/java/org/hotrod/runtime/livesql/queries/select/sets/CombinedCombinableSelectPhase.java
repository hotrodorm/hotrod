package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.SelectObject;

public class CombinedCombinableSelectPhase<R> extends CombinableSelectPhase<R> {

  public CombinedCombinableSelectPhase(final LiveSQLContext context, final SelectObject<R> select) {
    super(context, select);
  }

  // Set Operators - Inline

  // .select() .selectDistinct()

  @Override
  public CombinedSelectLinkingPhase<R> union() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select.getParent(), new UnionOperator<>());
  }

  @Override
  public CombinedSelectLinkingPhase<R> unionAll() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select.getParent(), new UnionAllOperator<>());
  }

  @Override
  public CombinedSelectLinkingPhase<R> except() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select.getParent(), new ExceptOperator<>());
  }

  @Override
  public CombinedSelectLinkingPhase<R> exceptAll() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select.getParent(), new ExceptAllOperator<>());
  }

  @Override
  public CombinedSelectLinkingPhase<R> intersect() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select.getParent(), new IntersectOperator<>());
  }

  @Override
  public CombinedSelectLinkingPhase<R> intersectAll() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select.getParent(), new IntersectAllOperator<>());
  }

}
