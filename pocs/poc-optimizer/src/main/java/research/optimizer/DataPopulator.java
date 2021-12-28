package research.optimizer;

import java.sql.SQLException;
import java.util.Random;

public class DataPopulator {

  private static final long PSEUDO_RANDOM_SEED = 5739644;

  public static void main(final String[] args) throws SQLException {

    if (args.length != 4) {

      usage();
      System.exit(1);

    } else {

      SQLExecutor.setJDBCDriver(args[0]);
      SQLExecutor.setDatabaseUrl(args[1]);
      SQLExecutor.setUsername(args[2]);
      SQLExecutor.setPassword(args[3]);

      Random random = new Random(PSEUDO_RANDOM_SEED);

      System.out.println("[Starting Data Populator]");

      CodesPopulator codes = new CodesPopulator(random);
      AddressesPopulator adresses = new AddressesPopulator(random);
      CustomersPopulator customers = new CustomersPopulator(random);
      ShipmentsPopulator shipments = new ShipmentsPopulator(random);
      ProductsPopulator products = new ProductsPopulator(random);
      OrdersPopulator orders = new OrdersPopulator(random);
      OrderItemsPopulator items = new OrderItemsPopulator(random);

      // Clean up

      items.truncate();
      orders.truncate();
      products.truncate();
      shipments.truncate();
      customers.truncate();
      adresses.truncate();
      codes.truncate();

      // Populate

      codes.populate();
      adresses.populate();
      customers.populate(adresses);
      shipments.populate(adresses);
      products.populate();
      orders.populate(customers, shipments, codes);
      items.populate(products, codes);

      System.out.println("[Data Populator Complete]");

    }

  }

  private static void usage() {
    System.out.println("parameters: <driver-class> <url> <username> <password>");
  }

}
