package org.hotrod.plugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.Constants;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.runtime.BuildInformation;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.CellStyle.AbbreviationStyle;
import org.nocrala.tools.texttablefmt.CellStyle.HorizontalAlign;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

public class ExportColumnsToTXTOperation extends AbstractExportColumnsOperation {

  private static final Logger log = LogManager.getLogger(ExportColumnsToTXTOperation.class);

  private static final int PAGE_SIZE = 40;

  public ExportColumnsToTXTOperation(final File baseDir, final String configfilename, final String generator,
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

    int columns = 15 + nativeNames.size();
    Table t = new Table(columns, BorderStyle.DESIGN_FORMAL_WIDE, ShownBorders.HEADER_AND_COLUMNS);

    t.setColumnWidth(6, 1, 30);

    CellStyle right = new CellStyle(HorizontalAlign.RIGHT);
    CellStyle limited = new CellStyle(HorizontalAlign.LEFT, AbbreviationStyle.DOTS);

    // Header

    t.addCell("catalog");
    t.addCell("schema");
    t.addCell("objectName");
    t.addCell("ordinal", right);
    t.addCell("name");

    t.addCell("typeName");
    t.addCell("dataType");
    t.addCell("size", right);
    t.addCell("scale", right);
    t.addCell("default", limited);

    t.addCell("autogeneration");
    t.addCell("belongsToPK");
    t.addCell("isVersionControlColumn");
    t.addCell("nature");
    t.addCell("nullable");

    nativeNames.forEach(n -> t.addCell("native." + n));

    // Body

    g.getConfig().getTypeSolverTag().getRetrievedColumns().stream().forEach(c -> {

      t.addCell(c.getCatalog());
      t.addCell(c.getSchema());
      t.addCell(c.getObjectName());
      t.addCell(c.getOrdinal() == null ? "" : "" + c.getOrdinal(), right);
      t.addCell(c.getName());

      t.addCell(c.getTypeName());
      t.addCell(c.getDataType() == null ? "" : "" + c.getDataType());
      t.addCell(c.getSize() == null ? "" : "" + c.getSize(), right);
      t.addCell(c.getScale() == null ? "" : "" + c.getScale(), right);
      t.addCell(c.getDefault() == null ? "" : "" + c.getDefault(), limited);

      t.addCell(c.getAutogeneration() == null ? "" : "" + c.getAutogeneration());
      t.addCell(c.getBelongsToPK() == null ? "" : "" + c.getBelongsToPK());
      t.addCell(c.getIsVersionControlColumn() == null ? "" : "" + c.getIsVersionControlColumn());
      t.addCell(c.getNature() == null ? "" : "" + c.getNature());
      t.addCell(c.getNullable() == null ? "" : "" + c.getNullable());

      nativeNames.stream().map(n -> c.getNative() == null ? null : c.getNative().get(n))
          .map(v -> v == null ? "" : v.toString()).forEach(x -> t.addCell(x));
    });

    DateTimeFormatter df = DateTimeFormatter.ofPattern("d MMM yyyy 'at' HH:mm:ss Z");
    OffsetDateTime now = OffsetDateTime.now();

    try (BufferedWriter w = new BufferedWriter(new FileWriter(this.exportFile))) {
      String[] content = t.renderAsStringArray();

      w.write("HotRod Column Export\n");
      w.write("--------------------\n");

      w.write("\n");

      w.write("  From live database at : " + this.jdbcurl + "\n");
      w.write("  Configuration file    : " + this.configFile + "\n");
      w.write("  Catalog               : " + (this.jdbccatalog == null ? "" : this.jdbccatalog) + "\n");
      w.write("  Schema                : " + (this.jdbcschema == null ? "" : this.jdbcschema) + "\n");
      w.write("  Exported              : " + now.format(df) + "\n");
      w.write("  Generated by          : " + Constants.TOOL_NAME + " version " + BuildInformation.VERSION + " (build "
          + BuildInformation.BUILD_ID + ")\n");

      int absLine = 2;
      int line = PAGE_SIZE;

      for (int l = absLine; l < content.length; l++) {
        if (line >= PAGE_SIZE) {
          w.write("\n");
          w.write(content[0] + "\n");
          w.write(content[1] + "\n");
          line = 0;
        }
        w.write(content[l] + "\n");
        line++;
      }

      w.write("\n");
    }

  }

}