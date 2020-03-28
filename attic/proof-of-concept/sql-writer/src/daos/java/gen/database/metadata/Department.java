package gen.database.metadata;

import org.hotrod.runtime.livesql.metadata.NumberColumn;
import org.hotrod.runtime.livesql.metadata.StringColumn;
import org.hotrod.runtime.livesql.metadata.Table;

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

  public NumberColumn id;
  public StringColumn name;
  public NumberColumn active;

  private void initialize() {
    this.id = new NumberColumn(this, "id");
    this.name = new StringColumn(this, "name");
    this.active = new NumberColumn(this, "active");
  }

}
