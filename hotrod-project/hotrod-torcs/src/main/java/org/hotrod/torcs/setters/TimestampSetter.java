package org.hotrod.torcs.setters;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

public class TimestampSetter extends Setter {

  private int type;
  private Timestamp x;
  private Calendar cal;

//void  setTimestamp(int parameterIndex, Timestamp x)
//void  setTimestamp(int parameterIndex, Timestamp x, Calendar cal)

  public TimestampSetter(int index, Timestamp x) {
    super(index);
    this.type = 1;
    this.x = x;
  }

  public TimestampSetter(int index, Timestamp x, Calendar cal) {
    super(index);
    this.type = 2;
    this.x = x;
    this.cal = cal;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    if (this.type == 1) {
      ps.setTimestamp(this.index, this.x);
    } else {
      ps.setTimestamp(this.index, this.x, this.cal);
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
