package org.hotrod.database.adapters;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.PropertyType;
import org.hotrod.exceptions.IdentitiesPostFetchNotSupportedException;
import org.hotrod.exceptions.SequencesNotSupportedException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.utils.JdbcTypes.JDBCType;
import org.hotrod.utils.JdbcUtils;
import org.hotrod.utils.identifiers.ObjectId;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.exception.CatalogNotSupportedException;
import org.nocrala.tools.database.tartarus.exception.InvalidCatalogException;
import org.nocrala.tools.database.tartarus.exception.InvalidSchemaException;
import org.nocrala.tools.database.tartarus.exception.SchemaNotSupportedException;

public class MariaDBAdapter extends DatabaseAdapter {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LogManager.getLogger(MariaDBAdapter.class);

  private MySQLAdapter mysqlAdaper;

  public MariaDBAdapter(final DatabaseMetaData dm) throws SQLException {
    super(dm);
    log.debug("init");
    this.mysqlAdaper = new MySQLAdapter(dm);
  }

  @Override
  public boolean supportsCatalog() {
    return this.mysqlAdaper.supportsCatalog();
  }

  @Override
  public boolean supportsSchema() {
    return this.mysqlAdaper.supportsSchema();
  }

  @Override
  public String getName() {
    return "MariaDB Adapter";
  }

  @Override
  public PropertyType getAdapterDefaultType(final ColumnMetadata m) throws UnresolvableDataTypeException {
    return this.mysqlAdaper.getAdapterDefaultType(m);
  }

  @Override
  public InsertIntegration getInsertIntegration() {
    return this.mysqlAdaper.getInsertIntegration();
  }

  @Override
  public boolean integratesUsingQuery() {
    return this.mysqlAdaper.integratesUsingQuery();
  }

  @Override
  public String renderInsertQueryColumn(final ColumnMetadata cm) {
    return this.mysqlAdaper.renderInsertQueryColumn(cm);
  }

  @Override
  public String renderSequencesPrefetch(final List<ColumnMetadata> sequenceGeneratedColumns)
      throws SequencesNotSupportedException {
    return this.mysqlAdaper.renderSequencesPrefetch(sequenceGeneratedColumns);
  }

  @Override
  public String renderSelectSequence(final ObjectId id) throws SequencesNotSupportedException {
    return this.mysqlAdaper.renderSelectSequence(id);
  }

  @Override
  public String renderInlineSequenceOnInsert(final ColumnMetadata cm) throws SequencesNotSupportedException {
    return this.mysqlAdaper.renderInlineSequenceOnInsert(cm);
  }

  @Override
  public String renderIdentitiesPostfetch(final List<ColumnMetadata> identityGeneratedColumns)
      throws IdentitiesPostFetchNotSupportedException {
    return this.mysqlAdaper.renderIdentitiesPostfetch(identityGeneratedColumns);
  }

  @Override
  public String renderAliasedSelectColumn(final StructuredColumnMetadata cm) {
    return this.mysqlAdaper.renderAliasedSelectColumn(cm);
  }

  @Override
  public String canonizeName(final String configName, final boolean quoted) {
    return this.mysqlAdaper.canonizeName(configName, quoted);
  }

  @Override
  public String renderSQLName(final String canonicalName) {
    return this.mysqlAdaper.renderSQLName(canonicalName);
  }

  @Override
  public boolean isTableIdentifier(final String jdbcName, final String name) {
    return this.mysqlAdaper.isTableIdentifier(jdbcName, name);
  }

  @Override
  public boolean isColumnIdentifier(final String jdbcName, final String name) {
    return this.mysqlAdaper.isColumnIdentifier(jdbcName, name);
  }

  @Override
  public String formatSchemaName(final String name) {
    return this.mysqlAdaper.formatSchemaName(name);
  }

  @Override
  public String createOrReplaceView(final String viewName, final String select) {
    return this.mysqlAdaper.createOrReplaceView(viewName, select);
  }

  @Override
  public String dropView(final String viewName) {
    return this.mysqlAdaper.dropView(viewName);
  }

  @Override
  public String formatJdbcTableName(final String tableName) {
    return this.mysqlAdaper.formatJdbcTableName(tableName);
  }

  @Override
  public boolean isSerial(final JdbcColumn c) {
    return this.mysqlAdaper.isSerial(c);
  }

  // Sorting

  @Override
  public boolean isCaseSensitiveSortableString(final ColumnMetadata cm) {
    return this.mysqlAdaper.isCaseSensitiveSortableString(cm);
  }

  @Override
  public String renderForCaseInsensitiveOrderBy(final ColumnMetadata cm) {
    return this.mysqlAdaper.renderForCaseInsensitiveOrderBy(cm);
  }

  @Override
  public UnescapedSQLCase getUnescapedSQLCase() {
    return this.mysqlAdaper.getUnescapedSQLCase();
  }

  @Override
  public String provideSampleValueFor(final JDBCType jdbcType) {
    return this.mysqlAdaper.provideSampleValueFor(jdbcType);
  }

  @Override
  public void setCurrentCatalogSchema(final Connection conn, final String catalog, final String schema)
      throws CatalogNotSupportedException, InvalidSchemaException, SQLException, InvalidCatalogException,
      SchemaNotSupportedException {
    if (catalog == null) {
      throw new InvalidCatalogException(JdbcUtils.getCatalogs(conn.getMetaData()));
    }
    if (schema != null) {
      throw new SchemaNotSupportedException();
    }
    JdbcUtils.runSQLStatement(conn, "use " + catalog);
  }

}
