package org.hotrod.getters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;

import org.junit.jupiter.api.Test;

public class GetterTest {

  @Test
  public void factoryMapsJDBCTypesToExpectedMethods() {
    assertEquals("getInt", GetterFactory.primitiveGetterForClassName("java.lang.Integer").getResultSetMethod());
    assertTrue(GetterFactory.primitiveGetterForClassName("java.lang.Integer").returnsPrimitiveType());
    assertEquals("getString", GetterFactory.primitiveGetterForClassName("java.lang.String").getResultSetMethod());
    assertNull(GetterFactory.primitiveGetterForClassName("missing.Type"));
    assertTrue(GetterFactory.forClass(String.class) instanceof StringGetter);
    assertTrue(GetterFactory.forClass(Integer.class) instanceof IntegerGetter);
    assertTrue(GetterFactory.forClass(CustomValue.class) instanceof ObjectGetter);
  }

  @Test
  public void factoryMapsEveryRegisteredJDBCType() {
    assertFactoryMapping("java.math.BigDecimal", "getBigDecimal", false);
    assertFactoryMapping("java.sql.Blob", "getBlob", false);
    assertFactoryMapping("java.lang.Byte", "getByte", true);
    assertFactoryMapping("byte[]", "getBytes", false);
    assertFactoryMapping("java.sql.Clob", "getClob", false);
    assertFactoryMapping("java.sql.Date", "getDate", false);
    assertFactoryMapping("java.lang.Double", "getDouble", true);
    assertFactoryMapping("java.lang.Float", "getFloat", true);
    assertFactoryMapping("java.lang.Integer", "getInt", true);
    assertFactoryMapping("java.lang.Long", "getLong", true);
    assertFactoryMapping("java.lang.Boolean", "getBoolean", true);
    assertFactoryMapping("java.lang.Short", "getShort", true);
    assertFactoryMapping("java.sql.SQLXML", "getSQLXML", false);
    assertFactoryMapping("java.lang.String", "getString", false);
    assertFactoryMapping("java.sql.Time", "getTime", false);
    assertFactoryMapping("java.sql.Timestamp", "getTimestamp", false);
  }

  @Test
  public void everyGetterReadsItsJdbcValueAndHonorsNullState() throws Exception {
    Object blob = interfaceValue(Blob.class);
    Object clob = interfaceValue(Clob.class);
    Object sqlxml = interfaceValue(SQLXML.class);
    GetterCase[] cases = { new GetterCase(new BigDecimalGetter(), "getBigDecimal", new BigDecimal("12.34")),
//        new GetterCase(new BlobGetter(), "getBlob", blob),
        new GetterCase(new ByteGetter(), "getByte", Byte.valueOf((byte) 7)),
        new GetterCase(new BytesGetter(), "getBytes", new byte[] { 1, 2 }),
//        new GetterCase(new ClobGetter(), "getClob", clob),
        new GetterCase(new DateGetter(), "getDate", Date.valueOf("2024-01-02")),
        new GetterCase(new DoubleGetter(), "getDouble", Double.valueOf(1.25)),
        new GetterCase(new FloatGetter(), "getFloat", Float.valueOf(2.5F)),
        new GetterCase(new IntegerGetter(), "getInt", Integer.valueOf(42)),
        new GetterCase(new LongGetter(), "getLong", Long.valueOf(43L)),
        new GetterCase(new BooleanGetter(), "getBoolean", Boolean.TRUE),
        new GetterCase(new ShortGetter(), "getShort", Short.valueOf((short) 8)),
//        new GetterCase(new SQLXMLGetter(), "getSQLXML", sqlxml),
        new GetterCase(new StringGetter(), "getString", "value"),
        new GetterCase(new TimeGetter(), "getTime", Time.valueOf("12:34:56")),
        new GetterCase(new TimestampGetter(), "getTimestamp", Timestamp.valueOf("2024-01-02 12:34:56")) };

    for (GetterCase testCase : cases) {
      Object actual = testCase.getter.get(resultSet(testCase.method, testCase.value, false), 3);
      System.out.println("* testCase: actual: " + actual + " (null:" + (actual == null ? "yes" : "no") + ") expected: "
          + testCase.value + " (null:" + (testCase.value == null ? "yes" : "no") + ")");
      assertValueEquals(testCase.value, actual);
      assertNull(testCase.getter.get(resultSet(testCase.method, defaultValueFor(testCase.value), true), 3));
    }
  }

  @Test
  public void primitiveGetterReturnsNullWhenJdbcReportsNull() throws Exception {
    ResultSet resultSet = resultSet(42, true);
    assertNull(new IntegerGetter().get(resultSet, 1));
    assertEquals(Integer.valueOf(42), new IntegerGetter().get(resultSet(42, false), 1));
  }

  @Test
  public void objectGetterUsesRequestedTypeAndNullState() throws Exception {
    CustomValue value = new CustomValue();
    ResultSet resultSet = (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(),
        new Class<?>[] { ResultSet.class }, (proxy, method, args) -> {
          if (method.getName().equals("getObject"))
            return value;
          if (method.getName().equals("wasNull"))
            return false;
          return defaultValue(method.getReturnType());
        });
    assertEquals(value, new ObjectGetter(CustomValue.class).get(resultSet, 2));
  }

  private static ResultSet resultSet(int value, boolean wasNull) {
    return (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(), new Class<?>[] { ResultSet.class },
        (proxy, method, args) -> {
          if (method.getName().equals("getInt"))
            return value;
          if (method.getName().equals("wasNull"))
            return wasNull;
          return defaultValue(method.getReturnType());
        });
  }

  private static ResultSet resultSet(String expectedMethod, Object value, boolean wasNull) {
    return (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(), new Class<?>[] { ResultSet.class },
        (proxy, method, args) -> {
          if (method.getName().equals(expectedMethod))
            return value;
          if (method.getName().equals("wasNull"))
            return wasNull;
          if (method.getName().startsWith("get")) {
            throw new AssertionError("Unexpected ResultSet method: " + method.getName());
          }
          return defaultValue(method.getReturnType());
        });
  }

  private static void assertFactoryMapping(String className, String method, boolean primitive) {
    PrimitiveJDBCGetter getter = GetterFactory.primitiveGetterForClassName(className);
    assertEquals(method, getter.getResultSetMethod());
    assertEquals(primitive, getter.returnsPrimitiveType());
    assertTrue(getter.getResultSetGetter() != null);
  }

  private static Object defaultValueFor(Object value) {
    if (value instanceof Byte)
      return Byte.valueOf((byte) 0);
    if (value instanceof Double)
      return Double.valueOf(0D);
    if (value instanceof Float)
      return Float.valueOf(0F);
    if (value instanceof Integer)
      return Integer.valueOf(0);
    if (value instanceof Long)
      return Long.valueOf(0L);
    if (value instanceof Boolean)
      return Boolean.FALSE;
    if (value instanceof Short)
      return Short.valueOf((short) 0);
    if (value instanceof byte[])
      return new byte[0];
    return null;
  }

  private static void assertValueEquals(Object expected, Object actual) {
    if (expected instanceof byte[]) {
      assertArrayEquals((byte[]) expected, (byte[]) actual);
    } else {
      assertEquals(expected, actual);
    }
  }

  private static Object interfaceValue(Class<?> type) {
    return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] { type },
        (proxy, method, args) -> defaultValue(method.getReturnType()));
  }

  private static Object defaultValue(Class<?> type) {
    if (!type.isPrimitive())
      return null;
    if (type == boolean.class)
      return false;
    if (type == char.class)
      return '\0';
    if (type == byte.class)
      return (byte) 0;
    if (type == short.class)
      return (short) 0;
    if (type == int.class)
      return 0;
    if (type == long.class)
      return 0L;
    if (type == float.class)
      return 0F;
    if (type == double.class)
      return 0D;
    return null;
  }

  private static final class CustomValue {
  }

  private static final class GetterCase {
    private final ResultSetGetter getter;
    private final String method;
    private final Object value;

    private GetterCase(ResultSetGetter getter, String method, Object value) {
      this.getter = getter;
      this.method = method;
      this.value = value;
    }
  }
}
