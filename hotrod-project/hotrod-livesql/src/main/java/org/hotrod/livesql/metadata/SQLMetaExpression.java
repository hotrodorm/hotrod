package org.hotrod.livesql.metadata;

import java.util.List;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.SQLExpression;
import org.hotrod.livesql.util.ToString;

public abstract class SQLMetaExpression extends SQLExpression {

  protected abstract List<Expression> expand();

  protected Expression getEmergingExpression() {
    // Wrapping columns cannot emerge
    return null;
  }

  public void log(ToString t) {
    t.printObject(this, this.getClass().getName());
  }

}
