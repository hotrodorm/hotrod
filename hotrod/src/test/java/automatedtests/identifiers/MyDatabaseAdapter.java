package automatedtests.identifiers;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import org.hotrod.config.HotRodConfigTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.PropertyType;
import org.hotrod.exceptions.IdentitiesPostFetchNotSupportedException;
import org.hotrod.exceptions.SequencesNotSupportedException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.utils.identifiers.Identifier;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;

public class MyDatabaseAdapter extends DatabaseAdapter {

  public MyDatabaseAdapter(HotRodConfigTag config, DatabaseMetaData dm) throws SQLException {
    super(config, dm);
    // TODO Auto-generated constructor stub
  }

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Override
  public String canonizeName(String configName, boolean quoted) {
    return configName.toUpperCase();
  }

  private static final String UNQUOTED_IDENTIFIER_PATTERN = "[A-Z][A-Z0-9_]*";

  @Override
  public String renderSQLName(final String canonicalName) {
    return canonicalName == null ? null
        : (canonicalName.matches(UNQUOTED_IDENTIFIER_PATTERN) ? canonicalName : super.quote(canonicalName));
  }

  @Override
  public boolean supportsCatalog() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean supportsSchema() {
    // TODO Auto-generated method stub
    return false;
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
  public String renderSelectSequence(Identifier sequence) throws SequencesNotSupportedException {
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
