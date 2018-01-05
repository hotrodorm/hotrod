package plugin001view3activator.views.tree;

public class FragmentConfigElement extends TreeContainerElement {

  public FragmentConfigElement(final String name, final boolean modified) {
    super(name, modified);
  }

  public FragmentConfigElement(final String name) {
    super(name, false);
  }

  @Override
  public String getIconPath() {
    return "icons/fragment1-16.png";
  }

}
