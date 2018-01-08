package org.hotrod.eclipseplugin.elements;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;

public abstract class TreeElement implements IAdaptable {

  private static int nextElementId = 0;

  private String name;
  protected boolean modified;
  private TreeContainerElement parent;
  private int id;

  public TreeElement(final String name, final boolean modified) {
    synchronized (TreeElement.class) {
      this.id = nextElementId++;
    }
    this.name = name;
    this.parent = null;
    this.modified = modified;
  }

  public void setName(final String name) {
    this.name = name;
    this.setModified();
  }

  public abstract void setUnmodified();

  public abstract void unmodifySubtree();

  public void setModified() {
    this.modified = true;
    if (this.parent != null) {
      this.parent.setModified();
    } else {
      this.refreshView();
    }
  }

  public TreeContainerElement getParent() {
    return this.parent;
  }

  @Override
  public <T> T getAdapter(Class<T> key) {
    return null;
  }

  public void setParent(final TreeContainerElement parent) {
    this.parent = parent;
  }

  public TreeViewer getViewer() {
    return this.parent != null ? this.parent.getViewer() : null;
  }

  public void refreshView() {
    TreeViewer viewer = this.getViewer();
    if (viewer != null) {
      viewer.refresh(this, true);
    }
  }

  // Tree display

  public String getLabel() {
    return (this.modified ? "> " : "") + this.name;
  }

  public abstract String getIconPath();

  public Image getImage() {
    return ImageCache.getImage(this.getIconPath());
  }

  public abstract String getTooltip();

  // Indexable

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TreeElement other = (TreeElement) obj;
    if (id != other.id)
      return false;
    return true;
  }

}