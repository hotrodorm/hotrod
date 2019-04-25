package gen.database.metadata;

import metadata.Column;
import metadata.Table;

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

  public Column id;
  public Column name;
  public Column active;

  private void initialize() {
    this.id = new Column(this, "id");
    this.name = new Column(this, "name");
    this.active = new Column(this, "active");
  }

}
