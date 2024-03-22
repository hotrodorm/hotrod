package org.hotrod.torcs.setters.index;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ByteSetter extends IndexSetter {

  private byte value;

  public ByteSetter(int index, byte value) {
    super(index);
    this.value = value;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    ps.setByte(this.index, this.value);
  }

  @Override
  public Object value() {
    return this.value;
  }

  @Override
  public String guessSQLServerDataType() {
    return "binary";
  }

}
