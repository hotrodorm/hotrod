package jdbc.generatedkeys.particularities;

import java.util.List;

import org.hotrod.runtime.util.ListWriter;

import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.DatabaseParticularities;
import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.PostRetrievalType;

public class OracleParticularities implements DatabaseParticularities {

  @Override
  public String getName() {
    return "Oracle";
  }

  @Override
  public boolean canRetrieveSequencesAsPartOfTheInsert() {
    // Use insert (...) values (...) returning col1, col2, col3, ...
    return true;
  }

  @Override
  public String inlineSequenceOnInsert(final String sequenceName) {
    return "nextval('" + sequenceName + "')";
  }

  @Override
  public boolean canRetrieveIdentitiesAsPartOfTheInsert() {
    return true;
  }

  @Override
  public PostRetrievalType getPostRetrievalType() {
    return PostRetrievalType.AS_QUERY;
  }

  @Override
  public String getAsQueryCoda(final List<String> columnNames) {
    ListWriter lw = new ListWriter(", ");
    for (String col : columnNames) {
      lw.add(col);
    }
    return " returning " + lw.toString();
  }

}
