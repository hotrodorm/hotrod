package org.hotrod.eclipseplugin.elements;

import org.hotrod.eclipseplugin.domain.Method;
import org.hotrod.eclipseplugin.domain.TableDAO;
import org.hotrod.eclipseplugin.elements.ElementFactory.InvalidConfigurationItemException;

public class TableElement extends TreeContainerElement {

  private TableDAO tableDAO;

  public TableElement(final TableDAO tableDAO) throws InvalidConfigurationItemException {
    super(tableDAO.getName(), false);
    this.tableDAO = tableDAO;
    for (Method m : this.tableDAO.getMethods()) {
      TreeLeafElement leaf = ElementFactory.getMethodElement(m);
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
  public String getLabel() {
    return super.getLabel() + " -- table";
  }

  @Override
  public String getTooltip() {
    return "Table " + super.getLabel();
  }

}
