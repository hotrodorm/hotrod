package org.hotrod.utils;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.runtime.util.ListWriter;
import org.hotrod.utils.identifiers.DbIdentifier;
import org.hotrod.utils.identifiers.Identifier;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcKey;
import org.nocrala.tools.database.tartarus.core.JdbcKeyColumn;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

public class DbUtils {

  public static boolean equals(final JdbcKey k1, final JdbcKey k2) {
    if (k1 == null || k2 == null) {
      return false;
    }
    if (k1.getKeyColumns().size() != k2.getKeyColumns().size()) {
      return false;
    }
    for (int i = 0; i < k1.getKeyColumns().size(); i++) {
      JdbcKeyColumn uiCol = k1.getKeyColumns().get(i);
      JdbcKeyColumn pkCol = k2.getKeyColumns().get(i);
      if (!uiCol.getColumn().getName().equals(pkCol.getColumn().getName())
          || uiCol.getColumnSequence() != pkCol.getColumnSequence()) {
        return false;
      }
    }
    return true;
  }



  public static JdbcTable findTable(final JdbcDatabase db, final String name, final DatabaseAdapter adapter) {
    for (JdbcTable t : db.getTables()) {
      if (adapter.isTableIdentifier(t.getName(), name)) {
        return t;
      }
    }
    return null;
  }

  public static JdbcColumn findColumn(final JdbcTable t, final String name, final DatabaseAdapter adapter) {
    for (JdbcColumn c : t.getColumns()) {
      if (adapter.isColumnIdentifier(c.getName(), name)) {
        return c;
      }
    }
    return null;
  }

}
