package org.hotrod.runtime.livesql.ordering;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public class OrdinalOrderingTerm extends CombinedOrderingTerm {

  private int ordinal;

  public OrdinalOrderingTerm(final int ordinal) {
    this.ordinal = ordinal;
  }

  public final CombinedOrderByDirectionPhase asc() {
    return new CombinedOrderByDirectionPhase(this.ordinal, true);
  }

  public final CombinedOrderByDirectionPhase desc() {
    return new CombinedOrderByDirectionPhase(this.ordinal, false);
  }

  @Override
  protected void renderTo(QueryWriter w) {
    w.write("" + this.ordinal);
  }

}
