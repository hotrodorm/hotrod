package plugin001view3activator.views.tree;

public class QueryElement extends TreeLeafElement {

  public QueryElement(final String name, final boolean modified) {
    super(name, modified);
  }

  public QueryElement(final String name) {
    super(name, false);
  }

  @Override
  public String getIconPath() {
    // return "icons/sql-query10-16.png";
    return "icons/sql-query6-16.png";
  }

  @Override
  public String getLabel() {
    return super.getLabel() + "()";
  }

  @Override
  public String getTooltip() {
    return "SQL query " + super.getLabel();
  }

}
