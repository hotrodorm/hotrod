package org.hotrod.eclipseplugin.elements;

public class ExecutorElement extends TreeContainerElement {

  public ExecutorElement(final String name, final boolean modified) {
    super(name, modified);
  }

  public ExecutorElement(final String name) {
    super(name, false);
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
