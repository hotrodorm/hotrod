package org.hotrod.livesql.queries.select.tuples;

import java.util.List;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.SQLExpression;
import org.hotrod.livesql.metadata.TableOrView;

public class NestedCollection extends NestedTuples {

  public NestedCollection(String property, TableOrView<?> tableOrView, List<SQLExpression> sqlExpressions) {
    super(property, tableOrView, sqlExpressions);
  }

  // Extends SQLExpression

  @Override
  protected List<Expression> expand() {
    // TODO Auto-generated method stub
    return null;
  }

}
