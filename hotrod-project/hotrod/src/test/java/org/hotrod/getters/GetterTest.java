package org.hotrod.getters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Proxy;
import java.sql.ResultSet;

import org.junit.Test;

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
          if (method.getName().equals("getObject")) return value;
          if (method.getName().equals("wasNull")) return false;
          return defaultValue(method.getReturnType());
        });
    assertEquals(value, new ObjectGetter(CustomValue.class).get(resultSet, 2));
  }

  private static ResultSet resultSet(int value, boolean wasNull) {
    return (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(), new Class<?>[] { ResultSet.class },
        (proxy, method, args) -> {
          if (method.getName().equals("getInt")) return value;
          if (method.getName().equals("wasNull")) return wasNull;
          return defaultValue(method.getReturnType());
        });
  }

  private static Object defaultValue(Class<?> type) {
    if (!type.isPrimitive()) return null;
    if (type == boolean.class) return false;
    if (type == char.class) return '\0';
    if (type == byte.class) return (byte) 0;
    if (type == short.class) return (short) 0;
    if (type == int.class) return 0;
    if (type == long.class) return 0L;
    if (type == float.class) return 0F;
    if (type == double.class) return 0D;
    return null;
  }

  private static final class CustomValue {
  }
}
