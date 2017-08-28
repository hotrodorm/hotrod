package jdbc.generatedkeys;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hotrod.utils.JdbcUtils;

import jdbc.generatedkeys.InsertRetriever.DataColumn;
import jdbc.generatedkeys.InsertRetriever.DefaultColumn;
import jdbc.generatedkeys.InsertRetriever.IdentityColumn;
import jdbc.generatedkeys.InsertRetriever.SequenceColumn;
import jdbc.generatedkeys.InsertRetriever.Table;
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

        if (p.combinesIdentities() && p.supportsMultipleIdentities() && p.combinesMultipleValues()) {
          getMultipleIdentitiesExample(conn, p);
        }

        if (p.combinesDefaults()) {
          getDefaultExample(conn, p);
        }

        if (p.combinesDefaults() && p.combinesMultipleValues()) {
          getMultipleDefaultsExample(conn, p);
        }

        if (p.combinesSequences() && p.combinesIdentities() && p.combinesMultipleValues()) {
          getSequencesIdentityExample(conn, p);
        }

        if (p.combinesSequences() && p.combinesIdentities() && p.combinesDefaults() && p.combinesMultipleValues()) {
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

    Table t = new Table("test_sequence1", //
        new SequenceColumn("id1", "gen_seq1"), //
        new DataColumn("name", "Chicago " + df.format(new Date())));

    System.out.println("=== Single Sequence ===");
    p.getInsertRetriever().insert(conn, p, t);

  }

  private static void getMultipleSequencesExample(final Connection conn, final DatabaseParticularities p)
      throws SQLException, ClassNotFoundException {

    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    Table t = new Table("test_sequence2", //
        new SequenceColumn("id1", "gen_seq1"), //
        new SequenceColumn("id2", "gen_seq2"), //
        new DataColumn("name", "Chicago " + df.format(new Date())));

    System.out.println("=== Multiple Sequences ===");
    p.getInsertRetriever().insert(conn, p, t);

  }

  // Identity

  private static void getIdentityExample(final Connection conn, final DatabaseParticularities p)
      throws SQLException, ClassNotFoundException {

    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    Table t = new Table("test_identity1", //
        new IdentityColumn("id"), //
        new DataColumn("name", "Chicago " + df.format(new Date())));

    System.out.println("=== Single Identity ===");
    p.getInsertRetriever().insert(conn, p, t);

  }

  private static void getMultipleIdentitiesExample(final Connection conn, final DatabaseParticularities p)
      throws SQLException, ClassNotFoundException {

    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    Table t = new Table("test_identity2", //
        new IdentityColumn("id"), //
        new IdentityColumn("id2"), //
        new DataColumn("name", "Chicago " + df.format(new Date())));

    System.out.println("=== Multiple Identities ===");
    p.getInsertRetriever().insert(conn, p, t);

  }

  // Default

  private static void getDefaultExample(final Connection conn, final DatabaseParticularities p)
      throws SQLException, ClassNotFoundException {

    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    Table t = new Table("test_default1", //
        new DataColumn("name", "Chicago " + df.format(new Date())), //
        new DefaultColumn("price") //
    );

    System.out.println("=== Single Default ===");
    p.getInsertRetriever().insert(conn, p, t);

  }

  private static void getMultipleDefaultsExample(final Connection conn, final DatabaseParticularities p)
      throws SQLException, ClassNotFoundException {

    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    Table t = new Table("test_default2", //
        new DataColumn("name", "Chicago " + df.format(new Date())), //
        new DefaultColumn("price"), //
        new DefaultColumn("branch_id") //
    );

    System.out.println("=== Multiple Defaults ===");
    p.getInsertRetriever().insert(conn, p, t);

  }

  // Sequence & Identity

  private static void getSequencesIdentityExample(final Connection conn, final DatabaseParticularities p)
      throws SQLException, ClassNotFoundException {

    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    Table t = new Table("test_mixed1", //
        new IdentityColumn("id"), //
        new DataColumn("name", "Chicago " + df.format(new Date())), //
        new SequenceColumn("extra_id1", "gen_seq1"), //
        new SequenceColumn("extra_id2", "gen_seq2") //
    );

    System.out.println("=== Sequences & Identity ===");
    p.getInsertRetriever().insert(conn, p, t);

  }

  // Sequences, Identity, and Defaults

  private static void getSequencesIdentityDefaultsExample(final Connection conn, final DatabaseParticularities p)
      throws SQLException, ClassNotFoundException {

    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    Table t = new Table("test_seq_ide_def1", //
        new IdentityColumn("id"), //
        new DataColumn("name", "Chicago " + df.format(new Date())), //
        new SequenceColumn("extra_id1", "gen_seq1"), //
        new SequenceColumn("extra_id2", "gen_seq2"), //
        new DefaultColumn("price"), //
        new DefaultColumn("branch_id") //
    );

    System.out.println("=== Sequences, Identity, and Defaults ===");
    p.getInsertRetriever().insert(conn, p, t);

  }

  // Utilities

  private static void usage() {
    System.out.println("Usage: GeneratedKeys <driverClassName> <url> <username> <password>");
  }

}
