package plugin001view3activator.views.tree;

public class DAOElement extends TreeLeafElement {

  public DAOElement(final String name, final boolean modified) {
    super(name, modified);
  }

  public DAOElement(final String name) {
    super(name, false);
  }

  @Override
  public String getIconPath() {
    return "icons/dao3-16.png";
  }

}
