package optimizer;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Random;

public class OrdersPopulator {

  public static final int TOTAL = 20000;

  private static final Date START_DATE = Date.valueOf("2012-10-14");
  private static final Date END_DATE = Date.valueOf("2018-02-05");
  private static final int DAYS = Math
      .round((1.0f * END_DATE.getTime() - START_DATE.getTime()) / (1000.0f * 60 * 60 * 24));

  private Random random;

  public OrdersPopulator(final Random random) {
    this.random = random;
  }

  public void truncate() throws SQLException {
    SQLExecutor.executeUpdate("delete from \"order\"");
  }

  public void populate(final CustomersPopulator customersPopulator, final ShipmentsPopulator shipmentsPopulator,
      final CodesPopulator codesPopulator) throws SQLException {
    PreparedStatement st = null;
    try {
      String sql = "insert into \"order\" (id, customer_id, placed, shipment_id, status_code) values (?, ?, ?, ?, ?)";
      st = SQLExecutor.getConnection().prepareStatement(sql);

      for (int id = 0; id < TOTAL; id++) {

        int customerId = customersPopulator.getRandomId();

        Calendar cal = Calendar.getInstance();
        cal.setTime(START_DATE);
        int offset = random.nextInt(DAYS);
        cal.add(Calendar.DAY_OF_MONTH, offset);
        java.util.Date d = cal.getTime();
        Date placed = new Date(d.getTime());

        int shipmentId = shipmentsPopulator.getRandomId();

        int statusCode = codesPopulator.getRandomOrderId();

        int col = 1;
        st.setInt(col++, id);
        st.setInt(col++, customerId);
        st.setDate(col++, placed);
        st.setInt(col++, shipmentId);
        st.setInt(col++, statusCode);

        st.execute();
      }

    } finally {
      if (st != null) {
        st.close();
      }
    }
  }

}
