package org.hotrod.torcs.setters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DoubleSetter extends Setter {

  private double value;

  public DoubleSetter(int index, double value) {
    super(index);
    this.value = value;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    ps.setDouble(this.index, this.value);
  }

  @Override
  public Object value() {
    return this.value;
  }

}
