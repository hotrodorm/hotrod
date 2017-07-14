package org.hotrod.config.dynamicsql;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.config.SQLParameter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;

public class ParameterisableSQLPart extends DynamicSQLPart {

  // Constants

  private static final Logger log = Logger.getLogger(ParameterisableSQLPart.class);

  // Properties

  protected List<SQLChunk> chunks = new ArrayList<SQLChunk>();
  protected List<SQLParameter> params = new ArrayList<SQLParameter>();

  private String txt;
  private List<SQLParameter> parameters;

  // Constructor

  public ParameterisableSQLPart(final String txt) {
    super("not-a-tag-but-sql-content");
    log.debug("init");
    this.txt = txt;
  }

  // Behavior

  @Override
  public void validate(final String tagIdentification) throws InvalidConfigurationFileException {
    int pos = 0;
    int prefix;
    int suffix;

    while (pos < this.txt.length() && (prefix = this.txt.indexOf(SQLParameter.PREFIX, pos)) != -1) {

      LiteralSQLPart l = new LiteralSQLPart(this.txt.substring(pos, prefix));
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
      LiteralSQLPart l = new LiteralSQLPart(this.txt.substring(pos));
      this.chunks.add(l);
    }
  }

  public String getErrorMessage(final String txt, final String tagIdentification, final String extraMessage) {
    return "Invalid SQL query as the body of the tag " + tagIdentification + ": invalid parameter section '" + txt
        + "'. Can include one or more parameters with the form: " + SQLParameter.PREFIX
        + "name,javaType=<JAVA-CLASS>,jdbcType=<JDBC-TYPE>" + SQLParameter.SUFFIX
        + (extraMessage == null ? "" : ":\n" + extraMessage);
  }

  @Override
  public List<SQLParameter> getParameters() {
    return this.parameters;
  }

  // Rendering

  @Override
  public String renderSQLSentence(final ParameterRenderer parameterRenderer) {
    return this.txt;
  }

}
