package org.hotrod.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.generator.GeneretableObject;
import org.hotrod.runtime.dynamicsql.SourceLocation;
import org.hotrod.runtime.util.SUtils;

public abstract class AbstractConfigurationTag implements Comparable<AbstractConfigurationTag>, Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger log = Logger.getLogger(HotRodConfigTag.class);

  public enum TagStatus {
    UP_TO_DATE, MODIFIED, ADDED, DELETED;
  }

  // Properties

  private String tagName;
  private SourceLocation location = null;

  private TagStatus status;
  protected List<AbstractConfigurationTag> subTags;

  private transient boolean generate = false;
  private transient List<GeneretableObject> generetables = new ArrayList<GeneretableObject>();

  // Constructor

  protected AbstractConfigurationTag(final String tagName) {
    this.tagName = tagName;
    this.status = TagStatus.UP_TO_DATE;
    this.subTags = new ArrayList<AbstractConfigurationTag>();
  }

  // Constructor just for JAXB's sake - never used
  @SuppressWarnings("unused")
  private AbstractConfigurationTag() {
    this.tagName = "<configuration-tag>";
  }

  public void activate() {
    this.generetables = new ArrayList<GeneretableObject>();
    for (AbstractConfigurationTag subTag : this.subTags) {
      subTag.activate();
    }
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

  public boolean getGenerateMark() {
    return this.generate;
  }

  public void markGenerate(boolean generate) {
    this.generate = generate;
  }

  public void markGenerateSubtree(boolean generate) {
    this.generate = generate;
    for (AbstractConfigurationTag subTag : this.subTags) {
      subTag.markGenerateSubtree(generate);
    }
  }

  public boolean subtreeIncludesGenerateMark() {
    if (this.generate) {
      return true;
    }
    for (AbstractConfigurationTag subTag : this.subTags) {
      if (subTag.subtreeIncludesGenerateMark()) {
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

  // Generable objects

  public void resetGeneretables() {
    this.generetables.clear();
  }

  public void resetTreeGeneretables() {
    this.resetGeneretables();
    for (AbstractConfigurationTag subTag : this.subTags) {
      subTag.resetTreeGeneretables();
    }
  }

  public void addGeneretableObject(final GeneretableObject go) {
    log.info("this.generetables=" + this.generetables);
    this.generetables.add(go);
  }

  public List<GeneretableObject> getGeneretables() {
    return generetables;
  }

  public void unmarkGenerateIfAllGenerated() {
    if (this.generate) {
      log.info("unmarking: " + this.getInternalCaption());
      boolean allObjectsGenerated = true;
      for (GeneretableObject g : this.generetables) {
        log.info(" -> is generated (" + g.getClass().getName() + ") =" + g.isGenerated());
        if (!g.isGenerated()) {
          allObjectsGenerated = false;
        }
      }
      if (allObjectsGenerated) {
        this.markGenerate(false);
        this.status = TagStatus.UP_TO_DATE;
      }
    }
  }

  public void unmarkTreeGenerateIfAllGenerated() {
    unmarkGenerateIfAllGenerated();
    for (AbstractConfigurationTag subTag : this.subTags) {
      subTag.unmarkTreeGenerateIfAllGenerated();
    }
  }

  // XmlLocatable

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

  // Display

  public void displayGenerateMark(final String title, final char c) {
    log(SUtils.getFiller(c, 10) + " " + title + " " + SUtils.getFiller(c, 10));
    displayGenerateMark(this, 0);
    log(SUtils.getFiller(c, 22 + title.length()));
  }

  public void displayGenerateMark(final AbstractConfigurationTag tag, final int level) {
    log(SUtils.getFiller(". ", level) + " " + (tag.getGenerateMark() ? "G" : "_") + " " + tag.getInternalCaption()
        + " - " + System.identityHashCode(tag));
    for (AbstractConfigurationTag subtag : tag.getSubTags()) {
      displayGenerateMark(subtag, level + 1);
    }
  }

  // Merging logic

  protected boolean coreCopyNonKeyProperties(final AbstractConfigurationTag f) {
    try {
      boolean different = false;

      this.tagName = f.tagName;
      this.location = f.location;
      this.subTags = f.subTags;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Simple Caption

  public abstract String getInternalCaption();

  private void log(final String txt) {
    System.out.println("[AConfigT] " + txt);
  }

}
