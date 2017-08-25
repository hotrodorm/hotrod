package jdbc.generatedkeys;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hotrod.utils.JdbcUtils;

import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory;
import jdbc.generatedkeys.particularities.DatabaseParticularitiesFactory.DatabaseParticularities;

public class GeneratedKeys {

  public static void main(final String[] args) throws SQLException, ClassNotFoundException {
    if (args.length != 4) {
      usage();
      System.exit(1);
    } else {
      String driverClassName = args[0];
      String url = args[1];
      String username = args[2];
      String password = args[3];
      System.out.println("Connecting to: " + url + " username=" + username + " (driver " + driverClassName + ")");

      Connection conn = null;
      try {
        conn = JdbcUtils.buildStandAloneConnection(driverClassName, url, username, password);
        DatabaseParticularities p = DatabaseParticularitiesFactory.getInstance(conn.getMetaData());

        getSequencesExample(conn, p);
        getIdentitiesExample(conn, p);
        getMixedKeysExample(conn, p);

      } finally {
        JdbcUtils.closeDbResources(conn);
      }

    }
  }

  // private static void retrieveIdentityKeys(final Connection conn, final
  // DatabaseParticularities p)
  // throws SQLException, ClassNotFoundException {
  // SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
  //
  // PreparedStatement st = null;
  // ResultSet rs = null;
  //
  // try {
  //
  // // DB2
  // // insert into test_identity1 (name) values (?)
  //
  // // PostgreSQL
  // // insert into test_identity1 (name) values (?) returning id
  //
  // String name = df.format(new Date());
  //
  // String sql = "insert into test_identity1 (name) values (?)";
  //
  // switch (p.getPostRetrievalType()) {
  //
  // case GET_GENERATED_KEYS:
  //
  // st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
  // st.setString(1, name);
  // int rows = st.executeUpdate();
  // System.out.println("inserted rows=" + rows);
  //
  // rs = st.getGeneratedKeys();
  // showKeys(rs);
  //
  // break;
  //
  // case AS_QUERY:
  //
  // List<String> keyColumns = new ArrayList<String>();
  // keyColumns.add("id");
  //
  // sql = sql + p.getAsQueryCoda(keyColumns);
  // st = conn.prepareStatement(sql);
  // st.setString(1, name);
  // rs = st.executeQuery();
  //
  // showKeys(rs);
  //
  // break;
  //
  // default:
  //
  // System.out.println("No Retrieval type specified.");
  //
  // }
  //
  // } finally {
  // JdbcUtils.closeDbResources(st, rs);
  // }
  // }

  private static void getSequencesExample(final Connection conn, final DatabaseParticularities p)
      throws SQLException, ClassNotFoundException {

    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    String tableName = "test_sequence1";

    Map<String, String> sequenceColumns = new LinkedHashMap<String, String>();
    sequenceColumns.put("id1", "gen_seq1");
    sequenceColumns.put("id2", "gen_seq2");

    List<String> identityColumns = new ArrayList<String>();

    Map<String, String> dataColumns = new LinkedHashMap<String, String>();
    dataColumns.put("name", "Chicago " + df.format(new Date()));

    System.out.println("=== Sequences ===");
    KeyRetriever.insert(conn, p, tableName, sequenceColumns, identityColumns, dataColumns);
  }

  private static void getIdentitiesExample(final Connection conn, final DatabaseParticularities p)
      throws SQLException, ClassNotFoundException {

    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    String tableName = "test_identity1";

    Map<String, String> sequenceColumns = new LinkedHashMap<String, String>();

    List<String> identityColumns = new ArrayList<String>();
    identityColumns.add("id");

    Map<String, String> dataColumns = new LinkedHashMap<String, String>();
    dataColumns.put("name", "Chicago " + df.format(new Date()));

    System.out.println("=== Identities ===");
    KeyRetriever.insert(conn, p, tableName, sequenceColumns, identityColumns, dataColumns);
  }

  private static void getMixedKeysExample(final Connection conn, final DatabaseParticularities p)
      throws SQLException, ClassNotFoundException {

    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    String tableName = "test_mixed1";

    Map<String, String> sequenceColumns = new LinkedHashMap<String, String>();
    sequenceColumns.put("extra_id1", "gen_seq1");
    sequenceColumns.put("extra_id2", "gen_seq2");

    List<String> identityColumns = new ArrayList<String>();
    identityColumns.add("id");

    Map<String, String> dataColumns = new LinkedHashMap<String, String>();
    dataColumns.put("name", "Chicago " + df.format(new Date()));

    System.out.println("=== Sequences & Identities ===");
    KeyRetriever.insert(conn, p, tableName, sequenceColumns, identityColumns, dataColumns);
  }

  // Utilities

  private static void usage() {
    System.out.println("Usage: GeneratedKeys <driverClassName> <url> <username> <password>");
  }

}
