package tests;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

import hotrod.test.generation.TypesBinaryVO;
import hotrod.test.generation.TypesCharVO;
import hotrod.test.generation.TypesDateTimeVO;
import hotrod.test.generation.TypesNumericVO;
import hotrod.test.generation.TypesOtherVO;
import hotrod.test.generation.primitives.TypesBinaryDAO;
import hotrod.test.generation.primitives.TypesCharDAO;
import hotrod.test.generation.primitives.TypesDateTimeDAO;
import hotrod.test.generation.primitives.TypesNumericDAO;
import hotrod.test.generation.primitives.TypesOtherDAO;

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
    TypesNumericDAO.deleteByExample(new TypesNumericVO());

    TypesNumericVO n = new TypesNumericVO();
    n.setId(1);

    n.setNum1((byte) 12);
    n.setNum2((byte) 123);
    n.setNum3((short) 876543210);
    n.setNum4(111122223);
    n.setNum5(1111222233334444L);

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

    n.setNum40(BigDecimal.TEN);
    n.setNum41(BigDecimal.ONE);

    n.setNum50(1.234);
    n.setNum51(1.235f);
    n.setNum52(1.236f);
    n.setNum53(1.237);
    n.setNum54(1.238);
    n.setNum55(1.239f);

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
    c.setCha11("h");

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
    d.setDat3(new Timestamp(System.currentTimeMillis()));
    d.setDat4(new Timestamp(System.currentTimeMillis()));
    d.setDat5(new Timestamp(System.currentTimeMillis()));
    d.setDat6(new Timestamp(System.currentTimeMillis()));
    d.setDat7(new Timestamp(System.currentTimeMillis()));
    d.setDat8(new Timestamp(System.currentTimeMillis()));
    d.setDat9(new Timestamp(System.currentTimeMillis()));

    d.setTim1(new Timestamp(System.currentTimeMillis()));
    d.setTim2(new Time(System.currentTimeMillis()));
    d.setTim3(new Timestamp(System.currentTimeMillis()));

    TypesDateTimeDAO.insert(d);

    for (TypesDateTimeVO x : TypesDateTimeDAO.selectByExample(new TypesDateTimeVO())) {
      System.out.println("date/time=" + x);
    }

  }

  private static void binaryTest() throws SQLException {
    TypesBinaryDAO.deleteByExample(new TypesBinaryVO());

    byte[] b1 = new byte[] { 1, 2, 3 };
    byte[] b2 = new byte[] { 4, 5, 6 };
    byte[] b3 = new byte[] { 7, 8, 9 };
    byte[] b4 = new byte[] { 10, 11, 12 };

    TypesBinaryVO b = new TypesBinaryVO();
    b.setId(1);

    b.setBin1(b1);
    b.setBin2(b2);
    b.setBin3(b3);
    b.setBin4(b4);

    TypesBinaryDAO.insert(b);

    for (TypesBinaryVO x : TypesBinaryDAO.selectByExample(new TypesBinaryVO())) {
      System.out.println("binary=" //
          + " [" + renderBA(x.getBin1()) + "], " //
          + " [" + renderBA(x.getBin2()) + "], " //
          + " [" + renderBA(x.getBin3()) + "], " //
          + " [" + renderBA(x.getBin4()) + "]" //
      );
    }
  }

  private static void otherTest() throws SQLException {

    // new TypesOtherDAO().deleteByExample();
    //
    // byte[] b1 = new byte[] { 1, 2, 3 };
    // byte[] b2 = new byte[] { 4, 5, 6 };
    // byte[] b3 = new byte[] { 7, 8, 9 };
    // byte[] b4 = new byte[] { 10, 11, 12 };
    //
    // TypesOtherDAO o = new TypesOtherDAO();
    // o.setId(1);
    //
    //
    //
    //
    // o.insert();

    for (TypesOtherVO x : TypesOtherDAO.selectByExample(new TypesOtherVO())) {
      System.out.println("[ id=" + x.getId() + " ]");
      System.out.println("* hie1=" + renderObject(x.getHie1()));
      System.out.println("* uni1=" + renderObject(x.getUni1()));
      System.out.println("* xml1=" + renderObject(x.getXml1()));
      System.out.println("* geog1=" + renderObject(x.getGeog1()));
      System.out.println("* geom1=" + renderObject(x.getGeom1()));
    }

    TypesOtherVO example = new TypesOtherVO();
    example.setId(1);

    TypesOtherVO newValues = new TypesOtherVO();

    newValues.setHie1(new byte[] { 1, 2, 3 });
    newValues.setUni1("2400f5bb-4532-48dc-9a65-5666bbd3f492");
    newValues.setXml1("<test>abc</test>");
    newValues.setGeog1(null);
    newValues.setGeom1(null);

    TypesOtherDAO.updateByExample(example, newValues);

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

    // for (TypesOtherDAO x : new TypesOtherDAO().select()) {
    // System.out.println("binary-row1=" + renderObject(x.getRow1()));
    // System.out.println("binary-itv2=" + renderObject(x.getItv2()));
    // System.out.println("binary-itv4=" + renderObject(x.getItv4()));
    // // System.out.println("binary-oth1="+ renderObject(x.getOth1()));
    // System.out.println("binary-oth2=" + renderObject(x.getOth2()));
    //
    // System.out.println("binary-names=" + renderObject(x.getNames()));
    // System.out.println("binary-stu1=" + renderObject(x.getStu1()));
    // // System.out.println("binary-ref1="+ renderObject(x.getRef1()));
    //
    // oracle.sql.STRUCT st = (oracle.sql.STRUCT) x.getOth2();
    // System.out.println(">>> [" + st.getLength() + "]");
    // }
  }

  // Utils

  private static String renderObject(final Object obj) {
    if (obj == null) {
      return "null";
    }
    String type = renderReadableClassName(obj.getClass());
    if ("byte[]".equals(type)) {
      return "'" + new String((byte[]) obj) + "' (" + type + ")";
    } else {
      return obj + " (" + type + ")";
    }
  }

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
