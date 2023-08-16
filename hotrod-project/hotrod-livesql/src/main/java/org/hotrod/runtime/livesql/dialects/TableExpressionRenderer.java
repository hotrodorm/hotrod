package org.hotrod.runtime.livesql.dialects;

public interface TableExpressionRenderer {

  String renderNamedColumns(String[] columns);

}
