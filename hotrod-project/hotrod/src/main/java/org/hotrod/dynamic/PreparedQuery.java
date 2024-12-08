package org.hotrod.dynamic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PreparedQuery {

  private StringBuilder sb = new StringBuilder();
  private List<DynamicParameter> appliedParameters = new ArrayList<>();

  public void addLiteral(final String literal) {
    this.sb.append(literal);
  }

  public void registerParameter(DynamicParameter p) {
    this.sb.append("?");
    this.appliedParameters.add(p);
  }

  public PreparedStatement prepareStatement(Connection conn) throws SQLException {
    PreparedStatement ps = conn.prepareStatement(this.sb.toString());
    int ordinal = 1;
    for (DynamicParameter ap : this.appliedParameters) {
      if (ap.getValue() != null) {
        ps.setObject(ordinal++, ap.getValue());
      } else {
        ps.setNull(ordinal++, ap.getSQLType());
      }
    }
    return ps;
  }

}
