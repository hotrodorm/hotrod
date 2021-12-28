package research.optimizer;

import java.sql.SQLException;
import java.util.Random;

public class CodesPopulator {

  private static final String[] STATUS = { "Submitted", "Shipping", "Delivered", "Canceled" };

  private Random random;

  public CodesPopulator(final Random random) {
    this.random = random;
  }

  public void truncate() throws SQLException {
    ConsoleProgress cp = new ConsoleProgress("Deleting Codes", 1);
    SQLExecutor.executeUpdate("delete from code");
    cp.complete();
  }

  public void populate() throws SQLException {
    ConsoleProgress cp = new ConsoleProgress("Adding Codes", 1);

    SQLExecutor.executeUpdate("insert into code (id, caption, type) values (1, 'Submitted', 'IN_PROCESS')");
    SQLExecutor.executeUpdate("insert into code (id, caption, type) values (2, 'Shipping', 'IN_PROCESS')");
    SQLExecutor.executeUpdate("insert into code (id, caption, type) values (3, 'Delivered', 'CLOSED')");
    SQLExecutor.executeUpdate("insert into code (id, caption, type) values (4, 'Canceled', 'CLOSED')");

    SQLExecutor.executeUpdate("insert into code (id, caption, type) values (5, 'Submitted', 'IN_PROCESS')");
    SQLExecutor.executeUpdate("insert into code (id, caption, type) values (6, 'Shipping', 'IN_PROCESS')");
    SQLExecutor.executeUpdate("insert into code (id, caption, type) values (7, 'Delivered', 'CLOSED')");
    SQLExecutor.executeUpdate("insert into code (id, caption, type) values (8, 'Canceled', 'CLOSED')");

    cp.complete();
  }

  public int getRandomOrderId() {
    return 1 + random.nextInt(STATUS.length);
  }

  public int getRandomItemId() {
    return 5 + random.nextInt(STATUS.length);
  }

}
