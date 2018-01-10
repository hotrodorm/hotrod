package org.hotrod.eclipseplugin.treeview;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.part.ViewPart;

public class HotRodViewContentProvider implements ITreeContentProvider {

  private ViewPart viewPart;
  private List<String> files;
  private List<MainConfigFace> mainConfigs;
  private TreeViewer viewer;
  private boolean refresh;

  public HotRodViewContentProvider(final ViewPart viewPart, final List<String> files) {
    super();
    this.viewPart = viewPart;
    this.files = files;
    this.mainConfigs = null;
    this.viewer = null;
    this.refresh = false;
    initializeTree();
  }

  private void initializeTree() {
    System.out.println("will load");
    FaceProducer p = new FaceProducer(this, this.files);
    System.out.println("loaded 1");
    this.mainConfigs = p.getConfigs();
    System.out.println("loaded 2");
  }

  @Override
  public Object[] getElements(final Object inputElement) {
    if (inputElement.equals(this.viewPart.getViewSite())) {
      return this.mainConfigs.toArray(new MainConfigFace[0]);
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

  public void setRefresh(final boolean refresh) {
    this.refresh = refresh;
  }

  // Getters

  public TreeViewer getViewer() {
    return this.refresh ? this.viewer : null;
  }

  public List<MainConfigFace> getMainConfigs() {
    return this.mainConfigs;
  }

}
