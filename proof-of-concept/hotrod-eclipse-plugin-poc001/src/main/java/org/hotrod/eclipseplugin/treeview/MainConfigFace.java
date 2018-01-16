package org.hotrod.eclipseplugin.treeview;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.TreeViewer;
import org.hotrod.eclipseplugin.domain.loader.FaceProducer.RelativeProjectPath;

public class MainConfigFace extends AbstractFace implements Comparable<MainConfigFace> {

  private HotRodViewContentProvider provider;
  private String absolutePath;
  private IProject project;
  private String relativePath;
  private boolean valid;

  // Constructors

  public MainConfigFace(final File f, final RelativeProjectPath path, final HotRodViewContentProvider provider) {
    super(path.getFileName());
    this.absolutePath = f.getAbsolutePath();
    this.project = path.getProject();
    this.relativePath = path.getRelativePath();
    this.provider = provider;
    this.valid = false;
  }

  public void setValid() {
    this.valid = true;
  }

  public void setInvalid(final ErrorMessageFace errorMessage) {
    this.valid = false;
    this.addChild(errorMessage);
  }

  // Getters

  public boolean isValid() {
    return valid;
  }

  public String getAbsolutePath() {
    return absolutePath;
  }

  public String getRelativePath() {
    return relativePath;
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
    return this.valid ? "icons/main-config5-16.png" : "icons/main-config5-bad-16.png";
  }

  @Override
  public String getTooltip() {
    return "HotRod Configuration File " + super.getName();
  }

  @Override
  public int compareTo(final MainConfigFace other) {
    return this.getName().compareTo(other.getName());
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
