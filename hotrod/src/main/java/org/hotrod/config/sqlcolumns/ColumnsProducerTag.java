package org.hotrod.config.sqlcolumns;

import java.sql.Connection;
import java.util.List;

import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.EnhancedSQLTag;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.utils.ColumnsMetadataRetriever;
import org.hotrod.utils.ColumnsMetadataRetriever.InvalidSQLException;
import org.hotrod.utils.ColumnsPrefixGenerator;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;

public abstract class ColumnsProducerTag extends EnhancedSQLTag {

  // Properties

  protected ColumnsMetadataRetriever columnsRetriever;
  private List<StructuredColumnMetadata> columnsMetadata;

  // Constructor

  protected ColumnsProducerTag(final String tagName) {
    super(tagName);
  }

  // Retrieve meta data

  public abstract void gatherMetadataPhase1(final SelectMethodTag selectTag, final DatabaseAdapter adapter,
      final JdbcDatabase db, final DatabaseLocation loc, final SelectGenerationTag selectGenerationTag,
      final ColumnsPrefixGenerator columnsPrefixGenerator, final Connection conn1) throws InvalidSQLException;

  // List<StructuredColumnMetadata>
  public abstract void gatherMetadataPhase2(final Connection conn2)
      throws InvalidSQLException, UncontrolledException, UnresolvableDataTypeException;

  // Utilities

  public final void prepareRetrieval(final SelectMethodTag selectTag, final DatabaseAdapter adapter,
      final JdbcDatabase db, final DatabaseLocation loc, final SelectGenerationTag selectGenerationTag,
      final ColumnsPrefixGenerator columnsPrefixGenerator, final Connection conn1) throws InvalidSQLException {
    this.columnsRetriever = new ColumnsMetadataRetriever(selectTag, adapter, db, loc, selectGenerationTag, this,
        columnsPrefixGenerator);
    this.columnsRetriever.prepareRetrieval(conn1);
  }

  public final void retrieve(final Connection conn2)
      throws InvalidSQLException, UncontrolledException, UnresolvableDataTypeException {
    this.columnsMetadata = this.columnsRetriever.retrieve(conn2);
  }

  public final List<StructuredColumnMetadata> getColumnsMetadata() {
    return columnsMetadata;
  }

  // Render SQL

}
