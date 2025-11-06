package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.config.dynamicsql.DynamicSQLPart.ParameterDefinitions;
import org.hotrod.config.dynamicsql.LiteralTextPart;
import org.hotrod.config.dynamicsql.NitroTokenizer;
import org.hotrod.config.dynamicsql.NitroTokenizer.Token;
import org.hotrod.config.dynamicsql.ParameterInjection;
import org.hotrod.config.dynamicsql.SQLSegment;
import org.hotrod.config.dynamicsql.VariableOccurrence;
import org.hotrod.config.structuredcolumns.ColumnsProvider;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.metadata.Metadata;

@Deprecated
public class TextContent extends EnhancedSQLPart {

  // Constants

  private static final Logger log = Logger.getLogger(TextContent.class.getName());

  private static final String VALID_NAME_PATTERN = "[a-zA-Z][a-zA-Z0-9_]*";

  // Properties

  private String txt;
  private List<SQLSegment> segments;

  // Constructor

  public TextContent(final String txt) {
    super("not-a-tag-but-parameterisable-content");
    log.fine("init");
    this.txt = txt;
    this.segments = new ArrayList<SQLSegment>();
  }

  // Behavior

  @Override
  public void validate(final JDBCTag jdbcTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final ParameterDefinitions parameterDefinitions,
      final DatabaseAdapter adapter) throws InvalidConfigurationFileException {

    AbstractConfigurationTag tag = null;

    parameterDefinitions.validate();

    NitroTokenizer tokenizer = new NitroTokenizer(tag, this.txt);
    Token token;
    while ((token = tokenizer.next()) != null) {
      log.info("TOKEN: " + token.getType() + " - " + token.getBody());
      switch (token.getType()) {

      case SQL_PARAMETER:
        String name = token.getBody();

        if (!name.matches(VALID_NAME_PATTERN)) {
          if (name.indexOf(',') != -1) {
            throw new InvalidConfigurationFileException(tag,
                "Invalid parameter reference " + SQLParameter.PREFIX + name + SQLParameter.SUFFIX
                    + " in the body of the tag. " + "The parameter must include a single alphanumeric name");
          } else {
            throw new InvalidConfigurationFileException(tag, "Invalid parameter reference " + SQLParameter.PREFIX + name
                + SQLParameter.SUFFIX + " in the body of the tag. "
                + "\nA parameter name must start with a letter and continue with letters, digits, and/or underscores.");
          }
        }

        ParameterTag parameterDefinition = parameterDefinitions.findParameter(name);
        if (parameterDefinition != null) {
          SQLParameter p = new SQLParameter(name, tag, false);
          p.setDefinition(parameterDefinition);
          this.segments.add(p);
        } else {
          if (parameterDefinitions.findVariable(name)) {
            VariableOccurrence v = new VariableOccurrence(name);
            this.segments.add(v);
          } else {
            throw new InvalidConfigurationFileException(tag, "Invalid parameter reference " + SQLParameter.PREFIX + name
                + SQLParameter.SUFFIX + " in the body of the tag. There's no parameter with that name.");
          }
        }
        break;

      case PARAMETER_INJECTION:
        name = token.getBody();

        if (!name.matches(VALID_NAME_PATTERN)) {
          if (name.indexOf(',') != -1) {
            throw new InvalidConfigurationFileException(tag,
                "Invalid parameter reference " + SQLParameter.PREFIX + name + SQLParameter.SUFFIX
                    + " in the body of the tag. " + "The parameter must include a single alphanumeric name");
          } else {
            throw new InvalidConfigurationFileException(tag, "Invalid parameter reference " + SQLParameter.PREFIX + name
                + SQLParameter.SUFFIX + " in the body of the tag. "
                + "\nA parameter name must start with a letter and continue with letters, digits, and/or underscores.");
          }
        }

        parameterDefinition = parameterDefinitions.findParameter(name);
        if (parameterDefinition != null) {
          ParameterInjection p = new ParameterInjection(name);
          this.segments.add(p);
        } else {
          if (parameterDefinitions.findVariable(name)) {
            ParameterInjection p = new ParameterInjection(name);
            this.segments.add(p);
          } else {
            throw new InvalidConfigurationFileException(tag, "Invalid parameter reference " + SQLParameter.PREFIX + name
                + SQLParameter.SUFFIX + " in the body of the tag. There's no parameter with that name.");
          }
        }
        break;

      default: // literal
        LiteralTextPart literal = new LiteralTextPart(this.getSourceLocation(), token.getBody());
        this.segments.add(literal);
        break;

      }
    }

  }

  @Override
  public void validateAgainstDatabase(final Metadata metadata) throws InvalidConfigurationFileException {
    // Nothing to do
  }

  // Rendering

  @Override
  public String renderSQLFoundation(ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (SQLSegment s : this.segments) {
      sb.append(s.renderSQLFoundation(parameterRenderer));
    }
    String t = sb.toString();
    return t;
  }

  @Override
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (SQLSegment s : this.segments) {
      sb.append(s.renderStatic(parameterRenderer));
    }
    return sb.toString();
  }

  @Override
  public String renderSQLAngle(final DatabaseAdapter adapter, final ColumnsProvider cp) {
    return this.txt;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

  public List<SQLSegment> getSegments() {
    return segments;
  }

}
