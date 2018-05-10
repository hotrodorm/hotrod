package org.hotrod.generator.springjdbc;

import java.io.IOException;
import java.io.Writer;

import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.metadata.DataSetMetadata;

public class SpringBeanTable implements SpringBean {

  private DataSetMetadata dsm;

  private final DAOPrimitives daoPrimitives;

  public SpringBeanTable(final DataSetMetadata dsm, final DAOPrimitives daoPrimitives) {
    this.dsm = dsm;
    this.daoPrimitives = daoPrimitives;
  }

  public void writeBeanTag(Writer w) throws UncontrolledException {
    try {
      w.write("\t<bean id=\"" + this.getBeanName() + "\"\n");
      w.write("\t\tclass=\"" + this.daoPrimitives.getFullClassName() + "."
          + dsm.getIdentifier().getJavaClassIdentifier() + CodeGenerationHelper.SPRING_BEAN_SUFFIX + "\">\n");
      w.write("\t\t<property name=\"dataSource\" ref=\"dataSource\" />\n");
      w.write("\t</bean>\n\n");

    } catch (IOException e) {
      throw new UncontrolledException(
          "Could not generate DAO class: could not write to file '" + daoPrimitives.getFullClassName() + "'.", e);
    }

  }

  public String getBeanName() {
    return this.dsm.getIdentifier().getJavaMemberIdentifier() + CodeGenerationHelper.SPRING_BEAN_SUFFIX;
  }

  public String getBeanClassName() {
    return this.dsm.getIdentifier().getJavaClassIdentifier() + CodeGenerationHelper.SPRING_BEAN_SUFFIX;
  }
}
