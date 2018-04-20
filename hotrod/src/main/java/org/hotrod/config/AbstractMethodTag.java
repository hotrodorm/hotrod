package org.hotrod.config;

public abstract class AbstractMethodTag<M extends AbstractMethodTag<M>> extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Properties

  protected String method = null;

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

  // Methods

  public abstract String getMethod();

}
