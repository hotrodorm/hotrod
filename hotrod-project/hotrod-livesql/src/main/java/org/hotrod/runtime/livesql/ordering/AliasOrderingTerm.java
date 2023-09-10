package org.hotrod.runtime.livesql.ordering;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class AliasOrderingTerm implements CombinedOrderingTerm {

  private String alias;

  public AliasOrderingTerm(final String alias) {
    this.alias = alias;
  }

  public final OrderByDirectionStage asc() {
    return new OrderByDirectionStage(this.alias, true);
  }

  public final OrderByDirectionStage desc() {
    return new OrderByDirectionStage(this.alias, false);
  }

  @Override
  public void renderTo(QueryWriter w) {
    w.write(w.getSQLDialect().canonicalToNatural(this.alias));
  }

}
