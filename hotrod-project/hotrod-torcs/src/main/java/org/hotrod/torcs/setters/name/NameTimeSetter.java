package org.hotrod.torcs.setters.name;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;

public class NameTimeSetter extends NameSetter {

  private int type;
  private Time x;
  private Calendar cal;

  public NameTimeSetter(String name, Time x) {
    super(name);
    this.type = 1;
    this.x = x;
  }

  public NameTimeSetter(String name, Time x, Calendar cal) {
    super(name);
    this.type = 2;
    this.x = x;
    this.cal = cal;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    if (this.type == 1) {
      cs.setTime(this.name, this.x);
    } else {
      cs.setTime(this.name, this.x, this.cal);
    }
  }

  @Override
  public Object value() {
    return this.x;
  }

  @Override
  public String guessSQLServerDataType() {
    return "time";
  }

}
