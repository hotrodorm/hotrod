package jdbc.generatedkeys.particularities;

import java.util.List;

import jdbc.generatedkeys.InsertRetriever;
import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.DatabaseParticularities;

public class HyperSQLParticularities implements DatabaseParticularities {

  // General

  @Override
  public String getName() {
    return "HyperSQL (HSQL)";
  }

  @Override
  public boolean combinesMultipleValues() {
    return true;
  }

  // Sequences

  @Override
  public boolean combinesSequences() {
    return false;
  }

  @Override
  public String inlineSequenceOnInsert(final String sequenceName) {
    throw new UnsupportedOperationException("In HyperSQL inline sequences values on inserts cannot be retrieved.");
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
    return false;
  }

  // Retrieval

  @Override
  public InsertRetriever getInsertRetriever() {
    // return InsertRetriever.RETURN_GENERATED_KEYS_1; // works
    // return InsertRetriever.RETURN_COLUMN_NAMES_2; // fails
    return InsertRetriever.RETURN_COLUMN_INDEXES_3; // works
    // return InsertRetriever.QUERY_RETURNING_COLUMNS_4; // fails
  }

  @Override
  public String getReturningCoda(final List<String> columnNames) {
    // throw new UnsupportedOperationException("In HyperSQL the generated keys
    // cannot be retrieved as queries.");
    return InsertRetriever.getCoda(columnNames);
  }

}
