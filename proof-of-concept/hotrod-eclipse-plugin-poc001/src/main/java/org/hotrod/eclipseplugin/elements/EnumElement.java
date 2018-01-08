package org.hotrod.eclipseplugin.elements;

import org.hotrod.eclipseplugin.domain.EnumDAO;
import org.hotrod.eclipseplugin.domain.Method;
import org.hotrod.eclipseplugin.elements.ElementFactory.InvalidConfigurationItemException;

public class EnumElement extends TreeContainerElement {

  private EnumDAO enumDAO;

  public EnumElement(final EnumDAO enumDAO) throws InvalidConfigurationItemException {
    super(enumDAO.getName(), false);
    this.enumDAO = enumDAO;
    for (Method m : this.enumDAO.getMethods()) {
      TreeLeafElement leaf = ElementFactory.getMethodElement(m);
      super.addChild(leaf);
    }
  }

  @Override
  public String getIconPath() {
    // return "icons/enum3-16.png";

    // return "icons/enum20-16.png";
    return "icons/enum21-16.png";
  }

  @Override
  public String getLabel() {
    return super.getLabel() + " -- enum";
  }

  @Override
  public String getTooltip() {
    return "Table " + super.getLabel();
  }

}
