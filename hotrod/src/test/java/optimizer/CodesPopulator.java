package optimizer;

import java.sql.SQLException;
import java.util.Random;

public class CodesPopulator {

  private static final String[] STATUS = { "Submitted", "Shipping", "Delivered", "Canceled" };

  private Random random;

  public CodesPopulator(final Random random) {
    this.random = random;
  }

  public void truncate() throws SQLException {
    SQLExecutor.executeUpdate("delete from code");
  }

  public void populate() throws SQLException {
    SQLExecutor.executeUpdate("insert into code (id, caption, type) values (1, 'Submitted', 'ORDER')");
    SQLExecutor.executeUpdate("insert into code (id, caption, type) values (2, 'Shipping', 'ORDER')");
    SQLExecutor.executeUpdate("insert into code (id, caption, type) values (3, 'Delivered', 'ORDER')");
    SQLExecutor.executeUpdate("insert into code (id, caption, type) values (4, 'Canceled', 'ORDER')");

    SQLExecutor.executeUpdate("insert into code (id, caption, type) values (5, 'Submitted', 'ITEM')");
    SQLExecutor.executeUpdate("insert into code (id, caption, type) values (6, 'Shipping', 'ITEM')");
    SQLExecutor.executeUpdate("insert into code (id, caption, type) values (7, 'Delivered', 'ITEM')");
    SQLExecutor.executeUpdate("insert into code (id, caption, type) values (8, 'Canceled', 'ITEM')");
  }

  public int getRandomOrderId() {
    return 1 + random.nextInt(STATUS.length);
  }

  public int getRandomItemId() {
    return 5 + random.nextInt(STATUS.length);
  }

}
