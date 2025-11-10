package org.hotrod.config.dynamicsql;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.ParameterTag;
import org.hotrod.config.JDBCParameterOccurrence;
import org.hotrod.config.dynamicsql.NitroTokenizer.Token;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;

public class ParameterisableTextPart extends DynamicSQLPart {

  // Constants

  private static final Logger log = Logger.getLogger(ParameterisableTextPart.class.getName());

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

    super.retrievePartsAndValidate(parameterDefinitions);

    NitroTokenizer tokenizer = new NitroTokenizer(tag, this.txt);
    Token token;
    while ((token = tokenizer.next()) != null) {
//      log.info("TOKEN: " + token.getType() + " - " + token.getBody());
      switch (token.getType()) {

      case SQL_PARAMETER:
        String expression = token.getBody();

        ParameterTag parameterDefinition = parameterDefinitions.findParameter(expression);
        if (parameterDefinition != null) {
          JDBCParameterOccurrence p = new JDBCParameterOccurrence(expression, tag, false);
          p.setDefinition(parameterDefinition);
          this.segments.add(p);
        } else {
          // a variable occurrence can be any JEXL expression; maybe we could validate its
          // syntax against JEXL in the future
          VariableOccurrence v = new VariableOccurrence(expression);
          this.segments.add(v);
        }
        break;

      case PARAMETER_INJECTION:
        // the body can be any JEXL expression; maybe we could validate its syntax
        // against JEXL in the future
        this.segments.add(new ParameterInjection(token.getBody()));
        break;

      default: // literal
        LiteralTextPart literal = new LiteralTextPart(tag.getSourceLocation(), token.getBody());
        this.segments.add(literal);
        break;

      }
    }

  }

  public boolean includesSQLInjection() {
    for (SQLSegment s : this.segments) {
      if (s.hasSQLInjection()) {
        return true;
      }
    }
    return false;
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
  public String renderSQLFoundation(ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (SQLSegment s : this.segments) {
      sb.append(s.renderSQLFoundation(parameterRenderer));
    }
    String sss = sb.toString();
    return sss;
  }

  public List<SQLSegment> getSegments() {
    return segments;
  }

}
