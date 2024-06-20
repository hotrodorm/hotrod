package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;

public class SHelper {

  public static void validateTableReferences(TableExpression te, TableReferences tableReferences, AliasGenerator ag) {
    te.validateTableReferences(tableReferences, ag);
  }

}
