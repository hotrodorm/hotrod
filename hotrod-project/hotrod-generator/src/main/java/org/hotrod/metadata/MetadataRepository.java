package org.hotrod.metadata;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.utils.identifiers.ObjectId;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

public interface MetadataRepository {

  TableDataSetMetadata findTableMetadata(ObjectId id);

  JdbcTable findJdbcTable(String name);

  JdbcTable findJdbcView(String name);

  JdbcColumn findJdbcColumn(JdbcTable t, String name);

  TableDataSetMetadata findViewMetadata(ObjectId id);

  VORegistry getVORegistry();

  DatabaseAdapter getAdapter();

  JdbcDatabase getJdbcDatabase();

  DatabaseLocation getLoc();

}
