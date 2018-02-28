package selects.example;

import java.sql.SQLException;

public class Examples {

  public static void main(final String[] args) throws SQLException {

//    // Select by PK
//    BrandVO fiat = BrandDAO.select(17);
//
//    // Select by Unique Index
//    BrandVO volvo = BrandDAO.selectByUIName("Volvo");
//
//    // Update
//    fiat.setName("Fiat");
//    BrandDAO.update(fiat);
//
//    // Delete by PK
//    BrandDAO.delete(volvo);
//
//    // Insert
//    BrandVO b = new BrandVO();
//    b.setName("Toyota");
//    BrandDAO.insert(b);
//    System.out.println("id=" + b.getId());

//    // =========================================
//
//    // Select by example - Find vans with no brand ID
//    CarVO example = new CarVO();
//    example.setType("VAN");
//    example.setBrandId(null);
//    List<CarVO> vans = CarDAO.selectByExample(example);
//
//    // Update by example - Set brand ID 17 to vans 
//    //                     with no brand ID
//    CarVO newValues = new CarVO();
//    newValues.setBrandId(17);
//    CarDAO.updateByExample(example, newValues);
//
//    // Delete by example - Delete all coupe 
//    //                     with no brand ID
//    example = new CarVO();
//    example.setType("COUPE");
//    example.setBrandId(null);
//    CarDAO.deleteByExample(example);
//    
//    List<CarVO> vans2 = vans; 
    
//
//    // =========================================
//
//    // Select parent VO
//    CarVO myCar = CarDAO.select(1045);
//    BrandVO myBrand = CarDAO.selectParentBrand().byBrandId(myCar);
//
//    // Select children VO
//    List<CarVO> cars = BrandDAO.selectChildrenCar().byBrandId(myBrand);
//
//    // =========================================
//
//    List<AssessedCarVO> assessed = CarDAO.findAssessedCarVO(123);
//
//    // =========================================
//
//    System.out.println(trucks);
//    System.out.println(cars);
//    System.out.println(assessed);

  }

}
