package org.hotrod.config.dynamicsql;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.ParameterTag;
import org.hotrod.config.SQLParameter;
import org.hotrod.config.dynamicsql.Tokenizer.Token;
import org.hotrod.dynamicsql.existing.expressions.CollectionExpression;
import org.hotrod.dynamicsql.existing.expressions.LiteralExpression;
import org.hotrod.dynamicsql.existing.expressions.OldDynamicExpression;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidJavaExpressionException;
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

//    int pos = 0;
//    int prefix;
//    int suffix;

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

//    while (pos < this.txt.length() && (prefix = this.txt.indexOf(SQLParameter.PREFIX, pos)) != -1) {
//
//      LiteralTextPart literal = new LiteralTextPart(tag.getSourceLocation(), this.txt.substring(pos, prefix));
//      this.segments.add(literal);
//
//      suffix = this.txt.indexOf(SQLParameter.SUFFIX, prefix + SQLParameter.PREFIX.length());
//      if (suffix == -1) {
//        throw new InvalidConfigurationFileException(tag, "Unmatched parameter delimiters; found an '"
//            + SQLParameter.PREFIX + "' but not an '" + SQLParameter.SUFFIX + "'.");
//      }
//
//      String name = this.txt.substring(prefix + SQLParameter.PREFIX.length(), suffix);
//
//      if (!name.matches(VALID_NAME_PATTERN)) {
//        if (name.indexOf(',') != -1) {
//          throw new InvalidConfigurationFileException(tag, "Invalid parameter reference " + SQLParameter.PREFIX + name
//              + SQLParameter.SUFFIX + " in the body of the tag. "
//              + "The parameter must include a single alphanumeric name. "
//              + "\n - Note: Extra sections such as 'javaType' or 'jdbcType' are now obsolete and should be removed. You should use <parameter> tags instead.");
//        } else {
//          throw new InvalidConfigurationFileException(tag, "Invalid parameter reference " + SQLParameter.PREFIX + name
//              + SQLParameter.SUFFIX + " in the body of the tag. "
//              + "\nA parameter name must start with a letter and continue with letters, digits, and/or underscores.");
//        }
//      }
//
//      ParameterTag parameterDefinition = parameterDefinitions.findParameter(name);
//      if (parameterDefinition != null) {
//        SQLParameter p = new SQLParameter(name, tag, false);
//        p.setDefinition(parameterDefinition);
//        this.segments.add(p);
//      } else {
//        if (parameterDefinitions.findVariable(name)) {
//          VariableOccurrence v = new VariableOccurrence(name);
//          this.segments.add(v);
//        } else {
//          throw new InvalidConfigurationFileException(tag, "Invalid parameter reference " + SQLParameter.PREFIX + name
//              + SQLParameter.SUFFIX + " in the body of the tag. There's no parameter with that name.");
//        }
//      }
//
//      pos = suffix + SQLParameter.SUFFIX.length();
//    }
//
//    if (pos < this.txt.length()) {
//      LiteralTextPart literal = new LiteralTextPart(tag.getSourceLocation(), this.txt.substring(pos));
//      this.segments.add(literal);
//    }
  }

  public boolean isEmpty() {
    for (SQLSegment s : this.segments) {

      // log.info("s=" + s + " - " + s.renderSegmentStatic(new
      // ParameterRenderer() {
      // @Override
      // public String render(SQLParameter parameter) {
      // return "[[" + parameter.getName() + "]]";
      // }
      // }));

      if (!s.isEmpty()) {
        return false;
      }
    }
    // log.info("it's empty!");
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
      // log.info("s=" + s + " -> XML=" + sxml);
      sb.append(sxml);
    }
    String text = sb.toString();
    return text;
  }

  // Java Expression

  @Override
  protected OldDynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {

    try {

      List<OldDynamicExpression> exprs = new ArrayList<OldDynamicExpression>();
      LiteralExpression last = null;
      for (SQLSegment s : this.segments) {
        OldDynamicExpression expr = s.getJavaExpression(parameterRenderer);
        try {
          LiteralExpression v = (LiteralExpression) expr;
          if (last == null) {
            last = v;
          } else {
            last = last.concat(v);
          }
        } catch (ClassCastException e) {
          if (last != null) {
            exprs.add(last);
            last = null;
          }
          exprs.add(expr);
        }
      }
      if (last != null) {
        exprs.add(last);
      }

      return new CollectionExpression(exprs.toArray(new OldDynamicExpression[0]));

    } catch (RuntimeException e) {
      throw new InvalidJavaExpressionException(this.getSourceLocation(),
          "Could not produce Java expression for parameter or variable on file '"
              + this.getSourceLocation().getFile().getPath() + "' at line " + this.getSourceLocation().getLineNumber()
              + ", col " + this.getSourceLocation().getColumnNumber() + ": " + e.getMessage());
    }

  }

  public List<SQLSegment> getSegments() {
    return segments;
  }

}
