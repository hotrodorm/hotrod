package org.hotrod.config.dynamicsql;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.config.ParameterTag;
import org.hotrod.config.SQLParameter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.SourceLocation;
import org.hotrod.runtime.dynamicsql.expressions.CollectionExpression;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.LiteralExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;

public class ParameterisableTextPart extends DynamicSQLPart {

  // Constants

  private static final Logger log = Logger.getLogger(ParameterisableTextPart.class);

  private static final String VALID_NAME_PATTERN = "[a-zA-Z][a-zA-Z0-9_]*";

  // Properties

  private String txt;

  protected List<SQLSegment> segments = new ArrayList<SQLSegment>();

  // Constructor

  public ParameterisableTextPart(final String txt, final SourceLocation location,
      final ParameterDefinitions parameterDefinitions) throws InvalidConfigurationFileException {
    super("not-a-tag-but-sql-content");
    log.debug("init");
    this.txt = txt;
    this.validate(location, parameterDefinitions);
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

  private void validate(final SourceLocation location, final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    int pos = 0;
    int prefix;
    int suffix;

    while (pos < this.txt.length() && (prefix = this.txt.indexOf(SQLParameter.PREFIX, pos)) != -1) {

      LiteralTextPart literal = new LiteralTextPart(location, this.txt.substring(pos, prefix));
      this.segments.add(literal);

      suffix = this.txt.indexOf(SQLParameter.SUFFIX, prefix + SQLParameter.PREFIX.length());
      if (suffix == -1) {
        throw new InvalidConfigurationFileException(location, "Unmatched parameter delimiters; found an '"
            + SQLParameter.PREFIX + "' but not an '" + SQLParameter.SUFFIX + "'.");
      }

      String name = this.txt.substring(prefix + SQLParameter.PREFIX.length(), suffix);

      if (!name.matches(VALID_NAME_PATTERN)) {
        if (name.indexOf(',') != -1) {
          throw new InvalidConfigurationFileException(location, "invalid parameter reference " + SQLParameter.PREFIX
              + name + SQLParameter.SUFFIX + " in the body of the tag. "
              + "The parameter must include a single alphanumeric name. "
              + "\n - Note: Extra sections such as 'javaType' or 'jdbcType' are now obsolete and should be removed. You should use <parameter> tags instead.");
        } else {
          throw new InvalidConfigurationFileException(location, "invalid parameter reference " + SQLParameter.PREFIX
              + name + SQLParameter.SUFFIX + " in the body of the tag. "
              + "\nA parameter name must start with a letter and continue with letters, digits, and/or underscores.");
        }
      }

      ParameterTag definition = parameterDefinitions.find(name);
      if (definition != null) {
        SQLParameter p = new SQLParameter(name, location, false);
        p.setDefinition(definition);
        this.segments.add(p);
      } else {
        throw new InvalidConfigurationFileException(location, "invalid parameter reference " + SQLParameter.PREFIX
            + name + SQLParameter.SUFFIX + " in the body of the tag. There's no parameter with that name.");
      }

      pos = suffix + SQLParameter.SUFFIX.length();
    }

    if (pos < this.txt.length()) {
      LiteralTextPart literal = new LiteralTextPart(location, this.txt.substring(pos));
      this.segments.add(literal);
    }
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
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (SQLSegment s : this.segments) {
      sb.append(s.renderStatic(parameterRenderer));
    }
    String text = sb.toString();
    return text;
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
  protected DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {

    try {

      List<DynamicExpression> exprs = new ArrayList<DynamicExpression>();
      LiteralExpression last = null;
      for (SQLSegment s : this.segments) {
        DynamicExpression expr = s.getJavaExpression(parameterRenderer);
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

      return new CollectionExpression(exprs.toArray(new DynamicExpression[0]));

    } catch (RuntimeException e) {
      throw new InvalidJavaExpressionException(this.getSourceLocation(),
          "Could not produce Java expression for parameter or variable on file '"
              + this.getSourceLocation().getFile().getPath() + "' at line " + this.getSourceLocation().getLineNumber()
              + ", col " + this.getSourceLocation().getColumnNumber() + ": " + e.getMessage());
    }

  }

}
