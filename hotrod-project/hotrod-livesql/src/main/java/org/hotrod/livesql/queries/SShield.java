package org.hotrod.livesql.queries;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.metadata.EntityColumn;

public abstract class SShield {

  private SShield() {
    // static class
  }

  public static void addSetter(UpdateObject update, EntityColumn c, Expression e) {
    update.addSetter(c, e);
  }

}
