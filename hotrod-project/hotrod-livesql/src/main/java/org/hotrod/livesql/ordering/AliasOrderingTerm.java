package org.hotrod.livesql.ordering;

import org.hotrod.livesql.queries.QueryWriter;

public class AliasOrderingTerm extends CombinedOrderingTerm {

  private String alias;

  public AliasOrderingTerm(final String alias) {
    super();
    this.alias = alias;
  }

  public final CombinedOrderByDirectionPhase asc() {
    return new CombinedOrderByDirectionPhase(this.alias, true);
  }

  public final CombinedOrderByDirectionPhase desc() {
    return new CombinedOrderByDirectionPhase(this.alias, false);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.write(w.getSQLDialect().canonicalToNatural(this.alias));
  }

}
