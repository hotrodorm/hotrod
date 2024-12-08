package org.hotrod.dynamic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class DynamicModificationQuery extends DynamicQuery {

  public int execute(DataSource dataSource, ParameterContext context) throws DynamicExpressionException, SQLException {
    PreparedQuery pq = super.prepare(context);

    try (Connection conn = dataSource.getConnection()) {
      try (PreparedStatement ps = pq.prepareStatement(conn)) {
        return ps.executeUpdate();
      }
    }
  }

}
