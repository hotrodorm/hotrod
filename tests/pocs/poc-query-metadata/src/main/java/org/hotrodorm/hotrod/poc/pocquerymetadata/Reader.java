package org.hotrodorm.hotrod.poc.pocquerymetadata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.CellStyle.HorizontalAlign;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

public class Reader {

  // 1. driver class
  // 2. URL
  // 3. username
  // 4. password
  public static void main(final String[] args) throws ClassNotFoundException, SQLException {

    String driverClass = args[0];
    String url = args[1];
    String username = args[2];
    String password = args[3];

    System.out.println(" - Will load driver...");
    Class<?> c = Class.forName(driverClass);
    System.out.println(" - Driver loaded... c=" + c);
    Connection conn = DriverManager.getConnection(url, username, password);
    System.out.println(" - Connected.");

    PreparedStatement ps = conn.prepareStatement("select * from all_types_view");
    ResultSetMetaData md = ps.getMetaData();
    // System.out.println(" - md=" + md);

    int cols = md.getColumnCount();
    // System.out.println(" - cols=" + cols);

    Table t = new Table(17, BorderStyle.DESIGN_FORMAL_WIDE, ShownBorders.HEADER_AND_COLUMNS);
    // t.setColumnWidth(6, 1, 30);
    CellStyle right = new CellStyle(HorizontalAlign.RIGHT);
    // CellStyle limited = new CellStyle(HorizontalAlign.LEFT,
    // AbbreviationStyle.DOTS);

//    t.addCell("catalog");
//    t.addCell("schema");
//    t.addCell("table");
//    t.addCell("name");
    t.addCell("Column");

    t.addCell("Type");
    t.addCell("Size", right);
    t.addCell("Precision", right);
    t.addCell("Scale", right);
    t.addCell("Signed");
    t.addCell("Nullable");

    t.addCell("JDBC #", right);
    t.addCell("JDBC type");
    t.addCell("Class suggested by driver");

    t.addCell("ainc");
    t.addCell("case");
    t.addCell("curr");
    t.addCell("dwrit");
    t.addCell("ronly");
    t.addCell("search");
    t.addCell("writ");

    for (int i = 1; i <= cols; i++) {

      t.addCell(md.getColumnLabel(i)); // this is the result set column name

      t.addCell(md.getColumnTypeName(i));
      t.addCell("" + md.getColumnDisplaySize(i), right);
      t.addCell("" + md.getPrecision(i), right);
      t.addCell("" + md.getScale(i), right);
      t.addCell("" + md.isSigned(i));
      t.addCell(md.isNullable(i) == ResultSetMetaData.columnNoNulls ? "No"
          : (md.isNullable(i) == ResultSetMetaData.columnNullable ? "Yes" : "Unknown"));

      t.addCell("" + md.getColumnType(i), right);
      JDBCType jt;
      try {
        jt = JDBCType.valueOf(md.getColumnType(i));
      } catch (IllegalArgumentException e) {
        jt = null;
      }
      t.addCell(jt == null ? "  ?" : jt.getName());
      t.addCell(md.getColumnClassName(i));

      t.addCell("" + md.isAutoIncrement(i));
      t.addCell("" + md.isCaseSensitive(i));
      t.addCell("" + md.isCurrency(i));
      t.addCell("" + md.isDefinitelyWritable(i));
      t.addCell("" + md.isReadOnly(i));
      try {
        t.addCell("" + md.isSearchable(i));
      } catch (SQLException e) {
        t.addCell("?");
      }
      t.addCell("" + md.isWritable(i));

    }

    System.out.println(t.render());

  }

}
