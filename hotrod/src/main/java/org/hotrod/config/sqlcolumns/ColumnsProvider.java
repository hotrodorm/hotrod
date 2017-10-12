package org.hotrod.config.sqlcolumns;

import java.sql.Connection;

import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.DaosTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.utils.ColumnsMetadataRetriever.InvalidSQLException;
import org.hotrod.utils.ColumnsPrefixGenerator;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;

public interface ColumnsProvider {

  void validate(final DaosTag daosTag, final HotRodConfigTag config, final HotRodFragmentConfigTag fragmentConfig,
      final boolean singleVOResult) throws InvalidConfigurationFileException;

  void validateAgainstDatabase(final HotRodGenerator generator) throws InvalidConfigurationFileException;

  void gatherMetadataPhase1(SelectMethodTag selectTag, DatabaseAdapter adapter, JdbcDatabase db, DatabaseLocation loc,
      SelectGenerationTag selectGenerationTag, ColumnsPrefixGenerator columnsPrefixGenerator, Connection conn1)
      throws InvalidSQLException;

  void gatherMetadataPhase2(final Connection conn2)
      throws InvalidSQLException, UncontrolledException, UnresolvableDataTypeException;

}
