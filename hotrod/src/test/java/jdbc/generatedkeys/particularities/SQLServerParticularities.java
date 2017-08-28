package jdbc.generatedkeys.particularities;

import java.util.List;

import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.DatabaseParticularities;
import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.RetrievalType;

public class SQLServerParticularities implements DatabaseParticularities {

  // General

  @Override
  public String getName() {
    return "SQL Server";
  }

  @Override
  public boolean combinesMultipleValues() {
    return false;
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
    return false;
  }

  // Retrieval

  @Override
  public RetrievalType getRetrievalType() {
    // return RetrievalType.GET_GENERATED_KEYS1; // works for identities!
    return RetrievalType.REQUEST_COLUMNS_2; // works for identities!
  }

  @Override
  public String getReturningCoda(final List<String> columnNames) {
    throw new UnsupportedOperationException("Not supported.");
  }

}
