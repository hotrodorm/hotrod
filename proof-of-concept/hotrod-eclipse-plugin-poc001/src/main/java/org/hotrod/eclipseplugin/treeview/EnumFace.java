package org.hotrod.eclipseplugin.treeview;

import org.hotrod.eclipseplugin.domain.EnumDAO;
import org.hotrod.eclipseplugin.domain.Method;
import org.hotrod.eclipseplugin.treeview.FaceFactory.InvalidConfigurationItemException;

public class EnumFace extends AbstractContainerFace {

  private EnumDAO enumDAO;

  public EnumFace(final EnumDAO enumDAO) throws InvalidConfigurationItemException {
    super(enumDAO.getName(), false);
    this.enumDAO = enumDAO;
    for (Method m : this.enumDAO.getMethods()) {
      AbstractLeafFace leaf = FaceFactory.getMethodElement(m);
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
