package research.optimizer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class ProductsPopulator {

  private static final int TOTAL = 4300;

  private static final String[] COLOR = { "Red", "Blue", "Caribbean Turquoise", "Yellow", "Oriental Orange", "Black",
      "White", "Blue" };

  private static final String[] BASE = { "Jeans", "Pants", "Jacket", "Sweater", "Oldfashioned Coat", "Hat", "Boots",
      "Shoes" };

  private static final String[] SIZE = { "XS", "S", "M", "L", "XL", "XXL", "XXXL", "Petite" };

  private Random random;

  public ProductsPopulator(final Random random) {
    this.random = random;
  }

  public void truncate() throws SQLException {
    ConsoleProgress cp = new ConsoleProgress("Deleting Products", 1);
    SQLExecutor.executeUpdate("delete from product");
    cp.complete();
  }

  public void populate() throws SQLException {
    PreparedStatement st = null;
    ConsoleProgress cp = new ConsoleProgress("Adding Products", TOTAL);
    try {
      String sql = "insert into product (id, description) values (?, ?)";
      st = SQLExecutor.getConnection().prepareStatement(sql);

      for (int id = 0; id < TOTAL; id++) {

        String description = COLOR[random.nextInt(COLOR.length)] + " " + BASE[random.nextInt(BASE.length)] + " "
            + SIZE[random.nextInt(SIZE.length)];

        int col = 1;
        st.setInt(col++, id);
        st.setString(col++, description);

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
