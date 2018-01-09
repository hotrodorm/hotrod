package org.hotrod.eclipseplugin.elements;

import java.io.File;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.part.ViewPart;

public class HotRodViewContentProvider implements ITreeContentProvider {

  private ViewPart viewPart;
  private List<String> files;
  private List<MainConfigElement> mainConfigs;
  private TreeViewer viewer;
  private boolean refresh;

  public HotRodViewContentProvider(final ViewPart viewPart, final List<String> files) {
    super();
    this.viewPart = viewPart;
    this.files = files;
    this.mainConfigs = null;
    this.viewer = null;
    this.refresh = false;
  }

  @Override
  public Object[] getElements(final Object inputElement) {
    if (inputElement.equals(this.viewPart.getViewSite())) {
      if (this.mainConfigs == null) {
        System.out.println("will load");
        ElementProducer p = new ElementProducer(this, this.files);
        System.out.println("loaded 1");
        this.mainConfigs = p.getConfigs();
        System.out.println("loaded 2");
      }
      return this.mainConfigs.toArray(new MainConfigElement[0]);
    }
    return getChildren(inputElement);
  }

  @Override
  public Object getParent(final Object child) {
    if (child instanceof TreeElement) {
      return ((TreeElement) child).getParent();
    }
    return null;
  }

  @Override
  public boolean hasChildren(final Object parent) {
    if (parent instanceof TreeContainerElement) {
      return ((TreeContainerElement) parent).hasChildren();
    }
    return false;
  }

  @Override
  public Object[] getChildren(final Object parent) {
    if (parent instanceof TreeContainerElement) {
      return ((TreeContainerElement) parent).getChildren();
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

  public List<MainConfigElement> getMainConfigs() {
    return this.mainConfigs;
  }

}
