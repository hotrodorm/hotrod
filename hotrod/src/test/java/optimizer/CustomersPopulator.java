package optimizer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class CustomersPopulator {

  private static final int TOTAL = 8000;

  private static final String[] FIRST_NAME = { "John", "Peter", "Alan", "Mary", "Ann", "Susie", "Oscar", "Leticia" };
  private static final String[] LAST_NAME = { "Bell", "Martinez", "Orman", "Rivers", "Sellers", "Manet", "Poppins",
      "Riverdale" };

  private Random random;

  public CustomersPopulator(final Random random) {
    this.random = random;
  }

  public void truncate() throws SQLException {
    SQLExecutor.executeUpdate("delete from customer");
  }

  public void populate(final AddressesPopulator addressesPopulator) throws SQLException {
    PreparedStatement st = null;
    try {
      String sql = "insert into customer (id, first_name, last_name, phone_number, address_id) values (?, ?, ?, ?, ?)";
      st = SQLExecutor.getConnection().prepareStatement(sql);

      for (int i = 0; i < TOTAL; i++) {

        String firstName = FIRST_NAME[random.nextInt(FIRST_NAME.length)];
        String lastName = LAST_NAME[random.nextInt(LAST_NAME.length)];
        int phoneNumber = 2025550000 + random.nextInt(10000);
        int addressId = addressesPopulator.getRandomId();

        int col = 1;
        st.setInt(col++, i);
        st.setString(col++, firstName);
        st.setString(col++, lastName);
        st.setString(col++, "" + phoneNumber);
        st.setInt(col++, addressId);

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
