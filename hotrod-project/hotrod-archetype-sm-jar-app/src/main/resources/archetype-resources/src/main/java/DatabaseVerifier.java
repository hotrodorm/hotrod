package ${package};

import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.nocrala.tools.database.sentinel.Snapshot;
import org.nocrala.tools.database.sentinel.SnapshotComparator;
import org.nocrala.tools.database.tartarus.exception.ReaderException;

@Component
public class DatabaseVerifier {

  private static final String SENTINEL_SNAPSHOT_RESOURCE = "/database-snapshot.sentinel";

  @Autowired
  private DataSource dataSource;
  
  public boolean verify() throws CouldNotVerifyDatabaseException {

    try {

      // 1. Load the snapshot

      Snapshot fs = null;
      try (BufferedInputStream bis = new BufferedInputStream(
          DatabaseVerifier.class.getResourceAsStream(SENTINEL_SNAPSHOT_RESOURCE))) {
        fs = Snapshot.readFrom(bis);
      } catch (ClassNotFoundException e) {
        throw new CouldNotVerifyDatabaseException("Could not load database snapshot", e);
      } catch (IOException e) {
        throw new CouldNotVerifyDatabaseException("Could not load database snapshot", e);
      }

      // 2. Display snapshot's summary

      System.out.println("Baseline snapshot: " + SENTINEL_SNAPSHOT_RESOURCE);
      fs.renderHeader().stream().forEach(h -> System.out.println("  " + h));

      // 3. Retrieve database info

      System.out.println("Inspecting live database:");

      Connection conn = null;
      try {

        // 4. Connect to database

        try {
          conn = this.dataSource.getConnection();
        } catch (SQLException e) {
          throw new CouldNotVerifyDatabaseException("Could not get database connection", e);
        }

        // 5. Retrieve live database structure

        Snapshot ls;
        try {
          ls = Snapshot.takeSnapshot(conn, "Live Database");
        } catch (ReaderException | SQLException e) {
          throw new CouldNotVerifyDatabaseException("Could retrieve live database structure", e);
        }

        // 6. Display live database summary

        ls.renderHeader().stream().forEach(l -> System.out.println(l));

        // Compare structures

        SnapshotComparator comp = new SnapshotComparator(fs, ls);

        List<String> diff = comp.getDiff();

        System.out.println("Differences found: " + diff.size());
        for (String d : diff) {
          System.out.println(" * " + d);
        }
        System.out.println("=== Database Verifier Complete === ");

        return diff.isEmpty();

      } finally {

        // 5. Close the database connection

        if (conn != null) {
          try {
            conn.close();
          } catch (SQLException e) {
            throw new CouldNotVerifyDatabaseException("Could not close database connection", e);
          }
        }

      }

    } catch (RuntimeException e) {
      throw new CouldNotVerifyDatabaseException("Runtime exception", e);
    }
  }

  public static class CouldNotVerifyDatabaseException extends Exception {

    private static final long serialVersionUID = 1L;

    public CouldNotVerifyDatabaseException(final String message, final Throwable cause) {
      super(message, cause);
    }

  }

}
