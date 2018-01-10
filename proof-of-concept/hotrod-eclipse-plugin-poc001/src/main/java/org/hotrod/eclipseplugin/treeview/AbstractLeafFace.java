package org.hotrod.eclipseplugin.treeview;

public abstract class AbstractLeafFace extends AbstractFace {

  public AbstractLeafFace(final String name, final boolean modified) {
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
