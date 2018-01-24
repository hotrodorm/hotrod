package org.hotrod.eclipseplugin.treeview;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;

public abstract class AbstractFace implements IAdaptable {

  private static final AbstractFace[] EMPTY_FACE_ARRAY = new AbstractFace[0];

  public static final String GENERATING_MARKER = "\u21ba ";

  private static int nextElementId = 0;

  public static enum ChangeStatus {

    // UNCHANGED(null), //
    // MODIFIED("> "), //
    // ADDED("+ "), //
    // DELETED("- ");

    // UNCHANGED(null), //
    // MODIFIED("\u2734 \u2731 \u2605 \u2736 \u2b51 \u2b52 "), //
    // ADDED("\u2795 + \uff0b \u2795 "), //
    // DELETED("\u24e7 \u274c \u2716 \u2715 ");

    UNCHANGED(null), //
    MODIFIED("\u2736 "), //
    ADDED("+ "), //
    DELETED("\u2715 ");

    private String prefix;

    private ChangeStatus(final String prefix) {
      this.prefix = prefix;
    }

    public String getPrefix() {
      return prefix;
    }

  }

  private String name;
  protected ChangeStatus aggregatedStatus;
  private boolean generating;
  private AbstractFace parent;
  private int id;

  private List<AbstractFace> children;

  public AbstractFace(final String name) {
    synchronized (AbstractFace.class) {
      this.id = nextElementId++;
    }
    this.name = name;
    this.children = new ArrayList<AbstractFace>();
    this.parent = null;
    this.aggregatedStatus = ChangeStatus.UNCHANGED;
  }

  public void setName(final String name) {
    this.name = name;
    this.setModified();
  }

  public void removeAllChildren() {
    this.children.clear();
  }

  public void addChild(final AbstractFace child) {
    child.setParent(this);
    this.children.add(child);
    this.setModified();
  }

  // void removeChild(final AbstractFace child) {
  // this.children.remove(child);
  // child.setParent(null);
  // this.setModified();
  // }

  public AbstractFace[] getChildren() {
    return this.children.toArray(EMPTY_FACE_ARRAY);
  }

  public boolean hasChildren() {
    return !this.children.isEmpty();
  }

  // Status change managing

  private void recomputeState(final AbstractFace face) {
    ChangeStatus current = this.aggregatedStatus;

  }

  public void setUnchanged() {
    this.markUnchanged();
    this.refreshView();
  }

  private void markUnchanged() {
    this.aggregatedStatus = ChangeStatus.UNCHANGED;
    for (AbstractFace child : this.children) {
      child.markUnchanged();
    }
  }

  public final void setModified() {
    this.markModified();
    this.refreshView();
  }

  private void markModified() {
    this.aggregatedStatus = ChangeStatus.MODIFIED;
    for (AbstractFace child : this.children) {
      child.markModified();
    }
  }

  public void setAdded() {
    this.aggregatedStatus = ChangeStatus.ADDED;
    // if (this.parent != null) {
    // this.parent.setModified();
    // } else {
    this.refreshView();
    // }
  }

  public void setDeleted() {
    this.aggregatedStatus = ChangeStatus.DELETED;
    // if (this.parent != null) {
    // this.parent.setModified();
    // } else {
    this.refreshView();
    // }
  }

  // Generating marker

  public boolean isGenerating() {
    return generating;
  }

  public void setGenerating(boolean generating) {
    this.generating = generating;
    this.refreshView();
  }

  // Navigation

  public AbstractFace getParent() {
    return this.parent;
  }

  @Override
  public <T> T getAdapter(Class<T> key) {
    return null;
  }

  public void setParent(final AbstractFace parent) {
    this.parent = parent;
  }

  public String getName() {
    return name;
  }

  public TreeViewer getViewer() {
    return this.parent != null ? this.parent.getViewer() : null;
  }

  public MainConfigFace getMainConfigFace() {
    return this.parent.getMainConfigFace();
  }

  public HotRodViewContentProvider getProvider() {
    return this.parent != null ? this.parent.getProvider() : null;
  }

  public void refreshView() {
    TreeViewer viewer = this.getViewer();
    if (viewer != null) {
      viewer.refresh(this, true);
    }
  }

  // Tree display

  public final ChangeStatus getStatus() {
    // return this.aggregatedStatus;
    return ChangeStatus.UNCHANGED;
  }

  public abstract String getDecoration();

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
    AbstractFace other = (AbstractFace) obj;
    if (id != other.id)
      return false;
    return true;
  }

}