package tests;

import java.io.IOException;
import java.sql.SQLException;

public class TypesTest {

  public static void main(final String[] args) throws IOException, SQLException {
    testTypes();
  }

  private static void testTypes() throws SQLException {

    // numericTest();
    // charTest();
    // dateTimeTest();
    // binaryTest();
//    otherTest();

  }

//  private static void numericTest() throws SQLException {
//
//    TypesNumericDAO.deleteByExample(new TypesNumericVO());
//
//    TypesNumericVO n = new TypesNumericVO();
//    n.setId(1);
//
//    n.setNum1((byte) 12);
//    n.setNum2((short) 1234);
//    n.setNum3(876543210);
//    n.setNum4(1111222233334444L);
//    n.setNum5(new BigInteger("-1234567890123456789012345678901"));
//
//    n.setNum7(new BigDecimal("123.01"));
//    n.setNum8(new BigDecimal("123.02"));
//    n.setNum9(78901);
//
//    n.setNum10((short) 31000);
//    n.setNum11(987654321);
//    n.setNum12(11112222333344445L);
//
//    n.setNum20(123456.789123456789);
//    n.setNum21(123456.789123456789f);
//    n.setNum22(123456.789123456789);
//    n.setNum23(123456.789123456789f);
//    n.setNum24(123456.789123456789);
//
//    TypesNumericDAO.insert(n);
//
//    for (TypesNumericVO x : TypesNumericDAO.selectByExample(new TypesNumericVO())) {
//      System.out.println("numeric=" + x);
//    }
//  }
//
//  private static void charTest() throws SQLException {
//    TypesCharDAO.deleteByExample(new TypesCharVO());
//
//    TypesCharVO c = new TypesCharVO();
//    c.setId(1);
//
//    c.setCha1("Abc1234567");
//    c.setCha2("Def12345678901234567");
//    c.setCha3("Ghi");
//    c.setCha4("Jkl");
//
//    TypesCharDAO.insert(c);
//
//    for (TypesCharVO x : TypesCharDAO.selectByExample(new TypesCharVO())) {
//      System.out.println("char=" + x);
//    }
//  }
//
//  private static void dateTimeTest() throws SQLException {
//    TypesDateTimeDAO.deleteByExample(new TypesDateTimeVO());
//
//    TypesDateTimeVO d = new TypesDateTimeVO();
//    d.setId(1);
//
//    d.setDat1(java.sql.Date.valueOf("1999-12-07"));
//    d.setDat2(java.sql.Time.valueOf("17:05:06"));
//    d.setDat3(java.sql.Timestamp.valueOf("2005-04-05 19:51:43.123456789"));
//    TypesDateTimeDAO.insert(d);
//
//    for (TypesDateTimeVO x : TypesDateTimeDAO.selectByExample(new TypesDateTimeVO())) {
//      System.out.println("date/time=" + x);
//    }
//  }
//
//  private static void binaryTest() throws SQLException {
//    TypesBinaryDAO.deleteByExample(new TypesBinaryVO());
//
//    byte[] b1 = new byte[] { 1, 2, 3 };
//    byte[] b2 = new byte[] { 4, 5, 6 };
//    byte[] b3 = new byte[] { 7, 8, 9 };
//    byte[] b4 = new byte[] { 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 };
//
//    TypesBinaryVO b = new TypesBinaryVO();
//    b.setId(1);
//
//    b.setBin1(b1);
//    b.setBin2(b2);
//    b.setBin3(b3);
//    b.setBin4(b4);
//    TypesBinaryDAO.insert(b);
//
//    for (TypesBinaryVO x : TypesBinaryDAO.selectByExample(new TypesBinaryVO())) {
//      System.out.println("binary=" //
//          + " [" + renderBA(x.getBin1()) + "], " //
//          + " [" + renderBA(x.getBin2()) + "], " //
//          + " [" + renderBA(x.getBin3()) + "], " //
//          + " [" + renderBA(x.getBin4()) + "]" //
//      );
//    }
//  }
//
//  private static void otherTest() throws SQLException {
//    TypesOtherDAO.deleteByExample(new TypesOtherVO());
//
//    TypesOtherVO o = new TypesOtherVO();
//    o.setId(1);
//
//    o.setBoo1(true);
//    TypesOtherDAO.insert(o);
//
//    for (TypesOtherVO x : TypesOtherDAO.selectByExample(new TypesOtherVO())) {
//      System.out.println("other=" + x);
//    }
//  }

  // Utils

  @SuppressWarnings("unused")
  private static String renderObject(final Object obj) {
    if (obj == null) {
      return "null";
    }
    return obj + " (" + obj.getClass().getName() + ")";
  }

  @SuppressWarnings("unused")
  private static String renderBA(final byte[] ba) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < ba.length; i++) {
      sb.append((i > 0 ? ", " : "") + ba[i]);
    }
    return sb.toString();
  }

}
