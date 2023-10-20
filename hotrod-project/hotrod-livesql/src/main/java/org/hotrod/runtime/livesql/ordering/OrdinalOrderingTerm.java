package org.hotrod.runtime.livesql.ordering;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class OrdinalOrderingTerm implements CombinedOrderingTerm {

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
  public void renderTo(QueryWriter w) {
    w.write("" + this.ordinal);
  }

}
