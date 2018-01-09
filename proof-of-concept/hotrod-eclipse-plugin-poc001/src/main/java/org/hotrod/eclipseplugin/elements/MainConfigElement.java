package org.hotrod.eclipseplugin.elements;

import org.eclipse.jface.viewers.TreeViewer;

public class MainConfigElement extends TreeContainerElement {

  private HotRodViewContentProvider provider;

  // Constructors

  public MainConfigElement(final String name, final HotRodViewContentProvider provider) {
    super(name, false);
    this.provider = provider;
  }

  // Getters

  public HotRodViewContentProvider getProvider() {
    return provider;
  }

  @Override
  public String getIconPath() {
    return "icons/main-config3-16.png";
  }

  public TreeViewer getViewer() {
    return this.provider.getViewer();
  }

  @Override
  public String getTooltip() {
    return "HotRod Main Configuration file " + super.getLabel();
  }

}
