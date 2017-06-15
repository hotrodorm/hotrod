package org.hotrod.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.runtime.util.ListWriter;
import org.hotrod.utils.SUtils;

public class ConverterTag {

  static final String TAG_NAME = "converter";

  private static final String NAME_PATTERN = "[a-zA-Z][a-zA-Z0-9_]*";
  private static final String FULL_CLASS_NAME_PATTERN = "[a-zA-Z][a-zA-Z0-9_]*(\\.[a-zA-Z][a-zA-Z0-9_]*)*(\\[\\])?";

  private String name = null;
  private String javaType = null;
  private String javaIntermediateType = null;
  private String javaClass = null;

  private String jdbcGetterMethod;
  private String jdbcSetterMethod;

  public void validate() throws InvalidConfigurationFileException {

    // name

    if (SUtils.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(
          "Attribute 'name' of tag <" + TAG_NAME + "> cannot be empty. " + "You must specify a converter name.");
    }
    if (!this.name.matches(NAME_PATTERN)) {
      throw new InvalidConfigurationFileException("Attribute 'name' of tag <" + TAG_NAME
          + "> must be a valid name. An alphanumeric name was expected but '" + this.name + "' was specified.");
    }

    // java-type

    if (SUtils.isEmpty(this.javaType)) {
      throw new InvalidConfigurationFileException(
          "Attribute 'java-type' of tag <" + TAG_NAME + "> with name '" + this.name + "' cannot be empty.");
    }
    if (!this.javaType.matches(FULL_CLASS_NAME_PATTERN)) {
      throw new InvalidConfigurationFileException("Attribute 'java-type' of tag <" + TAG_NAME
          + "> must be a valid java full class name, but '" + this.javaType + "' was specified.");
    }

    // java-intermediate-type

    if (SUtils.isEmpty(this.javaIntermediateType)) {
      throw new InvalidConfigurationFileException("Attribute 'java-intermediate-type' of tag <" + TAG_NAME
          + "> with name '" + this.name + "' cannot be empty.");
    }
    if (!this.javaIntermediateType.matches(FULL_CLASS_NAME_PATTERN)) {
      throw new InvalidConfigurationFileException("Attribute 'java-intermediate-type' of tag <" + TAG_NAME
          + "> must be a valid java full class name, but '" + this.javaIntermediateType + "' was specified.");
    }
    Accessors gs = ACCESSORS.get(this.javaIntermediateType);
    if (gs == null) {

      String types = ListWriter.render(new ArrayList<String>(ACCESSORS.keySet()), "", " - ", "\n", "", "", "");
      throw new InvalidConfigurationFileException("Unsupported java-intermediate-type '" + this.javaIntermediateType
          + "' on tag <" + TAG_NAME + "> with name '" + this.name
          + "'. No simple JDBC getter and setter found for this type. " + "The supported types are:\n" + types);
    }
    this.jdbcGetterMethod = gs.getGetter();
    this.jdbcSetterMethod = gs.getSetter();

    // class

    if (SUtils.isEmpty(this.javaClass)) {
      throw new InvalidConfigurationFileException(
          "Attribute 'class' of tag <" + TAG_NAME + "> with name '" + this.name + "' cannot be empty.");
    }
    if (!this.javaClass.matches(FULL_CLASS_NAME_PATTERN)) {
      throw new InvalidConfigurationFileException("Attribute 'java-class' of tag <" + TAG_NAME
          + "> must be a valid java full class name, but '" + this.javaType + "' was specified.");
    }

  }

  private static class Accessors {
    private String getter;
    private String setter;

    public Accessors(String getter, String setter) {
      super();
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

  private static Map<String, Accessors> ACCESSORS = new LinkedHashMap<String, Accessors>();
  static {

    ACCESSORS.put("String", new Accessors("getString", "setString"));
    ACCESSORS.put("java.lang.String", new Accessors("getString", "setString"));
    ACCESSORS.put("java.sql.Clob", new Accessors("getClob", "setClob"));

    ACCESSORS.put("Byte", new Accessors("getByte", "setByte"));
    ACCESSORS.put("java.lang.Byte", new Accessors("getByte", "setByte"));
    ACCESSORS.put("Short", new Accessors("getShort", "setShort"));
    ACCESSORS.put("java.lang.Short", new Accessors("getShort", "setShort"));
    ACCESSORS.put("Integer", new Accessors("getInt", "setInt"));
    ACCESSORS.put("java.lang.Integer", new Accessors("getInt", "setInt"));
    ACCESSORS.put("Long", new Accessors("getLong", "setLong"));
    ACCESSORS.put("java.lang.Long", new Accessors("getLong", "setLong"));
    ACCESSORS.put("java.math.BigDecimal", new Accessors("getBigDecimal", "setBigDecimal"));
    ACCESSORS.put("Float", new Accessors("getFloat", "setFloat"));
    ACCESSORS.put("java.lang.Float", new Accessors("getFloat", "setFloat"));
    ACCESSORS.put("Double", new Accessors("getDouble", "setDouble"));
    ACCESSORS.put("java.lang.Double", new Accessors("getDouble", "setDouble"));

    ACCESSORS.put("java.sql.Date", new Accessors("getDate", "setDate"));
    ACCESSORS.put("java.sql.Time", new Accessors("getTime", "setTime"));
    ACCESSORS.put("java.sql.Timestamp", new Accessors("getTimestamp", "setTimestamp"));

    ACCESSORS.put("Boolean", new Accessors("getBoolean", "setBoolean"));
    ACCESSORS.put("java.lang.Boolean", new Accessors("getBoolean", "setBoolean"));

    ACCESSORS.put("byte[]", new Accessors("getBytes", "setBytes"));
    ACCESSORS.put("java.sql.Blob", new Accessors("getBlob", "setBlob"));

    ACCESSORS.put("java.sql.Array", new Accessors("getArray", "setArray"));
    ACCESSORS.put("java.sql.NClob", new Accessors("getNClob", "setNClob"));
    ACCESSORS.put("java.sql.SQLXML", new Accessors("getSQLXML", "setSQLXML"));
    ACCESSORS.put("java.net.URL", new Accessors("getURL", "setURL"));
    ACCESSORS.put("java.sql.RowId", new Accessors("getRowId", "setRowId"));
    ACCESSORS.put("java.sql.Ref", new Accessors("getRef", "setRef"));

  }

  // Setters (digester)

  public void setName(String name) {
    this.name = name;
  }

  public void setJavaType(String javaType) {
    this.javaType = javaType;
  }

  public void setJavaIntermediateType(String javaIntermediateType) {
    this.javaIntermediateType = javaIntermediateType;
  }

  public void setJavaClass(String javaClass) {
    this.javaClass = javaClass;
  }

  // Getters

  public String getName() {
    return name;
  }

  public String getJavaType() {
    return javaType;
  }

  public String getJavaIntermediateType() {
    return javaIntermediateType;
  }

  public String getJavaClass() {
    return javaClass;
  }

  public String getJdbcGetterMethod() {
    return jdbcGetterMethod;
  }

  public String getJdbcSetterMethod() {
    return jdbcSetterMethod;
  }

}
