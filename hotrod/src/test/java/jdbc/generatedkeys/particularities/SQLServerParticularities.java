package jdbc.generatedkeys.particularities;

import java.util.List;

import jdbc.generatedkeys.InsertRetriever;
import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.DatabaseParticularities;

public class SQLServerParticularities implements DatabaseParticularities {

  // General

  @Override
  public String getName() {
    return "SQL Server";
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
    return "next value for " + sequenceName;
  }

  // Identities

  @Override
  public boolean supportsMultipleIdentities() {
    return false;
  }

  @Override
  public boolean combinesIdentities() {
    return true;
  }

  // Defaults

  @Override
  public boolean combinesDefaults() {
    return true;
  }

  // Retrieval

  @Override
  public InsertRetriever getInsertRetriever() {

    // works only for identities
    // return InsertRetriever.RETURN_GENERATED_KEYS_1; // works i

    // works only for identities
    // return InsertRetriever.RETURN_COLUMN_NAMES_2; // works i

    // works only for identities
    // return InsertRetriever.RETURN_COLUMN_INDEXES_3; // works i

    // return InsertRetriever.QUERY_RETURNING_COLUMNS_4; // crashes

    // return InsertRetriever.QUERY_SELECT_5; // crashes

    return InsertRetriever.QUERY_OUTPUT_6; // works s+i+d

  }

  @Override
  public String getReturningCoda(final List<String> columnNames) {
    // throw new UnsupportedOperationException("Not supported.");
    return InsertRetriever.getCoda(columnNames);
  }

}
