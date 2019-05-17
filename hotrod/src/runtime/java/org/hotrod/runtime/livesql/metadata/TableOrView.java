package org.hotrod.runtime.livesql.metadata;

import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

public abstract class TableOrView extends DatabaseObject {

  private String alias;
  private String designatedAlias;

  TableOrView(final String catalog, final String schema, final String name, final String type, final String alias) {
    super(catalog, schema, name, type);
    this.alias = alias;
    this.designatedAlias = null;
  }

  public final String getAlias() {
    return this.alias != null ? this.alias : this.designatedAlias;
  }

  // Validation

  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    tableReferences.register(this);
    ag.register(this.alias, this);
  }

  public void designateAliases(final AliasGenerator ag) {
    if (this.alias == null) {
      this.designatedAlias = ag.next();
    }
  }

}
