package org.hotrod.torcs.setters.name;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;

public class NameBigDecimalSetter extends NameSetter {

  private BigDecimal value;

  public NameBigDecimalSetter(String name, BigDecimal value) {
    super(name);
    this.value = value;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    cs.setBigDecimal(this.name, this.value);
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
