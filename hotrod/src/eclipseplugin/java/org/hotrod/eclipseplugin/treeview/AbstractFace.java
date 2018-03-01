package org.hotrod.eclipseplugin.treeview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.AbstractConfigurationTag.TagStatus;

public abstract class AbstractFace implements IAdaptable {

  private static final AbstractFace[] EMPTY_FACE_ARRAY = new AbstractFace[0];

  public static final String GENERATING_MARKER = "\u21ba ";

  private static int nextElementId = 0;

  private AbstractConfigurationTag tag;

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

  public AbstractFace(final String name, final AbstractConfigurationTag tag) {
    synchronized (AbstractFace.class) {
      this.id = nextElementId++;
    }
    this.name = name;
    this.tag = tag;
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

  private TagStatus treeStatus = null;

  public final TagStatus getTreeStatus() {
    log("0 name=" + this.name + " this.getStatus()=" + this.getStatus() + " this.treeStatus=" + this.treeStatus);
    if (this.treeStatus == null) {
      log("1");
      if (this.getStatus() == TagStatus.MODIFIED) {
        log("2");
        this.treeStatus = TagStatus.MODIFIED;
      } else {
        log("3");
        boolean changed = false;
        for (AbstractFace c : this.children) {
          if (c.getTreeStatus() != TagStatus.UNAFFECTED) {
            changed = true;
          }
        }
        this.treeStatus = changed ? TagStatus.MODIFIED : this.getStatus();
      }
    }
    log("4 this.treeStatus=" + this.treeStatus);
    return this.treeStatus;
  }

  public final TagStatus getStatus() {
    return this.tag == null ? TagStatus.UNAFFECTED : this.tag.getStatus();
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

  private final void unsetTreeStatus() {
    this.treeStatus = null;
    if (this.parent != null) {
      this.parent.unsetTreeStatus();
    }
  }

  public final void applyChangesFrom(final AbstractFace fresh) {

    // own changes

    if (this.tag.copyNonKeyProperties(fresh.tag)) {
      this.tag.setStatus(TagStatus.MODIFIED);
      this.unsetTreeStatus();
    }

    // sub item changes

    List<AbstractFace> existing = new ArrayList<AbstractFace>(this.children);
    this.children.clear();

    for (AbstractFace f : fresh.children) {
      AbstractFace e = extractChildren(f, existing);
      if (e != null) {
        e.applyChangesFrom(f);
        this.children.add(e);
        e.parent = this;
      } else {
        this.children.add(f);
        f.parent = this;
        f.unsetTreeStatus();
        if (f.tag != null) {
          f.tag.setStatus(TagStatus.ADDED);
        }
      }
    }
    log("applyChangesFrom() name=" + this.name + " existing.isEmpty()=" + existing.isEmpty());
    if (!existing.isEmpty()) { // some children were removed
      this.unsetTreeStatus();
      this.treeStatus = TagStatus.MODIFIED;
    }

  }

  private AbstractFace extractChildren(final AbstractFace f, final List<AbstractFace> existing) {
    for (Iterator<AbstractFace> it = existing.iterator(); it.hasNext();) {
      AbstractFace e = it.next();
      if (f.tag == null) {
        System.out.println("f.item is null!");
      }
      if (e.tag == null) {
        System.out.println("e.item is null!");
      }
      if (f.tag.sameKey(e.tag)) {
        it.remove();
        return e;
      }
    }
    return null;
  }

  private static void log(final String txt) {
    // System.out.println("[" + new Object() {
    // }.getClass().getEnclosingClass().getName() + "] " + txt);
  }

}