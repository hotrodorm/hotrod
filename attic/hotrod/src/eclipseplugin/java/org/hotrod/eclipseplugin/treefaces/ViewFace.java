package org.hotrod.eclipseplugin.treefaces;

import org.hotrod.config.AbstractMethodTag;
import org.hotrod.config.ViewTag;
import org.hotrod.eclipseplugin.HotRodView;
import org.hotrod.eclipseplugin.treefaces.FaceFactory.InvalidConfigurationItemException;

public class ViewFace extends AbstractFace {

  public ViewFace(final ViewTag viewDAO) throws InvalidConfigurationItemException {
    super(viewDAO.getId().getCanonicalSQLName(), viewDAO);
    for (AbstractMethodTag<?> m : viewDAO.getMethods()) {
      AbstractFace leaf = FaceFactory.getMethodElement(m);
      super.addChild(leaf);
    }
  }

  @Override
  public String getIconPath() {
    return HotRodView.ICONS_DIR + "view.png";
  }

  @Override
  public String getErrorIconPath() {
    return HotRodView.ICONS_DIR + "view-error.png";
  }

  @Override
  public String getDecoration() {
    return "view";
  }

  @Override
  public String getTooltip() {
    return "View " + super.getName();
  }

}
