package org.hotrod.livesql.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.select.TableExpression;
import org.hotrod.livesql.queries.select.TableReferences;
import org.hotrod.livesql.queries.select.UnarySelectObject.AliasGenerator;
import org.hotrod.livesql.util.ToString;

public abstract class TableOrView<M> extends TableExpression {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(TableOrView.class.getName());

  private Name catalog;
  private Name schema;
  private Name name;
  private String type;

  private String alias;
  private String designatedAlias;
  protected List<Expression> columns;

  Class<?> modelClass;

  TableOrView(final Name catalog, final Name schema, final Name name, final String type, final String alias,
      final Class<?> modelClass) {
    this.catalog = catalog;
    this.schema = schema;
    this.name = name;
    this.type = type;

    this.alias = alias;
    this.designatedAlias = null;
    this.columns = new ArrayList<>();

    this.modelClass = modelClass;
  }

  protected Name getAliasName() {
    return Name.of(this.getAlias(), false);
  }

  protected void add(final Expression expr) {
    this.columns.add(expr);
  }

  public final String getAlias() {
    return this.alias != null ? this.alias : this.designatedAlias;
  }

  protected void removeAlias() {
    this.alias = null;
    this.designatedAlias = null;
  }

  // Validation

  @Override
  protected void assembleColumns() {
    // Tables and views have already all their assembled columns; nothing to do
//    log.info("ASSEMBLING COLUMNS for: " + this.name);
  }

  @Override
  protected void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    if (this.alias == null) {
      this.designatedAlias = ag.next();
    }
    tableReferences.register(this.alias, this);
  }

  // Rendering

  @Override
  protected void renderTo(final QueryWriter w) {
    LiveSQLDialect dialect = w.getSQLDialect();
    String alias = this.getAlias() == null ? null
        : dialect.canonicalToNatural(dialect.naturalToCanonical(this.getAlias()));
    w.write(dialect.canonicalToNatural(this) + (alias != null ? (" " + alias) : ""));
  }

  // Getters

  public final Name getCatalog() {
    return catalog;
  }

  public final Name getSchema() {
    return schema;
  }

  @Override
  protected final Name getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  protected String renderUnescapedName() {
    StringBuilder sb = new StringBuilder();
    if (this.catalog != null) {
      sb.append(this.catalog);
      sb.append(".");
    }
    if (this.schema != null) {
      sb.append(this.schema);
    }
    if (this.catalog != null || this.schema != null) {
      sb.append(".");
    }
    sb.append(this.name);
    return sb.toString();
  }
  
  @Override
  protected final void log(ToString t) {
    t.printObject(this, this.getName() + "(" + this.getAlias() + ")");
  }


  // --- Indexable methods (hashCode & equals) ---
  // DO NOT implement these methods, since the code relies on the default
  // [shallow] JVM implementation.
  // ---------------------------------------------

}
