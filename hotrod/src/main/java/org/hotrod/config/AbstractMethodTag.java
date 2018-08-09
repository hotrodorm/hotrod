package org.hotrod.config;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.utils.identifiers2.Id;

public abstract class AbstractMethodTag<M extends AbstractMethodTag<M>> extends AbstractConfigurationTag
    implements GenerationUnit<AbstractMethodTag<M>> {

  private static final long serialVersionUID = 1L;

  // Properties

  protected String method = null;
  protected Id id = null;

  // Constructor

  protected AbstractMethodTag(final String tagName) {
    super(tagName);
  }

  // Duplicate

  public abstract M duplicate();

  protected void copyCommon(final M source) {
    super.copyCommon(source);
    this.method = source.method;
  }

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig) throws InvalidConfigurationFileException {

    // method

    if (this.method == null) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute 'method' cannot be empty", //
          "Attribute 'method' of tag <" + getTagName() + "> cannot be empty. " + "Must specify a unique name.");
    }

    try {
      this.id = Id.fromJavaMember(this.method);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid Java method name '" + this.method + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg, msg);
    }

  }

  // Methods

  @Override
  public boolean concludeGeneration(final AbstractMethodTag<M> cache, final DatabaseAdapter adapter) {
    return this.concludeGenerationMarkTag();
  }

  // Getters

  public abstract String getMethod();

  public Id getId() {
    return id;
  }

}
