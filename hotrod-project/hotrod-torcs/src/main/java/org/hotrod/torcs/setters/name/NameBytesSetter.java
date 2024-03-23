package org.hotrod.torcs.setters.name;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class NameBytesSetter extends NameSetter {

  private byte[] value;

  public NameBytesSetter(String name, byte[] value) {
    super(name);
    this.value = value;
  }

  @Override
  public boolean isLOBParameter() {
    return true;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    cs.setBytes(this.name, this.value);
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
