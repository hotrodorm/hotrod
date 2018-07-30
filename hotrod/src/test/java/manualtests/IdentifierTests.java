package manualtests;

import org.hotrod.utils.identifiers.DbIdentifier;
import org.hotrod.utils.identifiers.Identifier;

public class IdentifierTests {

  public static void main(final String[] args) {
    test("abc", "Abc");
    test("car_brand1", "CarBrand1");
    test("coord_x", "CoordX");
    test("jdbc_value", "JDBCValue");
    test("a_jdbc_value", "aJDBCValue");
    test("_total", "_Total");
    test("car_brand1_value", "CarBrand1value");
    test("car_brand2_value", "CarBrand2Value");
  }

  private static void test(final String sqlName, final String javaName) {
    Identifier id = new DbIdentifier(sqlName, javaName);
    System.out.println("\n[" + sqlName + ", " + javaName + "] ");
    System.out.println("SQLIdentifier          -> " + id.getSQLIdentifier());
    System.out.println("JavaClassIdentifier    -> " + id.getJavaClassIdentifier());
    System.out.println("JavaMemberIdentifier   -> " + id.getJavaMemberIdentifier());
    System.out.println("JavaConstantIdentifier -> " + id.getJavaConstantIdentifier());
    System.out.println("MapperFileIdentifier   -> " + id.getMapperIdentifier());
  }

}
