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

            int col = 1;
            long id = rs.getLong(col++); // id
            String selectType = rs.getString(col++); // select_type
            String table = rs.getString(col++); // table
            String partitions = rs.getString(col++); // partitions
            String type = rs.getString(col++); // type
            String possibleKeys = rs.getString(col++); // possible_keys
            String key = rs.getString(col++); // key
            String keyLen = rs.getString(col++); // key_len
            String ref = rs.getString(col++); // ref
            long rows = rs.getLong(col++); // rows
            double filtered = rs.getDouble(col++); // filtered
            String extra = rs.getString(col++); // Extra

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
