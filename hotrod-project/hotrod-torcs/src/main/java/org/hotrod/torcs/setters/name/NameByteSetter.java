package org.hotrod.torcs.setters.name;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class NameByteSetter extends NameSetter {

  private byte value;

  public NameByteSetter(String name, byte value) {
    super(name);
    this.value = value;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    cs.setByte(this.name, this.value);
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
