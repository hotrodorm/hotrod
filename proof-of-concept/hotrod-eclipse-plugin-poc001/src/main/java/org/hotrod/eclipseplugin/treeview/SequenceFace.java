package org.hotrod.eclipseplugin.treeview;

import org.hotrod.eclipseplugin.domain.SequenceMethod;

public class SequenceFace extends AbstractLeafFace {

  private SequenceMethod method;

  public SequenceFace(final SequenceMethod method) {
    super(method.getName(), false);
    this.method = method;
  }

  @Override
  public String getIconPath() {
    // return "icons/sql-sequence5-16.png";
    return "icons/sql-sequence7b-16.png";
  }

  @Override
  public String getLabel() {
    return super.getLabel() + "() -- retrieve sequence value";
  }

  @Override
  public String getTooltip() {
    return "Get SQL sequence " + super.getLabel();
  }

}
