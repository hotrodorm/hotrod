package org.hotrod.eclipseplugin.treeview;

import org.apache.log4j.Logger;
import org.hotrod.config.AbstractMethodTag;
import org.hotrod.config.TableTag;
import org.hotrod.eclipseplugin.treeview.FaceFactory.InvalidConfigurationItemException;

public class TableFace extends AbstractFace {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(TableFace.class);

  public TableFace(final TableTag tableDAO) throws InvalidConfigurationItemException {
    super(tableDAO.getName(), tableDAO);
    for (AbstractMethodTag<?> m : tableDAO.getMethods()) {
      AbstractFace leaf = FaceFactory.getMethodElement(m);
      // log.info("leaf=" + leaf);
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
