package tests;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Timestamp;

import hotrod.test.generation.TypesBinaryVO;
import hotrod.test.generation.TypesCharVO;
import hotrod.test.generation.TypesDateTimeVO;
import hotrod.test.generation.TypesNumericVO;
import hotrod.test.generation.primitives.TypesBinaryDAO;
import hotrod.test.generation.primitives.TypesCharDAO;
import hotrod.test.generation.primitives.TypesDateTimeDAO;
import hotrod.test.generation.primitives.TypesNumericDAO;

public class TypesTest {

  public static void main(final String[] args) throws IOException, SQLException {
    testTypes();
  }

  private static void testTypes() throws SQLException {

    // numericTest();
    // charTest();
    // dateTimeTest();
    binaryTest();
    // otherTest();

  }

  private static void numericTest() throws SQLException {
    TypesNumericDAO.deleteByExample(new TypesNumericVO());

    TypesNumericVO n = new TypesNumericVO();
    n.setId(1);

    n.setNum1((byte) 1);
    n.setNum2((byte) 123);
    n.setNum3((byte) 87);
    n.setNum4((short) 22223);
    n.setNum5(42223);

    n.setNum6(233454222);
    n.setNum7(344442223L);
    n.setNum8(1111222233334448L);
    n.setNum9(new BigInteger("10111222233334448"));

    n.setNum10(1111222233334445L);
    n.setNum11((byte) 9);
    n.setNum12((byte) 99);
    n.setNum13((short) 998);
    n.setNum14((short) 9999);
    n.setNum15(11112);
    n.setNum16(111122);
    n.setNum17(1111222);
    n.setNum18(11112222);
    n.setNum19(111122227);
    n.setNum20(1111222233L);
    n.setNum21(11112222333344478L);
    n.setNum22(111122223333444891L);
    n.setNum23(BigInteger.TEN);

    n.setNum25(1234567890L);
    n.setNum26(new BigDecimal("12345678.91"));
    n.setNum27(new BigDecimal("12.34567891"));

    n.setNum40(BigDecimal.TEN);
    n.setNum41(BigDecimal.ONE);

    n.setNum50(1.234);
    n.setNum51(1.235);
    n.setNum52(1.236);
    n.setNum53(1.237);
    n.setNum54(1.238);
    n.setNum55(1.239);
    n.setNum56(1.232);

    TypesNumericDAO.insert(n);

    for (TypesNumericVO x : TypesNumericDAO.selectByExample(new TypesNumericVO())) {
      System.out.println("numeric=" + x);
    }
  }

  private static void charTest() throws SQLException {
    TypesCharDAO.deleteByExample(new TypesCharVO());

    TypesCharVO c = new TypesCharVO();
    c.setId(1);

    c.setCha1("a");
    c.setCha2("b");
    c.setCha3("c");
    c.setCha4("d");
    c.setCha5("e");
    c.setCha6("f");

    c.setCha10("g");
    c.setCha12("h");
    c.setCha14("i");
    c.setCha15("j");

    // byte[] b3 = new byte[] { 7, 8, 9 };
    // c.setCha10(b3);

    TypesCharDAO.insert(c);

    for (TypesCharVO x : TypesCharDAO.selectByExample(new TypesCharVO())) {
      System.out.println("char=" + x);
    }
  }

  private static void dateTimeTest() throws SQLException {
    TypesDateTimeDAO.deleteByExample(new TypesDateTimeVO());

    TypesDateTimeVO d = new TypesDateTimeVO();
    d.setId(1);

    d.setDat1(new java.sql.Date(System.currentTimeMillis()));
    d.setDat2(new Timestamp(System.currentTimeMillis()));
    d.setDat9(new Timestamp(System.currentTimeMillis()));
    d.setDat10(new Timestamp(System.currentTimeMillis()));
    d.setDat11(new Timestamp(System.currentTimeMillis()));

    d.setTim1(new Timestamp(System.currentTimeMillis()));

    TypesDateTimeDAO.insert(d);

    for (TypesDateTimeVO x : TypesDateTimeDAO.selectByExample(new TypesDateTimeVO())) {
      System.out.println("date/time=" + x);
    }

  }

  private static void binaryTest() throws SQLException {
    TypesBinaryDAO.deleteByExample(new TypesBinaryVO());

    byte[] b1 = new byte[] { 1, 2, 3 };
    byte[] b2 = new byte[] { 4, 5, 6 };
    byte[] b4 = new byte[] { 10, 11, 12 };

    TypesBinaryVO b = new TypesBinaryVO();
    b.setId(1);

    b.setBin1(b1);
    b.setBin2(b2);
    b.setBin4(b4);

    TypesBinaryDAO.insert(b);

    for (TypesBinaryVO x : TypesBinaryDAO.selectByExample(new TypesBinaryVO())) {
      System.out.println("binary=" //
          + " [" + renderBA(x.getBin1()) + "], " //
          + " [" + renderBA(x.getBin2()) + "], " //
          + " [" + renderBA(x.getBin4()) + "]" //
      );
    }
  }

  private static void otherTest() throws SQLException {

  }

  // Utils

  private static String renderReadableClassName(final Class<?> c) {
    if (c.isArray()) {
      return renderReadableClassName(c.getComponentType()) + "[]";
    } else {
      return c.getName();
    }

  }

  private static String renderBA(final byte[] ba) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < ba.length; i++) {
      sb.append((i > 0 ? ", " : "") + ba[i]);
    }
    return sb.toString();
  }

}
