package org.hotrod.runtime.livesql.queries.subqueries;

import org.hotrod.runtime.livesql.expressions.TypeHandler;
import org.hotrod.runtime.livesql.metadata.Name;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class EmergingColumn {

  private Name tableExpressionAlias;
  private String alias;
  private TypeHandler typeHandler;

  public EmergingColumn(final Name tableExpressionAlias, final String alias, final TypeHandler typeHandler) {
    this.tableExpressionAlias = tableExpressionAlias;
    this.alias = alias;
    this.typeHandler = typeHandler;
  }

  public Name getTableExpressionAlias() {
    return this.tableExpressionAlias;
  }

  public void setTableExpressionAlias(Name tableExpressionAlias) {
    this.tableExpressionAlias = tableExpressionAlias;
  }

  public String getAlias() {
    return alias;
  }

  public TypeHandler getTypeHandler() {
    return typeHandler;
  }

  public EmergingColumn asEmergingColumnOf(final Subquery subquery) {
    return new EmergingColumn(subquery.getName(), this.alias, this.typeHandler);
  }

  // Rendering

  public void renderTo(QueryWriter w) {
    if (this.tableExpressionAlias != null) {
      this.tableExpressionAlias.renderTo(w);
      w.write(".");
    }
    w.write(w.getSQLDialect().canonicalToNatural(w.getSQLDialect().naturalToCanonical(this.alias)));
  }

  // toString

  public String toString() {
    return (this.tableExpressionAlias != null ? this.tableExpressionAlias.toString() + "." : "") + this.alias + " ("
        + this.typeHandler + ")";
  }

}
