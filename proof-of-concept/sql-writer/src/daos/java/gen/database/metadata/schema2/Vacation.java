package gen.database.metadata.schema2;

import metadata.Column;
import metadata.Table;

public class Vacation extends Table {

  // Constructor

  public Vacation() {
    super(null, "schema2", "vacation", null);
    initialize();
  }

  public Vacation(final String alias) {
    super(null, "schema2", "vacation", alias);
    initialize();
  }

  // Properties

  public Column employeeId;
  public Column startDate;
  public Column endDate;
  public Column approved;

  private void initialize() {
    this.employeeId = new Column(this, "employee_id");
    this.startDate = new Column(this, "start_date");
    this.endDate = new Column(this, "end_date");
    this.approved = new Column(this, "approved");
  }

}
