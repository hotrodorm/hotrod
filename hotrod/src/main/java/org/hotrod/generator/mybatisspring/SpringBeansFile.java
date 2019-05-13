package org.hotrod.generator.mybatisspring;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;

public class SpringBeansFile {

  private static transient final Logger log = Logger.getLogger(SpringBeansFile.class);

  private File f;
  private TreeMap<String, SpringBeanFile> beanFiles;

  public SpringBeansFile(final File f) throws UncontrolledException {
    if (f == null) {
      throw new UncontrolledException("Cannot generate Spring Beans file: no file name specified.", null);
    }
    this.f = f;
    this.beanFiles = new TreeMap<String, SpringBeanFile>();
  }

  // For DAOs included in the facet
  public void addFacetDAO(final String daoClassName, final String daoFullClassName) {
    SpringBeanFile bf = new SpringBeanFile(this.f.getParentFile(), daoClassName, daoFullClassName, true);
    this.beanFiles.put(daoFullClassName, bf);
  }

  // For ALL DAOS, included or not in the facet
  public void addAnyDAO(final String daoClassName, final String daoFullClassName) {
    SpringBeanFile bf = new SpringBeanFile(this.f.getParentFile(), daoClassName, daoFullClassName, false);
    if (!this.beanFiles.containsKey(daoFullClassName)) {
      this.beanFiles.put(daoFullClassName, bf);
    }
  }

  public void generate(final FileGenerator fileGenerator) throws UncontrolledException {

    TextWriter w = null;

    try {

      w = fileGenerator.createWriter(this.f);

      writeHeader(w);

      writeDAOImports(w);

      writeFooter(w);

      int inCount = 0;
      for (String daoFullClassName : this.beanFiles.keySet()) {
        SpringBeanFile bf = this.beanFiles.get(daoFullClassName);
        if (bf.isInFacet()) {
          inCount++;
          bf.generate(fileGenerator);
        }
      }
      log.debug("in-facet=" + inCount + " total=" + this.beanFiles.size());

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

  private void writeDAOImports(final TextWriter w) throws IOException {
    for (String daoFullClassName : this.beanFiles.keySet()) {
      SpringBeanFile bf = this.beanFiles.get(daoFullClassName);
      w.write("  <import resource=\"./" + bf.getFileName() + "\"/>\n");
    }
  }

  private void writeFooter(final TextWriter w) throws IOException {
    w.write("\n</beans>\n");
  }

}
