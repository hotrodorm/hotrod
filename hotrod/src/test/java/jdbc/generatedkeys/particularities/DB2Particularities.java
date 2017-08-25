package jdbc.generatedkeys.particularities;

import java.util.List;

import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.DatabaseParticularities;
import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.PostRetrievalType;

public class DB2Particularities implements DatabaseParticularities {

  @Override
  public String getName() {
    return "DB2";
  }

  @Override
  public boolean canRetrieveSequencesAsPartOfTheInsert() {
    // DB2 does not provide a way of retrieving sequences values as part of the
    // insert.
    return false;
  }

  @Override
  public String inlineSequenceOnInsert(final String sequenceName) {
    throw new UnsupportedOperationException("In DB2 inline sequences values on inserts cannot be retrieved.");
  }

  @Override
  public boolean canRetrieveIdentitiesAsPartOfTheInsert() {
    return true;
  }

  @Override
  public String getAsQueryCoda(final List<String> columnNames) {
    throw new UnsupportedOperationException("In DB2 the generated keys cannot be retrieved as queries.");
  }

  @Override
  public PostRetrievalType getPostRetrievalType() {
    return PostRetrievalType.GET_GENERATED_KEYS;
  }

}
