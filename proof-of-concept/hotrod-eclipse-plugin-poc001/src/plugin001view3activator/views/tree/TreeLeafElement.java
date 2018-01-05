package plugin001view3activator.views.tree;

public abstract class TreeLeafElement extends TreeElement {

  public TreeLeafElement(final String name, final boolean modified) {
    super(name, modified);
  }

  @Override
  public void setUnmodified() {
    unmodifySubtree();
    this.refreshView();
  }

  public void unmodifySubtree() {
    super.modified = false;
  }

}
