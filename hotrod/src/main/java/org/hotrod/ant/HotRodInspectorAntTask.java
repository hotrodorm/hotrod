package org.hotrod.ant;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.JdbcTypes;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcTable;
import org.nocrala.tools.database.tartarus.exception.ReaderException;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class HotRodInspectorAntTask extends Task {

  private static transient final Logger log = Logger.getLogger(HotRodInspectorAntTask.class);

  // Properties

  private String driverClass = null;
  private String url = null;
  private String username = null;
  private String password = null;
  private String catalog = null;
  private String schema = null;
  private String destFile = null;

  private File dest = null;

  // Execute

  private void validateParameters() {

    // driverclass

    if (this.driverClass == null) {
      throw new BuildException("driverclass attribute must be specified.");
    }
    if (SUtils.isEmpty(this.driverClass)) {
      throw new BuildException("driverclass attribute cannot be empty.");
    }

    // url

    if (this.url == null) {
      throw new BuildException("url attribute must be specified.");
    }
    if (SUtils.isEmpty(this.url)) {
      throw new BuildException("url attribute cannot be empty.");
    }

    // username

    if (this.username == null) {
      throw new BuildException("username attribute must be specified.");
    }
    if (SUtils.isEmpty(this.username)) {
      throw new BuildException("username attribute cannot be empty.");
    }

    // password

    if (this.password == null) {
      throw new BuildException("password attribute must be specified, even if empty.");
    }

    // catalog

    if (SUtils.isEmpty(this.catalog)) {
      this.catalog = null;
    }

    // schema

    if (SUtils.isEmpty(this.schema)) {
      this.schema = null;
    }

    // destfile

    if (this.destFile == null) {
      throw new BuildException("destfile attribute must be specified.");
    }
    if (SUtils.isEmpty(this.destFile)) {
      throw new BuildException("destfile attribute cannot be empty.");
    }
    this.dest = new File(this.destFile);
    if (this.dest.exists()) {
      throw new BuildException("destfile attribute specifies an existing file, but must specify a non-existent file.");
    }

  }

  public void execute() {

    log.debug("init");

    // Validate Parameters

    validateParameters();

    // Assemble database location

    DatabaseLocation loc = new DatabaseLocation(this.driverClass, this.url, this.username, this.password, this.catalog,
        this.schema);

    // Load the database metadata

    JdbcDatabase db;
    try {
      db = new JdbcDatabase(loc);
    } catch (ReaderException e) {
      e.printStackTrace();
      throw new BuildException(e.getMessage());
    } catch (SQLException e) {
      e.printStackTrace();
      throw new BuildException(e.getMessage());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new BuildException(e.getMessage());
    }

    try {

      // Title formats

      WritableFont bold = new WritableFont(WritableFont.ARIAL);
      bold.setBoldStyle(WritableFont.BOLD);
      WritableCellFormat title = new WritableCellFormat(bold);
      title.setBackground(Colour.ICE_BLUE);
      WritableCellFormat titleCenter = new WritableCellFormat(bold);
      titleCenter.setAlignment(jxl.format.Alignment.CENTRE);
      titleCenter.setBackground(Colour.ICE_BLUE);
      WritableCellFormat titleStrong = new WritableCellFormat(bold);
      titleStrong.setAlignment(jxl.format.Alignment.CENTRE);
      titleStrong.setBackground(Colour.GOLD);

      // Data formats

      WritableFont normal = new WritableFont(WritableFont.ARIAL);
      WritableCellFormat data = new WritableCellFormat(normal);
      WritableCellFormat dataCenter = new WritableCellFormat(normal);
      dataCenter.setAlignment(jxl.format.Alignment.CENTRE);

      // Write sheet

      WritableWorkbook wb;
      wb = Workbook.createWorkbook(this.dest);
      WritableSheet s = wb.createSheet("JDBC Data", 0);

      // Titles

      int row = 0;
      int col = 0;
      s.addCell(new Label(col++, row, "Catalog", title));
      s.addCell(new Label(col++, row, "Schema", title));
      s.addCell(new Label(col++, row, "Table/View", title));
      s.addCell(new Label(col++, row, "Column", title));
      s.addCell(new Label(col++, row, "Pos", titleCenter));
      s.addCell(new Label(col++, row, "JDBC Type", titleStrong));
      s.addCell(new Label(col++, row, "java.sql.Types", titleStrong));
      s.addCell(new Label(col++, row, "java.sql.Types", titleStrong));
      s.addCell(new Label(col++, row, "Size", titleStrong));
      s.addCell(new Label(col++, row, "Dec Digits", titleStrong));
      s.addCell(new Label(col++, row, "Nullable", titleCenter));
      s.addCell(new Label(col++, row, "Is Nullable", titleCenter));
      s.addCell(new Label(col++, row, "Default Value", title));

      // Columns widths

      col = 0;
      s.setColumnView(col++, 9);
      s.setColumnView(col++, 9);
      s.setColumnView(col++, 20);
      s.setColumnView(col++, 20);
      s.setColumnView(col++, 5);
      s.setColumnView(col++, 15);
      s.setColumnView(col++, 14);
      s.setColumnView(col++, 14);
      s.setColumnView(col++, 5);
      s.setColumnView(col++, 5);
      s.setColumnView(col++, 3);
      s.setColumnView(col++, 6);
      s.setColumnView(col++, 20);

      // Data

      for (JdbcTable t : db.getTables()) {
        for (JdbcColumn c : t.getColumns()) {
          row++;
          col = 0;

          s.addCell(new Label(col++, row, t.getCatalog(), data));
          s.addCell(new Label(col++, row, t.getSchema(), data));
          s.addCell(new Label(col++, row, t.getName(), data));
          s.addCell(new Label(col++, row, c.getName(), data));
          s.addCell(new Number(col++, row, c.getOrdinalPosition(), dataCenter));
          s.addCell(new Label(col++, row, c.getTypeName(), dataCenter));
          s.addCell(new Number(col++, row, c.getDataType(), dataCenter));
          s.addCell(new Label(col++, row, JdbcTypes.codeToShortName(c.getDataType()), dataCenter));

          if (c.getColumnSize() != null) {
            s.addCell(new Number(col++, row, c.getColumnSize(), dataCenter));
          } else {
            col++;
          }
          if (c.getDecimalDigits() != null) {
            s.addCell(new Number(col++, row, c.getDecimalDigits(), dataCenter));
          } else {
            col++;
          }

          s.addCell(new Number(col++, row, c.get_Nullable(), dataCenter));
          s.addCell(new Label(col++, row, c.get_IsNullable(), dataCenter));
          s.addCell(new Label(col++, row, c.getColumnDef(), data));

        }
      }

      // Save

      wb.write();
      wb.close();

    } catch (RowsExceededException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (WriteException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  // Ant Setters

  public void setDriverclass(final String driverclass) {
    this.driverClass = driverclass;
  }

  public void setUrl(final String url) {
    this.url = url;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public void setCatalog(final String catalog) {
    this.catalog = catalog;
  }

  public void setSchema(final String schema) {
    this.schema = schema;
  }

  public void setDestfile(String destFile) {
    this.destFile = destFile;
  }

  // Helpers

}
