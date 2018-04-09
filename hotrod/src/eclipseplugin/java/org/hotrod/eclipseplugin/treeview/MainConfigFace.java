package org.hotrod.eclipseplugin.treeview;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.TreeViewer;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.eclipseplugin.RelativeProjectPath;
import org.hotrod.eclipseplugin.treeview.FaceFactory.InvalidConfigurationItemException;

public class MainConfigFace extends AbstractFace implements Comparable<MainConfigFace> {

  private String absolutePath;
  private RelativeProjectPath path;
  private HotRodViewContentProvider provider;
  private HotRodConfigTag config;

  // Constructors

  public MainConfigFace(final File f, final RelativeProjectPath path, final HotRodViewContentProvider provider,
      final HotRodConfigTag config) {
    super(path.getFileName(), config);
    this.absolutePath = f.getAbsolutePath();
    this.path = path;
    this.provider = provider;
    this.config = config;

    for (AbstractConfigurationTag subTag : config.getSubTags()) {
      try {
        AbstractFace face = FaceFactory.getFace(subTag);
        this.addChild(face);
      } catch (InvalidConfigurationItemException e) {
        this.config = null;
        this.removeAllChildren();
        this.addChild(new ErrorMessageFace(e.getLocation(), e.getMessage()));
        return;
      }
    }
  }

  public MainConfigFace(final File f, final RelativeProjectPath path, final HotRodViewContentProvider provider,
      final ErrorMessageFace errorMessageFace) {
    super(path.getFileName(), null);
    this.absolutePath = f.getAbsolutePath();
    this.path = path;
    this.provider = provider;
    this.config = null;
    this.addChild(errorMessageFace);
  }

  // Behavior

  // public void applyFreshVersion(final MainConfigFace fresh) {
  // super.computeChangesFrom(fresh);
  // }

  void setConfig(HotRodConfigTag config) {
    this.config = config;
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
    return this.config != null ? "eclipse-plugin/icons/main-config5-16.png"
        : "eclipse-plugin/icons/main-config5-bad-16.png";
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
