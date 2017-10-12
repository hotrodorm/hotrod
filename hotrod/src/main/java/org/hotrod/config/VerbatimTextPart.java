package org.hotrod.config;

import org.apache.log4j.Logger;
import org.hotrod.config.dynamicsql.DynamicSQLPart.ParameterDefinitions;
import org.hotrod.config.structuredcolumns.ColumnsProvider;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.LiteralExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;
import org.hotrod.runtime.util.SUtils;

public class VerbatimTextPart extends EnhancedSQLTag {

  // Constants

  private static final Logger log = Logger.getLogger(VerbatimTextPart.class);

  // Properties

  private String text;

  // Constructor

  public VerbatimTextPart(final String text) {
    super("not-a-tag-but-literal-text");
    log.debug("init");
    this.text = text;
  }

  // Behavior

  @Override
  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final ParameterDefinitions parameters)
      throws InvalidConfigurationFileException {
    // Nothing to do
  }

  @Override
  public void validateAgainstDatabase(final HotRodGenerator generator) throws InvalidConfigurationFileException {
    // Nothing to do
  }

  // Rendering

  @Override
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    return this.text;
  }

  @Override
  public String renderXML(final ParameterRenderer parameterRenderer) {
    return SUtils.escapeXmlBody(this.text);
  }

  @Override
  public String renderSQLAngle(final DatabaseAdapter adapter, final ColumnsProvider cp) {
    return this.text;
  }

  // Java Expression

  @Override
  public DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {

    try {

      return new LiteralExpression(this.text);

    } catch (RuntimeException e) {
      throw new InvalidJavaExpressionException(this.getSourceLocation(),
          "Could not produce Java expression for literal text on file '" + this.getSourceLocation().getFile().getPath()
              + "' at line " + this.getSourceLocation().getLineNumber() + ", col "
              + this.getSourceLocation().getColumnNumber() + ": " + e.getMessage());
    }

  }

}
