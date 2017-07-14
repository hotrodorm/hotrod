package org.hotrod.config.dynamicsql;

import java.util.List;

import org.hotrod.config.SQLParameter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;

public class LiteralSQLPart extends DynamicSQLPart implements SQLChunk {

  private String text;

  public LiteralSQLPart(final String text) {
    super("not-a-tag-but-sql-content");
    this.text = text;
  }

  // Behavior

  @Override
  public void validate(final String tagIdentification) throws InvalidConfigurationFileException {
    if (this.text != null && this.text.contains("#{")) {
      throw new InvalidConfigurationFileException(
          "Literal SQL text cannot include parameters, marked with #{ and }, found on the tag " + tagIdentification
              + ".");
    }
  }

  @Override
  public List<SQLParameter> getParameters() {
    return null;
  }

  // Rendering

  @Override
  public String renderSQLSentence(final ParameterRenderer parameterRenderer) {
    return this.text;
  }

}
