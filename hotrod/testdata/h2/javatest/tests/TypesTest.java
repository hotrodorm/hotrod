package tests;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

import hotrod.test.generation.TypesBinaryDAO;
import hotrod.test.generation.TypesCharDAO;
import hotrod.test.generation.TypesDateTimeDAO;
import hotrod.test.generation.TypesNumericDAO;
import hotrod.test.generation.TypesOtherDAO;

public class TypesTest {

  public static void main(final String[] args) throws IOException, SQLException {
    testTypes();
  }

  private static void testTypes() throws SQLException {

    // numericTest();
    // charTest();
    // dateTimeTest();
    // binaryTest();
    otherTest();

  }

  private static void numericTest() throws SQLException {
    TypesNumericDAO.deleteByExample(new TypesNumericDAO());

    TypesNumericDAO n = new TypesNumericDAO();
    n.setId(1L);

    n.setI1(123456);
    n.setI2(123456);
    n.setI3(123456);
    n.setI4(123456);
    n.setI5(123456);

    n.setI10((byte) 123);

    n.setI20((short) 1234);
    n.setI21((short) 1235);
    n.setI22((short) 1236);

    n.setI30(1111222233334444L);
    n.setI31(1111222233334445L);

    n.setDec1(BigDecimal.ONE);
    n.setDec2(BigDecimal.ONE);
    n.setDec3(BigDecimal.ONE);
    n.setDec4(BigDecimal.ONE);
    n.setDec5(BigDecimal.ONE);

    n.setDou1(123.456);
    n.setDou2(123.456);
    n.setDou3(123.456);
    n.setDou4(123.456);

    n.setRea1(123.456789f);
    n.setRea2(124.456789f);

    n.insert();

    for (TypesNumericDAO x : TypesNumericDAO.selectByExample(new TypesNumericDAO())) {
      System.out.println("numeric=" + x);
    }
  }

  private static void charTest() throws SQLException {
    TypesCharDAO.deleteByExample(new TypesCharDAO());

    TypesCharDAO c = new TypesCharDAO();
    c.setId(1);

    c.setVc1("a");
    c.setVc2("b");
    c.setVc3("c");
    c.setVc4("d");
    c.setVc5("e");
    c.setVc6("f");
    c.setVc7("g");

    c.setCha1("a");
    c.setCha2("b");
    c.setCha3("c");

    c.setClo1("c1");
    c.setClo2("c2");
    c.setClo3("c3");
    c.setClo4("c4");
    c.setClo5("c5");
    c.setClo6("c6");
    c.setClo7("c7");

    c.insert();

    for (TypesCharDAO x : TypesCharDAO.selectByExample(new TypesCharDAO())) {
      System.out.println("char=" + x);
    }
  }

  private static void dateTimeTest() throws SQLException {

    TypesDateTimeDAO.deleteByExample(new TypesDateTimeDAO());

    TypesDateTimeDAO d = new TypesDateTimeDAO();
    d.setId(1);

    d.setTim1(new Time(System.currentTimeMillis()));

    d.setDat1(new java.sql.Date(System.currentTimeMillis()));

    d.setTs1(new Timestamp(System.currentTimeMillis()));
    d.setTs2(new Timestamp(System.currentTimeMillis()));
    d.setTs3(new Timestamp(System.currentTimeMillis()));

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
    byte[] b4 = new byte[] { 10, 11, 12 };
    byte[] b5 = new byte[] { 13, 14, 15 };
    byte[] b6 = new byte[] { 16, 17, 18 };

    TypesBinaryDAO b = new TypesBinaryDAO();
    b.setId(1);

    b.setBin1(b1);
    b.setBin2(b2);
    b.setBin3(b3);
    b.setBin4(b4);
    b.setBin5(b5);

    b.setBlo1(b1);
    b.setBlo2(b2);
    b.setBlo3(b3);
    b.setBlo4(b4);
    b.setBlo5(b5);
    b.setBlo6(b6);

    b.insert();

    for (TypesBinaryDAO x : TypesBinaryDAO.selectByExample(new TypesBinaryDAO())) {
      System.out.println("binary=" //
          + " [" + renderBA(x.getBin1()) + "], " //
          + " [" + renderBA(x.getBin2()) + "], " //
          + " [" + renderBA(x.getBin3()) + "], " //
          + " [" + renderBA(x.getBin4()) + "], " //
          + " [" + renderBA(x.getBin5()) + "] / " //
          + " [" + renderBA(x.getBlo1()) + "], " //
          + " [" + renderBA(x.getBlo2()) + "], " //
          + " [" + renderBA(x.getBlo3()) + "], " //
          + " [" + renderBA(x.getBlo4()) + "], " //
          + " [" + renderBA(x.getBlo5()) + "], " //
          + " [" + renderBA(x.getBlo6()) + "]" //
      );
    }
  }

  private static void otherTest() throws SQLException {

    TypesOtherDAO.deleteByExample(new TypesOtherDAO());

    TypesOtherDAO o = new TypesOtherDAO();

    o.setBoo1(true);
    o.setBoo2(false);
    o.setBoo3(true);

    o.setOth1(toByteArray(123L));

    o.setId1(toByteArray(123L, 456L));

    // String[] sa = { "abc", "def" };
    // o.setArr1(sa);
    
    Object[] objs = new Object[2];
    objs[0] = new Integer(123);
    objs[1] = new Integer(456);
    o.setArr1(objs);

    o.setGeo1(null);

    o.insert();

    for (TypesOtherDAO x : TypesOtherDAO.selectByExample(new TypesOtherDAO())) {
      System.out.println("other=" + x);
      System.out.println(" * UUID=" + renderBA(x.getId1()));
      System.out.println(" * oth1=" + renderBA(x.getOth1()));
      System.out.println(" * arr1=" + x.getArr1());

      // System.out.println("binary-row1=" + renderObject(x.getRow1()));
      // System.out.println("binary-itv2=" + renderObject(x.getItv2()));
      // System.out.println("binary-itv4=" + renderObject(x.getItv4()));
      // // System.out.println("binary-oth1="+ renderObject(x.getOth1()));
      // System.out.println("binary-oth2=" + renderObject(x.getOth2()));
      //
      // System.out.println("binary-names=" + renderObject(x.getNames()));
      // System.out.println("binary-stu1=" + renderObject(x.getStu1()));
      // System.out.println("binary-ref1="+ renderObject(x.getRef1()));

      // oracle.sql.STRUCT st = (oracle.sql.STRUCT) x.getOth2();
      // System.out.println(">>> [" + st.getLength() + "]");
    }
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

  private static byte[] toByteArray(final long v) {
    return ByteBuffer.allocate(8).putLong(v).array();
  }

  private static byte[] toByteArray(final long v1, final long v2) {
    return ByteBuffer.allocate(16).putLong(v1).putLong(v2).array();
  }

}
