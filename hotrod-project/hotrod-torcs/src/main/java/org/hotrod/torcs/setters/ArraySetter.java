package org.hotrod.torcs.setters;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArraySetter extends Setter {

  private Array value;

  public ArraySetter(int index, Array value) {
    super(index);
    this.value = value;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    ps.setArray(this.index, this.value);
  }

  @Override
  public Object value() {
    return this.value;
  }

}