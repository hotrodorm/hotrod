package org.hotrod.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.generator.GeneratableObject;
import org.hotrod.runtime.dynamicsql.SourceLocation;
import org.hotrod.utils.ErrorMessage;
import org.hotrodorm.hotrod.utils.SUtil;

public abstract class AbstractConfigurationTag implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LogManager.getLogger(HotRodConfigTag.class);

  public enum TagStatus {
    UP_TO_DATE("."), MODIFIED("*"), ADDED("+"), DELETED("-");

    private String icon;

    private TagStatus(final String icon) {
      this.icon = icon;
    }

    public String getIcon() {
      return icon;
    }

  }

  // Properties

  private String tagName;

  private SourceLocation location = null;

  private TagStatus status;

  private AbstractConfigurationTag parent;
  private List<AbstractConfigurationTag> subTags;

  private ErrorMessage errorMessage = null;

  private enum GenerationStatus {
    NO_ACTION, TO_BE_GENERATED, GENERATION_COMPLETE
  }

  private transient GenerationStatus generate = GenerationStatus.NO_ACTION;
  private transient List<GeneratableObject> generatables = new ArrayList<GeneratableObject>();

  // Constructor

  protected AbstractConfigurationTag(final String tagName) {
    log.trace("init.");
    this.tagName = tagName;
    this.status = TagStatus.UP_TO_DATE;
    this.parent = null;
    this.subTags = new ArrayList<AbstractConfigurationTag>();
  }

  // Constructor just for JAXB's sake - never used
  @SuppressWarnings("unused")
  private AbstractConfigurationTag() {
    this.tagName = "<configuration-tag>";
  }

  public void activate() {
    this.generatables = new ArrayList<GeneratableObject>();
    for (AbstractConfigurationTag subTag : this.subTags) {
      subTag.activate();
    }
  }

  protected void clearChildren() {
    this.subTags.clear();
  }

  protected void addChild(final AbstractConfigurationTag t) {
    t.parent = this;
    this.subTags.add(t);
  }

  protected void addChildren(final Collection<? extends AbstractConfigurationTag> collection) {
    for (AbstractConfigurationTag t : collection) {
      this.addChild(t);
    }
  }

  public AbstractConfigurationTag getParent() {
    return parent;
  }

  // Getters

  public final String getTagName() {
    return tagName;
  }

  public GenerationStatus getGenerate() {
    return generate;
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

  public void resetTreeGeneration() {
    this.generate = GenerationStatus.NO_ACTION;
    for (AbstractConfigurationTag subTag : this.subTags) {
      subTag.resetTreeGeneration();
    }
  }

  public boolean isNoAction() {
    return this.generate == GenerationStatus.NO_ACTION;
  }

  public boolean isToBeGenerated() {
    return this.generate == GenerationStatus.TO_BE_GENERATED;
  }

  public boolean isGenerationComplete() {
    return this.generate == GenerationStatus.GENERATION_COMPLETE;
  }

  public void markGenerate() {
    // log.info(" === MARKING ===\n" + LogUtil.renderStack());
    this.generate = GenerationStatus.TO_BE_GENERATED;
  }

  public void markGenerateTree() {
    markGenerate();
    for (AbstractConfigurationTag subTag : this.subTags) {
      subTag.markGenerateTree();
    }
  }

  public boolean treeIncludesIsToBeGenerated() {
    return treeIncludesIsToBeGenerated(0);
  }

  public boolean treeIncludesIsToBeGenerated(final int level) {
    log.debug("@@ " + SUtil.getFiller(". ", level) + "[" + (this.isToBeGenerated() ? "g" : "_") + "] "
        + (this.status.getIcon()) + " " + this.getInternalCaption());
    if (this.isToBeGenerated()) {
      return true;
    }
    for (AbstractConfigurationTag subTag : this.subTags) {
      if (subTag.treeIncludesIsToBeGenerated(level + 1)) {
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
    if (this.isToBeGenerated()) {
      list.add(this);
    }
    for (AbstractConfigurationTag subTag : this.subTags) {
      subTag.addTagsToGenerate(list);
    }
  }

  // Error Mark

  public boolean hasError() {
    return this.errorMessage != null;
  }

  public ErrorMessage getErrorMessage() {
    return this.errorMessage;
  }

  public void setErrorMessage(final String errorMessage) {
    this.errorMessage = new ErrorMessage(this.location, errorMessage);
  }

  public void eraseTreeErrorMessages() {
    this.errorMessage = null;
    for (AbstractConfigurationTag subTag : this.subTags) {
      subTag.eraseTreeErrorMessages();
    }
  }

  public ErrorMessage getBranchError() {
    log.debug(
        " ::: " + this.getInternalCaption() + "[" + this.subTags.size() + "] this.errorMessage=" + this.errorMessage);
    if (this.errorMessage != null) {
      return this.errorMessage;
    }
    for (AbstractConfigurationTag subTag : this.subTags) {
      ErrorMessage branchError = subTag.getBranchError();
      if (branchError != null) {
        log.debug(" --> BRANCH ERROR FOUND!");
        return branchError;
      }
    }
    return null;
  }

  // Generable objects

  public void resetTreeGeneratables() {
    this.generatables.clear();
    for (AbstractConfigurationTag subTag : this.subTags) {
      subTag.resetTreeGeneratables();
    }
  }

  public void addGeneratableObject(final GeneratableObject g) {
    // log.info("this.generatables=" + this.generatables);
    this.generatables.add(g);
  }

  public List<GeneratableObject> getGeneratables() {
    return generatables;
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
