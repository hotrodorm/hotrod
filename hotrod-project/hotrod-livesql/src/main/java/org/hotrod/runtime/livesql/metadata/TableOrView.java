package org.hotrod.runtime.livesql.metadata;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.TableExpression;
import org.hotrodorm.hotrod.utils.SUtil;

public abstract class TableOrView extends DatabaseObject implements TableExpression {

  private String alias;
  private String designatedAlias;
  protected List<Column> columns;

  TableOrView(final String catalog, final String schema, final String name, final String type, final String alias) {
    super(catalog, schema, name, type);
    this.alias = alias;
    this.designatedAlias = null;
    this.columns = new ArrayList<>();
  }

  public final String getAlias() {
    return this.alias != null ? this.alias : this.designatedAlias;
  }

  public String renderTree() {
    StringBuilder sb = new StringBuilder();
    this.renderTree(sb, 0);
    return sb.toString();
  }

  public void renderTree(final StringBuilder sb, final int level) {
    sb.append(SUtil.getFiller(". ", level) + "+ [table-or-view] " + this.getClass().getName() + "\n");
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    tableReferences.register(this.alias, this);
    ag.register(this.alias, this);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    if (this.alias == null) {
      this.designatedAlias = ag.next();
    }
  }

  // Rendering

  @Override
  public void renderTo(QueryWriter w, LiveSQLDialect dialect) {
    String alias = this.alias == null ? null : dialect.canonicalToNatural(dialect.naturalToCanonical(this.alias));
    w.write(dialect.canonicalToNatural(this) + (alias != null ? (" " + alias) : ""));
  }

}
