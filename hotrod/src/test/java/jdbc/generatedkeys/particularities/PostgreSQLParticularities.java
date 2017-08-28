package jdbc.generatedkeys.particularities;

import java.util.List;

import org.hotrod.runtime.util.ListWriter;

import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.DatabaseParticularities;
import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.RetrievalType;

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
  public RetrievalType getRetrievalType() {
    return RetrievalType.QUERY_RETURNING_COLUMNS_3;
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
