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
import hotrod.test.generation.TypesOtherVO;
import hotrod.test.generation.TypesPseudo1VO;
import hotrod.test.generation.TypesPseudo2VO;
import hotrod.test.generation.primitives.TypesBinaryDAO;
import hotrod.test.generation.primitives.TypesCharDAO;
import hotrod.test.generation.primitives.TypesDateTimeDAO;
import hotrod.test.generation.primitives.TypesNumericDAO;
import hotrod.test.generation.primitives.TypesOtherDAO;
import hotrod.test.generation.primitives.TypesPseudo1DAO;
import hotrod.test.generation.primitives.TypesPseudo2DAO;

public class TypesTest {

  public static void main(final String[] args) throws IOException, SQLException {
    testTypes();
  }

  private static void testTypes() throws SQLException {

    // Numeric

    TypesNumericDAO.deleteByExample(new TypesNumericVO());

    TypesNumericVO n = new TypesNumericVO();
    n.setNum1((short) 1234);
    n.setNum2(123456789);
    n.setNum3(987654321);
    n.setNum4(1111222233334444L);
    n.setNum5(123);
    n.setNum6(456);
    n.setNum7(789);
    n.setNum8(BigDecimal.TEN);
    n.setNum8a((byte) 99);
    n.setNum8b((short) 9999);
    n.setNum8c(99999999);
    n.setNum8d(999999999999999999L);
    n.setNum8e(new BigInteger("9999999999999999999999999999999"));
    n.setNum9(BigDecimal.ONE);
    n.setNum10(BigDecimal.ONE);
    n.setNum11(123.456f);
    n.setNum12(789.456789123);
    n.setNum13(123.45678901);
    TypesNumericDAO.insert(n);

    for (TypesNumericVO x : TypesNumericDAO.selectByExample(new TypesNumericVO())) {
      System.out.println("numeric=" + x);
    }

    // Chars

    byte[] b1 = new byte[] { 1, 2, 3 };
    byte[] b2 = new byte[] { 4, 5, 6 };
    byte[] b3 = new byte[] { 7, 8, 9 };

    TypesCharDAO.deleteByExample(new TypesCharVO());

    TypesCharVO c = new TypesCharVO();
    c.setCha1("a");
    c.setCha2("b");
    c.setCha3("c");
    c.setCha4("d");
    c.setCha5("e");
    c.setCha6("f");
    c.setCha7("g");
    c.setCha8("h");
    c.setCha9("i");
    c.setCha10("j");
    c.setCha11("k");
    c.setCha12("l");
    c.setCha13(b1);
    c.setCha14(b2);
    c.setCha15(b3);
    TypesCharDAO.insert(c);

    for (TypesCharVO x : TypesCharDAO.selectByExample(new TypesCharVO())) {
      System.out.println("char=" + x + " [" + renderBA(x.getCha13()) + "]" + " [" + renderBA(x.getCha14()) + "]" + " ["
          + renderBA(x.getCha15()) + "]");
    }

    // Date & Time

    TypesDateTimeDAO.deleteByExample(new TypesDateTimeVO());

    TypesDateTimeVO d = new TypesDateTimeVO();
    d.setDat1(new java.sql.Date(System.currentTimeMillis()));
    d.setDat2(new java.sql.Time(System.currentTimeMillis()));
    d.setDat3(new Timestamp(System.currentTimeMillis()));
    TypesDateTimeDAO.insert(d);

    for (TypesDateTimeVO x : TypesDateTimeDAO.selectByExample(new TypesDateTimeVO())) {
      System.out.println("date/time=" + x);
    }

    // Binary

    TypesBinaryDAO.deleteByExample(new TypesBinaryVO());

    TypesBinaryVO b = new TypesBinaryVO();
    b.setBin1(b1);
    TypesBinaryDAO.insert(b);

    for (TypesBinaryVO x : TypesBinaryDAO.selectByExample(new TypesBinaryVO())) {
      System.out.println("binary=" + x + " [" + renderBA(x.getBin1()) + "]");
    }

    // Other

    TypesOtherDAO.deleteByExample(new TypesOtherVO());

    TypesOtherVO o = new TypesOtherVO();
    o.setOth1("<body>abc</body>");
    TypesOtherDAO.insert(o);

    for (TypesOtherVO x : TypesOtherDAO.selectByExample(new TypesOtherVO())) {
      System.out.println("other=" + x);
    }

    // Pseudo 1

    for (TypesPseudo1VO x : TypesPseudo1DAO.selectByExample(new TypesPseudo1VO())) {
      System.out.println("pseudo1=" + x);
    }

    // Pseudo 1

    for (TypesPseudo2VO x : TypesPseudo2DAO.selectByExample(new TypesPseudo2VO())) {
      System.out.println("pseudo2=" + x);
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
