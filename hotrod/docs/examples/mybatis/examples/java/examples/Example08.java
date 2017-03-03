package examples;

import java.sql.SQLException;
import java.util.List;

import daos.CarDAO;
import daos.primitives.CarDAOPrimitives.CarDAOOrderBy;

/**
 * Example 08 - Select on Views
 * 
 * @author valarcon
 * 
 */
public class Example08 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 08 - Select on Views ===");
    CarDAO example;

    // 1. Select by Example
    // Example: Find all vehicles of brand Toyota on the view CAR

    example = new CarDAO();
    example.setBrand("Toyota");
    List<CarDAO> cars1 = CarDAO.selectByExample(example);
    Utilities.displayCars("1. Select by Example:", cars1);

    // 2. Select by Example - Using Ordering
    // Example: Find all vehicles of brand Toyota on the view CAR, ordered by
    // model, then descending by mileage

    example = new CarDAO();
    example.setBrand("Toyota");
    List<CarDAO> cars2 = CarDAO.selectByExample(example, CarDAOOrderBy.MODEL, CarDAOOrderBy.MILEAGE$DESC);
    Utilities.displayCars("2. Select by Example - Using Ordering:", cars2);

    System.out.println(" ");
    System.out.println("=== Example 08 Complete ===");

  }

}
