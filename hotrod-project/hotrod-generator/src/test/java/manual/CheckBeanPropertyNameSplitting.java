package manual;

import java.util.List;

import org.hotrod.identifiers.Id.NamePart;
import org.hotrod.utils.BeanUtils;

public class CheckBeanPropertyNameSplitting {

  public static void main(String[] args) {
    String[] testNames = { "firstName", "lastName", "FIRSTNAME", "OrderID", "HTTPResponse", "UserProfile", "XMLParser",
        "totalJNDIValueACRONYM", "TotalJNDIValueACRONYM", "jndi", "JNDI" };

    test1(testNames);
//    test2(testNames);
  }

  private static void test1(String[] testNames) {
    for (String propertyName : testNames) {
      List<NamePart> parts = BeanUtils.splitJava(propertyName);
      System.out.print(propertyName + " -> ");
      for (NamePart part : parts) {
        System.out.print(part + (part.isAcronym() ? "*" : "") + " ");
      }
      System.out.println();
    }
  }

  private static void test2(String[] testNames) {
    for (String propertyName : testNames) {
      String[] parts = BeanUtils.splitPropertyName(propertyName);
      System.out.print(propertyName + " -> ");
      for (String part : parts) {
        System.out.print(part + " ");
      }
      System.out.println();
    }
  }

}
