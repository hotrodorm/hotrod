package org.hotrod.runtime.interfaces;

import org.hotrodorm.hotrod.utils.ListWriter;

public class OrderByRenderer {

  public static String render(final OrderBy[] orderBies) {
    ListWriter lw = new ListWriter(", ");
    for (OrderBy ob : orderBies) {
      lw.add((ob.getTableName() != null ? ob.getTableName() + "." : "") + ob.getColumnName()
          + (ob.isAscending() ? "" : " desc"));
    }
    return lw.getCount() == 0 ? "" : " order by " + lw.toString();
  }

}
