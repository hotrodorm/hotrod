package jdbc.generatedkeys.particularities;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import org.hotrod.runtime.util.ListWriter;

import jdbc.generatedkeys.InsertRetriever;
import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.DatabaseParticularities;

public class OracleParticularities implements DatabaseParticularities {

  private boolean is12_1OrNewer;

  public OracleParticularities(final DatabaseMetaData dm) throws SQLException {
    this.is12_1OrNewer = this.isOracle12_1OrNewer(dm);
  }

  // General

  @Override
  public String getName() {
    return "Oracle";
  }

  @Override
  public boolean combinesMultipleValues() {
    return true;
  }

  // Sequences

  @Override
  public boolean combinesSequences() {
    return true;
  }

  @Override
  public String inlineSequenceOnInsert(final String sequenceName) {
    return sequenceName + ".nextval";
  }

  // Identities

  @Override
  public boolean supportsMultipleIdentities() {
    return false;
  }

  @Override
  public boolean combinesIdentities() {
    return this.is12_1OrNewer;
  }

  // Defaults

  @Override
  public boolean combinesDefaults() {
    return true;
  }

  // Retrieval

  @Override
  public InsertRetriever getInsertRetriever() {
    // return InsertRetriever.RETURN_GENERATED_KEYS_1; // fails (ROWIDs?)
    return InsertRetriever.RETURN_COLUMN_NAMES_2; // works
    // return InsertRetriever.RETURN_COLUMN_INDEXES_3; // works
    // return InsertRetriever.QUERY_RETURNING_COLUMNS_4; // fails
  }

  @Override
  public String getReturningCoda(final List<String> columnNames) {
    // throw new UnsupportedOperationException("Oracle cannot use coda to
    // retrieve generated keys.");
    ListWriter lw = new ListWriter(", ");
    for (String col : columnNames) {
      lw.add(col + " as :" + col);
    }
    return " returning " + lw.toString();
  }

  // Utilities

  private boolean isOracle12_1OrNewer(final DatabaseMetaData dm) throws SQLException {
    int majorVersion = dm.getDatabaseMajorVersion();
    int minorVersion = dm.getDatabaseMinorVersion();
    if (majorVersion < 12) {
      return false;
    }
    if (majorVersion > 12) {
      return true;
    }
    return minorVersion >= 1;
  }

}
