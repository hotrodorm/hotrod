package org.hotrod.generator.mybatisspring;

import java.io.File;
import java.io.IOException;

import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.generator.mybatis.DataSetLayout;

public class LiveSQLMapper {

  private static final String MAPPER_FILE_NAME = "live-sql.xml";

  private DataSetLayout layout;
  private File f;
  private String runtimeName;

  public LiveSQLMapper(final DataSetLayout layout) throws UncontrolledException {
    this.layout = layout;
    this.f = new File(this.layout.getMapperPrimitiveDir(null), MAPPER_FILE_NAME);

    File rf = new File(this.layout.getMapperRuntimeDir(null), MAPPER_FILE_NAME);
    this.runtimeName = rf.getPath();
  }

  public void generate(final FileGenerator fileGenerator) throws UncontrolledException {

    TextWriter w = null;

    try {

      w = fileGenerator.createWriter(this.f);

      println(w, "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
      println(w, "<!DOCTYPE mapper");
      println(w, "  PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"");
      println(w, "  \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
      println(w, "<mapper namespace=\"livesql\">");
      println(w, "");
      println(w, "  <select id=\"select\" resultType=\"map\">");
      println(w, "    ${sql}");
      println(w, "  </select>");
      println(w, "");
      println(w, "</mapper>");

    } catch (IOException e) {
      throw new UncontrolledException("Could not generate Spring beans file: could not write to file '" + f + "'.", e);
    } finally {
      if (w != null) {
        try {
          w.close();
        } catch (IOException e) {
          throw new UncontrolledException("Could not generate Spring beans file: could not close file '" + f + "'.", e);
        }
      }
    }

  }

  private void println(final TextWriter w, final String line) throws IOException {
    w.write(line);
    w.write("\n");
  }

  public String getFileName() {
    return this.runtimeName;
  }

}
