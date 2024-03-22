package org.hotrod.torcs.setters.name;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class NameStringSetter extends NameSetter {

  private String value;

  public NameStringSetter(String name, String value) {
    super(name);
    this.value = value;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    cs.setString(this.name, this.value);
  }

  @Override
  public Object value() {
    return this.value;
  }

  @Override
  public String guessSQLServerDataType() {
    return "varchar";
  }

}
