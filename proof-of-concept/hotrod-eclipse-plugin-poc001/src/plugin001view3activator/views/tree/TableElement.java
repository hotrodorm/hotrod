package plugin001view3activator.views.tree;

public class TableElement extends TreeContainerElement {

  public TableElement(final String name, final boolean modified) {
    super(name, modified);
  }

  public TableElement(final String name) {
    super(name, false);
  }

  @Override
  public String getIconPath() {
    // return "icons/table2-16.png";
    return "icons/table1-16.png";
  }

  @Override
  public String getTooltip() {
    return "Table " + super.getLabel();
  }

}
