package org.hotrod.dynamic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamic.segments.ParameterSegment;
import org.hotrod.dynamic.segments.StaticSegmentConsumer;
import org.hotrod.dynamic.segments.WhereSegment;
import org.hotrod.utils.JDBCTypes;
import org.hotrod.utils.SUtil;

public class PreparedQuery implements StaticSegmentConsumer {

  private static final Logger log = Logger.getLogger(PreparedQuery .class.getName());

  private StringBuilder sb = new StringBuilder();
  private List<ParameterSegment> parameters = new ArrayList<>();

  @Override
  public void consume(String literal) {
//    log.info(">> received literal");
    this.sb.append(literal);
  }

  @Override
  public void consume(ParameterSegment p) {
//    log.info(">> received parameter");
    this.parameters.add(p);
  }

//  @Override
//  public void consume(StaticSegment s) {
//    String literal = s.getLiteral();
//    if (literal != null) {
//      this.sb.append(literal);
//    }
//    ParameterSegment p = s.getParameter();
//    if (p != null) {
//      this.parameters.add(p);
//    }
//  }

//  public void addLiteral(final String literal) {
//    this.sb.append(literal);
//  }
//
//  public void registerParameter(ParameterSegment p) {
//    this.parameters.add(p);
//  }

  private static final int MAX_DISPLAY_VALUE = 100;

  public String getPreview() {
    StringBuilder p = new StringBuilder();
    p.append(this.sb.toString());
    p.append("\n=== Parameters (" + this.parameters.size() + ") ===\n");
    int pos = 1;
    for (ParameterSegment ps : this.parameters) {
      String sqlTypeName = JDBCTypes.codeToShortName(ps.getSQLType());
      Object value = ps.getValue();
      String tostring = "" + value;
      if (tostring.length() > MAX_DISPLAY_VALUE) {
        tostring = tostring.substring(0, MAX_DISPLAY_VALUE - 3) + "...";
      }
      p.append("" + pos++ + ". " + ps.getName() + " (" + SUtil.coalesce(sqlTypeName, "OTHER/" + ps.getSQLType()) + "): "
          + tostring + (value == null ? "" : " (" + value.getClass().getName() + ")") + "\n");
    }
    if (!this.parameters.isEmpty()) {
      p.append("======================\n");
    }
    return p.toString();
  }

  public int execute(Connection conn) throws SQLException, DynamicExpressionException {
    try (PreparedStatement ps = conn.prepareStatement(this.sb.toString())) {
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
