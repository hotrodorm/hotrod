package explainplan;

import java.sql.SQLException;
import java.util.Random;

import optimizer.SQLExecutor;

public class DataPopulator {

  private static final long PSEUDO_RANDOM_SEED = 5739644;

  public static void main(final String[] args) throws SQLException {

    if (args.length != 4) {

      usage();
      System.exit(1);

    } else {

      SQLExecutor.setJDBCDriver(args[0]);
      SQLExecutor.setDatabaseUrl(args[1]);
      SQLExecutor.setUsername(args[2]);
      SQLExecutor.setPassword(args[3]);

      Random random = new Random(PSEUDO_RANDOM_SEED);

      System.out.println("[Starting Data Populator]");

      PaymentPopulator adresses = new PaymentPopulator(random);

      // Clean up

      adresses.truncate();

      // Populate

      adresses.populate();

      System.out.println("[Data Populator Complete]");

    }

  }

  private static void usage() {
    System.out.println("parameters: <driver-class> <url> <username> <password>");
  }

}
