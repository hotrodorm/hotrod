package tests;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

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

    // Numeric

    TypesNumericDAO.deleteByExample(new TypesNumericVO()); // delete all rows

    TypesNumericVO n = new TypesNumericVO();
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

    TypesNumericDAO.insert(n);

    for (TypesNumericVO x : TypesNumericDAO.selectByExample(new TypesNumericVO())) {
      System.out.println("numeric=" + x);
    }

    // Chars

    TypesCharDAO.deleteByExample(new TypesCharVO());

    TypesCharVO c = new TypesCharVO();
    c.setCha1("a");
    c.setCha2("b");
    c.setCha3("c");
    TypesCharDAO.insert(c);

    for (TypesCharVO x : TypesCharDAO.selectByExample(new TypesCharVO())) {
      System.out.println("char=" + x);
    }

    // Date & Time

    TypesDateTimeDAO.deleteByExample(new TypesDateTimeVO());

    TypesDateTimeVO d = new TypesDateTimeVO();
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

    TypesDateTimeDAO.insert(d);

    for (TypesDateTimeVO x : TypesDateTimeDAO.selectByExample(new TypesDateTimeVO())) {
      System.out.println("date/time=" + x);
      System.out.println("date/time=" + x.getTim1());
    }

    // Binary

    TypesBinaryDAO.deleteByExample(new TypesBinaryVO());

    byte[] b1 = new byte[] { 1, 2, 3 };

    TypesBinaryVO b = new TypesBinaryVO();
    b.setBin1(b1);
    b.setBol1(false);
    TypesBinaryDAO.insert(b);

    // Other

    for (TypesBinaryVO x : TypesBinaryDAO.selectByExample(new TypesBinaryVO())) {
      System.out.println("binary=" //
          + x + " [" + renderBA(x.getBin1()) + "]" //
      );
    }

    // Extra

    // org.apache.ibatis.type.JdbcType.x

    // byte[] uuid1 = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
    // 14, 15, 16 };
    // String uuid1 = "01020304050607080910111213141516";
    UUID uuid1 = UUID.randomUUID();

    // byte[] valbit1 = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    // boolean[] valbit1 = new boolean[] { true, false, false, true, true,
    // false, true, false, true, false };
    // String valbit1 = "0101011100";

    TypesOtherVO o = new TypesOtherVO();
    o.setUui1(uuid1);
    // o.setXml1("<mytag>abc</mytag>");
    // o.setJso1("{ \"name\":\"John\", \"age\":31 }");
    o.setCom1("10, 20");
    // o.setBit1(valbit1);
    // o.setCurrentMood("happy");
    TypesOtherDAO.insert(o);

    for (TypesOtherVO x : TypesOtherDAO.selectByExample(new TypesOtherVO())) {
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
