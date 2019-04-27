package sql.metadata;

public abstract class View extends TableOrView {

  public View(final String catalog, final String schema, final String name, final String alias) {
    super(catalog, schema, name, alias);
  }

}
