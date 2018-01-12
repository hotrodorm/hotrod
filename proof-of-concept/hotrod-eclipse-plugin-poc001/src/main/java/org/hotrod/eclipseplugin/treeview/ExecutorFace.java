package org.hotrod.eclipseplugin.treeview;

import org.hotrod.eclipseplugin.domain.ExecutorDAO;
import org.hotrod.eclipseplugin.domain.Method;
import org.hotrod.eclipseplugin.treeview.FaceFactory.InvalidConfigurationItemException;

public class ExecutorFace extends AbstractFace {

  private ExecutorDAO executorDAO;

  public ExecutorFace(final ExecutorDAO executorDAO) throws InvalidConfigurationItemException {
    super(executorDAO.getName());
    this.executorDAO = executorDAO;
    for (Method m : this.executorDAO.getMethods()) {
      AbstractFace leaf = FaceFactory.getMethodElement(m);
      super.addChild(leaf);
    }
  }

  @Override
  public String getIconPath() {
    return "icons/executor3-16.png";
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
