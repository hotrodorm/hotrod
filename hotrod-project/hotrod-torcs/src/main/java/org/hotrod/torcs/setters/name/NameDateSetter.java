package org.hotrod.torcs.setters.name;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;

public class NameDateSetter extends NameSetter {

  private int type;
  private Date x;
  private Calendar cal;

  public NameDateSetter(String name, Date x) {
    super(name);
    this.type = 1;
    this.x = x;
  }

  public NameDateSetter(String name, Date x, Calendar cal) {
    super(name);
    this.type = 2;
    this.x = x;
    this.cal = cal;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    if (this.type == 1) {
      cs.setDate(this.name, this.x);
    } else {
      cs.setDate(this.name, this.x, this.cal);
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
