package org.hotrod.generator.jdbc;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.JDBCTag;
import org.hotrod.config.LiveSQLFunctionTag;
import org.hotrod.config.LiveSQLTag;
import org.hotrod.exceptions.FaultException;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.livesql.functions.LiveSQLFunctionTemplate;
import org.hotrod.livesql.functions.LiveSQLFunctionTemplateBuilder;
import org.hotrod.utils.ClassWriter;
import org.hotrod.utils.FunctionUtils;

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
      w.println("  // Function template: " + f.getOriginalTemplate());
      w.println("  public static final ", LiveSQLFunctionTemplate.class, " ", f.getName(), " = ");
      LiveSQLFunctionTemplate template = LiveSQLFunctionTemplateBuilder.build(f.getName(), f.getTemplateSections());
      String serialized = FunctionUtils.render(template);

      w.println("    ", FunctionUtils.class, ".prepare(");

      for (int i = 0; i < serialized.length(); i += 50) {
        w.print("      \"" + serialized.substring(i, Math.min(i + 50, serialized.length())) + "\"");
        if (i + 50 < serialized.length()) {
          w.println(" +");
        } else {
          w.println(");");
        }
      }
//      w.print("    ", FunctionUtils.class, ".deserialize(\"" + serialized + "\");");

//      w.println("\"" + serialized + "\";");
//      w.println(LiveSQLFunctionTemplateBuilder.class, ".name(\"" + SUtil.escapeJavaString(f.getName()) + "\")");
//      for (Section s : f.getTemplateSections()) {
//        w.print("      ");
//        s.printTo(w);
//        w.println();
//      }
//      w.println("      .end();");
    }
  }

  private void writeFooter() {
    w.println();
    w.println("}");
  }

}
