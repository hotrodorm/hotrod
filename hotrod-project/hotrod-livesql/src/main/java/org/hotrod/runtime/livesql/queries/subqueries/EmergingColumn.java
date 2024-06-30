package org.hotrod.runtime.livesql.queries.subqueries;

import org.hotrod.runtime.livesql.expressions.TypeHandler;
import org.hotrod.runtime.livesql.metadata.Name;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.SHelper;
import org.hotrod.runtime.livesql.queries.select.TableExpression;

public class EmergingColumn {

  private Name namespace;
  private String name;
  private String alias;
  private TypeHandler typeHandler;

  public EmergingColumn(final Name namespace, final String name, final String alias, final TypeHandler typeHandler) {
    this.namespace = namespace;
    this.name = name;
    this.alias = alias;
    this.typeHandler = typeHandler;
  }

  public Name getNamespace() {
    return this.namespace;
  }

  public void setNamespace(Name namespace) {
    this.namespace = namespace;
  }

  public String getAlias() {
    return alias;
  }

  public TypeHandler getTypeHandler() {
    return typeHandler;
  }

  public EmergingColumn asEmergingColumnOf(final TableExpression te) {
    if (te == null) {
      return this;
    }
    return new EmergingColumn(SHelper.getName(te), this.alias, this.alias, this.typeHandler);
  }

  // Rendering

  public void renderTo(final QueryWriter w) {
    if (this.namespace != null) {
      this.namespace.renderTo(w);
      w.write(".");
    }
    w.write(w.getSQLDialect().canonicalToNatural(w.getSQLDialect().naturalToCanonical(this.name)));
    if (this.alias != null) {
      w.write(" as ");
      w.write(w.getSQLDialect().canonicalToNatural(this.alias));

    }
  }

  // toString

  public String toString() {
    return (this.namespace != null ? this.namespace.toString() + "." : "") + this.name + " as '" + this.alias + "' ("
        + this.typeHandler + ")";
  }

}
