package org.hotrod.config.sql;

import org.hotrod.config.AbstractSQLDAOTag;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;

public class SQLFoundationSection extends AbstractSQLSection {

  public SQLFoundationSection(final String txt, final AbstractSQLDAOTag tag, final String attName, final String name)
      throws InvalidConfigurationFileException {
    super(txt, tag, attName, name);
  }

  @Override
  public String getSQLFoundation(final ParameterRenderer parameterRenderer) {
    return super.renderSQLSentence(parameterRenderer);
  }

  @Override
  public String renderAugmentedSQL() {
    return super.renderChunksSQL();
  }

}
