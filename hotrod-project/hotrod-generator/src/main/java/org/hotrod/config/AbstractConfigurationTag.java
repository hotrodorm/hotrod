package org.hotrod.config;

import java.io.Serializable;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.SourceLocation;

public abstract class AbstractConfigurationTag implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger log = Logger.getLogger(HotRodConfigTag.class.getName());

  // Properties

  private String tagName;

  private SourceLocation location = null;

  // Constructor

  protected AbstractConfigurationTag(final String tagName) {
    log.finer("init.");
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

  public void setSourceLocation(final SourceLocation location) {
    this.location = location;
  }

  public SourceLocation getSourceLocation() {
    return this.location;
  }

  // Simple Caption

  public abstract String getInternalCaption();

}
