package org.hotrod.eclipseplugin.treeview;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContainerFace extends AbstractFace {

  private List<AbstractFace> children;

  public AbstractContainerFace(final String name, final boolean modified) {
    super(name, modified);
    this.children = new ArrayList<AbstractFace>();
  }

  void addChild(final AbstractFace child) {
    child.setParent(this);
    this.children.add(child);
    this.setModified();
  }

  void removeChild(final AbstractFace child) {
    this.children.remove(child);
    child.setParent(null);
    this.setModified();
  }

  public AbstractFace[] getChildren() {
    return this.children.toArray(new AbstractFace[0]);
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
    for (AbstractFace te : this.children) {
      te.setUnmodified();
    }
  }

}
