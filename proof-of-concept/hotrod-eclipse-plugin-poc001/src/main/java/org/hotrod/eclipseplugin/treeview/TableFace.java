package org.hotrod.eclipseplugin.treeview;

import org.hotrod.eclipseplugin.domain.Method;
import org.hotrod.eclipseplugin.domain.TableDAO;
import org.hotrod.eclipseplugin.treeview.FaceFactory.InvalidConfigurationItemException;

public class TableFace extends AbstractFace {

  public TableFace(final TableDAO tableDAO) throws InvalidConfigurationItemException {
    super(tableDAO.getName(), tableDAO);
    for (Method m : tableDAO.getMethods()) {
      AbstractFace leaf = FaceFactory.getMethodElement(m);
      super.addChild(leaf);
    }
  }

  @Override
  public String getIconPath() {
    // return "icons/table2-16.png";
    // return "icons/table1-16.png";

    // return "icons/table20-16.png";
    return "icons/table21-16.png";
  }

  @Override
  public String getDecoration() {
    return "table";
  }

  @Override
  public String getTooltip() {
    return "Table " + super.getName();
  }

}
