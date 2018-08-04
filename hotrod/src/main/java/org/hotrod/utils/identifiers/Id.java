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

  private String dashedName; // ------- my-property, car--price

  private DatabaseAdapter adapter;
  private List<NamePart> canonicalParts;

  // Constructor

  private Id(final DatabaseAdapter adapter, final List<NamePart> canonicalParts, final String canonicalSQLName,
      final String javaClassName, final String javaMemberName, final String javaConstantName, final String dashedName)
      throws InvalidIdentifierException {

    if (canonicalSQLName != null && adapter == null) {
      throw new InvalidIdentifierException("'adapter' cannot be null when the canonicalSQLName is specified.");
    }
    if (canonicalParts == null) {
      throw new InvalidIdentifierException("'canonicalParts' cannot be null.");
    }
    if (canonicalParts.isEmpty()) {
      throw new InvalidIdentifierException("'canonicalParts' cannot be empty.");
    }
    if (javaClassName == null) {
      throw new InvalidIdentifierException("'javaClassName' cannot be empty.");
    }
    if (javaMemberName == null) {
      throw new InvalidIdentifierException("'javaMemberName' cannot be empty.");
    }
    if (javaConstantName == null) {
      throw new InvalidIdentifierException("'javaConstantName' cannot be empty.");
    }
    if (dashedName == null) {
      throw new InvalidIdentifierException("'dashedName' cannot be empty.");
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

    this.javaClassName = javaClassName;
    this.javaMemberName = javaMemberName;

    this.javaConstantName = javaConstantName;
    this.javaGetter = "get" + this.javaClassName;
    this.javaSetter = "set" + this.javaClassName;

    this.dashedName = dashedName;

  }

  public static Id fromSQL(final String commonName, final boolean quoted, final DatabaseAdapter adapter)
      throws InvalidIdentifierException {
    if (adapter == null) {
      throw new InvalidIdentifierException("'adapter' cannot be null.");
    }
    if (commonName == null || commonName.isEmpty()) {
      throw new InvalidIdentifierException("'commonName' cannot be null or empty.");
    }

    String canonicalSQLName = adapter.canonizeName(commonName, quoted);

    List<NamePart> nameParts = splitSQL(commonName);
    if (nameParts == null || nameParts.isEmpty()) {
      throw new InvalidIdentifierException("SQL name must produce at least one part");
    }

    String javaClassName = assembleJavaClassName(nameParts);
    String javaMemberName = assembleJavaMemberName(nameParts);
    String javaConstantName = assembleJavaConstantName(nameParts);
    String dashedName = assembleDashedName(nameParts);

    Id id = new Id(adapter, nameParts, canonicalSQLName, javaClassName, javaMemberName, javaConstantName, dashedName);
    return id;
  }

  public static Id fromJavaClass(final String javaClassName) throws InvalidIdentifierException {
    if (javaClassName == null || javaClassName.isEmpty()) {
      throw new InvalidIdentifierException("'javaClassName' cannot be null or empty.");
    }
    if (!javaClassName.matches("[A-Z_][A-Za-z0-9_]*")) {
      throw new InvalidIdentifierException(
          "'javaClassName' must start with an upper case letter or underscore, and continue with letters, digits, or underscores.");
    }
    DatabaseAdapter adapter = null;
    String canonicalSQLName = null;
    List<NamePart> nameParts = splitJava(javaClassName);
    if (nameParts == null || nameParts.isEmpty()) {
      throw new InvalidIdentifierException("javaClassName must produce at least one part");
    }
    String javaMemberName = assembleJavaMemberName(nameParts);
    String javaConstantName = assembleJavaConstantName(nameParts);
    String dashedName = assembleDashedName(nameParts);

    Id id = new Id(adapter, nameParts, canonicalSQLName, javaClassName, javaMemberName, javaConstantName, dashedName);
    return id;
  }

  public static Id fromJavaMember(final String javaMemberName) throws InvalidIdentifierException {
    if (javaMemberName == null || javaMemberName.isEmpty()) {
      throw new InvalidIdentifierException("'javaMemberName' cannot be null or empty.");
    }
    if (!javaMemberName.matches("[a-z_][A-Za-z0-9_]*")) {
      throw new InvalidIdentifierException(
          "'javaMemberName' must start with a lower case letter or underscore, and continue with letters, digits, or underscores.");
    }
    DatabaseAdapter adapter = null;
    String canonicalSQLName = null;
    List<NamePart> nameParts = splitJava(javaMemberName);
    if (nameParts == null || nameParts.isEmpty()) {
      throw new InvalidIdentifierException("javaClassName must produce at least one part");
    }
    String javaClassName = assembleJavaClassName(nameParts);
    String javaConstantName = assembleJavaConstantName(nameParts);
    String dashedName = assembleDashedName(nameParts);

    Id id = new Id(adapter, nameParts, canonicalSQLName, javaClassName, javaMemberName, javaConstantName, dashedName);
    return id;
  }

  public static Id fromSQLAndJavaClass(final String commonName, final boolean quoted, final DatabaseAdapter adapter,
      final String javaClassName) throws InvalidIdentifierException {
    if (adapter == null) {
      throw new InvalidIdentifierException("'adapter' cannot be null.");
    }
    if (commonName == null || commonName.isEmpty()) {
      throw new InvalidIdentifierException("'commonName' cannot be null or empty.");
    }
    if (javaClassName == null || javaClassName.isEmpty()) {
      throw new InvalidIdentifierException("'javaClassName' cannot be null or empty.");
    }
    if (!javaClassName.matches("[A-Z_].*")) {
      throw new InvalidIdentifierException("'javaClassName' must start with an upper case letter or an underscore.");
    }

    String canonicalSQLName = adapter.canonizeName(commonName, quoted);

    List<NamePart> nameParts = splitSQL(commonName);
    if (nameParts == null || nameParts.isEmpty()) {
      throw new InvalidIdentifierException("SQL name must produce at least one part");
    }

    List<NamePart> javaNameParts = splitJava(javaClassName);
    String javaMemberName = assembleJavaMemberName(javaNameParts);
    String javaConstantName = assembleJavaConstantName(javaNameParts);
    String dashedName = assembleDashedName(javaNameParts);

    Id id = new Id(adapter, nameParts, canonicalSQLName, javaClassName, javaMemberName, javaConstantName, dashedName);
    return id;
  }

  public static Id fromSQLAndJavaMember(final String commonName, final boolean quoted, final DatabaseAdapter adapter,
      final String javaMemberName) throws InvalidIdentifierException {
    if (adapter == null) {
      throw new InvalidIdentifierException("'adapter' cannot be null.");
    }
    if (commonName == null || commonName.isEmpty()) {
      throw new InvalidIdentifierException("'commonName' cannot be null or empty.");
    }
    if (javaMemberName == null || javaMemberName.isEmpty()) {
      throw new InvalidIdentifierException("'javaMemberName' cannot be null or empty.");
    }
    if (!javaMemberName.matches("[a-z_].*")) {
      throw new InvalidIdentifierException("'javaMemberName' must start with an lower case letter or an underscore.");
    }

    String canonicalSQLName = adapter.canonizeName(commonName, quoted);

    List<NamePart> nameParts = splitSQL(commonName);
    if (nameParts == null || nameParts.isEmpty()) {
      throw new InvalidIdentifierException("SQL name must produce at least one part");
    }

    List<NamePart> javaNameParts = splitJava(javaMemberName);
    String javaClassName = assembleJavaClassName(javaNameParts);
    String javaConstantName = assembleJavaConstantName(javaNameParts);
    String dashedName = assembleDashedName(javaNameParts);

    Id id = new Id(adapter, nameParts, canonicalSQLName, javaClassName, javaMemberName, javaConstantName, dashedName);
    return id;
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

  public String getDashedName() {
    return dashedName;
  }

  public String getJavaGetter() {
    return this.javaGetter;
  }

  public String getJavaSetter() {
    return this.javaSetter;
  }

  public List<NamePart> getCanonicalParts() {
    return canonicalParts;
  }

  // Helper -- Parsing

  public static List<NamePart> splitSQL(final String sqlName) {
    List<NamePart> parts = new ArrayList<NamePart>();

    if (sqlName.matches("_+")) {
      parts.add(new NamePart(sqlName));
      return parts;
    }

    if (sqlName.matches("\\ +")) {
      String repeat = repeat("_", sqlName.length());
      parts.add(new NamePart(repeat));
      return parts;
    }

    String separator = sqlName.contains(" ") ? " " : "_";
    for (String p : sqlName.split(separator)) { // split on "_" and space
      if (!p.isEmpty()) {
        parts.add(new NamePart(p));
      }
    }
    return parts;
  }

  public static List<NamePart> splitJava(final String javaName) {

    List<NamePart> parts = new ArrayList<NamePart>();

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
      log(" - pos=" + lastStart);

      // Single char left

      if (javaName.length() - lastStart == 1) {
        log(" - Single char left");
        parts.add(new NamePart(javaName.substring(lastStart).toLowerCase()));
        return parts;
      }

      // Multiple chars left

      boolean firstUpper = Character.isUpperCase(javaName.charAt(lastStart));
      boolean secondUpper = Character.isUpperCase(javaName.charAt(lastStart + 1));

      if (firstUpper && secondUpper) { // acronym mode (full upper)
        log(" - acronym mode (full upper)");
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
          parts.add(new NamePart(javaName.substring(lastStart).toLowerCase(), true));
          lastStart = javaName.length();
        } else {
          parts.add(new NamePart(javaName.substring(lastStart, pos - 1).toLowerCase(), true));
          lastStart = pos - 1;
        }

      } else if (firstUpper && !secondUpper) { // class mode (upper + lower)
        log(" - class mode (upper + lower)");
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
          parts.add(new NamePart(javaName.substring(lastStart).toLowerCase()));
          lastStart = javaName.length();
        } else {
          parts.add(new NamePart(javaName.substring(lastStart, pos).toLowerCase()));
          lastStart = pos;
        }

      } else if (!firstUpper && secondUpper) { // single-letter - split!
        log(" - single-letter - split!");
        parts.add(new NamePart(javaName.substring(lastStart, lastStart + 1).toLowerCase()));
        lastStart++;

      } else { // member mode (full lower)
        log(" - member mode (full lower)");
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
          parts.add(new NamePart(javaName.substring(lastStart).toLowerCase()));
          lastStart = javaName.length();
        } else {
          parts.add(new NamePart(javaName.substring(lastStart, pos).toLowerCase()));
          lastStart = pos;
        }
      }

    }

    return parts;
  }

  // Helpers -- Rendering

  private static String assembleJavaClassName(final List<NamePart> canonicalParts) {
    StringBuilder sb = new StringBuilder();
    if (canonicalParts != null && !canonicalParts.isEmpty()
        && !canonicalParts.get(0).getToken().matches("[A-Za-z_].*")) {
      sb.append("_");
    }
    for (NamePart p : canonicalParts) {
      if (p.isAcronym()) {
        sb.append(p.getToken().toUpperCase());
      } else {
        sb.append(SUtils.sentenceFormat(p.getToken().toLowerCase()));
      }
    }
    return sb.toString();
  }

  private static String assembleJavaMemberName(final List<NamePart> canonicalParts) {
    StringBuilder sb = new StringBuilder();
    if (canonicalParts != null && !canonicalParts.isEmpty()
        && !canonicalParts.get(0).getToken().matches("[A-Za-z_].*")) {
      sb.append("_");
    }
    boolean first = true;
    for (NamePart p : canonicalParts) {
      if (first) {
        sb.append(p.getToken().toLowerCase());
      } else {
        if (p.isAcronym()) {
          sb.append(p.getToken().toUpperCase());
        } else {
          sb.append(SUtils.sentenceFormat(p.getToken().toLowerCase()));
        }
      }
      first = false;
    }
    return sb.toString();
  }

  private static String assembleJavaConstantName(final List<NamePart> canonicalParts) {
    StringBuilder sb = new StringBuilder();
    if (canonicalParts != null && !canonicalParts.isEmpty()
        && !canonicalParts.get(0).getToken().matches("[A-Za-z_].*")) {
      sb.append("_");
    }
    boolean first = true;
    for (NamePart p : canonicalParts) {
      if (!first) {
        sb.append("_");
      }
      sb.append(p.getToken().toUpperCase());
      first = false;
    }
    return sb.toString();
  }

  private static String assembleDashedName(final List<NamePart> canonicalParts) {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (NamePart p : canonicalParts) {
      if (!first) {
        sb.append("-");
      }
      sb.append(p.getToken().toLowerCase());
      first = false;
    }
    return sb.toString();
  }

  private static String repeat(final String s, final int times) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < times; i++) {
      sb.append(s);
    }
    return sb.toString();
  }

  private static void log(final String txt) {
    // System.out.println("[LOG] " + txt);
  }

  public static class NamePart {

    private String token;
    private boolean acronym;

    private NamePart(final String token, final boolean acronym) {
      if (token == null) {
        throw new IllegalArgumentException("Name part 'token' can not be null");
      }
      this.token = token;
      this.acronym = acronym;
    }

    private NamePart(final String token) {
      if (token == null) {
        throw new IllegalArgumentException("Name part 'token' can not be null");
      }
      this.token = token;
      this.acronym = false;
    }

    public String getToken() {
      return token;
    }

    public boolean isAcronym() {
      return acronym;
    }

    // equals

    @Override
    public boolean equals(final Object obj) {
      if (obj == null) {
        return false;
      }
      // As a NamePart
      try {
        NamePart p = (NamePart) obj;
        return p.acronym = this.acronym && p.token.equals(this.token);
      } catch (ClassCastException e) {
        // As a String
        try {
          String s = (String) obj;
          return this.acronym ? this.token.toUpperCase().equals(s) : this.token.equals(s);
        } catch (ClassCastException e2) {
          return false;
        }
      }
    }

  }

  // Exceptions

  public static class InvalidIdentifierException extends Exception {

    private static final long serialVersionUID = 1L;

    private InvalidIdentifierException(String message) {
      super(message);
    }

  }

}
