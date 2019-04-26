package metadata;

public abstract class TableOrView extends DatabaseObject {

  private String alias;
  private String designatedAlias;

  TableOrView(final String catalog, final String schema, final String name, final String alias) {
    super(catalog, schema, name);
    this.alias = alias;
    this.designatedAlias = null;
  }

  public final String getAlias() {
    return this.alias != null ? this.alias : this.designatedAlias;
  }

  public final void setDesignatedAlias(final String designatedAlias) {
    this.designatedAlias = designatedAlias;
  }

}
