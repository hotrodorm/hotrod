package explainplan;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Random;

import optimizer.ConsoleProgress;
import optimizer.SQLExecutor;

public class PaymentPopulator {

  private static final int TOTAL = 10000;

  private static final Date START_DATE = Date.valueOf("2012-10-14");
  private static final Date END_DATE = Date.valueOf("2018-02-05");

  private Random random;

  public PaymentPopulator(final Random random) {
    this.random = random;
  }

  // create table payment (
  // id number(18) primary key not null,
  // amount number(14,2) not null,
  // placed_at date not null,
  // status number(2) not null,
  // charged_at date null,
  // payment_processor_id varchar2(24) not null
  // );

  public void truncate() throws SQLException {
    ConsoleProgress cp = new ConsoleProgress("Deleting Payment", 1);
    SQLExecutor.executeUpdate("delete from payment");
    cp.complete();
  }

  public void populate() throws SQLException {
    PreparedStatement st = null;
    ConsoleProgress cp = new ConsoleProgress("Adding Addresses", TOTAL);
    DecimalFormat df9 = new DecimalFormat("000000000");
    DecimalFormat df4 = new DecimalFormat("0000");

    SQLExecutor.getConnection().setAutoCommit(false);

    try {
      String sql = "insert into payment (id, amount, placed_at, status, charged_at, payment_processor_id) values (?, ?, ?, ?, ?, ?)";
      st = SQLExecutor.getConnection().prepareStatement(sql);

      for (int id = 0; id < TOTAL; id++) {

        // double amount = 0.01 + random.nextDouble() * 200;
        double amount = random.nextBoolean() ? (0.01 + random.nextDouble() * 5.0) : (5 + random.nextDouble() * 200);

        Date placedAt = getRandomDate(START_DATE, END_DATE);
        int status = random.nextInt(4);
        Date chargedAt = getRandomDate(START_DATE, END_DATE);
        String paymentProcessorId = df9.format(random.nextInt(1000000000)) + "-" + df4.format(random.nextInt(10000))
            + "-" + df9.format(random.nextInt(1000000000));
        // System.out.println("paymentProcessorId[" +
        // paymentProcessorId.length() + "]=" + paymentProcessorId);

        int col = 1;
        st.setInt(col++, id);
        st.setDouble(col++, amount);
        st.setDate(col++, placedAt);
        st.setInt(col++, status);
        st.setDate(col++, chargedAt);
        st.setString(col++, paymentProcessorId);

        st.execute();
        cp.update(id);
      }
      cp.complete();

      SQLExecutor.getConnection().commit();
      SQLExecutor.getConnection().setAutoCommit(true);

    } finally {
      if (st != null) {
        st.close();
      }
    }
  }

  public int getRandomId() {
    return this.random.nextInt(TOTAL);
  }

  private Date getRandomDate(final Date begin, final Date end) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(begin);
    int days = Math.round((1.0f * end.getTime() - begin.getTime()) / (1000.0f * 60 * 60 * 24));
    int offset = random.nextInt(days);
    cal.add(Calendar.DAY_OF_MONTH, offset);
    java.util.Date d = cal.getTime();
    return new Date(d.getTime());
  }

}
