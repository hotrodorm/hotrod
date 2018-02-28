package org.hotrod.eclipseplugin.treeview;

import org.hotrod.config.AbstractMethodTag;
import org.hotrod.config.EnumTag;
import org.hotrod.eclipseplugin.treeview.FaceFactory.InvalidConfigurationItemException;

public class EnumFace extends AbstractFace {

  public EnumFace(final EnumTag enumDAO) throws InvalidConfigurationItemException {
    super(enumDAO.getName(), enumDAO);
    for (AbstractMethodTag m : enumDAO.getMethods()) {
      AbstractFace leaf = FaceFactory.getMethodElement(m);
      super.addChild(leaf);
    }
  }

  @Override
  public String getIconPath() {
    // return "icons/enum3-16.png";

    // return "icons/enum20-16.png";
    return "eclipse-plugin/icons/enum21-16.png";
  }

  @Override
  public String getDecoration() {
    return "enum";
  }

  @Override
  public String getTooltip() {
    return "Table " + super.getName();
  }

}
