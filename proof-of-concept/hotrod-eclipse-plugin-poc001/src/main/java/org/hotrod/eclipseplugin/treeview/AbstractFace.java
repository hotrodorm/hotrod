package org.hotrod.eclipseplugin.treeview;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.hotrod.eclipseplugin.domain.ConfigItem;
import org.hotrod.eclipseplugin.domain.ConfigItem.ItemStatus;

public abstract class AbstractFace implements IAdaptable {

  private static final AbstractFace[] EMPTY_FACE_ARRAY = new AbstractFace[0];

  public static final String GENERATING_MARKER = "\u21ba ";

  private static int nextElementId = 0;

  private ConfigItem item;

  // public static enum ChangeStatus {
  //
  // // UNCHANGED(null), //
  // // MODIFIED("> "), //
  // // ADDED("+ "), //
  // // DELETED("- ");
  //
  // // UNCHANGED(null), //
  // // MODIFIED("\u2734 \u2731 \u2605 \u2736 \u2b51 \u2b52 "), //
  // // ADDED("\u2795 + \uff0b \u2795 "), //
  // // DELETED("\u24e7 \u274c \u2716 \u2715 ");
  //
  // UNCHANGED(null), //
  // MODIFIED("\u2736 "), //
  // ADDED("+ "), //
  // DELETED("\u2715 ");
  //
  // private String prefix;
  //
  // private ChangeStatus(final String prefix) {
  // this.prefix = prefix;
  // }
  //
  // public String getPrefix() {
  // return prefix;
  // }
  //
  // }

  private String name;
  private boolean generating;
  private AbstractFace parent;
  private int id;

  private List<AbstractFace> children;

  public AbstractFace(final String name, final ConfigItem item) {
    synchronized (AbstractFace.class) {
      this.id = nextElementId++;
    }
    this.name = name;
    this.item = item;
    this.children = new ArrayList<AbstractFace>();
    this.parent = null;
  }

  public void setName(final String name) {
    this.name = name;
    // this.setModified();
  }

  public void removeAllChildren() {
    this.children.clear();
  }

  public void removeChildren(final int index) {
    try {
      this.children.remove(index);
    } catch (IndexOutOfBoundsException e) {
      // Ignore
    }
  }

  public void addChild(final AbstractFace child) {
    child.setParent(this);
    this.children.add(child);
    // this.setModified();
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

  // public void setUnchanged() {
  // this.markUnchanged();
  // this.refreshView();
  // }

  // private void markUnchanged() {
  // for (AbstractFace child : this.children) {
  // child.markUnchanged();
  // }
  // }

  // public final void setModified() {
  // this.markModified();
  // this.refreshView();
  // }

  // private void markModified() {
  // this.aggregatedStatus = ChangeStatus.MODIFIED;
  // for (AbstractFace child : this.children) {
  // child.markModified();
  // }
  // }

  // public void setAdded() {
  // this.aggregatedStatus = ChangeStatus.ADDED;
  // // if (this.parent != null) {
  // // this.parent.setModified();
  // // } else {
  // this.refreshView();
  // // }
  // }

  // public void setDeleted() {
  // this.aggregatedStatus = ChangeStatus.DELETED;
  // // if (this.parent != null) {
  // // this.parent.setModified();
  // // } else {
  // this.refreshView();
  // // }
  // }

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

  public final ItemStatus getStatus() {
    return this.item == null ? ItemStatus.UNAFFECTED : this.item.getStatus();
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

  public final void applyChangesFrom(final AbstractFace fresh) {

    // own changes

    if (this.item.copyProperties(fresh.item)) {
      this.item.setStatus(ItemStatus.MODIFIED);
    }

    // sub item changes

    List<AbstractFace> existing = new ArrayList<AbstractFace>(this.children);
    this.children.clear();

    for (AbstractFace f : fresh.children) {
      AbstractFace e = findFace(f, existing);
      if (e != null) {
        e.applyChangesFrom(f);
        this.children.add(e);
      } else {
        this.children.add(f);
        if (f.item != null) {
          f.item.setStatus(ItemStatus.ADDED);
        }
      }
    }

  }

  private AbstractFace findFace(final AbstractFace f, final List<AbstractFace> existing) {
    for (AbstractFace e : existing) {
      if (f.item == null) {
        System.out.println("f.item is null!");
      }
      if (e.item == null) {
        System.out.println("e.item is null!");
      }
      if (f.item.sameID(e.item)) {
        return e;
      }
    }
    return null;
  }

}