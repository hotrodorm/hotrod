package org.hotrod.eclipseplugin.treeview;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.part.ViewPart;
import org.hotrod.eclipseplugin.LoadedConfigurationFiles;

public class HotRodViewContentProvider implements ITreeContentProvider {

  private ViewPart viewPart;
  private TreeViewer viewer;
  private boolean visible;

  private LoadedConfigurationFiles files;

  public HotRodViewContentProvider(final ViewPart viewPart) {
    super();
    this.viewPart = viewPart;
    this.viewer = null;
    this.visible = false;
    this.files = new LoadedConfigurationFiles(this);
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
    if (parent instanceof AbstractContainerFace) {
      return ((AbstractContainerFace) parent).hasChildren();
    }
    return false;
  }

  @Override
  public Object[] getChildren(final Object parent) {
    if (parent instanceof AbstractContainerFace) {
      return ((AbstractContainerFace) parent).getChildren();
    }
    return new Object[0];
  }

  @Override
  public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
    this.viewer = (TreeViewer) viewer;
    System.out.println("viewer= " + this.viewer);
    System.out.println("oldInput: " + oldInput + " newInput: " + newInput);
  }

  public void setVisible(final boolean visible) {
    this.visible = visible;
  }

  public void refresh() {
    TreeViewer v = this.getViewer();
    if (v != null) {
      v.refresh();
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
