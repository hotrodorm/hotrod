package org.hotrod.torcs.setters.index;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BigDecimalSetter extends IndexSetter {

  private BigDecimal value;

  public BigDecimalSetter(int index, BigDecimal value) {
    super(index);
    this.value = value;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    ps.setBigDecimal(this.index, this.value);
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
