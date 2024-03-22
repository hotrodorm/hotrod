package org.hotrod.torcs.setters.name;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class NameShortSetter extends NameSetter {

  private short value;

  public NameShortSetter(String name, short value) {
    super(name);
    this.value = value;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    cs.setShort(this.name, this.value);
  }

  @Override
  public Object value() {
    return this.value;
  }

  @Override
  public String guessSQLServerDataType() {
    return "numeric";
  }

}
