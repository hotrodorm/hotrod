package optimizer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class AddressesPopulator {

  private static final int TOTAL = 1800;

  private static final String[] STREET_NAME_PREFIX = { "Maple", "Oak", "Smith", "Peters", "Battle", "Orchard", "Minute",
      "Cloak" };
  private static final String[] STREET_NAME_SUFFIX = { "Ave", "Rd", "Ct", "St", "Ridge", "Loop", "Towers", "Circle" };

  private static final String[] APT = { "", "Apt 601", "Suite 4B", "Unit F", "Unit 401", "Apartment 1403", "B2",
      "Box 1154" };

  private static final String[] CITY = { "Richmond", "Las Vegas", "Denver", "Cincinnati", "Detroit", "San Francisco",
      "Los Angeles", "Boise" };

  private static final String[] STATE = { "VA", "DC", "NV", "CA", "CO", "OR", "WA", "OH" };

  private static final String[] ZIP_CODE = { "90125", "20001", "34330", "77392", "28332", "45001", "11293", "94005" };

  private Random random;

  public AddressesPopulator(final Random random) {
    this.random = random;
  }

  public void truncate() throws SQLException {
    SQLExecutor.executeUpdate("delete from address");
  }

  public void populate() throws SQLException {
    PreparedStatement st = null;
    try {
      String sql = "insert into address (id, line1, line2, city, state, zip_code) values (?, ?, ?, ?, ?, ?)";
      st = SQLExecutor.getConnection().prepareStatement(sql);

      for (int id = 0; id < TOTAL; id++) {
        int streetNumber = 1 + random.nextInt(12000);
        String streetName = STREET_NAME_PREFIX[random.nextInt(STREET_NAME_PREFIX.length)] + " "
            + STREET_NAME_SUFFIX[random.nextInt(STREET_NAME_SUFFIX.length)];
        String apt = APT[random.nextInt(APT.length)];
        String city = CITY[random.nextInt(CITY.length)];
        String state = STATE[random.nextInt(STATE.length)];
        String zipCode = ZIP_CODE[random.nextInt(ZIP_CODE.length)];

        int col = 1;
        st.setInt(col++, id);
        st.setString(col++, "" + streetNumber + " " + streetName);
        st.setString(col++, apt);
        st.setString(col++, city);
        st.setString(col++, state);
        st.setString(col++, zipCode);

        st.execute();
      }

    } finally {
      if (st != null) {
        st.close();
      }
    }
  }

  public int getRandomId() {
    return this.random.nextInt(TOTAL);
  }

}
