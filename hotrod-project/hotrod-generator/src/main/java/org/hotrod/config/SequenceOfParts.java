package org.hotrod.config;

import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.dynamicsql.DynamicSQLPart.ParameterDefinitions;
import org.hotrod.config.structuredcolumns.ColumnsProvider;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.metadata.Metadata;

@XmlRootElement(name = "not-a-tag")
public class SequenceOfParts extends EnhancedSQLPart {

  private static final Logger log = Logger.getLogger(SequenceOfParts.class.getName());

  private static final long serialVersionUID = 1L;

  // Constructor

  public SequenceOfParts(final List<EnhancedSQLPart> parts) {
    super("not-a-tag");
    super.eparts = parts;
  }

  // Behavior

  @Override
  public void validate(final JDBCTag jdbcTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final ParameterDefinitions parameters,
      final DatabaseAdapter adapter) throws InvalidConfigurationFileException {
    // Nothing to do
  }

  @Override
  public void validateAgainstDatabase(Metadata metadata) throws InvalidConfigurationFileException {
    // Nothing to do
  }

  @Override
  public String renderSQLAngle(DatabaseAdapter adapter, ColumnsProvider cp) {
    // Nothing to do
    return null;
  }

  @Override
  public String renderSQLFoundation(ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (EnhancedSQLPart p : super.eparts) {
      sb.append(p.renderSQLFoundation(parameterRenderer));
    }
    return sb.toString();
  }

  @Override
  public String renderStatic(ParameterRenderer parameterRenderer) {
    // Nothing to do
    return null;
  }

  @Override
  public void renderXML(final SQLFormatter formatter, final ParameterRenderer parameterRenderer) {
    for (EnhancedSQLPart p : super.eparts) {
      p.renderXML(formatter, parameterRenderer);
    }
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

  public List<EnhancedSQLPart> getParts() {
    return super.eparts;
  }

}