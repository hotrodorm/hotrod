package org.hotrod.dynamicsql.parameters;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ParameterInjectionInstance extends ParameterInstance {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(ParameterInjectionInstance.class.getName());

  public ParameterInjectionInstance(String originalParameterName, Integer index, String value) {
    super(originalParameterName, index, value);
  }

  @Override
  public void applyTo(PreparedStatement ps, int ordinal) throws SQLException {
    ps.setObject(ordinal, this.value);
  }

}
