package org.hotrod.config;

import org.hotrod.config.AbstractHotRodConfigTag.SourceLocatable;
import org.hotrod.runtime.dynamicsql.SourceLocation;

public abstract class AbstractConfigurationTag implements SourceLocatable {

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

  @Override
  public void setSourceLocation(final SourceLocation location) {
    this.location = location;
  }

  @Override
  public SourceLocation getSourceLocation() {
    return this.location;
  }

}
