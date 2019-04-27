package gen.database.metadata;

import sql.metadata.Column;
import sql.metadata.Table;

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

  public Column id;
  public Column name;
  public Column salary;
  public Column departmentId;
  public Column managerId;

  private void initialize() {
    this.id = new Column(this, "id");
    this.name = new Column(this, "name");
    this.salary = new Column(this, "salary");
    this.departmentId = new Column(this, "department_id");
    this.managerId = new Column(this, "manager_id");
  }

}
