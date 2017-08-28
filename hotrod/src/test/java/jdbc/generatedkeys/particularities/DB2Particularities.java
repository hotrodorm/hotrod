package jdbc.generatedkeys.particularities;

import java.util.List;

import jdbc.generatedkeys.InsertRetriever;
import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.DatabaseParticularities;

public class DB2Particularities implements DatabaseParticularities {

  // General

  @Override
  public String getName() {
    return "DB2";
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
    // return InsertRetriever.RETURN_GENERATED_KEYS_1; // works i
    return InsertRetriever.RETURN_COLUMN_NAMES_2; // works s+i+d
    // return InsertRetriever.RETURN_COLUMN_INDEXES_3; // works s+i+d
    // return InsertRetriever.QUERY_RETURNING_COLUMNS_4; // fails
    // return InsertRetriever.QUERY_SELECT_5; // works s+i+d
  }

  @Override
  public String getReturningCoda(final List<String> columnNames) {
    // throw new UnsupportedOperationException("In DB2 the generated keys cannot
    // be retrieved as queries.");
    return InsertRetriever.getCoda(columnNames);
  }

}
