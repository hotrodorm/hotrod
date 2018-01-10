package org.hotrod.eclipseplugin.treeview;

import org.eclipse.jface.viewers.TreeViewer;

public class MainConfigFace extends AbstractContainerFace implements Comparable<MainConfigFace> {

  private HotRodViewContentProvider provider;
  private String path;
  private boolean valid;

  // Constructors

  public MainConfigFace(final String name, final String path, final HotRodViewContentProvider provider) {
    super(name, false);
    this.path = path;
    this.provider = provider;
    this.valid = false;
  }

  public void setValid(boolean valid) {
    this.valid = valid;
  }

  // Getters

  public boolean isValid() {
    return valid;
  }

  public String getPath() {
    return path;
  }

  public HotRodViewContentProvider getProvider() {
    return provider;
  }

  @Override
  public String getDecoration() {
    return "main config file";
  }

  @Override
  public String getIconPath() {
    return this.valid ? "icons/main-config5-16.png" : "icons/main-config5-bad-16.png";
  }

  public TreeViewer getViewer() {
    return this.provider.getViewer();
  }

  @Override
  public String getTooltip() {
    return "HotRod Main Configuration file " + super.getLabel();
  }

  @Override
  public int compareTo(final MainConfigFace other) {
    return this.getName().compareTo(other.getName());
  }

}
