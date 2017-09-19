package org.hotrod.config;

import org.hotrod.runtime.dynamicsql.SourceLocation;

public abstract class AbstractConfigurationTag {

  // Properties

  private String tagName;

  // Constructor

  protected AbstractConfigurationTag(final String tagName) {
    this.tagName = tagName;
  }

  // Constructor just for JAXB's sake - never used
  @SuppressWarnings("unused")
  private AbstractConfigurationTag() {
    this.tagName = "<configuration-tag>";
  }

  // Getters

  public final String getTagName() {
    return tagName;
  }

  // XmlLocatable

  private SourceLocation location = null;

  public void setSourceLocation(final SourceLocation location) {
    this.location = location;
  }

  public SourceLocation getSourceLocation() {
    return this.location;
  }

}
