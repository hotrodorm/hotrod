package gen.database.metadata;

import org.hotrod.runtime.livesql.metadata.NumberColumn;
import org.hotrod.runtime.livesql.metadata.StringColumn;
import org.hotrod.runtime.livesql.metadata.Table;

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

  public NumberColumn id;
  public StringColumn name;
  public NumberColumn salary;
  public NumberColumn departmentId;
  public NumberColumn managerId;

  private void initialize() {
    this.id = new NumberColumn(this, "id");
    this.name = new StringColumn(this, "name");
    this.salary = new NumberColumn(this, "salary");
    this.departmentId = new NumberColumn(this, "department_id");
    this.managerId = new NumberColumn(this, "manager_id");
  }

}
