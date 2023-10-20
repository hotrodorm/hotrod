package org.hotrod.runtime.livesql.ordering;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class OrdinalOrderingTerm implements CombinedOrderingTerm {

  private int ordinal;

  public OrdinalOrderingTerm(final int ordinal) {
    this.ordinal = ordinal;
  }

  public final OrderByDirectionPhase asc() {
    return new OrderByDirectionPhase(this.ordinal, true);
  }

  public final OrderByDirectionPhase desc() {
    return new OrderByDirectionPhase(this.ordinal, false);
  }

  @Override
  public void renderTo(QueryWriter w) {
    w.write("" + this.ordinal);
  }

}
