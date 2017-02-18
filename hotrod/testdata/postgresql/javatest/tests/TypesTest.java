package tests;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

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

    // Numeric

    TypesNumericDAO.deleteByExample(new TypesNumericDAO()); // delete all rows

    TypesNumericDAO n = new TypesNumericDAO();
    n.setInt1((short) 12345);
    n.setInt2(123456789);
    n.setInt3(1111222233334444L);
    n.setInt4(Short.MAX_VALUE);
    n.setInt5(Integer.MAX_VALUE);
    n.setInt6(Long.MAX_VALUE);

    n.setDec1(BigDecimal.TEN);
    n.setDec2(BigDecimal.TEN);
    n.setDec3((byte) 12);
    n.setDec4((short) 1234);
    n.setDec5(12345678);
    n.setDec6(111122223333444455L);
    n.setDec7(BigInteger.TEN);

    n.setFlo1(123.456f);
    n.setFlo2(123.45678901);

    n.insert();

    for (TypesNumericDAO x : TypesNumericDAO.selectByExample(new TypesNumericDAO())) {
      System.out.println("numeric=" + x);
    }

    // Chars

    TypesCharDAO.deleteByExample(new TypesCharDAO());

    TypesCharDAO c = new TypesCharDAO();
    c.setCha1("a");
    c.setCha2("b");
    c.setCha3("c");
    c.insert();

    for (TypesCharDAO x : TypesCharDAO.selectByExample(new TypesCharDAO())) {
      System.out.println("char=" + x);
    }

    // Date & Time

    TypesDateTimeDAO.deleteByExample(new TypesDateTimeDAO());

    TypesDateTimeDAO d = new TypesDateTimeDAO();
    d.setId(1);

    d.setDat1(new Date(System.currentTimeMillis()));

    d.setTs1(new Timestamp(System.currentTimeMillis()));
    d.setTs2(new Timestamp(System.currentTimeMillis()));
    d.setTs3(new Timestamp(System.currentTimeMillis()));
    d.setTs4(new Timestamp(System.currentTimeMillis()));
    d.setTs5(new Timestamp(System.currentTimeMillis()));

    d.setTim1(new Timestamp(System.currentTimeMillis()));
    d.setTim2(new Timestamp(System.currentTimeMillis()));
    d.setTim3(new Timestamp(System.currentTimeMillis()));
    d.setTim4(new Timestamp(System.currentTimeMillis()));
    d.setTim5(new Timestamp(System.currentTimeMillis()));

    d.insert();

    for (TypesDateTimeDAO x : TypesDateTimeDAO.selectByExample(new TypesDateTimeDAO())) {
      System.out.println("date/time=" + x);
      System.out.println("date/time=" + x.getTim1());
    }

    // Binary

    TypesBinaryDAO.deleteByExample(new TypesBinaryDAO());

    byte[] b1 = new byte[] { 1, 2, 3 };

    TypesBinaryDAO b = new TypesBinaryDAO();
    b.setBin1(b1);
    b.setBol1(false);
    b.insert();

    // Other

    for (TypesBinaryDAO x : TypesBinaryDAO.selectByExample(new TypesBinaryDAO())) {
      System.out.println("binary=" //
          + x + " [" + renderBA(x.getBin1()) + "]" //
      );
    }

    // Extra
    
//    org.apache.ibatis.type.JdbcType.x

    // byte[] uuid1 = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
    // 14, 15, 16 };
    // String uuid1 = "01020304050607080910111213141516";
    UUID uuid1 = UUID.randomUUID();

    // byte[] valbit1 = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    // boolean[] valbit1 = new boolean[] { true, false, false, true, true,
    // false, true, false, true, false };
    // String valbit1 = "0101011100";

    TypesOtherDAO o = new TypesOtherDAO();
    o.setUui1(uuid1);
//    o.setXml1("<mytag>abc</mytag>");
//    o.setJso1("{ \"name\":\"John\", \"age\":31 }");
    o.setCom1("10, 20");
    // o.setBit1(valbit1);
    // o.setCurrentMood("happy");
    o.insert();

    for (TypesOtherDAO x : TypesOtherDAO.selectByExample(new TypesOtherDAO())) {
      System.out.println("other=" + x);
      UUID u1 = (UUID) x.getUui1();
      System.out.println(" --> uuid=" + u1);
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
