package plugin001view3activator.views.tree;

import java.util.ArrayList;
import java.util.List;

public abstract class TreeContainerElement extends TreeElement {

  private List<TreeElement> children;

  public TreeContainerElement(final String name, final boolean modified) {
    super(name, modified);
    this.children = new ArrayList<TreeElement>();
  }

  public void addChild(final TreeElement child) {
    child.setParent(this);
    this.children.add(child);
    this.setModified();
  }

  public void removeChild(final TreeElement child) {
    this.children.remove(child);
    child.setParent(null);
    this.setModified();
  }

  public TreeElement[] getChildren() {
    return this.children.toArray(new TreeElement[0]);
  }

  public boolean hasChildren() {
    return !this.children.isEmpty();
  }

  @Override
  public void setUnmodified() {
    unmodifySubtree();
    this.refreshView();
  }

  @Override
  public void unmodifySubtree() {
    super.modified = false;
    for (TreeElement te : this.children) {
      te.setUnmodified();
    }
  }

}
