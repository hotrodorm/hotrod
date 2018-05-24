package org.hotrod.eclipseplugin.treefaces;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.TreeViewer;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.eclipseplugin.ErrorMessage;
import org.hotrod.eclipseplugin.FileProperties;
import org.hotrod.eclipseplugin.HotRodView;
import org.hotrod.eclipseplugin.HotRodViewContentProvider;
import org.hotrod.eclipseplugin.ProjectProperties;
import org.hotrod.eclipseplugin.RelativeProjectPath;
import org.hotrod.eclipseplugin.WorkspaceProperties;
import org.hotrod.eclipseplugin.treefaces.FaceFactory.InvalidConfigurationItemException;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.UncontrolledException;

public class MainConfigFace extends AbstractFace implements Comparable<MainConfigFace> {

  private static final Logger log = Logger.getLogger(MainConfigFace.class);

  private boolean valid;
  private String absolutePath;
  private RelativeProjectPath path;
  private transient HotRodViewContentProvider provider;
  private HotRodConfigTag config;

  // Constructors

  public MainConfigFace(final File f, final RelativeProjectPath path, final HotRodViewContentProvider provider,
      final HotRodConfigTag config) {
    super(path.getFileName(), config);
    this.valid = true;
    this.absolutePath = f.getAbsolutePath();
    this.path = path;
    this.provider = provider;

    this.config = config;
    addSubFaces(config);
  }

  private void addSubFaces(final HotRodConfigTag config) {
    for (AbstractConfigurationTag subTag : config.getSubTags()) {
      try {
        AbstractFace face = FaceFactory.getFace(subTag);
        this.addChild(face);
      } catch (InvalidConfigurationItemException e) {
        this.config = null;
        this.removeAllChildren();
        return;
      }
    }
  }

  public MainConfigFace(final File f, final RelativeProjectPath path, final HotRodViewContentProvider provider,
      final ErrorMessage errorMessage) {
    super(path.getFileName(), null, errorMessage);
    this.valid = false;
    this.absolutePath = f.getAbsolutePath();
    this.path = path;
    this.provider = provider;
    this.config = null;
  }

  // Behavior

  public boolean isValid() {
    return valid;
  }

  public void setInvalid(final ErrorMessage errorMessage) {
    if (errorMessage == null) {
      throw new IllegalArgumentException("Cannot set null error message.");
    }
    this.valid = false;
    super.setErrorMessage(errorMessage);
  }

  public void setValid() {
    this.valid = true;
    super.setErrorMessage(null);
  }

  public void initializeConfig(final HotRodConfigTag config) {
    this.config = config;
    super.removeAllChildren();
    addSubFaces(config);
  }

  public void setConfig(final HotRodConfigTag config) {
    this.config = config;
  }

  @Override
  public AbstractFace[] getChildren() {
    log.debug("this.valid=" + this.valid);
    if (this.valid) {
      AbstractFace[] children = super.getChildren();
      log.debug("children[" + children.length + "]");
      return children;
    } else {
      return new AbstractFace[0];
    }
  }

  @Override
  public boolean hasChildren() {
    log.info("this.valid=" + this.valid);
    if (this.valid) {
      return super.hasChildren();
    } else {
      return false;
    }
  }

  @Override
  public boolean hasBranchChanges() {
    log.info("this.valid=" + this.valid);
    if (this.valid) {
      return super.hasBranchChanges();
    } else {
      return false;
    }
  }

  // Getters

  public String getAbsolutePath() {
    return absolutePath;
  }

  public IProject getProject() {
    return this.path.getProject();
  }

  public String getRelativePath() {
    return this.path.getRelativePath();
  }

  public String getRelativeFileName() {
    return this.path.getRelativeFileName();
  }

  @Override
  public HotRodViewContentProvider getProvider() {
    return provider;
  }

  @Override
  public MainConfigFace getMainConfigFace() {
    return this;
  }

  @Override
  public TreeViewer getViewer() {
    return this.provider.getViewer();
  }

  public void remove() {
    this.provider.removeFace(this);
  }

  @Override
  public String getDecoration() {
    return "main config file";
  }

  @Override
  public String getIconPath() {
    if (!this.isValid()) {
      return getErrorIconPath();
    }
    return isConfigured() ? HotRodView.ICONS_DIR + "main-config-on.png" : HotRodView.ICONS_DIR + "main-config-off.png";
  }

  @Override
  public String getErrorIconPath() {
    return isConfigured() ? HotRodView.ICONS_DIR + "main-config-on-error.png"
        : HotRodView.ICONS_DIR + "main-config-off-error.png";
  }

  private boolean isConfigured() {
    ProjectProperties projectProperties = WorkspaceProperties.getInstance().getProjectProperties(this.getProject());
    FileProperties fileProperties = projectProperties.getFileProperties(this.getRelativeFileName());
    return fileProperties == null ? false : fileProperties.isConfigured();
  }

  @Override
  public String getTooltip() {
    return "HotRod Configuration File " + super.getName();
  }

  @Override
  public int compareTo(final MainConfigFace other) {
    return this.getName().compareTo(other.getName());
  }

  public HotRodConfigTag getConfig() {
    return config;
  }

  // Processing file system changes

  public boolean informFileAdded(final File f) throws UncontrolledException, ControlledException {
    return this.config.informFileAdded(f);
  }

  public boolean informFileChanged(final File f) throws UncontrolledException, ControlledException {
    return this.config.informFileChanged(f);
  }

  public boolean informFileRemoved(final File f) throws UncontrolledException, ControlledException {
    return this.config.informFileRemoved(f);
  }

  // Indexable

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((absolutePath == null) ? 0 : absolutePath.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    MainConfigFace other = (MainConfigFace) obj;
    if (absolutePath == null) {
      if (other.absolutePath != null)
        return false;
    } else if (!absolutePath.equals(other.absolutePath))
      return false;
    return true;
  }

}
