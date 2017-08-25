package jdbc.generatedkeys.particularities;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

public class DatabaseParticularitiesFactory {

  public enum PostRetrievalType {
    GET_GENERATED_KEYS, AS_QUERY
  };

  public static interface DatabaseParticularities {
    String getName();

    boolean canRetrieveSequencesAsPartOfTheInsert();
    
    String inlineSequenceOnInsert(String sequenceName);

    boolean canRetrieveIdentitiesAsPartOfTheInsert();
    
    String getAsQueryCoda(List<String> columnNames);

    PostRetrievalType getPostRetrievalType();

  }

  public static DatabaseParticularities getInstance(final DatabaseMetaData dm) throws SQLException {
    String name = dm.getDatabaseProductName();
    if (name.startsWith("DB2/")) {
      return new DB2Particularities();
    }
    if (name.startsWith("PostgreSQL")) {
      return new PostgreSQLParticularities();
    }
    throw new IllegalArgumentException("Database '" + name + "' not supported.");
  }

}
