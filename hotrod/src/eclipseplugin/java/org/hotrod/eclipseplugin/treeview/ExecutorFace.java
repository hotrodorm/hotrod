package org.hotrod.eclipseplugin.treeview;

import org.hotrod.config.AbstractMethodTag;
import org.hotrod.config.ExecutorTag;
import org.hotrod.eclipseplugin.HotRodView;
import org.hotrod.eclipseplugin.treeview.FaceFactory.InvalidConfigurationItemException;

public class ExecutorFace extends AbstractFace {

  public ExecutorFace(final ExecutorTag executorDAO) throws InvalidConfigurationItemException {
    super(executorDAO.getJavaClassName(), executorDAO);
    for (AbstractMethodTag<?> m : executorDAO.getMethods()) {
      AbstractFace leaf = FaceFactory.getMethodElement(m);
      super.addChild(leaf);
    }
  }

  @Override
  public String getIconPath() {
    return HotRodView.ICONS_DIR + "executor.png";
  }

  @Override
  public String getDecoration() {
    return "executor";
  }

  @Override
  public String getTooltip() {
    return "Executor " + super.getName();
  }

}
