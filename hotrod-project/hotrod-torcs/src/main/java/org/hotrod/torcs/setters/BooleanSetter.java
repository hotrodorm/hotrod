package org.hotrod.torcs.setters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BooleanSetter extends Setter {

  private boolean value;

  public BooleanSetter(int index, boolean value) {
    super(index);
    this.value = value;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    ps.setBoolean(this.index, this.value);
  }

  @Override
  public Object value() {
    return this.value;
  }

}
