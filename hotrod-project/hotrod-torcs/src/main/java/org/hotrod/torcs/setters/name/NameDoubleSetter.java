package org.hotrod.torcs.setters.name;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class NameDoubleSetter extends NameSetter {

  private double value;

  public NameDoubleSetter(String name, double value) {
    super(name);
    this.value = value;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    cs.setDouble(this.name, this.value);
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
