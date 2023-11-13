package org.hotrod.torcs.setters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LongSetter extends Setter {

  private long value;

  public LongSetter(int index, long value) {
    super(index);
    this.value = value;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    ps.setLong(this.index, this.value);
  }

  @Override
  public Object value() {
    return this.value;
  }

}
