package org.hotrod.livesql.metadata;

public interface ColumnRenamer {

  String newName(EntityColumnMetadata c);

}
