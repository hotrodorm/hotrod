package plugin001view3activator.views.tree;

import org.eclipse.jface.viewers.TreeViewer;

public class MainConfigElement extends TreeContainerElement {

  private HotRodViewContentProvider provider;

  // Constructors

  public MainConfigElement(final String name, final HotRodViewContentProvider provider, final boolean modified) {
    super(name, modified);
    this.provider = provider;
  }

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

}
