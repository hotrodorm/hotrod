package metadata;

public abstract class Table implements TableOrView {

  private String catalog;
  private String schema;
  private String name;
  private String alias;

  public Table(final String catalog, final String schema, final String name, final String alias) {
    this.catalog = catalog;
    this.schema = schema;
    this.name = name;
    this.alias = alias;
  }

  public String getCatalog() {
    return catalog;
  }

  public String getSchema() {
    return schema;
  }

  public String getName() {
    return name;
  }

  public String getAlias() {
    return alias;
  }

}
