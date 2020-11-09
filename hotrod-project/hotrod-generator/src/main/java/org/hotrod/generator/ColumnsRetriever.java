package org.hotrod.generator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectGenerationTag.SelectStrategy;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.config.structuredcolumns.ColumnsProvider;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.exceptions.InvalidSQLException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.SelectMethodMetadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;

public interface ColumnsRetriever extends AutoCloseable {

  void phase1Flat(String key, SelectMethodTag tag, SelectMethodMetadata sm)
      throws InvalidSQLException, InvalidConfigurationFileException;

  void phase1Structured(String key, SelectMethodTag selectTag, ColumnsProvider columnsProvider, SelectMethodMetadata sm)
      throws InvalidSQLException;

  List<ColumnMetadata> phase2Flat(String key)
      throws SQLException, UnresolvableDataTypeException, InvalidIdentifierException;

  List<StructuredColumnMetadata> phase2Structured(String key, SelectMethodTag selectTag, String aliasPrefix,
      String entityPrefix, ColumnsProvider columnsProvider)
      throws UnresolvableDataTypeException, InvalidConfigurationFileException, UncontrolledException;

  static ColumnsRetriever getInstance(final HotRodConfigTag config, final DatabaseLocation dloc,
      final DatabaseAdapter adapter, final JdbcDatabase db, final Connection conn) throws SQLException {
    SelectGenerationTag selectGenerationTag = config.getGenerators().getSelectedGeneratorTag().getSelectGeneration();
    if (selectGenerationTag.getStrategy() == SelectStrategy.RESULT_SET) {
      return new ResultSetColumnsRetriever(config, dloc, selectGenerationTag, adapter, db, conn);
    } else {
      return new CreateViewColumnsRetriever(config, dloc, selectGenerationTag, adapter, db, conn);
    }
  }

}