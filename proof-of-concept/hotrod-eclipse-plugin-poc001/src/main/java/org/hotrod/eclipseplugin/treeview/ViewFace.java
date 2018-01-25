package org.hotrod.eclipseplugin.treeview;

import org.hotrod.eclipseplugin.domain.Method;
import org.hotrod.eclipseplugin.domain.ViewDAO;
import org.hotrod.eclipseplugin.treeview.FaceFactory.InvalidConfigurationItemException;

public class ViewFace extends AbstractFace {

  public ViewFace(final ViewDAO viewDAO) throws InvalidConfigurationItemException {
    super(viewDAO.getName(), viewDAO);
    for (Method m : viewDAO.getMethods()) {
      AbstractFace leaf = FaceFactory.getMethodElement(m);
      super.addChild(leaf);
    }
  }

  @Override
  public String getIconPath() {
    // return "icons/view6-16.png";

    // return "icons/view20-16.png";
    return "icons/view21-16.png";

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
