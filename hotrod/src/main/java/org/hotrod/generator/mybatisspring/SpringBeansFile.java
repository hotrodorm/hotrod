package org.hotrod.generator.mybatisspring;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.runtime.util.SUtils;

public class SpringBeansFile {

  private File f;
  private List<ObjectDAO> daos;

  public SpringBeansFile(final File f) throws UncontrolledException {
    if (f == null) {
      throw new UncontrolledException("Cannot generate Spring Beans file: no file name specified.", null);
    }
    this.f = f;
    this.daos = new ArrayList<ObjectDAO>();
  }

  public void addDAO(final ObjectDAO dao) {
    this.daos.add(dao);
  }

  public void generate(final FileGenerator fileGenerator) throws UncontrolledException {

    TextWriter w = null;

    try {

      w = fileGenerator.createWriter(this.f);

      writeHeader(w);

      writeDAOs(w);

      writeFooter(w);

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

  private void writeHeader(final TextWriter w) throws IOException {
    w.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" //
        + "<beans xmlns=\"http://www.springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" //
        + "  xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd\">\n" //
        + "\n");
  }

  private void writeDAOs(final TextWriter w) throws IOException {
    for (ObjectDAO dao : this.daos) {
      String className = SUtils.lowerFirst(dao.getClassName());
      String fullClassName = dao.getFullClassName();
      w.write("  <bean id=\"" + className + "\" class=\"" + fullClassName + "\">\n");
      w.write("    <property name=\"sqlSession\" ref=\"sqlSession\" />\n");
      w.write("    <property name=\"sqlDialect\" value=\"#{sqlDialectFactory.sqlDialect}\" />\n");
      w.write("  </bean>\n" //
          + "\n" //
          + "");
    }
  }

  private void writeFooter(final TextWriter w) throws IOException {
    w.write("</beans>\n");
  }

}
