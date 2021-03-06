package org.hotrod.eclipseplugin.treeview;

import org.hotrod.domain.SequenceMethod;

public class SequenceFace extends AbstractMethodFace {

  public SequenceFace(final SequenceMethod method) {
    super(method.getName(), method);
  }

  @Override
  public String getIconPath() {
    // return "icons/sql-sequence5-16.png";
    return "icons/sql-sequence7b-16.png";
  }

  @Override
  public String getDecoration() {
    return "sequence retriever";
  }

  @Override
  public String getTooltip() {
    return "Get SQL sequence " + super.getName();
  }

}
