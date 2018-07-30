package org.hotrod.utils.identifiers;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.runtime.util.SUtils;

public class Id {

  // Properties

  private String canonicalSQLName; // - MY_PROPERTY, Car$Price
  private String renderedSQLName; // -- my_property, "Car$Price"

  private String javaClassName; // ---- MyProperty, Car_Price
  private String javaMemberName; // --- myProperty, car_Price

  private String javaConstantName; // - MY_PROPERTY, CAR__PRICE
  private String javaGetter; // ------- getMyProperty, getCar_Price
  private String javaSetter; // ------- setMyProperty, setCar_Price

  private String mapperName; // ------- my-property, car--price

  private DatabaseAdapter adapter;
  private List<String> canonicalParts;

  // Constructor

  private Id(final DatabaseAdapter adapter, final List<String> canonicalParts, final String canonicalSQLName,
      final String javaClassName, final String javaMemberName) throws InvalidIdentifierException {

    if (canonicalSQLName != null && adapter == null) {
      throw new InvalidIdentifierException("'adapter' cannot be null when the canonicalSQLName is specified.");
    }
    if (canonicalParts == null) {
      throw new InvalidIdentifierException("'canonicalParts' cannot be null.");
    }
    if (canonicalParts.isEmpty()) {
      throw new InvalidIdentifierException("'canonicalParts' cannot be empty.");
    }

    this.adapter = adapter;
    this.canonicalParts = canonicalParts;

    if (canonicalSQLName != null) { // has a SQL side
      this.canonicalSQLName = canonicalSQLName;
      this.renderedSQLName = this.adapter.renderSQLName(this.canonicalSQLName);
    } else { // does not have a SQL side
      this.canonicalSQLName = null;
      this.renderedSQLName = null;
    }

    if (javaClassName != null) {
      this.javaClassName = javaClassName;
    } else {
      this.javaClassName = assembleJavaClassName(this.canonicalParts);
    }

    if (javaMemberName != null) {
      this.javaMemberName = javaMemberName;
    } else {
      this.javaMemberName = assembleJavaMemberName(this.canonicalParts);
    }

    this.javaConstantName = assembleJavaConstantName(this.canonicalParts);
    this.javaGetter = "get" + this.javaClassName;
    this.javaSetter = "set" + this.javaClassName;

    this.mapperName = assembleMapperName(this.canonicalParts);

  }

  public static Id fromSQL(final String sqlName, final boolean quoted, final DatabaseAdapter adapter)
      throws InvalidIdentifierException {
    if (adapter == null) {
      throw new InvalidIdentifierException("'adapter' cannot be null.");
    }
    if (sqlName == null || sqlName.isEmpty()) {
      throw new InvalidIdentifierException("'sqlName' cannot be null or empty.");
    }
    String canonicalSQLName = adapter.canonizeName(sqlName, quoted);
    List<String> canonicalParts = splitSQL(sqlName);
    String javaClassName = null;
    String javaMemberName = null;
    Id id = new Id(adapter, canonicalParts, canonicalSQLName, javaClassName, javaMemberName);
    return id;
  }

  public static Id fromJavaClass(final String javaClassName) throws InvalidIdentifierException {
    if (javaClassName == null || javaClassName.isEmpty()) {
      throw new InvalidIdentifierException("'javaClassName' cannot be null or empty.");
    }
    DatabaseAdapter adapter = null;
    // String canonicalSQLName = adapter.canonizeName(sqlName, quoted);
    // List<String> canonicalParts = splitSQL(sqlName);
    String javaMemberName = javaClassName.substring(0, 1).toLowerCase() + javaClassName.substring(1);
//    Id id = new Id(adapter, canonicalParts, canonicalSQLName, javaClassName, javaMemberName);
//    return id;
    return null;
  }

  public static Id fromJavaMember(final String javaMemberName) {
    return null;
  }

  public static Id fromSQLAndJavaClass(final String sqlName, final boolean quoted, final DatabaseAdapter adapter,
      final String javaClassName) {
    return null;
  }

  public static Id fromSQLAndJavaMember(final String sqlName, final boolean quoted, final DatabaseAdapter adapter,
      final String javaPropertyName) {
    return null;
  }

  // Getters

  public String getCanonicalSQLName() {
    return this.canonicalSQLName;
  }

  public String getRenderedSQLName() {
    return this.renderedSQLName;
  }

  public String getJavaClassName() {
    return this.javaClassName;
  }

  public String getJavaMemberName() {
    return this.javaMemberName;
  }

  public String getJavaConstantName() {
    return javaConstantName;
  }

  public String getMapperName() {
    return mapperName;
  }

  public String getJavaGetter() {
    return this.javaGetter;
  }

  public String getJavaSetter() {
    return this.javaSetter;
  }

  // Helper -- Parsing

  public static List<String> splitSQL(final String sqlName) {
    List<String> parts = new ArrayList<String>();
    for (String p : sqlName.split("[_ ]")) { // split on "_" and space
      if (!p.isEmpty()) {
        parts.add(p.toLowerCase());
      }
    }
    return parts;
  }

  public static List<String> splitJava(final String javaName) {
    List<String> parts = new ArrayList<String>();

    // null or empty

    if (javaName == null || javaName.isEmpty()) {
      return parts;
    }

    int lastStart = 0;

    // a -- resolved
    // A -- resolved
    // _ -- resolved
    // _B
    // _aB
    // a_aB
    // A_Bb
    // aBb
    // aaBb
    // AaBb
    // aaaBb
    // AaaBb
    // AAABb

    while (lastStart < javaName.length()) {

      // Single char left

      if (javaName.length() == 1) {
        parts.add(javaName.toLowerCase());
        return parts;
      }

      // Multiple chars left

      boolean firstUpper = Character.isUpperCase(javaName.charAt(lastStart));
      boolean secondUpper = Character.isUpperCase(javaName.charAt(lastStart + 1));

      if (firstUpper && secondUpper) { // acronym mode (full upper)
        int pos = lastStart + 2;
        boolean goOn = true;
        while (goOn && pos < javaName.length()) {
          if (!Character.isUpperCase(javaName.charAt(pos))) {
            goOn = false;
          } else {
            pos++;
          }
        }
        if (goOn) {
          parts.add(javaName.substring(lastStart).toLowerCase());
          lastStart = javaName.length();
        } else {
          parts.add(javaName.substring(lastStart, pos - 1).toLowerCase());
          lastStart = pos - 1;
        }

      } else if (firstUpper && !secondUpper) { // class mode (upper + lower)
        int pos = lastStart + 2;
        boolean goOn = true;
        while (goOn && pos < javaName.length()) {
          if (Character.isUpperCase(javaName.charAt(pos))) {
            goOn = false;
          } else {
            pos++;
          }
        }
        if (goOn) {
          parts.add(javaName.substring(lastStart).toLowerCase());
          lastStart = javaName.length();
        } else {
          parts.add(javaName.substring(lastStart, pos).toLowerCase());
          lastStart = pos;
        }

      } else if (!firstUpper && secondUpper) { // single-letter - split!
        parts.add(javaName.substring(lastStart, lastStart + 1).toLowerCase());
        lastStart++;

      } else { // member mode (full lower)
        int pos = lastStart + 2;
        boolean goOn = true;
        while (goOn && pos < javaName.length()) {
          if (Character.isUpperCase(javaName.charAt(pos))) {
            goOn = false;
          } else {
            pos++;
          }
        }
        if (goOn) {
          parts.add(javaName.substring(lastStart).toLowerCase());
          lastStart = javaName.length();
        } else {
          parts.add(javaName.substring(lastStart, pos).toLowerCase());
          lastStart = pos;
        }
      }

    }

    return parts;
  }

  // Helpers -- Rendering

  private String assembleJavaClassName(final List<String> canonicalParts) {
    StringBuilder sb = new StringBuilder();
    for (String p : canonicalParts) {
      sb.append(SUtils.capitalize(p));
    }
    return sb.toString();
  }

  private String assembleJavaMemberName(final List<String> canonicalParts) {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (String p : canonicalParts) {
      if (first) {
        first = false;
        sb.append(p.toLowerCase());
      } else {
        sb.append(SUtils.capitalize(p));
      }
    }
    return sb.toString();
  }

  private String assembleJavaConstantName(final List<String> canonicalParts) {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (String p : canonicalParts) {
      if (first) {
        first = false;
      } else {
        sb.append("_");
      }
      sb.append(p.toUpperCase());
    }
    return sb.toString();
  }

  private String assembleMapperName(final List<String> canonicalParts) {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (String p : canonicalParts) {
      if (first) {
        first = false;
      } else {
        sb.append("-");
      }
      sb.append(p.toLowerCase());
    }
    return sb.toString();
  }

  // Exceptions

  public static class InvalidIdentifierException extends Exception {

    private static final long serialVersionUID = 1L;

    private InvalidIdentifierException(String message) {
      super(message);
    }

  }

}
