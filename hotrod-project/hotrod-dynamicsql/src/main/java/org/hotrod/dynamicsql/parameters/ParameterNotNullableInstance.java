package org.hotrod.dynamicsql.parameters;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ParameterNotNullableInstance extends ParameterInstance {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(ParameterNotNullableInstance.class.getName());

  public ParameterNotNullableInstance(String name, Integer index, Object value) {
    super(name, index, value);
  }

  @Override
  public void applyTo(PreparedStatement ps, int ordinal) throws SQLException {
    ps.setObject(ordinal, this.value);
  }

}
