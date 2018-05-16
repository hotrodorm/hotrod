package org.hotrod.eclipseplugin.treefaces;

import org.hotrod.config.SequenceMethodTag;
import org.hotrod.eclipseplugin.HotRodView;

public class SequenceFace extends AbstractMethodFace {

  public SequenceFace(final SequenceMethodTag method) {
    super(method.getName(), method);
  }

  @Override
  public String getIconPath() {
    return HotRodView.ICONS_DIR + "sql-sequence.png";
  }

  @Override
  public String getErrorIconPath() {
    return HotRodView.ICONS_DIR + "sql-sequence-error.png";
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
