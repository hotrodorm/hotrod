package org.hotrod.torcs.setters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IntSetter extends Setter {

  private int value;

  public IntSetter(int index, int value) {
    super(index);
    this.value = value;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    ps.setInt(this.index, this.value);
  }

  @Override
  public Object value() {
    return this.value;
  }

}
