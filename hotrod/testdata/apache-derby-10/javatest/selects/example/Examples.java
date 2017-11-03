package selects.example;

import java.sql.SQLException;
import java.util.List;

import hotrod.test.generation.AssessedCarVO;
import hotrod.test.generation.BrandVO;
import hotrod.test.generation.CarVO;
import hotrod.test.generation.primitives.BrandDAO;
import hotrod.test.generation.primitives.CarDAO;
import hotrod.test.generation.primitives.NorthOfficeDAO;

public class Examples {

  public static void main(final String[] args) throws SQLException {

    // Insert
    BrandVO b = new BrandVO();
    b.setName("Volvo");
    BrandDAO.insert(b);
    System.out.println("id=" + b.getId());

    // Select by PK
    BrandVO fiat = BrandDAO.select(17);

    // Select by Unique Index
    BrandVO volvo = BrandDAO.selectByUIName("Volvo");

    // Update
    fiat.setName("FIAT");
    BrandDAO.update(fiat);

    // Delete by PK
    BrandDAO.delete(volvo);

    // =========================================

    // Select by example
    CarVO example = new CarVO();
    example.setType("TRUCK");
    List<CarVO> trucks = CarDAO.selectByExample(example);

    // Update by example
    CarVO newValues = new CarVO();
    newValues.setBrandId(17);
    CarDAO.updateByExample(example, newValues);

    // Delete by example
    example = new CarVO();
    example.setBrandId(17);
    CarDAO.deleteByExample(example);

    // =========================================

    // Select parent VO
    CarVO myCar = CarDAO.select(1045);
    BrandVO myBrand = CarDAO.selectParentBrand().byBrandId(myCar);

    // Select children VO
    List<CarVO> cars = BrandDAO.selectChildrenCar().byBrandId(myBrand);

    // =========================================

    List<AssessedCarVO> assessed = NorthOfficeDAO.findAssesedCarVO(123);

    // =========================================

    System.out.println(trucks);
    System.out.println(cars);
    System.out.println(assessed);

  }

}
