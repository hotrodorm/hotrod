package org.hotrod.eclipseplugin.treeview;

import org.hotrod.eclipseplugin.domain.Method;
import org.hotrod.eclipseplugin.domain.TableDAO;
import org.hotrod.eclipseplugin.treeview.FaceFactory.InvalidConfigurationItemException;

public class TableFace extends AbstractContainerFace {

  private TableDAO tableDAO;

  public TableFace(final TableDAO tableDAO) throws InvalidConfigurationItemException {
    super(tableDAO.getName(), false);
    this.tableDAO = tableDAO;
    for (Method m : this.tableDAO.getMethods()) {
      AbstractLeafFace leaf = FaceFactory.getMethodElement(m);
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
    return "Table " + super.getLabel();
  }

}
