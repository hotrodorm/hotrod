package org.hotrod.torcs.setters;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

public class DateSetter extends Setter {

  private int type;
  private Date x;
  private Calendar cal;

//void  setDate(int parameterIndex, Date x)
//void  setDate(int parameterIndex, Date x, Calendar cal)

  public DateSetter(int index, Date x) {
    super(index);
    this.type = 1;
    this.x = x;
  }

  public DateSetter(int index, Date x, Calendar cal) {
    super(index);
    this.type = 2;
    this.x = x;
    this.cal = cal;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    if (this.type == 1) {
      ps.setDate(this.index, this.x);
    } else {
      ps.setDate(this.index, this.x, this.cal);
    }
  }

  @Override
  public Object value() {
    return this.x;
  }

  @Override
  public String guessSQLServerDataType() {
    return "date";
  }

}
