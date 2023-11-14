package org.hotrod.torcs.setters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NStringSetter extends Setter {

  private String value;

//void  setNString(int parameterIndex, String value)

  public NStringSetter(int index, String value) {
    super(index);
    this.value = value;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    ps.setNString(this.index, this.value);
  }

  @Override
  public Object value() {
    return this.value;
  }

}
