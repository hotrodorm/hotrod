package org.hotrod.eclipseplugin.treefaces;

import org.hotrod.config.AbstractMethodTag;
import org.hotrod.config.EnumTag;
import org.hotrod.eclipseplugin.HotRodView;
import org.hotrod.eclipseplugin.treefaces.FaceFactory.InvalidConfigurationItemException;

public class EnumFace extends AbstractFace {

  public EnumFace(final EnumTag enumDAO) throws InvalidConfigurationItemException {
    super(enumDAO.getId().getCanonicalSQLName(), enumDAO);
    for (AbstractMethodTag<?> m : enumDAO.getMethods()) {
      AbstractFace leaf = FaceFactory.getMethodElement(m);
      super.addChild(leaf);
    }
  }

  @Override
  public String getIconPath() {
    return HotRodView.ICONS_DIR + "enum.png";
  }

  @Override
  public String getErrorIconPath() {
    return HotRodView.ICONS_DIR + "enum-error.png";
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
