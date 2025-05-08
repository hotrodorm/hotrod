package org.hotrod.dynamicsql.parameters;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ParameterNotNullableChangeableInstance extends ParameterInstance {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(ParameterNotNullableChangeableInstance.class.getName());

  public ParameterNotNullableChangeableInstance(String originalParameterName, Integer index, Object value) {
    super(originalParameterName, index, value);
  }

  public void setValue(Object value) {
    this.value = value;
  }

  @Override
  public void applyTo(PreparedStatement ps, int ordinal) throws SQLException {
    ps.setObject(ordinal, this.value);
  }

}
