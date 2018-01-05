package plugin001view3activator.views.tree;

public class TableElement extends TreeLeafElement {

  public TableElement(final String name, final boolean modified) {
    super(name, modified);
  }

  public TableElement(final String name) {
    super(name, false);
  }

  @Override
  public String getIconPath() {
    return "icons/table2-16.png";
  }

}
