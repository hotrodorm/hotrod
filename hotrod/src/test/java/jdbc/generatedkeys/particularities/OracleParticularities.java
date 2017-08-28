package jdbc.generatedkeys.particularities;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import org.hotrod.runtime.util.ListWriter;

import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.DatabaseParticularities;
import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.RetrievalType;

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
  public RetrievalType getRetrievalType() {
    // return RetrievalType.RETURN_GENERATED_KEYS_1; // fails - Returns ROWIDs
    return RetrievalType.REQUEST_COLUMNS_2; // works!
    // return RetrievalType.QUERY_RETURNING_COLUMNS_3; // fails
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
