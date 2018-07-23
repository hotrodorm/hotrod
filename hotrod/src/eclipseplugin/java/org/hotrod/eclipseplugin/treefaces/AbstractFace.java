package org.hotrod.eclipseplugin.treefaces;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.AbstractConfigurationTag.TagStatus;
import org.hotrod.config.AbstractMethodTag;
import org.hotrod.eclipseplugin.HotRodViewContentProvider;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.runtime.util.LogUtil;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.ErrorMessage;

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

  private ErrorMessage ownErrorMessage;
  private ErrorMessage subtagErrorMessage;
  private ErrorMessage branchErrorMessage;

  private List<AbstractFace> children;

  public AbstractFace(final String name, final AbstractConfigurationTag tag) {
    initialize(name, tag, null);
  }

  public AbstractFace(final String name, final AbstractConfigurationTag tag, final ErrorMessage errorMessage) {
    initialize(name, tag, errorMessage);
  }

  private void initialize(final String name, final AbstractConfigurationTag tag, final ErrorMessage errorMessage) {
    synchronized (AbstractFace.class) {
      this.id = nextElementId++;
    }
    this.name = name;
    this.tag = tag;
    this.ownErrorMessage = errorMessage;
    this.subtagErrorMessage = null;
    this.children = new ArrayList<AbstractFace>();
    this.parent = null;
  }

  public void setName(final String name) {
    this.name = name;
  }

  protected void setErrorMessage(final ErrorMessage errorMessage) {
    log.info("setting error message (1) errorMessage=" + errorMessage);
    log.info("*** Call Stack:\n" + LogUtil.renderStack());
    this.ownErrorMessage = errorMessage;
    this.branchErrorMessage = errorMessage;
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

  protected List<FragmentConfigFace> getFragments() {
    List<FragmentConfigFace> fragments = new ArrayList<FragmentConfigFace>();
    for (AbstractFace f : this.children) {
      try {
        FragmentConfigFace fragment = (FragmentConfigFace) f;
        fragments.add(fragment);
      } catch (ClassCastException e) {
        // Ignore non-fragments
      }
    }
    return fragments;
  }

  public AbstractFace[] getChildren() {
    log.debug("this.children[" + this.children.size() + "]");
    return this.children.toArray(EMPTY_FACE_ARRAY);
  }

  public boolean hasChildren() {
    log.debug("this.children.isEmpty()=" + this.children.isEmpty());
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

  public boolean hasError() {
    return this.getErrorMessage() != null;
  }

  public ErrorMessage getErrorMessage() {
    try {
      MainConfigFace face = (MainConfigFace) this;
      if (face.isValid()) {
        return null;
      }
    } catch (ClassCastException e) {
      // ignore
    }
    if (this.ownErrorMessage != null) {
      return this.ownErrorMessage;
    }
    if (this.subtagErrorMessage != null) {
      return this.subtagErrorMessage;
    }
    return this.branchErrorMessage;
  }

  public HotRodViewContentProvider getProvider() {
    return this.parent != null ? this.parent.getProvider() : null;
  }

  public void refreshView() {
    log.debug("### Refreshing view - this=" + this.getDecoration());
    TreeViewer viewer = this.getViewer();
    if (viewer != null) {

      Display.getDefault().asyncExec(new RefreshViewRunnable(this, viewer));
      log.debug("### Refresh complete");

    } else {
      log.debug("### Refresh could not be done");
    }
  }

  private class RefreshViewRunnable implements Runnable {

    private AbstractFace face;
    private TreeViewer viewer;

    private RefreshViewRunnable(final AbstractFace face, final TreeViewer viewer) {
      this.face = face;
      this.viewer = viewer;
    }

    @Override
    public void run() {
      log.debug("-- refreshing view");
      this.viewer.refresh(this.face, true);
      log.debug("-- refreshing view - complete");
    }

  }

  public void computeBranchMarkers() {
    log.debug("Computing changes & errors");
    this.computeBranchChanges();
    this.computeBranchError();
  }

  private boolean computeBranchChanges() {
    this.hasBranchChanges = this.getStatus() != TagStatus.UP_TO_DATE;
    for (AbstractFace c : this.children) {
      if (c.computeBranchChanges()) {
        this.hasBranchChanges = true;
      }
    }
    return this.hasBranchChanges;
  }

  // ownErrorMessage -- own error (precomputed)
  // subtagErrorMessage -- children's error
  // branchErrorMessage -- own, or children error

  private ErrorMessage computeBranchError() {
    log.info("computing branch error (2) this.ownErrorMessage=" + this.ownErrorMessage);
    this.branchErrorMessage = null;
    if (this.ownErrorMessage != null) { // face with own error
      this.branchErrorMessage = this.ownErrorMessage;
      return this.ownErrorMessage;
    }
    if (this.children.isEmpty()) { // leaf face node
      this.subtagErrorMessage = this.tag.getBranchError();
      this.branchErrorMessage = this.subtagErrorMessage;
    } else { // intermediate face node
      for (AbstractFace c : this.children) {
        ErrorMessage errorMessage = c.computeBranchError();
        if (errorMessage != null) {
          this.branchErrorMessage = errorMessage;
          return errorMessage;
        }
      }
    }
    return this.branchErrorMessage;
  }

  public boolean hasBranchChanges() {
    return this.hasBranchChanges;
  }

  public ErrorMessage getBranchErrorMessage() {
    return branchErrorMessage;
  }

  public boolean hasBranchErrors() {
    return this.branchErrorMessage != null;
  }

  public final TagStatus getStatus() {
    return this.tag == null ? TagStatus.UP_TO_DATE : this.tag.getStatus();
  }

  public abstract String getDecoration();

  public abstract String getIconPath();

  public abstract String getErrorIconPath();

  public Image getImage() {
    return ImageCache.getImage(this.getIconPath());
  }

  public Image getErrorImage() {
    return ImageCache.getImage(this.getErrorIconPath());
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

  // TODO: remove once tested
  // // Processing file system changes
  //
  // public boolean informFileAdded(final File f) throws UncontrolledException,
  // ControlledException {
  // return false;
  // }
  //
  // public boolean informFileChanged(final File f) throws
  // UncontrolledException, ControlledException {
  // return false;
  // }
  //
  // public boolean informFileRemoved(final File f) throws
  // UncontrolledException, ControlledException {
  // return false;
  // }

  // Extra

  public final void display() {
    display(0);
  }

  private final void display(final int level) {
    log.info(SUtils.getFiller("+ ", level) + this.getDecoration() + "[" + this.children.size() + "] - "
        + System.identityHashCode(this));
    if (this instanceof MainConfigFace) {
      displayChildrenIDs(">>>");
    }
    for (AbstractFace f : this.children) {
      f.display(level + 1);
    }
  }

  public final boolean applyChangesFrom(final AbstractFace fresh) {
    return applyChangesFrom(fresh, 0);
  }

  private final boolean applyChangesFrom(final AbstractFace fresh, final int level) {

    boolean display = this.tag instanceof AbstractMethodTag
        && "findAccountWithPerson".equals(((AbstractMethodTag<?>) this.tag).getMethod());
    if (display) {
      log.debug("***** " + SUtils.getFiller("- ", level) + "existing:" + this.getDecoration() + "["
          + this.children.size() + "] (" + System.identityHashCode(this) + ") - fresh:" + fresh.getDecoration() + "["
          + fresh.children.size() + "] (" + System.identityHashCode(fresh) + ")");
    }

    // own changes

    boolean changesDetected = false;

    // log.info("this.tag=" + this.tag);
    TagStatus currentStatus = this.tag.getStatus();
    if (this.tag.copyNonKeyProperties(fresh.tag)) {
      if (display) {
        log.debug("*** CHANGES DETECTED!");
      }
      currentStatus = TagStatus.MODIFIED;
      changesDetected = true;
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

    // sub item changes

    if (display) {
      log.debug("***** changesDetected=" + changesDetected);
    }

    List<AbstractFace> existing = new ArrayList<AbstractFace>(this.children);

    this.children.clear();

    for (AbstractFace f : fresh.children) {
      log.debug(SUtils.getFiller("- ", level) + "Searching child '" + f.getDecoration() + "' ("
          + System.identityHashCode(f) + ")");
      AbstractFace e = retrieve(f, existing);
      if (e != null) {
        log.debug(SUtils.getFiller("- ", level) + ".. child [" + e.getDecoration() + "] found ("
            + System.identityHashCode(e) + ")");
        boolean innerChanges = e.applyChangesFrom(f, level + 1);
        if (innerChanges) {
          changesDetected = true;
        }
        this.children.add(e);
        e.parent = this;
      } else {
        log.debug(SUtils.getFiller("- ", level) + ".. child not found -- adding new one.");
        changesDetected = true;
        this.children.add(f);
        f.parent = this;
        if (f.tag != null) {
          f.tag.setTreeStatus(TagStatus.ADDED);
        }
      }
    }

    log.debug(SUtils.getFiller("- ", level) + "  children removed '" + this.name + "': " + !existing.isEmpty());
    if (!existing.isEmpty()) { // some children were removed
      this.getTag().setStatus(TagStatus.MODIFIED);
      changesDetected = true;
    }

    if (display) {
      log.debug("***** changesDetected=" + changesDetected);
    }
    return changesDetected;

  }

  private AbstractFace retrieve(final AbstractFace f, final List<AbstractFace> existing) {
    for (Iterator<AbstractFace> it = existing.iterator(); it.hasNext();) {
      AbstractFace e = it.next();
      log.debug("--------- searching... " + e.getDecoration() + " (" + System.identityHashCode(e) + ")");
      if (f.tag == null) {
        log.trace("f.item is null!");
      }
      if (e.tag == null) {
        log.trace("e.item is null!");
      }
      if (f.tag.sameKey(e.tag)) {
        it.remove();
        return e;
      }
    }
    return null;
  }

  private void displayChildrenIDs(final String prompt) {
    for (AbstractFace f : this.children) {
      log.debug(prompt + " children id = " + System.identityHashCode(f));
    }
  }

  public AbstractConfigurationTag getTag() {
    return tag;
  }

}