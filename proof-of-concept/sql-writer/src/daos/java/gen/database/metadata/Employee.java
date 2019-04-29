package gen.database.metadata;

import org.hotrod.runtime.sql.metadata.Column;
import org.hotrod.runtime.sql.metadata.Table;

public class Employee extends Table {

  // Constructor

  public Employee() {
    super(null, null, "employee", null);
    initialize();
  }

  public Employee(final String alias) {
    super(null, null, "employee", alias);
    initialize();
  }

  // Properties

  public Column<Number> id;
  public Column<String> name;
  public Column<Number> salary;
  public Column<Number> departmentId;
  public Column<Number> managerId;

  private void initialize() {
    this.id = new Column<Number>(this, "id");
    this.name = new Column<String>(this, "name");
    this.salary = new Column<Number>(this, "salary");
    this.departmentId = new Column<Number>(this, "department_id");
    this.managerId = new Column<Number>(this, "manager_id");
  }

}
