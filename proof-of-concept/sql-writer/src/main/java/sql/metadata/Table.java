package sql.metadata;

public abstract class Table extends TableOrView {

  public Table(final String catalog, final String schema, final String name, final String alias) {
    super(catalog, schema, name, alias);
  }

}
