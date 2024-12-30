package org.hotrod.config.dynamicsql;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.ParameterTag;
import org.hotrod.config.SQLParameter;
import org.hotrod.config.dynamicsql.Tokenizer.Token;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;

public class ParameterisableTextPart extends DynamicSQLPart {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(ParameterisableTextPart.class.getName());

  private static final String VALID_NAME_PATTERN = "[a-zA-Z][a-zA-Z0-9_]*";

  // Properties

  private String txt;

  protected List<SQLSegment> segments;

  // Constructor

  public ParameterisableTextPart(final String txt, final AbstractConfigurationTag tag,
      final ParameterDefinitions parameterDefinitions) throws InvalidConfigurationFileException {
    super("not-a-tag-but-sql-content");
    log.fine("init");
    this.txt = txt;
    this.segments = new ArrayList<>();
    this.validate(tag, parameterDefinitions);
  }

  // Behavior

  @Override
  protected void validateAttributes(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // No attributes; nothing to do
  }

  @Override
  protected void specificBodyValidation(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // No extra validation on the body
  }

  private void validate(final AbstractConfigurationTag tag, final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {

    log.info("validate");
    super.retrievePartsAndValidate(parameterDefinitions);

    Tokenizer tokenizer = new Tokenizer(tag, this.txt);
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
        LiteralTextPart literal = new LiteralTextPart(tag.getSourceLocation(), token.getBody());
        this.segments.add(literal);
        break;

      }
    }

  }

  public boolean isEmpty() {
    for (SQLSegment s : this.segments) {
      if (!s.isEmpty()) {
        return false;
      }
    }
    return true;
  }

  // Rendering

  @Override
  protected boolean shouldRenderTag() {
    return false;
  }

  @Override
  protected TagAttribute[] getAttributes() {
    TagAttribute[] atts = {};
    return atts;
  }

  @Override
  public String renderXML(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (SQLSegment s : this.segments) {
      String sxml = s.renderXML(parameterRenderer);
      sb.append(sxml);
    }
    String text = sb.toString();
    return text;
  }

  public List<SQLSegment> getSegments() {
    return segments;
  }

}
