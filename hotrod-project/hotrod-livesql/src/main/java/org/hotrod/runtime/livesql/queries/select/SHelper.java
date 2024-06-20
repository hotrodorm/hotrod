package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.metadata.Name;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;

public class SHelper {

  public static void validateTableReferences(TableExpression te, TableReferences tableReferences, AliasGenerator ag) {
    te.validateTableReferences(tableReferences, ag);
  }

  public static Name getName(TableExpression te) {
    return te.getName();
  }

  public static <R> CombinedSelectObject<R> getCombinedSelect(Select<R> select) {
    return select.getCombinedSelect();
  }

}
