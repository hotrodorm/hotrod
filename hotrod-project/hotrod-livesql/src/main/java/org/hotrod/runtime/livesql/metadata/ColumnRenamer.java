package org.hotrod.runtime.livesql.metadata;

public interface ColumnRenamer {

  String newName(Column c);

}
