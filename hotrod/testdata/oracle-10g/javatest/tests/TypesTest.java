package tests;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import hotrod.test.generation.QuadrantDAO;
import hotrod.test.generation.TypesBinaryDAO;
import hotrod.test.generation.TypesCharDAO;
import hotrod.test.generation.TypesExtraDAO;
import hotrod.test.generation.TypesNumericDAO;
import hotrod.test.generation.TypesOtherDAO;
import hotrod.test.generation.accounting.finances.TypesDateTimeDAO;

public class TypesTest {

  public static void main(final String[] args) throws IOException, SQLException {
    testTypes();
  }

  private static void testTypes() throws SQLException {

    // numericTest();
    // charTest();
    // dateTimeTest();
    // binaryTest();
    // extraTest();
    // otherTest();

    typeHandlerTest();

  }

  private static void numericTest() throws SQLException {
    TypesNumericDAO.deleteByExample(new TypesNumericDAO());

    TypesNumericDAO n = new TypesNumericDAO();
    n.setId(1);

    n.setNum1((byte) 12);
    n.setNum2((short) 1234);
    n.setNum3(876543210);
    n.setNum4(1111222233334444L);
    n.setNum5(BigInteger.TEN);
    n.setNum6(BigDecimal.TEN);

    n.setNum7(BigDecimal.TEN);
    n.setNum8(123.456f);
    n.setNum9(1.2345678901);

    n.setNum10(BigDecimal.TEN);
    n.setNum11(BigDecimal.TEN);
    n.setNum12(BigDecimal.TEN);

    n.setNum20(BigInteger.TEN);
    n.setNum21(BigInteger.TEN);
    n.setNum22(BigInteger.TEN);

    n.insert();

    for (TypesNumericDAO x : TypesNumericDAO.selectByExample(new TypesNumericDAO())) {
      System.out.println("numeric=" + x);
    }
  }

  private static void charTest() throws SQLException {
    TypesCharDAO.deleteByExample(new TypesCharDAO());

    TypesCharDAO c = new TypesCharDAO();
    c.setId(1);

    c.setCha1("a");
    c.setCha2("b");
    c.setCha3("c");
    c.setCha4("d");
    c.setCha5("e");

    c.setCha6("f");

    c.setCha7("g");

    // byte[] b3 = new byte[] { 7, 8, 9 };
    // c.setCha10(b3);

    c.insert();

    for (TypesCharDAO x : TypesCharDAO.selectByExample(new TypesCharDAO())) {
      System.out.println("char=" + x);
    }
  }

  private static void dateTimeTest() throws SQLException {
    TypesDateTimeDAO.deleteByExample(new TypesDateTimeDAO());

    TypesDateTimeDAO d = new TypesDateTimeDAO();
    d.setId(1);

    d.setDat1(new java.sql.Date(System.currentTimeMillis()));
    d.setDat2(new Timestamp(System.currentTimeMillis()));
    d.setDat3(new Timestamp(System.currentTimeMillis()));
    d.setDat4(new Timestamp(System.currentTimeMillis()));
    d.insert();

    for (TypesDateTimeDAO x : TypesDateTimeDAO.selectByExample(new TypesDateTimeDAO())) {
      System.out.println("date/time=" + x);
    }
  }

  private static void binaryTest() throws SQLException {
    TypesBinaryDAO.deleteByExample(new TypesBinaryDAO());

    byte[] b1 = new byte[] { 1, 2, 3 };
    byte[] b2 = new byte[] { 4, 5, 6 };
    byte[] b3 = new byte[] { 7, 8, 9 };

    TypesBinaryDAO b = new TypesBinaryDAO();
    b.setId(1);

    b.setBin1(b1);
    b.setBin2(b2);
    b.setBin3(b3);
    b.insert();

    for (TypesBinaryDAO x : TypesBinaryDAO.selectByExample(new TypesBinaryDAO())) {
      System.out.println("binary=" //
          + " [" + renderBA(x.getBin1()) + "], " //
          + " [" + renderBA(x.getBin2()) + "], " //
          + " [" + renderBA(x.getBin3()) + "]" //
      );
    }
  }

  private static void otherTest() throws SQLException {

    // update types_other set row1 = 'AAAMaHAAEAAAAIHAAZ' where id = 1;
    // update types_other set itv2 = TO_YMINTERVAL('01-02') where id = 1;
    // update types_other set itv4 = TO_DSINTERVAL('2 10:20:30.456') where id =
    // 1;
    // update types_other set oth1 = XMLType('<Warehouse
    // whNo="100"><Building>Owned</Building></Warehouse>') where id = 1;
    // update types_other set oth2 =
    // httpuritype.createuri('http://www.oracle.com') where id = 1;
    // update types_other set names = namearray('Vine', 'Vidi', 'Vincere') where
    // id = 1;
    // update types_other set stu1 = person_struct(123, to_date('2003/07/09',
    // 'yyyy/mm/dd')) where id = 1;

    for (TypesOtherDAO x : TypesOtherDAO.selectByExample(new TypesOtherDAO())) {
      System.out.println("binary-row1=" + renderObject(x.getRow1()));
      System.out.println("binary-itv2=" + renderObject(x.getItv2()));
      System.out.println("binary-itv4=" + renderObject(x.getItv4()));
      // System.out.println("binary-oth1="+ renderObject(x.getOth1()));
      System.out.println("binary-oth2=" + renderObject(x.getOth2()));

      System.out.println("binary-names=" + renderObject(x.getNames()));
      System.out.println("binary-stu1=" + renderObject(x.getStu1()));
      // System.out.println("binary-ref1="+ renderObject(x.getRef1()));

      // oracle.sql.STRUCT st = (oracle.sql.STRUCT) x.getOth2();
      // System.out.println(">>> [" + st.getLength() + "]");
    }
  }

  private static void extraTest() throws SQLException {
    for (TypesExtraDAO x : TypesExtraDAO.selectByExample(new TypesExtraDAO())) {
      System.out.println("extra=" + x);
    }
  }

  private static void typeHandlerTest() throws SQLException {

    List<QuadrantDAO> list;

    // list = QuadrantDAO.selectByExample(new QuadrantDAO());

    QuadrantDAO example = new QuadrantDAO();
    example.setActiveState(true);
    list = QuadrantDAO.selectByExample(example);

    for (QuadrantDAO q : list) {
      System.out.println("quandrant=" + q);
      // if (q.getRegion() > 10) {
      // q.setActive(!q.isActive());
      // q.update();
      // }
    }

//    for (CurrentQuadrant q : CurrentQuadrant.select()) {
//      System.out.println("quadrant=" + q);
//    }
  }

  // Utils

  private static String renderObject(final Object obj) {
    if (obj == null) {
      return "null";
    }
    return obj + " (" + obj.getClass().getName() + ")";
  }

  private static String renderBA(final byte[] ba) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < ba.length; i++) {
      sb.append((i > 0 ? ", " : "") + ba[i]);
    }
    return sb.toString();
  }

}
