package org.hotrod.runtime.livesql.metadata;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.AbstractSelect.TableReferencesValidator;
import org.hotrod.runtime.livesql.exceptions.DuplicateLiveSQLAliasException;
import org.hotrod.runtime.livesql.exceptions.InvalidLiveSQLStatementException;

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

  public void validateTableReferences(final TableReferencesValidator tableReferences) {
    tableReferences.register(this);
  }

  public void gatherAliases(final AliasGenerator ag) {
    try {
      ag.register(this.alias);
    } catch (DuplicateLiveSQLAliasException e) {
      throw new InvalidLiveSQLStatementException("Duplicate table or view alias '" + this.alias + "'");
    }
  }

  public void designateAliases(final AliasGenerator ag) {
    if (this.alias == null) {
      this.designatedAlias = ag.next();
    }
  }

}
