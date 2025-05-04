package org.hotrod.runtime.livesql.expressions.predicates.converter;

import org.hotrod.runtime.livesql.expressions.EquatableExpression;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public abstract class ConvertedColumn<R, D> extends EquatableExpression {

  private TableOrView objectInstance;
  private String name;

  public ConvertedColumn(int precedence, TableOrView objectInstance, String name) {
    super(precedence);
    this.objectInstance = objectInstance;
    this.name = name;
  }

  protected void renderTo(QueryWriter w) {
    if (this.objectInstance.getAlias() != null) {
      w.write(
          w.getSQLDialect().canonicalToNatural(w.getSQLDialect().naturalToCanonical(this.objectInstance.getAlias())));
      w.write(".");
    }
    w.write(w.getSQLDialect().canonicalToNatural(this.name));
  }

}
