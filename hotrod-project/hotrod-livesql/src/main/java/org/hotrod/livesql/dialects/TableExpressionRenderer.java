package org.hotrod.livesql.dialects;

public interface TableExpressionRenderer {

  String renderNamedColumns(String[] columns);

}
