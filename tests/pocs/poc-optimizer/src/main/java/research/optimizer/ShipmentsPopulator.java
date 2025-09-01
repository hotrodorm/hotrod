package research.optimizer;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Random;

public class ShipmentsPopulator {

  private static final int TOTAL = 19000;

  private static final Date START_DATE = Date.valueOf("2012-10-14");
  private static final Date END_DATE = Date.valueOf("2018-02-05");
  private static final int DAYS = Math
      .round((1.0f * END_DATE.getTime() - START_DATE.getTime()) / (1000.0f * 60 * 60 * 24));

  private Random random;

  public ShipmentsPopulator(final Random random) {
    this.random = random;
  }

  public void truncate() throws SQLException {
    ConsoleProgress cp = new ConsoleProgress("Deleting Shipments", 1);
    SQLExecutor.executeUpdate("delete from shipment");
    cp.complete();
  }

  public void populate(final AddressesPopulator addressesPopulator) throws SQLException {
    PreparedStatement st = null;
    ConsoleProgress cp = new ConsoleProgress("Adding Shipments", TOTAL);
    try {
      String sql = "insert into shipment (id, address_id, shipped_on) values (?, ?, ?)";
      st = SQLExecutor.getConnection().prepareStatement(sql);

      for (int id = 0; id < TOTAL; id++) {

        int addressId = addressesPopulator.getRandomId();

        Calendar cal = Calendar.getInstance();
        cal.setTime(START_DATE);
        int offset = random.nextInt(DAYS);
        cal.add(Calendar.DAY_OF_MONTH, offset);
        java.util.Date d = cal.getTime();
        Date shippedOn = new Date(d.getTime());

        int col = 1;
        st.setInt(col++, id);
        st.setInt(col++, addressId);
        st.setDate(col++, shippedOn);

        st.execute();
        cp.update(id);
      }
      cp.complete();

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
