package org.hotrod.dynamicsql.parameters;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class VariableInstance extends ParameterInstance {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(VariableInstance.class.getName());

  public VariableInstance(String originalParameterName, Integer index, Object value) {
    super(originalParameterName, index, value);
  }

  @Override
  public void applyTo(PreparedStatement ps, int ordinal) throws SQLException {
    ps.setObject(ordinal, this.value);
  }

}
