package jdbc.generatedkeys.particularities;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

public class DatabaseParticularitiesFactory {

  public enum RetrievalType {
    RETURN_GENERATED_KEYS_1, REQUEST_COLUMNS_2, QUERY_RETURNING_COLUMNS_3
  };

  public static interface DatabaseParticularities {

    // General

    String getName();

    boolean combinesMultipleValues();

    // Sequences

    boolean combinesSequences();

    String inlineSequenceOnInsert(String sequenceName);

    // Identities

    boolean supportsMultipleIdentities();

    boolean combinesIdentities();

    // Defaults

    boolean combinesDefaults();

    // Retrieval

    RetrievalType getRetrievalType();

    String getReturningCoda(List<String> columnNames);

  }

  public static DatabaseParticularities getInstance(final DatabaseMetaData dm) throws SQLException {
    String name = dm.getDatabaseProductName();
    if (name.startsWith("Apache Derby")) {
      return new ApacheDerbyParticularities();
    }
    if (name.startsWith("DB2/")) {
      return new DB2Particularities();
    }
    if (name.startsWith("H2")) {
      return new H2Particularities();
    }
    if (name.startsWith("HSQL")) {
      return new HyperSQLParticularities();
    }
    if (name.startsWith("MySQL")) {
      return new MySQLParticularities();
    }
    if (name.startsWith("Oracle")) {
      return new OracleParticularities(dm);
    }
    if (name.startsWith("PostgreSQL")) {
      return new PostgreSQLParticularities();
    }
    if (name.startsWith("Adaptive Server Enterprise")) {
      return new SAPASEParticularities();
    }
    if (name.startsWith("Microsoft SQL Server")) {
      return new SQLServerParticularities();
    }
    throw new IllegalArgumentException("Database '" + name + "' not supported.");
  }

}
