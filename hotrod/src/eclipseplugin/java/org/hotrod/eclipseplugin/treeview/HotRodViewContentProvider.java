package org.hotrod.eclipseplugin.treeview;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.hotrod.eclipseplugin.HotRodView;
import org.hotrod.eclipseplugin.LoadedConfigurationFiles;

public class HotRodViewContentProvider implements ITreeContentProvider {

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
    if (inputElement.equals(this.viewPart.getViewSite())) {
      return this.files.getLoadedFiles().toArray(new MainConfigFace[0]);
    }
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
    TreeViewer v = this.getViewer();
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
