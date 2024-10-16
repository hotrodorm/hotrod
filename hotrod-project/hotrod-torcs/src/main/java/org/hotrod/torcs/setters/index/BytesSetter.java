package org.hotrod.torcs.setters.index;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BytesSetter extends IndexSetter {

  private byte[] value;

  public BytesSetter(int index, byte[] value) {
    super(index);
    this.value = value;
  }

  @Override
  public boolean isLOBParameter() {
    return true;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    ps.setBytes(this.index, this.value);
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
