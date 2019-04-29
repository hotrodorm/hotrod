package gen.database.metadata;

import org.hotrod.runtime.sql.metadata.Column;
import org.hotrod.runtime.sql.metadata.Table;

public class Department extends Table {

  // Constructor

  public Department() {
    super(null, null, "department", null);
    initialize();
  }

  public Department(final String alias) {
    super(null, null, "department", alias);
    initialize();
  }

  // Properties

  public Column<Number> id;
  public Column<String> name;
  public Column<Number> active;

  private void initialize() {
    this.id = new Column<Number>(this, "id");
    this.name = new Column<String>(this, "name");
    this.active = new Column<Number>(this, "active");
  }

}
