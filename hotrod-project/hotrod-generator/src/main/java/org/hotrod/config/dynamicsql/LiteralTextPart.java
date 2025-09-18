package org.hotrod.config.dynamicsql;

import java.util.logging.Logger;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.utils.SourceLocation;

public class LiteralTextPart extends DynamicSQLPart implements SQLSegment {

  // Constants

  private static final Logger log = Logger.getLogger(LiteralTextPart.class.getName());

  // Properties

  private SourceLocation location;
  private String text;

  // Constructor

  public LiteralTextPart(final SourceLocation location, final String text) {
    super("not-a-tag-but-literal-text");
    log.fine("init");
    this.location = location;
    this.text = text;
  }

  // Behavior

  @Override
  protected void specificBodyValidation(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // No extra validation on the body
  }

  public LiteralTextPart concat(final LiteralTextPart other) {
    return new LiteralTextPart(this.location, this.text + other.text);
  }

  // Rendering

  @Override
  protected void validateAttributes(ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // Nothing to validate. Not a tag.
  }

  @Override
  protected boolean shouldRenderTag() {
    return false;
  }

  @Override
  protected TagAttribute[] getAttributes() {
    return null;
  }

  @Override
  public String renderSQLFoundation(ParameterRenderer parameterRenderer) {
    return this.text;
  }

  @Override
  public String renderStatic(final ParameterRenderer parameterRenderer) {
    return this.text;
  }

  @Override
  public boolean isEmpty() {
    return this.text == null || this.text.trim().isEmpty();
  }

  // Getters

  public String getText() {
    return text;
  }

}
