package org.hotrod.dynamicsql.parameters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ParameterInjectionOccurrence extends ParameterOccurrence {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(ParameterInjectionOccurrence.class.getName());

  public ParameterInjectionOccurrence(String originalParameterName, Integer index, String value) {
    super(originalParameterName, index, value, null);
  }

  @Override
  public void applyTo(PreparedStatement ps, int ordinal, Connection conn) throws SQLException {
    ps.setObject(ordinal, this.value);
  }

}
