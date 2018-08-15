package automatedtests.identifiers;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.PropertyType;
import org.hotrod.exceptions.IdentitiesPostFetchNotSupportedException;
import org.hotrod.exceptions.SequencesNotSupportedException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.utils.identifiers.ObjectId;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;

public class TestDatabaseAdapter extends DatabaseAdapter {

  public static enum CaseSensitiveness {

    UPPERCASE("[A-Z][A-Z0-9_]*"), // Oracle, DB2, H2, Derby, HyperSQL
    LOWERCASE("[a-z][a-z0-9_]*"), // PostgreSQL
    INSENSITIVE("[A-Za-z][A-Za-z0-9_]*"), // SQL Server
    SENSITIVE("[A-Za-z][A-Za-z0-9_]*"); // MariaDB, MySQL, Sybase ASE

    // private static final String UNQUOTED_IDENTIFIER_PATTERN =
    // "[A-Z][A-Z0-9_]*";

    private String unquotedPattern;

    private CaseSensitiveness(final String unquotedPattern) {
      this.unquotedPattern = unquotedPattern;
    }

    public String canonize(final String commonName, final boolean quoted) {
      if (quoted) {
        return commonName;
      }
      if (commonName == null) {
        return null;
      }
      switch (this) {
      case UPPERCASE:
        return commonName.toUpperCase();
      case LOWERCASE:
        return commonName.toLowerCase();
      case INSENSITIVE:
        return commonName.toLowerCase();
      default: // SENSITIVE
        return commonName;
      }
    }

    public String commonize(final String canonicalName, final String initialQuote, final String endQuote) {
      if (canonicalName == null) {
        return null;
      }
      switch (this) {
      case UPPERCASE:
        return canonicalName.matches(this.unquotedPattern) ? canonicalName.toLowerCase()
            : initialQuote + canonicalName + endQuote;
      case LOWERCASE:
        return canonicalName.matches(this.unquotedPattern) ? canonicalName : initialQuote + canonicalName + endQuote;
      case INSENSITIVE:
        return canonicalName.matches(this.unquotedPattern) ? canonicalName.toLowerCase()
            : initialQuote + canonicalName + endQuote;
      default: // SENSITIVE
        return canonicalName.matches(this.unquotedPattern) ? canonicalName : initialQuote + canonicalName + endQuote;
      }
    }

  }

  public static enum CatalogSchemaSupport {
    NONE(false, false), //
    CATALOG_ONLY(true, false), //
    SCHEMA_ONLY(false, true), //
    CATALOG_AND_SCHEMA(true, true) //
    ;

    private boolean supportCatalog;
    private boolean supportsSchema;

    private CatalogSchemaSupport(boolean supportCatalog, boolean supportsSchema) {
      this.supportCatalog = supportCatalog;
      this.supportsSchema = supportsSchema;
    }

    public boolean supportCatalog() {
      return supportCatalog;
    }

    public boolean supportsSchema() {
      return supportsSchema;
    }

  }

  private CaseSensitiveness caseSensitiveness;
  private CatalogSchemaSupport catalogSchemaSupport;

  public TestDatabaseAdapter(final DatabaseMetaData dm, final CaseSensitiveness caseSensitiveness,
      final CatalogSchemaSupport catalogSchemaSupport) throws SQLException {
    super(dm);
    this.caseSensitiveness = caseSensitiveness;
    if (this.caseSensitiveness == null) {
      throw new IllegalArgumentException("'caseSensitiveness' should not be null");
    }
    this.catalogSchemaSupport = catalogSchemaSupport;
    if (this.catalogSchemaSupport == null) {
      throw new IllegalArgumentException("'catalogSchemaSupport' should not be null");
    }
    super.identifierQuoteString = "'";
  }

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Override
  public String canonizeName(final String commonName, final boolean quoted) {
    return this.caseSensitiveness.canonize(commonName, quoted);
  }

  @Override
  public String renderSQLName(final String canonicalName) {
    return this.caseSensitiveness.commonize(canonicalName, super.identifierQuoteString, super.identifierQuoteString);
  }

  @Override
  public boolean supportsCatalog() {
    return this.catalogSchemaSupport.supportCatalog();
  }

  @Override
  public boolean supportsSchema() {
    return this.catalogSchemaSupport.supportsSchema();
  }

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PropertyType getAdapterDefaultType(ColumnMetadata cm) throws UnresolvableDataTypeException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public InsertIntegration getInsertIntegration() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean integratesUsingQuery() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String renderInsertQueryColumn(ColumnMetadata cm) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String renderSequencesPrefetch(List<ColumnMetadata> sequenceGeneratedColumns)
      throws SequencesNotSupportedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String renderSelectSequence(ObjectId id) throws SequencesNotSupportedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String renderInlineSequenceOnInsert(ColumnMetadata cm) throws SequencesNotSupportedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String renderIdentitiesPostfetch(List<ColumnMetadata> identityGeneratedColumns)
      throws IdentitiesPostFetchNotSupportedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isTableIdentifier(String jdbcName, String name) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isColumnIdentifier(String jdbcName, String name) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String formatSchemaName(String name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String createOrReplaceView(String viewName, String select) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String dropView(String viewName) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String formatJdbcTableName(String tableName) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isSerial(JdbcColumn c) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String renderAliasedSelectColumn(StructuredColumnMetadata cm) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public UnescapedSQLCase getUnescapedSQLCase() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String renderForCaseInsensitiveOrderBy(ColumnMetadata cm) {
    // TODO Auto-generated method stub
    return null;
  }

}
