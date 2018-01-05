package plugin001view3activator.views.tree;

public class ViewElement extends TreeLeafElement {

  public ViewElement(final String name, final boolean modified) {
    super(name, modified);
  }

  public ViewElement(final String name) {
    super(name, false);
  }

  @Override
  public String getIconPath() {
    return "icons/view4-16.png";
  }

}
