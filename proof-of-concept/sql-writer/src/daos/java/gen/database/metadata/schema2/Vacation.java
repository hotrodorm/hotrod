package gen.database.metadata.schema2;

import org.hotrod.runtime.sql.metadata.NumberColumn;
import org.hotrod.runtime.sql.metadata.Table;
import org.hotrod.runtime.sql.metadata.DateColumn;

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

  public NumberColumn employeeId;
  public DateColumn startDate;
  public DateColumn endDate;
  public NumberColumn approved;

  private void initialize() {
    this.employeeId = new NumberColumn(this, "employee_id");
    this.startDate = new DateColumn(this, "start_date");
    this.endDate = new DateColumn(this, "end_date");
    this.approved = new NumberColumn(this, "approved");
  }

}
