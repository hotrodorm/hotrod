package org.hotrod.dynamicsql.parameters;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ParameterNullableInstance extends ParameterInstance {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(ParameterNullableInstance.class.getName());

  private int sqlType;

  public ParameterNullableInstance(int sqlType, String originalParameterName, Integer index, Object value) {
    super(originalParameterName, index, value);
    this.sqlType = sqlType;
  }

  public int getSQLType() {
    return sqlType;
  }

  @Override
  public void applyTo(PreparedStatement ps, int ordinal) throws SQLException {
    if (this.value != null) {
      ps.setObject(ordinal, this.value);
    } else {
      ps.setNull(ordinal, this.sqlType);
    }
  }

}
