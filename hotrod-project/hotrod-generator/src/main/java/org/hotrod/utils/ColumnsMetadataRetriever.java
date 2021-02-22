package org.hotrod.utils;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.config.structuredcolumns.ColumnsProvider;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidSQLException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.ColumnsRetriever;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;

public class ColumnsMetadataRetriever {

  // Constants

  private static final Logger log = LogManager.getLogger(ColumnsMetadataRetriever.class);

  // Properties

  private SelectMethodTag selectTag;
  @SuppressWarnings("unused")
  private DatabaseAdapter adapter;
  @SuppressWarnings("unused")
  private JdbcDatabase db;
  @SuppressWarnings("unused")
  private DatabaseLocation loc;
  @SuppressWarnings("unused")
  private SelectGenerationTag selectGenerationTag;
  private ColumnsProvider columnsProvider;
  private String entityPrefix;
  private String aliasPrefix;
  private ColumnsRetriever cr;

  // Constructor

  public ColumnsMetadataRetriever(final SelectMethodTag selectTag, final DatabaseAdapter adapter, final JdbcDatabase db,
      final DatabaseLocation loc, final SelectGenerationTag selectGenerationTag, final ColumnsProvider columnsProvider,
      final String entityPrefix, final ColumnsPrefixGenerator columnsPrefixGenerator, final ColumnsRetriever cr) {
    log.debug("init");
    this.selectTag = selectTag;
    this.adapter = adapter;
    this.db = db;
    this.loc = loc;
    this.selectGenerationTag = selectGenerationTag;
    this.columnsProvider = columnsProvider;
    this.entityPrefix = entityPrefix;
    this.aliasPrefix = columnsPrefixGenerator.next();
    this.cr = cr;
  }

  // TODO: Nothing to do; just a marker === Phase 1

  public void prepareRetrieval() throws InvalidSQLException, InvalidConfigurationFileException {
    this.cr.phase1Structured(getKey(), this.selectTag, this.aliasPrefix, this.entityPrefix, this.columnsProvider, null);
  }

  // TODO: Nothing to do; just a marker === Phase 2

  public List<StructuredColumnMetadata> retrieve() throws InvalidSQLException, UncontrolledException,
      UnresolvableDataTypeException, InvalidConfigurationFileException {
    return this.cr.phase2Structured(getKey(), this.selectTag, this.aliasPrefix, this.entityPrefix,
        this.columnsProvider);
  }

  // TODO: Nothing to do; just a marker === END

  private String getKey() {
    return "t" + System.identityHashCode(this.selectTag) + "#" + System.identityHashCode(this.columnsProvider);
  }

}
