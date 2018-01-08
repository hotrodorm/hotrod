package org.hotrod.eclipseplugin.elements;

import org.hotrod.eclipseplugin.domain.ExecutorDAO;
import org.hotrod.eclipseplugin.domain.Method;
import org.hotrod.eclipseplugin.elements.ElementFactory.InvalidConfigurationItemException;

public class ExecutorElement extends TreeContainerElement {

  private ExecutorDAO executorDAO;

  public ExecutorElement(final ExecutorDAO executorDAO) throws InvalidConfigurationItemException {
    super(executorDAO.getName(), false);
    this.executorDAO = executorDAO;
    for (Method m : this.executorDAO.getMethods()) {
      TreeLeafElement leaf = ElementFactory.getMethodElement(m);
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
