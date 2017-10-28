package selects.structuredvos;

import java.sql.SQLException;
import java.util.List;

import hotrod.test.generation.ExtendedPersonVO;
import hotrod.test.generation.primitives.PersonDAO;

public class StructuredTests {

  public void run() throws SQLException {
    test7();
  }

  // 7 - Collections and associations combined

  private static void test7() throws SQLException {
    List<ExtendedPersonVO> list = PersonDAO.findExtendedPerson(100);
    System.out.println("=== Extended Persons ===");
    for (ExtendedPersonVO ep : list) {
      System.out.println("ep=" + ep);
    }
  }

}
