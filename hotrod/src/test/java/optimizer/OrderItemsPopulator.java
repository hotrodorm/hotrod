package optimizer;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Random;

public class OrderItemsPopulator {

  private static final int AVG_ITEMS_PER_ORDER = 3;

  private static final Date START_DATE = Date.valueOf("2012-10-14");
  private static final Date END_DATE = Date.valueOf("2018-02-05");
  private static final int DAYS = Math
      .round((1.0f * END_DATE.getTime() - START_DATE.getTime()) / (1000.0f * 60 * 60 * 24));

  private Random random;

  public OrderItemsPopulator(final Random random) {
    this.random = random;
  }

  public void truncate() throws SQLException {
    SQLExecutor.executeUpdate("delete from order_item");
  }

  public void populate(final ProductsPopulator productsPopulator, final CodesPopulator codesPopulator)
      throws SQLException {
    PreparedStatement st = null;
    try {
      String sql = "insert into order_item (id, order_id, product_id, quantity, status_code, deferred_shipment_date) values (?, ?, ?, ?, ?, ?)";
      st = SQLExecutor.getConnection().prepareStatement(sql);

      int orderItemId = 5000;

      for (int orderId = 0; orderId < OrdersPopulator.TOTAL; orderId++) {

        int itemsInOrder = 1 + random.nextInt(AVG_ITEMS_PER_ORDER * 2 - 1);

        for (int m = 0; m < itemsInOrder; m++) {
          int productId = productsPopulator.getRandomId();
          int quantity = random.nextBoolean() ? random.nextInt(2) : random.nextInt(100);
          int statusCode = codesPopulator.getRandomItemId();

          Date deferredShipmentDate;
          if (random.nextDouble() < 0.2) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(START_DATE);
            int offset = random.nextInt(DAYS);
            cal.add(Calendar.DAY_OF_MONTH, offset);
            java.util.Date d = cal.getTime();
            deferredShipmentDate = new Date(d.getTime());
          } else {
            deferredShipmentDate = null;
          }

          int col = 1;
          st.setInt(col++, orderItemId);
          st.setInt(col++, orderId);
          st.setInt(col++, productId);
          st.setInt(col++, quantity);
          st.setInt(col++, statusCode);
          st.setDate(col++, deferredShipmentDate);

          st.execute();

          orderItemId++;
        }
      }

    } finally {
      if (st != null) {
        st.close();
      }
    }
  }

}
