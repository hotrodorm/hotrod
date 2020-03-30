package org.hotrod.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hotrod.config.Constants;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.runtime.BuildInformation;

public class ExportColumnsToXLSXOperation extends AbstractExportColumnsOperation {

  private static final Logger log = LogManager.getLogger(ExportColumnsToXLSXOperation.class);

  private int line;
  private int col;

  public ExportColumnsToXLSXOperation(final File baseDir, final String configfilename, final String generator,
      final String localproperties, final String jdbcdriverclass, final String jdbcurl, final String jdbcusername,
      final String jdbcpassword, final String jdbccatalog, final String jdbcschema, final String facets,
      final String display, final String exportfilename) {
    super(baseDir, configfilename, generator, localproperties, jdbcdriverclass, jdbcurl, jdbcusername, jdbcpassword,
        jdbccatalog, jdbcschema, facets, display, exportfilename);
    log.debug("init");
  }

  @Override
  protected void exportColumns(final HotRodGenerator g) throws IOException {
    LinkedHashSet<String> nativeNames = new LinkedHashSet<>();
    g.getConfig().getTypeSolverTag().getRetrievedColumns().stream().forEach(c -> {
      if (c.getNative() != null) {
        c.getNative().keySet().stream().forEach(n -> nativeNames.add(n));
      }
    });

    DateTimeFormatter df = DateTimeFormatter.ofPattern("d MMM yyyy 'at' HH:mm:ss Z");
    OffsetDateTime now = OffsetDateTime.now();

    try (Workbook workbook = new XSSFWorkbook(); OutputStream os = new FileOutputStream(this.exportFile);) {

      Sheet sheet = workbook.createSheet("Database Columns");

      this.line = 0;

      // Title

      CellStyle nameStyle = workbook.createCellStyle();
      XSSFFont nfont = ((XSSFWorkbook) workbook).createFont();
      nfont.setFontName("Arial");
      nfont.setFontHeightInPoints((short) 16);
      nfont.setBold(true);
      nameStyle.setFont(nfont);

      Row title;

      title = sheet.createRow(this.line++);
      this.col = 0;
      addCell(title, nameStyle, "HotRod Column Export");

      title = sheet.createRow(this.line++);
      this.col = 0;
      addCell(title, null,
          Constants.TOOL_NAME + " version " + BuildInformation.VERSION + " (build " + BuildInformation.BUILD_ID + ")");

      // Details

      Row details;

      details = sheet.createRow(this.line++);
      this.col = 0;
      addCell(details, null, "");
      details = sheet.createRow(this.line++);
      this.col = 0;
      addCell(details, null, "  From live database at: " + this.jdbcurl);
      details = sheet.createRow(this.line++);
      this.col = 0;
      addCell(details, null, "  Configuration file: " + this.configFile);
      details = sheet.createRow(this.line++);
      this.col = 0;
      addCell(details, null, "  Catalog: " + (this.jdbccatalog == null ? "" : this.jdbccatalog));
      details = sheet.createRow(this.line++);
      this.col = 0;
      addCell(details, null, "  Schema: " + (this.jdbcschema == null ? "" : this.jdbcschema));
      details = sheet.createRow(this.line++);
      this.col = 0;
      addCell(details, null, "  Exported: " + now.format(df));
      details = sheet.createRow(this.line++);
      this.col = 0;
      addCell(details, null, "");

      // Header

      Row header = sheet.createRow(this.line++);

      CellStyle headerStyle = workbook.createCellStyle();
      XSSFFont font = ((XSSFWorkbook) workbook).createFont();
      font.setFontName("Arial");
      font.setFontHeightInPoints((short) 10);
      font.setBold(true);
      headerStyle.setFont(font);

      this.col = 0;
      addCell(header, headerStyle, "catalog");
      addCell(header, headerStyle, "schema");
      addCell(header, headerStyle, "objectName");
      addCell(header, headerStyle, "ordinal");
      addCell(header, headerStyle, "name");

      addCell(header, headerStyle, "typeName");
      addCell(header, headerStyle, "dataType");
      addCell(header, headerStyle, "size");
      addCell(header, headerStyle, "scale");
      addCell(header, headerStyle, "default");

      addCell(header, headerStyle, "autogeneration");
      addCell(header, headerStyle, "belongsToPK");
      addCell(header, headerStyle, "isVersionControlColumn");
      addCell(header, headerStyle, "nature");
      addCell(header, headerStyle, "nullable");

      nativeNames.forEach(n -> addCell(header, headerStyle, "native." + n));

      // Body

//      Row body = sheet.createRow(this.line++);

      CellStyle style = workbook.createCellStyle();
//      style.setWrapText(true);

//      this.col = 0;
//      addCell(body, style, "catalog");

      g.getConfig().getTypeSolverTag().getRetrievedColumns().stream().forEach(c -> {

        Row body = sheet.createRow(this.line++);
        this.col = 0;

        addCell(body, style, c.getCatalog());
        addCell(body, style, c.getSchema());
        addCell(body, style, c.getObjectName());
        addCell(body, style, c.getOrdinal() == null ? "" : "" + c.getOrdinal()); // right
        addCell(body, style, c.getName());

        addCell(body, style, c.getTypeName());
        addCell(body, style, c.getDataType() == null ? "" : "" + c.getDataType());
        addCell(body, style, c.getSize() == null ? "" : "" + c.getSize()); // right
        addCell(body, style, c.getScale() == null ? "" : "" + c.getScale()); // right
        addCell(body, style, c.getDefault() == null ? "" : "" + c.getDefault());

        addCell(body, style, c.getAutogeneration() == null ? "" : "" + c.getAutogeneration());
        addCell(body, style, c.getBelongsToPK() == null ? "" : "" + c.getBelongsToPK());
        addCell(body, style, c.getIsVersionControlColumn() == null ? "" : "" + c.getIsVersionControlColumn());
        addCell(body, style, c.getNature() == null ? "" : "" + c.getNature());
        addCell(body, style, c.getNullable() == null ? "" : "" + c.getNullable());

        nativeNames.stream().map(n -> c.getNative() == null ? null : c.getNative().get(n))
            .map(v -> v == null ? "" : v.toString()).forEach(x -> addCell(body, style, x));
      });

      // Auto width

      try {
        for (int i = 0; i < 15 + nativeNames.size(); i++) {
          sheet.autoSizeColumn(i);
        }
      } catch (Exception e) {
        // Ignore. Some platforms (e.g. Android) may crash on the absence of fonts
        // and/or other resources)
      }

      // write XLSX

      workbook.write(os);

    }

  }

  private void addCell(final Row r, final CellStyle style, final String content) {
    Cell c = r.createCell(this.col++);
    c.setCellValue(content);
    if (style != null) {
      c.setCellStyle(style);
    }
  }

}