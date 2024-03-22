package org.hotrod.torcs.setters.name;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

public class NameTimestampSetter extends NameSetter {

  private int type;
  private Timestamp x;
  private Calendar cal;

  public NameTimestampSetter(String name, Timestamp x) {
    super(name);
    this.type = 1;
    this.x = x;
  }

  public NameTimestampSetter(String name, Timestamp x, Calendar cal) {
    super(name);
    this.type = 2;
    this.x = x;
    this.cal = cal;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    if (this.type == 1) {
      cs.setTimestamp(this.name, this.x);
    } else {
      cs.setTimestamp(this.name, this.x, this.cal);
    }
  }

  @Override
  public Object value() {
    return this.x;
  }

  @Override
  public String guessSQLServerDataType() {
    return "datetime2";
  }

}
