package automatedtests.identifiers;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class MyDatabaseMetaData implements DatabaseMetaData {

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean allProceduresAreCallable() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean allTablesAreSelectable() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String getURL() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getUserName() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isReadOnly() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean nullsAreSortedHigh() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean nullsAreSortedLow() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean nullsAreSortedAtStart() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean nullsAreSortedAtEnd() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String getDatabaseProductName() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getDatabaseProductVersion() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getDriverName() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getDriverVersion() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getDriverMajorVersion() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getDriverMinorVersion() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean usesLocalFiles() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean usesLocalFilePerTable() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsMixedCaseIdentifiers() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean storesUpperCaseIdentifiers() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean storesLowerCaseIdentifiers() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean storesMixedCaseIdentifiers() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String getIdentifierQuoteString() throws SQLException {
    return "\"";
  }

  @Override
  public String getSQLKeywords() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getNumericFunctions() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getStringFunctions() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getSystemFunctions() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getTimeDateFunctions() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getSearchStringEscape() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getExtraNameCharacters() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean supportsAlterTableWithAddColumn() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsAlterTableWithDropColumn() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsColumnAliasing() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean nullPlusNonNullIsNull() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsConvert() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsConvert(int fromType, int toType) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsTableCorrelationNames() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsDifferentTableCorrelationNames() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsExpressionsInOrderBy() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsOrderByUnrelated() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsGroupBy() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsGroupByUnrelated() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsGroupByBeyondSelect() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsLikeEscapeClause() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsMultipleResultSets() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsMultipleTransactions() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsNonNullableColumns() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsMinimumSQLGrammar() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsCoreSQLGrammar() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsExtendedSQLGrammar() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsANSI92EntryLevelSQL() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsANSI92IntermediateSQL() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsANSI92FullSQL() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsIntegrityEnhancementFacility() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsOuterJoins() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsFullOuterJoins() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsLimitedOuterJoins() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String getSchemaTerm() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getProcedureTerm() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getCatalogTerm() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isCatalogAtStart() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String getCatalogSeparator() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean supportsSchemasInDataManipulation() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsSchemasInProcedureCalls() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsSchemasInTableDefinitions() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsSchemasInIndexDefinitions() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsCatalogsInDataManipulation() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsCatalogsInProcedureCalls() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsCatalogsInTableDefinitions() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsPositionedDelete() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsPositionedUpdate() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsSelectForUpdate() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsStoredProcedures() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsSubqueriesInComparisons() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsSubqueriesInExists() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsSubqueriesInIns() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsSubqueriesInQuantifieds() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsCorrelatedSubqueries() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsUnion() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsUnionAll() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int getMaxBinaryLiteralLength() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMaxCharLiteralLength() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMaxColumnNameLength() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMaxColumnsInGroupBy() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMaxColumnsInIndex() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMaxColumnsInOrderBy() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMaxColumnsInSelect() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMaxColumnsInTable() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMaxConnections() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMaxCursorNameLength() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMaxIndexLength() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMaxSchemaNameLength() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMaxProcedureNameLength() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMaxCatalogNameLength() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMaxRowSize() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int getMaxStatementLength() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMaxStatements() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMaxTableNameLength() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMaxTablesInSelect() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getMaxUserNameLength() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getDefaultTransactionIsolation() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean supportsTransactions() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern)
      throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern,
      String columnNamePattern) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types)
      throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ResultSet getSchemas() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ResultSet getCatalogs() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ResultSet getTableTypes() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern)
      throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern)
      throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern)
      throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable)
      throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ResultSet getCrossReference(String parentCatalog, String parentSchema, String parentTable,
      String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ResultSet getTypeInfo() throws SQLException {
    return new MyResultSet();
  }

  @Override
  public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate)
      throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean supportsResultSetType(int type) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean ownUpdatesAreVisible(int type) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean ownDeletesAreVisible(int type) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean ownInsertsAreVisible(int type) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean othersUpdatesAreVisible(int type) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean othersDeletesAreVisible(int type) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean othersInsertsAreVisible(int type) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean updatesAreDetected(int type) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean deletesAreDetected(int type) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean insertsAreDetected(int type) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsBatchUpdates() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types)
      throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Connection getConnection() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean supportsSavepoints() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsNamedParameters() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsMultipleOpenResults() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsGetGeneratedKeys() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern,
      String attributeNamePattern) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean supportsResultSetHoldability(int holdability) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int getResultSetHoldability() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getDatabaseMajorVersion() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getDatabaseMinorVersion() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getJDBCMajorVersion() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getJDBCMinorVersion() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getSQLStateType() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean locatorsUpdateCopy() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsStatementPooling() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public RowIdLifetime getRowIdLifetime() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public ResultSet getClientInfoProperties() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern,
      String columnNamePattern) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  //

  public class MyResultSet implements ResultSet {

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public boolean next() throws SQLException {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public void close() throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public boolean wasNull() throws SQLException {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public byte getByte(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public short getShort(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Date getDate(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Time getTime(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public byte getByte(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public short getShort(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public long getLong(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public float getFloat(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public double getDouble(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public byte[] getBytes(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Date getDate(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Time getTime(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Timestamp getTimestamp(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public InputStream getAsciiStream(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public InputStream getBinaryStream(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public void clearWarnings() throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public String getCursorName() throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Object getObject(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public int findColumn(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public Reader getCharacterStream(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Reader getCharacterStream(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public boolean isAfterLast() throws SQLException {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public boolean isFirst() throws SQLException {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public boolean isLast() throws SQLException {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public void beforeFirst() throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void afterLast() throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public boolean first() throws SQLException {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public boolean last() throws SQLException {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public int getRow() throws SQLException {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public boolean absolute(int row) throws SQLException {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public boolean relative(int rows) throws SQLException {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public boolean previous() throws SQLException {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public int getFetchDirection() throws SQLException {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public int getFetchSize() throws SQLException {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public int getType() throws SQLException {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public int getConcurrency() throws SQLException {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public boolean rowUpdated() throws SQLException {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public boolean rowInserted() throws SQLException {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public boolean rowDeleted() throws SQLException {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public void updateNull(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateByte(int columnIndex, byte x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateShort(int columnIndex, short x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateInt(int columnIndex, int x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateLong(int columnIndex, long x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateFloat(int columnIndex, float x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateDouble(int columnIndex, double x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateString(int columnIndex, String x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateDate(int columnIndex, Date x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateTime(int columnIndex, Time x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateObject(int columnIndex, Object x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateNull(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateBoolean(String columnLabel, boolean x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateByte(String columnLabel, byte x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateShort(String columnLabel, short x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateInt(String columnLabel, int x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateLong(String columnLabel, long x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateFloat(String columnLabel, float x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateDouble(String columnLabel, double x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateString(String columnLabel, String x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateBytes(String columnLabel, byte[] x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateDate(String columnLabel, Date x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateTime(String columnLabel, Time x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateObject(String columnLabel, Object x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void insertRow() throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateRow() throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void deleteRow() throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void refreshRow() throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void moveToInsertRow() throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public Statement getStatement() throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Ref getRef(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Blob getBlob(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Clob getClob(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Array getArray(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Ref getRef(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Blob getBlob(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Clob getClob(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Array getArray(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public URL getURL(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public URL getURL(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public void updateRef(int columnIndex, Ref x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateRef(String columnLabel, Ref x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateBlob(int columnIndex, Blob x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateBlob(String columnLabel, Blob x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateClob(int columnIndex, Clob x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateClob(String columnLabel, Clob x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateArray(int columnIndex, Array x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateArray(String columnLabel, Array x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public int getHoldability() throws SQLException {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public boolean isClosed() throws SQLException {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public void updateNString(int columnIndex, String nString) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateNString(String columnLabel, String nString) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public String getNString(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public String getNString(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateClob(int columnIndex, Reader reader) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateClob(String columnLabel, Reader reader) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
      // TODO Auto-generated method stub
      
    }

  }

}
