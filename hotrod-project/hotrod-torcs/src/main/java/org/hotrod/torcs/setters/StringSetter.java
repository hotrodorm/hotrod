package org.hotrod.torcs.setters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StringSetter extends Setter {

  private String value;

//void  setString(int parameterIndex, String x)

  public StringSetter(int index, String value) {
    super(index);
    this.value = value;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    ps.setString(this.index, this.value);
  }

  @Override
  public Object value() {
    return this.value;
  }

}
