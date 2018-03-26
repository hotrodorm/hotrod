package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.dynamicsql.SourceLocation;

public abstract class AbstractConfigurationTag implements Comparable<AbstractConfigurationTag> {

  public enum TagStatus {
    UNAFFECTED, MODIFIED, ADDED, DELETED;
  }

  // Properties

  private String tagName;

  private TagStatus status;
  protected List<AbstractConfigurationTag> subTags;

  private boolean generate = false;

  // Constructor

  protected AbstractConfigurationTag(final String tagName) {
    this.tagName = tagName;
    this.status = TagStatus.UNAFFECTED;
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

  public TagStatus getStatus() {
    return status;
  }

  public void setStatus(final TagStatus status) {
    this.status = status;
  }

  public void setTreeStatus(final TagStatus status) {
    this.status = status;
    for (AbstractConfigurationTag subTag : this.subTags) {
      subTag.setTreeStatus(status);
    }
  }

  public List<AbstractConfigurationTag> getSubTags() {
    return subTags;
  }

  // Generation mark

  public boolean getGenerate() {
    return generate;
  }

  public void setGenerate(boolean generate) {
    this.generate = generate;
  }

  public void setBranchGenerate(boolean generate) {
    this.generate = generate;
    for (AbstractConfigurationTag subTag : this.subTags) {
      subTag.setBranchGenerate(generate);
    }
  }

  public boolean includesGenerate() {
    if (this.generate) {
      return true;
    }
    for (AbstractConfigurationTag subTag : this.subTags) {
      if (subTag.includesGenerate()) {
        return true;
      }
    }
    return false;
  }

  public List<AbstractConfigurationTag> getTagsToGenerate() {
    List<AbstractConfigurationTag> list = new ArrayList<AbstractConfigurationTag>();
    this.addTagsToGenerate(list);
    return list;
  }

  private void addTagsToGenerate(final List<AbstractConfigurationTag> list) {
    if (this.generate) {
      list.add(this);
    }
    for (AbstractConfigurationTag subTag : this.subTags) {
      subTag.addTagsToGenerate(list);
    }
  }

  // XmlLocatable

  private SourceLocation location = null;

  public void setSourceLocation(final SourceLocation location) {
    this.location = location;
  }

  public SourceLocation getSourceLocation() {
    return this.location;
  }

  // Comparable

  @Override
  public int compareTo(final AbstractConfigurationTag o) {
    if (this.same(o)) {
      return 0;
    }
    int v = this.tagName.compareTo(o.tagName);
    return v == 0 ? -1 : v;
  }

  // Abstract methods for computing item changes

  // Checks if it has the same KEY properties
  public abstract boolean sameKey(AbstractConfigurationTag fresh);

  // Checks if it has the same properties. Computed properties are not compared.
  public abstract boolean same(AbstractConfigurationTag fresh);

  // Copy non-KEY properties; informs if there were any changes.
  public abstract boolean copyNonKeyProperties(AbstractConfigurationTag fresh);

}
