package org.hotrod.eclipseplugin.treeview;

import org.hotrod.config.AbstractMethodTag;
import org.hotrod.config.TableTag;
import org.hotrod.eclipseplugin.treeview.FaceFactory.InvalidConfigurationItemException;

public class TableFace extends AbstractFace {

  public TableFace(final TableTag tableDAO) throws InvalidConfigurationItemException {
    super(tableDAO.getName(), tableDAO);
    for (AbstractMethodTag m : tableDAO.getMethods()) {
      AbstractFace leaf = FaceFactory.getMethodElement(m);
      super.addChild(leaf);
    }
  }

  @Override
  public String getIconPath() {
    // return "icons/table2-16.png";
    // return "icons/table1-16.png";

    // return "icons/table20-16.png";
    return "eclipse-plugin/icons/table21-16.png";
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
