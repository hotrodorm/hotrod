package org.hotrod.runtime.livesql.ordering;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class AliasOrderingTerm implements CombinedOrderingTerm {

  private String alias;

  public AliasOrderingTerm(final String alias) {
    this.alias = alias;
  }

  public final CombinedOrderByDirectionPhase asc() {
    return new CombinedOrderByDirectionPhase(this.alias, true);
  }

  public final CombinedOrderByDirectionPhase desc() {
    return new CombinedOrderByDirectionPhase(this.alias, false);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.write(w.getSQLDialect().canonicalToNatural(this.alias));
  }

}
