package org.hotrod.runtime.livesql.metadata;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.exceptions.DuplicateAliasException;
import org.hotrod.runtime.livesql.exceptions.InvalidSQLStatementException;

public abstract class TableOrView extends DatabaseObject {

  private String alias;
  private String designatedAlias;

  TableOrView(final String catalog, final String schema, final String name, final String alias) {
    super(catalog, schema, name);
    this.alias = alias;
    this.designatedAlias = null;
  }

  public final String getAlias() {
    System.out.println("[" + super.getName() + "] alias=" + this.alias + " designatedAlias=" + this.designatedAlias);
    return this.alias != null ? this.alias : this.designatedAlias;
  }

  // Apply aliases

  public void gatherAliases(final AliasGenerator ag) {
    try {
      ag.register(this.alias);
    } catch (DuplicateAliasException e) {
      throw new InvalidSQLStatementException("Duplicate table or view alias '" + this.alias + "'");
    }
  }

  public void designateAliases(final AliasGenerator ag) {
    if (this.alias == null) {
      this.designatedAlias = ag.next();
    }
  }

}
