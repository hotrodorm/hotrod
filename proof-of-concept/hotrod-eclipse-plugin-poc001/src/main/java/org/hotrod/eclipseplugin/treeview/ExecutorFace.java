package org.hotrod.eclipseplugin.treeview;

import org.hotrod.eclipseplugin.domain.ExecutorDAO;
import org.hotrod.eclipseplugin.domain.Method;
import org.hotrod.eclipseplugin.treeview.FaceFactory.InvalidConfigurationItemException;

public class ExecutorFace extends AbstractContainerFace {

  private ExecutorDAO executorDAO;

  public ExecutorFace(final ExecutorDAO executorDAO) throws InvalidConfigurationItemException {
    super(executorDAO.getName(), false);
    this.executorDAO = executorDAO;
    for (Method m : this.executorDAO.getMethods()) {
      AbstractLeafFace leaf = FaceFactory.getMethodElement(m);
      super.addChild(leaf);
    }
  }

  @Override
  public String getIconPath() {
    return "icons/executor3-16.png";
  }

  @Override
  public String getLabel() {
    return super.getLabel() + " -- executor";
  }

  @Override
  public String getTooltip() {
    return "Executor " + super.getLabel();
  }

}
