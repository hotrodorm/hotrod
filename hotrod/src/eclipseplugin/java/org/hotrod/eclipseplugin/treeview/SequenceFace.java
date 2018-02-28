package org.hotrod.eclipseplugin.treeview;

import org.hotrod.config.SequenceMethodTag;

public class SequenceFace extends AbstractMethodFace {

  public SequenceFace(final SequenceMethodTag method) {
    super(method.getName(), method);
  }

  @Override
  public String getIconPath() {
    // return "icons/sql-sequence5-16.png";
    return "eclipse-plugin/icons/sql-sequence7b-16.png";
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
