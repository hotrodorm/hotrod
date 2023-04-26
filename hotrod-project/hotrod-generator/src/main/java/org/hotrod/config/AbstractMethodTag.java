package org.hotrod.config;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.identifiers.Id;

public abstract class AbstractMethodTag<M extends AbstractMethodTag<M>> extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Properties

  protected String method = null;
  protected Id id = null;

  // Constructor

  protected AbstractMethodTag(final String tagName) {
    super(tagName);
  }

  protected void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig) throws InvalidConfigurationFileException {

    // method

    if (this.method == null) {
      throw new InvalidConfigurationFileException(this,
          "Attribute 'method' of tag <" + getTagName() + "> cannot be empty. " + "Must specify a unique name.");
    }

    try {
      this.id = Id.fromJavaMember(this.method);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid Java method name '" + this.method + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg);
    }

  }

  // Getters

  public abstract String getMethod();

  public Id getId() {
    return id;
  }

}
