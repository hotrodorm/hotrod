package org.hotrod.torcs.plan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

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

public class MySQLPlanRetriever implements PlanRetriever {

  @Override
  public String getEstimatedExecutionPlan(final QueryExecution execution) throws SQLException {
    DataSource ds = execution.getDataSourceReference().getDataSource();
    try (Connection conn = ds.getConnection();) {
      conn.setAutoCommit(false);
      try (PreparedStatement ps = conn.prepareStatement("explain " + execution.getSQL());) {
        for (Setter s : execution.getSetters()) {
          s.applyTo(ps);
        }
        try (ResultSet rs = ps.executeQuery();) {
          StringBuilder sb = new StringBuilder();

          boolean first = true;

          CellStyle left = new CellStyle(HorizontalAlign.LEFT, AbbreviationStyle.CROP, NullStyle.NULL_TEXT);
          CellStyle right = new CellStyle(HorizontalAlign.RIGHT, AbbreviationStyle.CROP, NullStyle.NULL_TEXT);
          Table t = new Table(12, BorderStyle.DESIGN_FORMAL, ShownBorders.HEADER_AND_COLUMNS, false, 0);
          t.addCell("id", right);
          t.addCell("select_type", left);
          t.addCell("table", left);
          t.addCell("partitions", left);
          t.addCell("type", left);
          t.addCell("possible_keys", left);
          t.addCell("key", left);
          t.addCell("key_len", right);
          t.addCell("ref", left);
          t.addCell("rows", right);
          t.addCell("filtered", right);
          t.addCell("Extra", left);

          DecimalFormat filteredFormat = new DecimalFormat("0.0000");

          while (rs.next()) {
            sb.append((first ? "" : "\n") + rs.getString(1));
            first = false;

            long id = rs.getLong("id"); // id
            String selectType = rs.getString("select_type"); // select_type
            String table = rs.getString("table"); // table
            String partitions = rs.getString("partitions"); // partitions
            String type = rs.getString("type"); // type
            String possibleKeys = rs.getString("possible_keys"); // possible_keys
            String key = rs.getString("key"); // key
            String keyLen = rs.getString("key_len"); // key_len
            String ref = rs.getString("ref"); // ref
            long rows = rs.getLong("rows"); // rows
            double filtered = rs.getDouble("filtered"); // filtered
            String extra = rs.getString("Extra"); // Extra

            t.addCell("" + id, right);
            t.addCell(selectType, left);
            t.addCell(table, left);
            t.addCell(partitions, left);
            t.addCell(type, left);
            t.addCell(possibleKeys, left);
            t.addCell(key, left);
            t.addCell(keyLen, right);
            t.addCell(ref, left);
            t.addCell("" + rows, right);
            t.addCell(filteredFormat.format(filtered), right);
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
