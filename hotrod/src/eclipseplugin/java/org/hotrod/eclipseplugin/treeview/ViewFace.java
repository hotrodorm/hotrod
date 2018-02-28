package org.hotrod.eclipseplugin.treeview;

import org.hotrod.config.AbstractMethodTag;
import org.hotrod.config.ViewTag;
import org.hotrod.eclipseplugin.treeview.FaceFactory.InvalidConfigurationItemException;

public class ViewFace extends AbstractFace {

  public ViewFace(final ViewTag viewDAO) throws InvalidConfigurationItemException {
    super(viewDAO.getName(), viewDAO);
    for (AbstractMethodTag m : viewDAO.getMethods()) {
      AbstractFace leaf = FaceFactory.getMethodElement(m);
      super.addChild(leaf);
    }
  }

  @Override
  public String getIconPath() {
    // return "icons/view6-16.png";

    // return "icons/view20-16.png";
    return "eclipse-plugin/icons/view21-16.png";

    // return "icons/animate/ani2.png";

    // return "icons/view5-16.png";
    // return "icons/view7-16.png";
    // return "icons/view9-16.png";
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
