package org.hotrod.torcs.plan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hotrod.torcs.QueryExecution;
import org.hotrod.torcs.setters.Setter;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.CellStyle.AbbreviationStyle;
import org.nocrala.tools.texttablefmt.CellStyle.HorizontalAlign;
import org.nocrala.tools.texttablefmt.CellStyle.NullStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

public class MariaDBPlanRetriever implements PlanRetriever {

  @Override
  public String getEstimatedExecutionPlan(final QueryExecution execution, final int format) throws SQLException {

    String formatClause;
    switch (format) {
    case 0:
      formatClause = " format = traditional";
      break;
    case 1:
      formatClause = " format = json";
      break;
    default:
      throw new SQLException(
          "Invalid MariaDB plan format '" + format + "'. Valid values are: 0 (TRADITIONAL), and 1 (JSON).");
    }

    DataSource ds = execution.getDataSourceReference().getDataSource();
    try (Connection conn = ds.getConnection();) {
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement("explain" + formatClause + " " + execution.getSQL());) {
        for (Setter s : execution.getSetters()) {
          s.applyTo(ps);
        }
        try (ResultSet rs = ps.executeQuery();) {
          StringBuilder sb = new StringBuilder();

          boolean first = true;

          CellStyle left = new CellStyle(HorizontalAlign.LEFT, AbbreviationStyle.CROP, NullStyle.NULL_TEXT);
          CellStyle right = new CellStyle(HorizontalAlign.RIGHT, AbbreviationStyle.CROP, NullStyle.NULL_TEXT);
          Table t = new Table(10, BorderStyle.DESIGN_FORMAL, ShownBorders.HEADER_AND_COLUMNS, false, 0);
          t.addCell("id", right);
          t.addCell("select_type", left);
          t.addCell("table", left);
          t.addCell("type", left);
          t.addCell("possible_keys", left);
          t.addCell("key", left);
          t.addCell("key_len", right);
          t.addCell("ref", left);
          t.addCell("rows", right);
          t.addCell("Extra", left);

          while (rs.next()) {
            sb.append((first ? "" : "\n") + rs.getString(1));
            first = false;

            long id = rs.getLong("id"); // id
            String selectType = rs.getString("select_type"); // select_type
            String table = rs.getString("table"); // table
            String otype = rs.getString("type"); // type
            String possibleKeys = rs.getString("possible_keys"); // possible_keys
            String key = rs.getString("key"); // key
            String keyLen = rs.getString("key_len"); // key_len
            String ref = rs.getString("ref"); // ref
            long rows = rs.getLong("rows"); // rows
            String extra = rs.getString("Extra"); // Extra

            t.addCell("" + id, right);
            t.addCell(selectType, left);
            t.addCell(table, left);
            t.addCell(otype, left);
            t.addCell(possibleKeys, left);
            t.addCell(key, left);
            t.addCell(keyLen, right);
            t.addCell(ref, left);
            t.addCell("" + rows, right);
            t.addCell(extra, left);

          }
          return t.render();
        }
      } finally {
        conn.rollback();
      }
    }
  }

}
