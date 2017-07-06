package org.hotrod.config;

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

}
