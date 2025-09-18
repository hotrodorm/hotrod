package org.hotrod.utils;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.config.SelectMethodTag;
import org.hotrod.config.structuredcolumns.ColumnsProvider;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ErrorMessageException;
import org.hotrod.exceptions.FaultException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidSQLException;
import org.hotrod.generator.ColumnsRetriever;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.typesolver.UnresolvableDataTypeException;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;

public class ColumnsMetadataRetriever {

  // Constants

  private static final Logger log = Logger.getLogger(ColumnsMetadataRetriever.class.getName());

  // Properties

  private SelectMethodTag selectTag;
  @SuppressWarnings("unused")
  private DatabaseAdapter adapter;
  @SuppressWarnings("unused")
  private JdbcDatabase db;
  @SuppressWarnings("unused")
  private DatabaseLocation loc;
  private ColumnsProvider columnsProvider;
  private String entityPrefix;
  private String aliasPrefix;
  private ColumnsRetriever cr;

  // Constructor

  public ColumnsMetadataRetriever(final SelectMethodTag selectTag, final DatabaseAdapter adapter, final JdbcDatabase db,
      final DatabaseLocation loc, final ColumnsProvider columnsProvider, final String entityPrefix,
      final ColumnsPrefixGenerator columnsPrefixGenerator, final ColumnsRetriever cr) {
    log.fine("init");
    this.selectTag = selectTag;
    this.adapter = adapter;
    this.db = db;
    this.loc = loc;
    this.columnsProvider = columnsProvider;
    this.entityPrefix = entityPrefix;
    this.aliasPrefix = columnsPrefixGenerator.next();
    this.cr = cr;
  }

  // TODO: Nothing to do; just a marker === Phase 1

  public void prepareRetrieval() throws ErrorMessageException, FaultException {
    try {
      this.cr.phase1Structured(getKey(), this.selectTag, this.aliasPrefix, this.entityPrefix, this.columnsProvider,
          null);
    } catch (InvalidConfigurationFileException e) {
      throw new ErrorMessageException(e.getTag(), "Could not retrieve the meta data");
    } catch (InvalidSQLException e) {
      throw new FaultException(this.selectTag,
          "Could not retrieve meta data using the SQL query:\n" + e.getInvalidSQL() + "\n", e);
    }
  }

  // TODO: Nothing to do; just a marker === Phase 2

  public List<StructuredColumnMetadata> retrieve() throws FaultException, ErrorMessageException {
    try {
      return this.cr.phase2Structured(getKey(), this.selectTag, this.aliasPrefix, this.entityPrefix,
          this.columnsProvider);
    } catch (UnresolvableDataTypeException e) {
      throw new ErrorMessageException(this.selectTag,
          "Could not retrieve the meta data for column '" + e.getColumnMetadata().getName() + "'");
    } catch (InvalidConfigurationFileException e) {
      throw new ErrorMessageException(e.getTag(), "Could not retrieve the meta data: '" + e.getMessage());
    }
  }

  // TODO: Nothing to do; just a marker === END

  private String getKey() {
    return "t" + System.identityHashCode(this.selectTag) + "#" + System.identityHashCode(this.columnsProvider);
  }

}
