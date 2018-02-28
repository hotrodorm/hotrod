package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.dynamicsql.SourceLocation;

public abstract class AbstractConfigurationTag {

  public enum ItemStatus {
    UNAFFECTED, MODIFIED, ADDED, DELETED;
  }

  // Properties

  private String tagName;

  private ItemStatus status;
  private List<AbstractConfigurationTag> subTags;

  // Constructor

  protected AbstractConfigurationTag(final String tagName) {
    this.tagName = tagName;
    this.status = ItemStatus.UNAFFECTED;
    this.subTags = new ArrayList<AbstractConfigurationTag>();
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

  public ItemStatus getStatus() {
    return status;
  }

  public void setStatus(ItemStatus status) {
    this.status = status;
  }

  public List<AbstractConfigurationTag> getSubTags() {
    return subTags;
  }

  // XmlLocatable

  private SourceLocation location = null;

  public void setSourceLocation(final SourceLocation location) {
    this.location = location;
  }

  public SourceLocation getSourceLocation() {
    return this.location;
  }

  // Abstract methods for computing item changes

  // Checks if it has the same KEY properties
  public abstract boolean sameKey(AbstractConfigurationTag fresh);

  // Checks if it has the same properties. Computed properties are not compared.
  public abstract boolean same(AbstractConfigurationTag fresh);

  // Copy non-KEY properties; informs if there were any changes.
  public abstract boolean copyNonKeyProperties(AbstractConfigurationTag fresh);

}
