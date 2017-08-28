package jdbc.generatedkeys.particularities;

import java.util.List;

import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.DatabaseParticularities;
import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.RetrievalType;

public class H2Particularities implements DatabaseParticularities {

  // General

  @Override
  public String getName() {
    return "H2";
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
    return sequenceName + ".nextval";
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
    throw new UnsupportedOperationException("In H2 the generated keys cannot be retrieved as queries.");
  }

}
