package examples.crud2;

import java.sql.SQLException;
import java.util.List;

import daos.CarVO;
import daos.primitives.CarDAO;
import daos.primitives.CarDAO.CarOrderBy;
import examples.Utilities;

/**
 * Example CRUD 208 - Select on Views
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud208 {

  public static void main(String[] args) throws SQLException {

    CarVO example;

    // 1. Select by Example
    // Example: Find all vehicles of brand Toyota on the view CAR

    example = new CarVO();
    example.setBrand("Toyota");
    List<CarVO> cars1 = CarDAO.selectByExample(example);
    Utilities.displayCars("1. Select by Example - get all Toyota cars:", cars1);

    // 2. Select by Example - Using Ordering
    // Example: Find all vehicles of brand Toyota on the view CAR, ordered by
    // model, then descending by mileage

    example = new CarVO();
    example.setBrand("Toyota");
    List<CarVO> cars2 = CarDAO.selectByExample(example, CarOrderBy.MODEL, CarOrderBy.MILEAGE$DESC);
    Utilities.displayCars(
        "2. Select by Example using ordering - " + "get all Toyota cars, ordered by model, and mileage (desc):", cars2);

  }

}
