package org.hotrod.config.sqlcolumns;

import java.sql.Connection;
import java.util.List;

import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.DaosTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.config.dynamicsql.DynamicSQLPart.ParameterDefinitions;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.runtime.util.ListWriter;
import org.hotrod.utils.ColumnsMetadataRetriever;
import org.hotrod.utils.ColumnsMetadataRetriever.InvalidSQLException;
import org.hotrod.utils.ColumnsPrefixGenerator;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;

public abstract class ColumnsProducerTag extends AbstractConfigurationTag {

  // Properties

  protected ColumnsMetadataRetriever columnsRetriever;
  private List<StructuredColumnMetadata> columnsMetadata;

  // Constructor

  protected ColumnsProducerTag(final String tagName) {
    super(tagName);
  }

  // Validation

  public final void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final ParameterDefinitions parameters)
      throws InvalidConfigurationFileException {
    validate(daosTag, config, fragmentConfig);
  }

  public abstract void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig) throws InvalidConfigurationFileException;

  public abstract void validateAgainstDatabase(final HotRodGenerator generator)
      throws InvalidConfigurationFileException;

  // Retrieve meta data

  public abstract void gatherMetadataPhase1(final SelectMethodTag selectTag, final DatabaseAdapter adapter,
      final JdbcDatabase db, final DatabaseLocation loc, final SelectGenerationTag selectGenerationTag,
      final ColumnsPrefixGenerator columnsPrefixGenerator, final Connection conn1) throws InvalidSQLException;

  public abstract void gatherMetadataPhase2(final Connection conn2)
      throws InvalidSQLException, UncontrolledException, UnresolvableDataTypeException;

  // Utilities

  public final void prepareRetrieval(final SelectMethodTag selectTag, final DatabaseAdapter adapter,
      final JdbcDatabase db, final DatabaseLocation loc, final SelectGenerationTag selectGenerationTag,
      final ColumnsPrefixGenerator columnsPrefixGenerator, final Connection conn1) throws InvalidSQLException {
//    this.columnsRetriever = new ColumnsMetadataRetriever(selectTag, adapter, db, loc, selectGenerationTag, this,
//        columnsPrefixGenerator);
//    this.columnsRetriever.prepareRetrieval(conn1);
  }

  public final void retrieve(final Connection conn2)
      throws InvalidSQLException, UncontrolledException, UnresolvableDataTypeException {
    this.columnsMetadata = this.columnsRetriever.retrieve(conn2);
  }

  // List of columns

  public final List<StructuredColumnMetadata> getColumnsMetadata() {
    return columnsMetadata;
  }

  // Render SQL Angle

  public final String renderSQLAngle(final DatabaseAdapter adapter, final ColumnsProducerTag cp) {
    ListWriter w = new ListWriter(", ");
    for (StructuredColumnMetadata scm : cp.getColumnsMetadata()) {
      w.add(adapter.renderAliasedSelectColumn(scm));
    }
    return w.toString();
  }

}
