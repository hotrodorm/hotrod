package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.AbstractSelectPhase;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectLinkingPhase;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.runtime.livesql.queries.select.sets.ExceptAllOperator;
import org.hotrod.runtime.livesql.queries.select.sets.ExceptOperator;
import org.hotrod.runtime.livesql.queries.select.sets.IntersectAllOperator;
import org.hotrod.runtime.livesql.queries.select.sets.IntersectOperator;
import org.hotrod.runtime.livesql.queries.select.sets.UnionAllOperator;
import org.hotrod.runtime.livesql.queries.select.sets.UnionOperator;

public class EnclosedSelectPhase<R> extends AbstractSelectPhase<R> {

  private CombinedSelectObject<R> external;

  // Constructor

  public EnclosedSelectPhase(final LiveSQLContext context, final SelectObject<R> select) {
    super(context, select);
    CombinedSelectObject<R> mid = new CombinedSelectObject<>(select.findRoot());
    select.setParent(mid);
    this.external = new CombinedSelectObject<>(mid);
    mid.setParent(this.external);
  }

  // Set Operators - Inline

  public CombinedSelectLinkingPhase<R> union() {
    return new CombinedSelectLinkingPhase<>(this.context, this.external, new UnionOperator<>());
  }

  public CombinedSelectLinkingPhase<R> unionAll() {
    return new CombinedSelectLinkingPhase<>(this.context, this.external, new UnionAllOperator<>());
  }

  public CombinedSelectLinkingPhase<R> except() {
    return new CombinedSelectLinkingPhase<>(this.context, this.external, new ExceptOperator<>());
  }

  public CombinedSelectLinkingPhase<R> exceptAll() {
    return new CombinedSelectLinkingPhase<>(this.context, this.external, new ExceptAllOperator<>());
  }

  public CombinedSelectLinkingPhase<R> intersect() {
    return new CombinedSelectLinkingPhase<>(this.context, this.external, new IntersectOperator<>());
  }

  public CombinedSelectLinkingPhase<R> intersectAll() {
    return new CombinedSelectLinkingPhase<>(this.context, this.external, new IntersectAllOperator<>());
  }

  // Set Operators - Enclosed

  // .union(select()...)
  // .union(selectDistinct()...)

}
