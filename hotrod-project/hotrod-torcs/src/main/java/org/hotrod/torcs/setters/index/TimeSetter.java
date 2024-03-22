package org.hotrod.torcs.setters.index;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;

public class TimeSetter extends IndexSetter {

  private int type;
  private Time x;
  private Calendar cal;

//void  setTime(int parameterIndex, Time x)
//void  setTime(int parameterIndex, Time x, Calendar cal)

  public TimeSetter(int index, Time x) {
    super(index);
    this.type = 1;
    this.x = x;
  }

  public TimeSetter(int index, Time x, Calendar cal) {
    super(index);
    this.type = 2;
    this.x = x;
    this.cal = cal;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    if (this.type == 1) {
      ps.setTime(this.index, this.x);
    } else {
      ps.setTime(this.index, this.x, this.cal);
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
