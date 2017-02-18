package fulltest.appconfig;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TestApplicationConfig {

  public static void main(final String[] args) throws IOException, SQLException {
    simpleSelect();
  }

  private static void simpleSelect() throws SQLException {

    System.out.println("[ Simple Select ]");

    List<AppConfig> acs = AppConfig.select();
    System.out.println("Found " + acs.size() + " ac(s).");
    System.out.println("acs=" + acs);
    for (AppConfig ac : acs) {
      System.out.println(" * " + ac.getConfigId() + ", " + ac.getConfigName()
          + ", " + ac.getConfigValue());
    }

  }

}
