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

        if (p.combinesSequences()) {
          getSequenceExample(conn, p);
        }

        if (p.combinesSequences() && p.combinesMultipleValues()) {
          getMultipleSequencesExample(conn, p);
        }

        if (p.combinesIdentities()) {
          getIdentityExample(conn, p);
        }

        if (p.combinesIdentities() && p.supportsMultipleIdentities()
            && p.combinesMultipleValues()) {
          getMultipleIdentitiesExample(conn, p);
        }

        if (p.combinesDefaults()) {
          getDefaultExample(conn, p);
        }

        if (p.combinesDefaults() && p.combinesMultipleValues()) {
          getMultipleDefaultsExample(conn, p);
        }

        if (p.combinesSequences() && p.combinesIdentities()
            && p.combinesMultipleValues()) {
          getSequencesIdentityExample(conn, p);
        }

        if (p.combinesSequences() && p.combinesIdentities()
            && p.combinesDefaults() && p.combinesMultipleValues()) {
          getSequencesIdentityDefaultsExample(conn, p);
        }

      } finally {
        JdbcUtils.closeDbResources(conn);
      }

    }
  }

  // Sequence

  private static void getSequenceExample(final Connection conn, final DatabaseParticularities p)
      throws SQLException, ClassNotFoundException {

    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    String tableName = "test_sequence1";

    Map<String, String> sequenceColumns = new LinkedHashMap<String, String>();
    sequenceColumns.put("id1", "gen_seq1");

    List<String> identityColumns = new ArrayList<String>();

    List<String> defaultColumns = new ArrayList<String>();

    Map<String, String> dataColumns = new LinkedHashMap<String, String>();
    dataColumns.put("name", "Chicago " + df.format(new Date()));

    System.out.println("=== Single Sequence ===");
    KeyRetriever.insert(conn, p, tableName, sequenceColumns, identityColumns, defaultColumns, dataColumns);
  }

  private static void getMultipleSequencesExample(final Connection conn, final DatabaseParticularities p)
      throws SQLException, ClassNotFoundException {

    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    String tableName = "test_sequence2";

    Map<String, String> sequenceColumns = new LinkedHashMap<String, String>();
    sequenceColumns.put("id1", "gen_seq1");
    sequenceColumns.put("id2", "gen_seq2");

    List<String> identityColumns = new ArrayList<String>();

    List<String> defaultColumns = new ArrayList<String>();

    Map<String, String> dataColumns = new LinkedHashMap<String, String>();
    dataColumns.put("name", "Chicago " + df.format(new Date()));

    System.out.println("=== Multiple Sequences ===");
    KeyRetriever.insert(conn, p, tableName, sequenceColumns, identityColumns, defaultColumns, dataColumns);
  }

  // Identity

  private static void getIdentityExample(final Connection conn, final DatabaseParticularities p)
      throws SQLException, ClassNotFoundException {

    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    String tableName = "test_identity1";

    Map<String, String> sequenceColumns = new LinkedHashMap<String, String>();

    List<String> identityColumns = new ArrayList<String>();
    identityColumns.add("id");

    List<String> defaultColumns = new ArrayList<String>();

    Map<String, String> dataColumns = new LinkedHashMap<String, String>();
    dataColumns.put("name", "Chicago " + df.format(new Date()));

    System.out.println("=== Single Identity ===");
    KeyRetriever.insert(conn, p, tableName, sequenceColumns, identityColumns, defaultColumns, dataColumns);
  }

  private static void getMultipleIdentitiesExample(final Connection conn, final DatabaseParticularities p)
      throws SQLException, ClassNotFoundException {

    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    String tableName = "test_identity2";

    Map<String, String> sequenceColumns = new LinkedHashMap<String, String>();

    List<String> identityColumns = new ArrayList<String>();
    identityColumns.add("id");
    identityColumns.add("id2");

    List<String> defaultColumns = new ArrayList<String>();

    Map<String, String> dataColumns = new LinkedHashMap<String, String>();
    dataColumns.put("name", "Chicago " + df.format(new Date()));

    System.out.println("=== Multiple Identities ===");
    KeyRetriever.insert(conn, p, tableName, sequenceColumns, identityColumns, defaultColumns, dataColumns);
  }

  // Default

  private static void getDefaultExample(final Connection conn, final DatabaseParticularities p)
      throws SQLException, ClassNotFoundException {

    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    String tableName = "test_default1";

    Map<String, String> sequenceColumns = new LinkedHashMap<String, String>();

    List<String> identityColumns = new ArrayList<String>();

    List<String> defaultColumns = new ArrayList<String>();
    defaultColumns.add("price");

    Map<String, String> dataColumns = new LinkedHashMap<String, String>();
    dataColumns.put("name", "Chicago " + df.format(new Date()));

    System.out.println("=== Single Default ===");
    KeyRetriever.insert(conn, p, tableName, sequenceColumns, identityColumns, defaultColumns, dataColumns);
  }

  private static void getMultipleDefaultsExample(final Connection conn, final DatabaseParticularities p)
      throws SQLException, ClassNotFoundException {

    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    String tableName = "test_default2";

    Map<String, String> sequenceColumns = new LinkedHashMap<String, String>();

    List<String> identityColumns = new ArrayList<String>();

    List<String> defaultColumns = new ArrayList<String>();
    defaultColumns.add("price");
    defaultColumns.add("branch_id");

    Map<String, String> dataColumns = new LinkedHashMap<String, String>();
    dataColumns.put("name", "Chicago " + df.format(new Date()));

    System.out.println("=== Multiple Defaults ===");
    KeyRetriever.insert(conn, p, tableName, sequenceColumns, identityColumns, defaultColumns, dataColumns);
  }

  // Sequence & Identity

  private static void getSequencesIdentityExample(final Connection conn, final DatabaseParticularities p)
      throws SQLException, ClassNotFoundException {

    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    String tableName = "test_mixed1";

    Map<String, String> sequenceColumns = new LinkedHashMap<String, String>();
    sequenceColumns.put("extra_id1", "gen_seq1");
    sequenceColumns.put("extra_id2", "gen_seq2");

    List<String> identityColumns = new ArrayList<String>();
    identityColumns.add("id");

    List<String> defaultColumns = new ArrayList<String>();

    Map<String, String> dataColumns = new LinkedHashMap<String, String>();
    dataColumns.put("name", "Chicago " + df.format(new Date()));

    System.out.println("=== Sequences & Identity ===");
    KeyRetriever.insert(conn, p, tableName, sequenceColumns, identityColumns, defaultColumns, dataColumns);
  }

  // Sequences, Identity, and Defaults

  private static void getSequencesIdentityDefaultsExample(final Connection conn, final DatabaseParticularities p)
      throws SQLException, ClassNotFoundException {

    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    String tableName = "test_seq_ide_def1";

    Map<String, String> sequenceColumns = new LinkedHashMap<String, String>();
    sequenceColumns.put("extra_id1", "gen_seq1");
    sequenceColumns.put("extra_id2", "gen_seq2");

    List<String> identityColumns = new ArrayList<String>();
    identityColumns.add("id");

    List<String> defaultColumns = new ArrayList<String>();
    defaultColumns.add("price");
    defaultColumns.add("branch_id");

    Map<String, String> dataColumns = new LinkedHashMap<String, String>();
    dataColumns.put("name", "Chicago " + df.format(new Date()));

    System.out.println("=== Sequences, Identity, and Defaults ===");
    KeyRetriever.insert(conn, p, tableName, sequenceColumns, identityColumns, defaultColumns, dataColumns);
  }

  // Utilities

  private static void usage() {
    System.out.println("Usage: GeneratedKeys <driverClassName> <url> <username> <password>");
  }

}
