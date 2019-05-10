package org.hotrod.generator.mybatisspring;

import java.io.File;
import java.io.IOException;

import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.runtime.util.SUtils;

public class SpringBeanFile {

  private File baseDir;
  private File f;
  private ObjectDAO dao;

  public SpringBeanFile(final File baseDir, final ObjectDAO dao) {
    this.dao = dao;
    this.baseDir = baseDir;
    this.f = new File(this.baseDir, this.getFileName());
  }

  public void generate(final FileGenerator fileGenerator) throws UncontrolledException {

    TextWriter w = null;

    try {

      w = fileGenerator.createWriter(this.f);

      writeHeader(w);

      writeDAOs(w);

      writeFooter(w);

    } catch (IOException e) {
      throw new UncontrolledException("Could not generate Spring bean file: could not write to file '" + f + "'.", e);
    } finally {
      if (w != null) {
        try {
          w.close();
        } catch (IOException e) {
          throw new UncontrolledException("Could not generate Spring bean file: could not close file '" + f + "'.", e);
        }
      }
    }

  }

  private void writeHeader(final TextWriter w) throws IOException {
    w.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" //
        + "<beans xmlns=\"http://www.springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" //
        + "  xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd\">\n" //
        + "\n");
  }

  private void writeDAOs(final TextWriter w) throws IOException {
    String className = SUtils.lowerFirst(this.dao.getClassName());
    String fullClassName = this.dao.getFullClassName();
    w.write("  <bean id=\"" + className + "\" class=\"" + fullClassName + "\">\n");
    w.write("    <property name=\"sqlSession\" ref=\"sqlSession\" />\n");
    w.write("    <property name=\"sqlDialect\" value=\"#{sqlDialectFactory.sqlDialect}\" />\n");
    w.write("  </bean>\n" //
        + "\n" //
        + "");
  }

  private void writeFooter(final TextWriter w) throws IOException {
    w.write("</beans>\n");
  }

  public String getFileName() {
    String lower = this.dao.getClassName().toLowerCase();
    return "spring-" + lower + ".xml";
  }

  public ObjectDAO getDao() {
    return this.dao;
  }

}
