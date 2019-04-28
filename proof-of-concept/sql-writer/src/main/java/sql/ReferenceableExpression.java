package sql;

import sql.expressions.ResultSetColumn;

public interface ReferenceableExpression extends ResultSetColumn {

  void renderTo(QueryWriter w);

}
