package org.hotrod.torcs.setters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ShortSetter extends Setter {

  private short value;

  public ShortSetter(int index, short value) {
    super(index);
    this.value = value;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    ps.setShort(this.index, this.value);
  }

  @Override
  public Object value() {
    return this.value;
  }

}
