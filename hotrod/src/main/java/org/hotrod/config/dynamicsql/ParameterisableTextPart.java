package org.hotrod.config.dynamicsql;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.config.SQLParameter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.VerbatimExpression;

public class ParameterisableTextPart extends DynamicSQLPart {

  // Constants

  private static final Logger log = Logger.getLogger(ParameterisableTextPart.class);

  // Properties

  private String txt;

  protected List<SQLChunk> chunks = new ArrayList<SQLChunk>();
  protected List<SQLParameter> params = new ArrayList<SQLParameter>();

  // Constructor

  public ParameterisableTextPart(final String txt, final String tagIdentification)
      throws InvalidConfigurationFileException {
    super("not-a-tag-but-sql-content");
    log.debug("init");
    this.txt = txt;
    this.validate(tagIdentification);
  }

  // Behavior

  @Override
  protected void validateAttributes(final String tagIdentification) throws InvalidConfigurationFileException {
    // No attributes; nothing to do
  }

  private void validate(final String tagIdentification) throws InvalidConfigurationFileException {
    int pos = 0;
    int prefix;
    int suffix;

    while (pos < this.txt.length() && (prefix = this.txt.indexOf(SQLParameter.PREFIX, pos)) != -1) {

      LiteralTextPart l = new LiteralTextPart(this.txt.substring(pos, prefix));
      this.chunks.add(l);

      suffix = this.txt.indexOf(SQLParameter.SUFFIX, prefix + SQLParameter.PREFIX.length());
      if (suffix == -1) {
        throw new InvalidConfigurationFileException(
            getErrorMessage(this.txt, tagIdentification, "Unmatched parameter delimiters; found an '"
                + SQLParameter.PREFIX + "' but not an '" + SQLParameter.SUFFIX + "'."));
      }

      SQLParameter p = new SQLParameter(this.txt.substring(prefix + SQLParameter.PREFIX.length(), suffix),
          tagIdentification, false);
      this.chunks.add(p);
      this.params.add(p);

      pos = suffix + SQLParameter.SUFFIX.length();
    }

    if (pos < this.txt.length()) {
      LiteralTextPart l = new LiteralTextPart(this.txt.substring(pos));
      this.chunks.add(l);
    }
  }

  public String getErrorMessage(final String txt, final String tagIdentification, final String extraMessage) {
    return "Invalid SQL query as the body of the tag " + tagIdentification + ": invalid parameter section '" + txt
        + "'. Can include one or more parameters with the form: " + SQLParameter.PREFIX
        + "name,javaType=<JAVA-CLASS>,jdbcType=<JDBC-TYPE>" + SQLParameter.SUFFIX
        + (extraMessage == null ? "" : ":\n" + extraMessage);
  }

  // Rendering

  @Override
  protected TagAttribute[] getAttributes() {
    TagAttribute[] atts = {};
    return atts;
  }

  @Override
  public String renderTag(final ParameterRenderer parameterRenderer) {
    ParameterRenderer conditionRenderer = new ParameterRenderer() {
      @Override
      public String render(SQLParameter parameter) {
        return parameter.getName();
      }
    };
    StringBuilder sb = new StringBuilder();
    for (SQLChunk ch : this.chunks) {
      sb.append(ch.renderSQLSentence(conditionRenderer));
    }
    return sb.toString();
  }

  // Java Expression

  @Override
  protected DynamicExpression getJavaExpression(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (SQLChunk ch : this.chunks) {
      sb.append(ch.renderSQLSentence(parameterRenderer));
    }
    return new VerbatimExpression(sb.toString());
  }

}
