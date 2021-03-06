package research.optimizer;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.Random;

public class OrdersPopulator {

  public static final int TOTAL = 50000;

  private static final Date START_DATE = Date.valueOf("2012-10-14");
  private static final Date END_DATE = Date.valueOf("2018-02-05");
  private static final int DAYS = Math
      .round((1.0f * END_DATE.getTime() - START_DATE.getTime()) / (1000.0f * 60 * 60 * 24));

  private Random random;
  private String quoteString;

  public OrdersPopulator(final Random random) throws SQLException {
    this.random = random;
    this.quoteString = SQLExecutor.getConnection().getMetaData().getIdentifierQuoteString();
  }

  public void truncate() throws SQLException {
    ConsoleProgress cp = new ConsoleProgress("Deleting Orders", 1);
    SQLExecutor.executeUpdate("delete from " + this.quoteString + "order" + this.quoteString + "");
    cp.complete();
  }

  public void populate(final CustomersPopulator customersPopulator, final ShipmentsPopulator shipmentsPopulator,
      final CodesPopulator codesPopulator) throws SQLException {
    PreparedStatement st = null;
    ConsoleProgress cp = new ConsoleProgress("Adding Order", TOTAL);
    try {
      String sql = "insert into " + this.quoteString + "order" + this.quoteString
          + " (id, customer_id, placed, shipment_id, status_code) values (?, ?, ?, ?, ?)";
      st = SQLExecutor.getConnection().prepareStatement(sql);

      for (int id = 0; id < TOTAL; id++) {

        int customerId = customersPopulator.getRandomId();

        Calendar cal = Calendar.getInstance();
        cal.setTime(START_DATE);
        int offset = random.nextInt(DAYS);
        cal.add(Calendar.DAY_OF_MONTH, offset);
        java.util.Date d = cal.getTime();
        Date placed = new Date(d.getTime());

        Integer shipmentId = random.nextDouble() < 0.8 ? shipmentsPopulator.getRandomId() : null;

        int statusCode = codesPopulator.getRandomOrderId();

        int col = 1;
        st.setInt(col++, id);
        st.setInt(col++, customerId);
        st.setDate(col++, placed);
        if (shipmentId != null) {
          st.setInt(col++, shipmentId);
        } else {
          st.setNull(col++, Types.NUMERIC);
        }
        st.setInt(col++, statusCode);

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

}
