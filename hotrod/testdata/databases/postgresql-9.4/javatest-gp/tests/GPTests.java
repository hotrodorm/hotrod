package tests;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import hotrod.test.generation.AlertVO;
import hotrod.test.generation.ClientAlertSummary;
import hotrod.test.generation.primitives.AlertDAO;

public class GPTests {

  public static void main(final String[] args) throws IOException, SQLException {
    countProperties();
  }

  private static void countProperties() throws SQLException {

    // selectAlerts();
    showAllAlerts();
  }

  private static void selectAlerts() throws SQLException {

    long current = System.currentTimeMillis();
    Timestamp upTo = new Timestamp(current);
    Timestamp since = new Timestamp(current - 1000L * 60 * 60 * 24 * 30);

    System.out.println("--- Alerts ---");
    System.out.println("---  from: " + since);
    System.out.println("---    to: " + upTo);

    for (ClientAlertSummary a : AlertDAO.selectAlertSummaryPerClient(3, since, upTo)) {
      System.out.println("a=" + a);
    }

  }

  private static void showAllAlerts() throws SQLException {

    for (AlertVO a : AlertDAO.selectByExample(new AlertVO())) {
      System.out.println("a=" + a);
    }

  }

}
