package org.hotrod.dynamic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.hotrod.dynamic.segments.ParameterSegment;

public class PreparedModificationQuery extends PreparedQuery {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(PreparedModificationQuery.class.getName());

  public PreparedModificationQuery(SimpleStaticSegmentConsumer sc) {
    super(sc.getSQL(), sc.getParameters());
  }

  public int execute(Connection conn) throws SQLException, DynamicExpressionException {
    try (PreparedStatement ps = conn.prepareStatement(super.sql)) {
      int ordinal = 1;
      for (ParameterSegment p : this.parameters) {
        if (p.getValue() != null) {
          ps.setObject(ordinal++, p.getValue());
        } else {
          ps.setNull(ordinal++, p.getSQLType());
        }
      }
      return ps.executeUpdate();
    }
  }

}
