package org.hotrod.utils;

import java.util.HashMap;
import java.util.Map;

public class TypesUtil {

  private TypesUtil() {
  }

  private static Map<String, String> TYPE_SYNONYMS = new HashMap<>();
  static {
    TYPE_SYNONYMS.put("Byte", "java.lang.Byte");
    TYPE_SYNONYMS.put("Short", "java.lang.Short");
    TYPE_SYNONYMS.put("Integer", "java.lang.Integer");
    TYPE_SYNONYMS.put("Long", "java.lang.Long");
    TYPE_SYNONYMS.put("Float", "java.lang.Float");
    TYPE_SYNONYMS.put("Double", "java.lang.Double");
    TYPE_SYNONYMS.put("Boolean", "java.lang.Boolean");
    TYPE_SYNONYMS.put("String", "java.lang.String");
  }

  public static String expand(String type) {
    String found = TYPE_SYNONYMS.get(type);
    return found == null ? type : found;
  }

  private static final String FULL_CLASS_NAME_PATTERN = "[a-zA-Z][a-zA-Z0-9_]*+(\\.[a-zA-Z][a-zA-Z0-9_]*+)*+(\\[\\])?";

  public static boolean isValidClassName(String className) {
    return className.matches(FULL_CLASS_NAME_PATTERN);
  }

  // Accessors

  public static class Accessors {
    private String getter;
    private String setter;

    public Accessors(String getter, String setter) {
      this.getter = getter;
      this.setter = setter;
    }

    public String getGetter() {
      return getter;
    }

    public String getSetter() {
      return setter;
    }

  }

//  private static Map<String, Accessors> ACCESSORS = new LinkedHashMap<>();
//  private static List<String> ACCESSORS_KEYS = new ArrayList<>();
//  static {
//
//    ACCESSORS.put("String", new Accessors("getString", "setString"));
//    ACCESSORS.put("java.lang.String", new Accessors("getString", "setString"));
//    ACCESSORS.put("java.sql.Clob", new Accessors("getClob", "setClob"));
//
//    ACCESSORS.put("Byte", new Accessors("getByte", "setByte"));
//    ACCESSORS.put("java.lang.Byte", new Accessors("getByte", "setByte"));
//    ACCESSORS.put("Short", new Accessors("getShort", "setShort"));
//    ACCESSORS.put("java.lang.Short", new Accessors("getShort", "setShort"));
//    ACCESSORS.put("Integer", new Accessors("getInt", "setInt"));
//    ACCESSORS.put("java.lang.Integer", new Accessors("getInt", "setInt"));
//    ACCESSORS.put("Long", new Accessors("getLong", "setLong"));
//    ACCESSORS.put("java.lang.Long", new Accessors("getLong", "setLong"));
//    ACCESSORS.put("java.math.BigDecimal", new Accessors("getBigDecimal", "setBigDecimal"));
//    ACCESSORS.put("Float", new Accessors("getFloat", "setFloat"));
//    ACCESSORS.put("java.lang.Float", new Accessors("getFloat", "setFloat"));
//    ACCESSORS.put("Double", new Accessors("getDouble", "setDouble"));
//    ACCESSORS.put("java.lang.Double", new Accessors("getDouble", "setDouble"));
//
//    ACCESSORS.put("java.sql.Date", new Accessors("getDate", "setDate"));
//    ACCESSORS.put("java.sql.Time", new Accessors("getTime", "setTime"));
//    ACCESSORS.put("java.sql.Timestamp", new Accessors("getTimestamp", "setTimestamp"));
//
//    ACCESSORS.put("Boolean", new Accessors("getBoolean", "setBoolean"));
//    ACCESSORS.put("java.lang.Boolean", new Accessors("getBoolean", "setBoolean"));
//
//    ACCESSORS.put("byte[]", new Accessors("getBytes", "setBytes"));
//    ACCESSORS.put("java.sql.Blob", new Accessors("getBlob", "setBlob"));
//
//    ACCESSORS.put("java.sql.Array", new Accessors("getArray", "setArray"));
//    ACCESSORS.put("java.sql.NClob", new Accessors("getNClob", "setNClob"));
//    ACCESSORS.put("java.sql.SQLXML", new Accessors("getSQLXML", "setSQLXML"));
//    ACCESSORS.put("java.net.URL", new Accessors("getURL", "setURL"));
//    ACCESSORS.put("java.sql.RowId", new Accessors("getRowId", "setRowId"));
//    ACCESSORS.put("java.sql.Ref", new Accessors("getRef", "setRef"));
//
//    ACCESSORS.put("Object", new Accessors("getObject", "setObject"));
//    ACCESSORS.put("java.lang.Object", new Accessors("getObject", "setObject"));
//
//    ACCESSORS_KEYS = new ArrayList<>(ACCESSORS.keySet());
//  }
//
//  public static Accessors accessorsForType(String type) {
//    return ACCESSORS.get(type);
//  }
//
//  public static List<String> getAccessorsKeys() {
//    return ACCESSORS_KEYS;
//  }

}
