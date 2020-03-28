package org.hotrod.eclipseplugin;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.hotrod.eclipseplugin.treefaces.AbstractFace;
import org.hotrod.eclipseplugin.treefaces.MainConfigFace;

public class HotRodViewContentProvider implements ITreeContentProvider {

  private static final Logger log = Logger.getLogger(HotRodViewContentProvider.class);

  private HotRodView viewPart;
  private TreeViewer viewer;
  private boolean visible;

  private LoadedConfigurationFiles files;

  public HotRodViewContentProvider(final HotRodView viewPart) {
    super();
    this.viewPart = viewPart;
    this.viewer = null;
    this.visible = false;
    this.files = new LoadedConfigurationFiles(this, this.viewPart);
  }

  @Override
  public Object[] getElements(final Object inputElement) {
    log.info("getting elements 1");
    if (inputElement.equals(this.viewPart.getViewSite())) {
      log.info("getting elements 2 - this.files.getLoadedFiles().size()=" + this.files.getLoadedFiles().size());
      return this.files.getLoadedFiles().toArray(new MainConfigFace[0]);
    }
    log.info("getting elements 3");
    return getChildren(inputElement);
  }

  @Override
  public Object getParent(final Object child) {
    if (child instanceof AbstractFace) {
      return ((AbstractFace) child).getParent();
    }
    return null;
  }

  @Override
  public boolean hasChildren(final Object parent) {
    if (parent instanceof AbstractFace) {
      return ((AbstractFace) parent).hasChildren();
    }
    return false;
  }

  @Override
  public Object[] getChildren(final Object parent) {
    if (parent instanceof AbstractFace) {
      return ((AbstractFace) parent).getChildren();
    }
    return new Object[0];
  }

  @Override
  public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
    this.viewer = (TreeViewer) viewer;
    // log("viewer= " + this.viewer);
    // log("oldInput: " + oldInput + " newInput: " + newInput);
  }

  public void setVisible(final boolean visible) {
    this.visible = visible;
  }

  public void removeFace(final MainConfigFace face) {
    this.files.remove(face);
  }

  public void refresh() {
    log.info("content provider refresh 1");
    TreeViewer v = this.getViewer();
    log.info("content provider refresh 2 v=" + v);
    if (v != null) {

      // Make sure the refresh happens in the singleton UI thread, even when
      // responding to non-UI events (e.g. file changes). Use:
      // Display.getDefault().syncExec(...)
      Display.getDefault().syncExec(new Runnable() {
        @Override
        public void run() {
          getViewer().refresh();
        }
      });

    }
  }

  // Getters

  public TreeViewer getViewer() {
    return this.visible ? this.viewer : null;
  }

  public LoadedConfigurationFiles getFiles() {
    return this.files;
  }

}
