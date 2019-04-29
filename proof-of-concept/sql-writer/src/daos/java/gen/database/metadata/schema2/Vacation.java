package gen.database.metadata.schema2;

import java.util.Date;

import org.hotrod.runtime.sql.metadata.Column;
import org.hotrod.runtime.sql.metadata.Table;

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

  public Column<Number> employeeId;
  public Column<Date> startDate;
  public Column<Date> endDate;
  public Column<Number> approved;

  private void initialize() {
    this.employeeId = new Column<Number>(this, "employee_id");
    this.startDate = new Column<Date>(this, "start_date");
    this.endDate = new Column<Date>(this, "end_date");
    this.approved = new Column<Number>(this, "approved");
  }

}
