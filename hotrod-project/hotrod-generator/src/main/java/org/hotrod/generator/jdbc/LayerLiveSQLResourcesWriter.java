package org.hotrod.generator.jdbc;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.JDBCTag;
import org.hotrod.config.LiveSQLFunctionTag;
import org.hotrod.config.LiveSQLFunctionTemplateParser.Section;
import org.hotrod.config.LiveSQLTag;
import org.hotrod.exceptions.FaultException;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.livesql.functions.LiveSQLFunctionTemplate;
import org.hotrod.livesql.functions.LiveSQLFunctionTemplateBuilder;
import org.hotrod.utils.ClassWriter;
import org.hotrod.utils.SUtil;

public class LayerLiveSQLResourcesWriter {

  private static final Logger log = Logger.getLogger(LayerLiveSQLResourcesWriter.class.getName());

  private HotRodConfigTag config;
  private JDBCTag jdbcTag;
  private String className;
  private ClassWriter w;

  public LayerLiveSQLResourcesWriter(final HotRodConfigTag config, JDBCTag jdbcTag) {
    log.fine("init");
    this.config = config;
    this.jdbcTag = jdbcTag;
    this.className = this.jdbcTag.getLayerResources().getLiveSQLFunctionsClassName();
  }

  public void generate(final FileGenerator fileGenerator) throws FaultException {

    File dir = this.jdbcTag.getLayerResources().getDir();
    dir.mkdirs();

    File f = new File(dir, this.className + ".java");

    try (TextWriter tw = fileGenerator.createWriter(f)) {

      this.w = new ClassWriter(this.jdbcTag.getLayerResources().getAssembledPackage());

      this.writeHeader();
      this.writeLiveSQLResources();
//      this.writeConverterBeans();
//      this.writeLayerConfig();
//
//      if (this.jdbcTag.generateQualifierBeans()) {
//        this.writeDataSourceProperties();
//        this.writeDataSource();
//        this.writeDialectProperties();
//        this.writeLiveSQL();
//      }
//
      this.writeFooter();

      w.writeTo(tw);

    } catch (IOException e) {
      throw new FaultException("Could not generate " + this.className + " class", e);
    }

  }

  private void writeHeader() {
    w.println("public class " + this.className + " {");
  }

  private void writeLiveSQLResources() {
    LiveSQLTag liveSQLTag = this.config.getLiveSQLTag();
    if (liveSQLTag == null) {
      return;
    }
    for (LiveSQLFunctionTag f : liveSQLTag.getFunctions()) {
      w.println();
      w.print("  public static final ", LiveSQLFunctionTemplate.class, " ", f.getName(), " = ");
      w.println(LiveSQLFunctionTemplateBuilder.class, ".name(\"" + SUtil.escapeJavaString(f.getName()) + "\")");
      for (Section s : f.getTemplateSections()) {
        w.print("      ");
        s.printTo(w);
        w.println();
      }
      w.println("      .end();");
    }
  }

  private void writeFooter() {
    w.println();
    w.println("}");
  }

}
