package jdbc.generatedkeys.particularities;

import java.util.List;

import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.DatabaseParticularities;
import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.RetrievalType;

public class MySQLParticularities implements DatabaseParticularities {

  // General

  @Override
  public String getName() {
    return "MySQL";
  }

  @Override
  public boolean combinesMultipleValues() {
    return false;
  }

  // Sequences

  @Override
  public boolean combinesSequences() {
    return false;
  }

  @Override
  public String inlineSequenceOnInsert(final String sequenceName) {
    throw new UnsupportedOperationException("MySQL does not have sequences.");
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
    return RetrievalType.REQUEST_COLUMNS_2;
  }

  @Override
  public String getReturningCoda(final List<String> columnNames) {
    throw new UnsupportedOperationException("Not supported.");
  }

}
