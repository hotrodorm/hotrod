package org.hotrod.config.sql;

import org.hotrod.config.AbstractSQLDAOTag;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;

public class SQLComplementSection extends AbstractSQLSection {

  /**
   * <pre>
   * and a.id = #{id,javaType=java.lang.Integer,jdbcType=NUMERIC}
   * </pre>
   * 
   * @throws InvalidConfigurationFileException
   */
  public SQLComplementSection(final String txt, final AbstractSQLDAOTag tag, final String attName, final String name)
      throws InvalidConfigurationFileException {
    super(txt, tag, attName, name);
  }

  @Override
  public String getSQLFoundation(final ParameterRenderer parameterRenderer) {
    return null;
  }

  @Override
  public String renderAugmentedSQL() {
    return "{* " + super.renderChunksSQL() + " *}";
  }

}
