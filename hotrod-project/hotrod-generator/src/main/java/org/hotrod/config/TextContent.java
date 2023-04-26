package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.dynamicsql.DynamicSQLPart.ParameterDefinitions;
import org.hotrod.config.dynamicsql.SQLSegment;
import org.hotrod.config.structuredcolumns.ColumnsProvider;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.metadata.Metadata;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.LiteralExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;

public class TextContent extends EnhancedSQLPart {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(TextContent.class);

  private static final String VALID_NAME_PATTERN = "[a-zA-Z][a-zA-Z0-9_]*";

  // Properties

  private String txt;
  private List<SQLSegment> segments;

  // Constructor

  public TextContent(final String txt) {
    super("not-a-tag-but-parameterisable-content");
    log.debug("init");
    this.txt = txt;
    this.segments = new ArrayList<SQLSegment>();
  }

  // Behavior

  @Override
  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final ParameterDefinitions parameters,
      final DatabaseAdapter adapter) throws InvalidConfigurationFileException {

    parameters.validate();

    int pos = 0;
    int prefix;
    int suffix;

    while (pos < this.txt.length() && (prefix = this.txt.indexOf(SQLParameter.PREFIX, pos)) != -1) {

      VerbatimTextPart verbatim = new VerbatimTextPart(this.getSourceLocation(), this.txt.substring(pos, prefix));
      this.segments.add(verbatim);

      suffix = this.txt.indexOf(SQLParameter.SUFFIX, prefix + SQLParameter.PREFIX.length());
      if (suffix == -1) {
        throw new InvalidConfigurationFileException(this, //
            "Unmatched parameter delimiters; found an '" + SQLParameter.PREFIX + "' but not an '" + SQLParameter.SUFFIX
                + "'");
      }

      String name = this.txt.substring(prefix + SQLParameter.PREFIX.length(), suffix);

      if (!name.matches(VALID_NAME_PATTERN)) {
        if (name.indexOf(',') != -1) {
          throw new InvalidConfigurationFileException(this, //
              "Invalid parameter reference " + SQLParameter.PREFIX + name + SQLParameter.SUFFIX
                  + " in the body of the tag. " + "The parameter must include a single alphanumeric name");
        } else {
          throw new InvalidConfigurationFileException(this, //
              "Invalid parameter reference " + SQLParameter.PREFIX + name + SQLParameter.SUFFIX
                  + " in the body of the tag. "
                  + "\nA parameter name must start with a letter and continue with letters, digits, and/or underscores");
        }
      }

      ParameterTag definition = parameters.find(name);

      if (definition != null) {
        SQLParameter p = new SQLParameter(name, this, false);
        p.setDefinition(definition);
        this.segments.add(p);
      } else {
        throw new InvalidConfigurationFileException(this, //
            "Invalid parameter reference " + SQLParameter.PREFIX + name + SQLParameter.SUFFIX
                + " in the body of the tag: no parameter with this name");
      }

      pos = suffix + SQLParameter.SUFFIX.length();
    }

    if (pos < this.txt.length()) {
      VerbatimTextPart literal = new VerbatimTextPart(this.getSourceLocation(), this.txt.substring(pos));
      this.segments.add(literal);
    }

  }

  @Override
  public void validateAgainstDatabase(final Metadata metadata) throws InvalidConfigurationFileException {
    // Nothing to do
  }

  // Rendering

  @Override
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (SQLSegment s : this.segments) {
      sb.append(s.renderStatic(parameterRenderer));
    }
    return sb.toString();
  }

  @Override
  public void renderXML(final SQLFormatter formatter, final ParameterRenderer parameterRenderer) {
    for (SQLSegment s : this.segments) {
      String sxml = s.renderXML(parameterRenderer);
      formatter.add(sxml);
    }
  }

  @Override
  public String renderSQLAngle(final DatabaseAdapter adapter, final ColumnsProvider cp) {
    return this.txt;
  }

  // Java Expression

  @Override
  public DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {

    try {

      return new LiteralExpression(this.txt);

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

}
