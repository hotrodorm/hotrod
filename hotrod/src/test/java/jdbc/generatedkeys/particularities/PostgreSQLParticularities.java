package jdbc.generatedkeys.particularities;

import java.util.List;

import org.hotrod.runtime.util.ListWriter;

import jdbc.generatedkeys.InsertRetriever;
import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.DatabaseParticularities;

public class PostgreSQLParticularities implements DatabaseParticularities {

  // General

  @Override
  public String getName() {
    return "PostgreSQL";
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
    return "nextval('" + sequenceName + "')";
  }

  // Identities

  @Override
  public boolean supportsMultipleIdentities() {
    return true;
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
    // Maybe it's useful: this options returns the whole row, not just the keys.
    // return InsertRetriever.RETURN_GENERATED_KEYS_1; // fails
    return InsertRetriever.RETURN_COLUMN_NAMES_2; // works
    // return InsertRetriever.RETURN_COLUMN_INDEXES_3; // fails
    // return InsertRetriever.QUERY_RETURNING_COLUMNS_4; // works
  }

  @Override
  public String getReturningCoda(final List<String> columnNames) {
    ListWriter lw = new ListWriter(", ");
    for (String col : columnNames) {
      lw.add(col);
    }
    return " returning " + lw.toString();
  }

}
