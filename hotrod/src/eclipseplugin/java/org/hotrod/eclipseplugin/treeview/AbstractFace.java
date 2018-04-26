package org.hotrod.eclipseplugin.treeview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.AbstractConfigurationTag.TagStatus;

public abstract class AbstractFace implements IAdaptable {

  private static final Logger log = Logger.getLogger(AbstractFace.class);

  private static final AbstractFace[] EMPTY_FACE_ARRAY = new AbstractFace[0];

  public static final String GENERATING_MARKER = "\u21ba ";

  private static int nextElementId = 0;

  private AbstractConfigurationTag tag;

  private String name;
  private AbstractFace parent;
  private int id;

  private boolean hasBranchChanges;

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
  }

  public void removeAllChildren() {
    this.children.clear();
  }

  public void removeChild(final int index) {
    try {
      this.children.remove(index);
    } catch (IndexOutOfBoundsException e) {
      // Ignore
    }
  }

  public void addChild(final AbstractFace child) {
    child.setParent(this);
    this.children.add(child);
  }

  public AbstractFace[] getChildren() {
    return this.children.toArray(EMPTY_FACE_ARRAY);
  }

  public boolean hasChildren() {
    return !this.children.isEmpty();
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
      this.computeBranchChanges();
      viewer.refresh(this, true);
    }
  }

  public boolean computeBranchChanges() {
    this.hasBranchChanges = this.getStatus() != TagStatus.UP_TO_DATE;
    for (AbstractFace c : this.children) {
      if (c.computeBranchChanges()) {
        this.hasBranchChanges = true;
      }
    }
    return this.hasBranchChanges;
  }

  public boolean hasBranchChanges() {
    return this.hasBranchChanges;
  }

  public final TagStatus getStatus() {
    return this.tag == null ? TagStatus.UP_TO_DATE : this.tag.getStatus();
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

    log.info("fresh=" + fresh);
    log("[1] applyChanges: " + this.name + " - this:" + System.identityHashCode(this.getTag()) + " fresh:"
        + System.identityHashCode(fresh.getTag()));

    // own changes

    TagStatus currentStatus = this.tag.getStatus();
    if (this.tag.copyNonKeyProperties(fresh.tag)) {
      currentStatus = TagStatus.MODIFIED;
    }
    this.tag = fresh.tag;
    this.tag.setStatus(currentStatus);

    try { // update the main tag, if it's a main face.
      MainConfigFace cf = (MainConfigFace) this;
      MainConfigFace ff = (MainConfigFace) fresh;
      cf.setConfig(ff.getConfig());
    } catch (ClassCastException e) {
      // Not a main tag - Ignore
    }

    log("  [2] applyChanges: " + this.name + " - this:" + System.identityHashCode(this.getTag()) + " status:"
        + this.getStatus());

    // sub item changes

    List<AbstractFace> existing = new ArrayList<AbstractFace>(this.children);
    log(" . children count '" + this.name + "': this=" + existing.size() + " fresh=" + fresh.children.size());
    this.children.clear();

    for (AbstractFace f : fresh.children) {
      AbstractFace e = retrieve(f, existing);
      if (e != null) {
        e.applyChangesFrom(f);
        this.children.add(e);
        e.parent = this;
      } else {
        this.children.add(f);
        f.parent = this;
        if (f.tag != null) {
          f.tag.setStatus(TagStatus.ADDED);
        }
      }
    }

    log(" . children removed '" + this.name + "': " + !existing.isEmpty());
    if (!existing.isEmpty()) { // some children were removed
      this.getTag().setStatus(TagStatus.MODIFIED);
    }

  }

  private AbstractFace retrieve(final AbstractFace f, final List<AbstractFace> existing) {
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

  public AbstractConfigurationTag getTag() {
    return tag;
  }

  private static void log(final String txt) {
    System.out.println("[" + new Object() {
    }.getClass().getEnclosingClass().getName() + "] " + txt);
  }

}