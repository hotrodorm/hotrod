package org.hotrod.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.dynamicsql.DynamicSQLPart.ParameterDefinitions;
import org.hotrod.config.structuredcolumns.ColumnsProvider;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.Generator;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.LiteralExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;
import org.hotrodorm.hotrod.utils.SUtil;

public class VerbatimTextPart extends EnhancedSQLPart {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(VerbatimTextPart.class);

  // Properties

  private String text;

  // Constructor

  public VerbatimTextPart(final String text) {
    super("not-a-tag-but-verbatim-text");
    log.debug("init");
    this.text = text;
  }

  // Behavior

  @Override
  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final ParameterDefinitions parameters,
      final DatabaseAdapter adapter) throws InvalidConfigurationFileException {
    // Nothing to do
  }

  @Override
  public void validateAgainstDatabase(final Generator generator) throws InvalidConfigurationFileException {
    // Nothing to do
  }

  // Rendering

  @Override
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    return this.text;
  }

  @Override
  public void renderXML(final SQLFormatter formatter, final ParameterRenderer parameterRenderer) {
    formatter.add(SUtil.escapeXmlBody(this.text));
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

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

  // Merging logic

  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      VerbatimTextPart f = (VerbatimTextPart) fresh;
      boolean equals = SUtil.equals(this.text, f.text);
      // log.info("[LITERAL] equals=" + equals);
      // log.info("this.text=" + this.text);
      // log.info("othe.text=" + f.text);
      return equals;
    } catch (ClassCastException e) {
      return false;
    }
  }

}
