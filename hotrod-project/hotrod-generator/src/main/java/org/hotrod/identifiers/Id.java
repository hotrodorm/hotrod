package org.hotrod.identifiers;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrodorm.hotrod.utils.SUtil;

public class Id implements Comparable<Id> {

  private static final Logger log = LogManager.getLogger(Id.class);

  // Properties

  private boolean isQuoted;

  private String canonicalSQLName; // - MY_PROPERTY, Car$Price
  private String renderedSQLName; // -- my_property, "Car$Price"

  private boolean wasJavaNameSpecified;
  private String javaClassName; // ---- MyProperty, Car_Price
  private String javaMemberName; // --- myProperty, car_Price

  private String javaConstantName; // - MY_PROPERTY, CAR__PRICE
  private String javaGetter; // ------- getMyProperty, getCar_Price
  private String javaSetter; // ------- setMyProperty, setCar_Price

  private String dashedName; // ------- my-property, car--price

  private DatabaseAdapter adapter;
  private List<NamePart> nameParts;

  // Constructor

  private Id(final DatabaseAdapter adapter, final List<NamePart> nameParts, final String canonicalSQLName,
      final boolean wasJavaNameSpecified, final String javaClassName, final String javaMemberName,
      final String javaConstantName, final String dashedName, final boolean isQuoted)
      throws InvalidIdentifierException {

    this.isQuoted = isQuoted;

    if (canonicalSQLName != null && adapter == null) {
      throw new InvalidIdentifierException("'adapter' cannot be null when the canonicalSQLName is specified.");
    }
    if (nameParts == null) {
      throw new InvalidIdentifierException("'nameParts' cannot be null.");
    }
    if (nameParts.isEmpty()) {
      throw new InvalidIdentifierException("'nameParts' cannot be empty.");
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
    this.nameParts = nameParts;

    if (canonicalSQLName != null) { // has a SQL side
      this.canonicalSQLName = canonicalSQLName;
      this.renderedSQLName = this.adapter.renderSQLName(this.canonicalSQLName, this.isQuoted);
    } else { // does not have a SQL side
      this.canonicalSQLName = null;
      this.renderedSQLName = null;

    }

    this.wasJavaNameSpecified = wasJavaNameSpecified;
    this.javaClassName = javaClassName;
    this.javaMemberName = javaMemberName;

    this.javaConstantName = javaConstantName;
    this.javaGetter = "get" + this.javaClassName;
    this.javaSetter = "set" + this.javaClassName;

    this.dashedName = dashedName;

  }

  public static Id fromTypedSQL(final String typedName, final DatabaseAdapter adapter)
      throws InvalidIdentifierException {
    if (adapter == null) {
      throw new InvalidIdentifierException("'adapter' cannot be null.");
    }
    if (typedName == null || typedName.isEmpty()) {
      throw new InvalidIdentifierException("'typedName' cannot be null or empty.");
    }

    log.debug("typedName=" + typedName);

    SQLName sqlName = new SQLName(typedName);

    String canonicalSQLName = adapter.canonizeName(sqlName.getName(), sqlName.isQuoted());

    List<NamePart> nameParts = splitSQL(sqlName.getName());
    if (nameParts == null || nameParts.isEmpty()) {
      throw new InvalidIdentifierException("SQL name must produce at least one part");
    }

    String javaClassName = assembleJavaClassName(nameParts);
    String javaMemberName = assembleJavaMemberName(nameParts);
    String javaConstantName = assembleJavaConstantName(nameParts);
    String dashedName = assembleDashedName(nameParts);

    Id id = new Id(adapter, nameParts, canonicalSQLName, false, javaClassName, javaMemberName, javaConstantName,
        dashedName, sqlName.isQuoted());
    return id;
  }

  public static Id fromCanonicalSQL(final String canonicalSQLName, final DatabaseAdapter adapter)
      throws InvalidIdentifierException {
    if (adapter == null) {
      throw new InvalidIdentifierException("'adapter' cannot be null.");
    }
    if (canonicalSQLName == null || canonicalSQLName.isEmpty()) {
      throw new InvalidIdentifierException("'canonicalSQLName ' cannot be null or empty.");
    }

    List<NamePart> nameParts = splitSQL(canonicalSQLName);
    if (nameParts == null || nameParts.isEmpty()) {
      throw new InvalidIdentifierException("SQL name must produce at least one part");
    }

    String javaClassName = assembleJavaClassName(nameParts);
    String javaMemberName = assembleJavaMemberName(nameParts);
    String javaConstantName = assembleJavaConstantName(nameParts);
    String dashedName = assembleDashedName(nameParts);

    Id id = new Id(adapter, nameParts, canonicalSQLName, false, javaClassName, javaMemberName, javaConstantName,
        dashedName, true);
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

    Id id = new Id(adapter, nameParts, canonicalSQLName, true, javaClassName, javaMemberName, javaConstantName,
        dashedName, true);
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

    Id id = new Id(adapter, nameParts, canonicalSQLName, true, javaClassName, javaMemberName, javaConstantName,
        dashedName, true);
    return id;
  }

  public static Id fromTypedSQLAndJavaClass(final String typedName, final DatabaseAdapter adapter,
      final String javaClassName) throws InvalidIdentifierException {
    if (adapter == null) {
      throw new InvalidIdentifierException("'adapter' cannot be null.");
    }
    if (typedName == null || typedName.isEmpty()) {
      throw new InvalidIdentifierException("'typedName' cannot be null or empty.");
    }
    if (javaClassName == null || javaClassName.isEmpty()) {
      throw new InvalidIdentifierException("'javaClassName' cannot be null or empty.");
    }
    if (!javaClassName.matches("[A-Z_].*")) {
      throw new InvalidIdentifierException("'javaClassName' must start with an upper case letter or an underscore.");
    }

    SQLName sqlName = new SQLName(typedName);

    String canonicalSQLName = adapter.canonizeName(sqlName.getName(), sqlName.isQuoted());

    List<NamePart> nameParts = splitSQL(sqlName.getName());
    if (nameParts == null || nameParts.isEmpty()) {
      throw new InvalidIdentifierException("SQL name must produce at least one part");
    }

    List<NamePart> javaNameParts = splitJava(javaClassName);
    String javaMemberName = assembleJavaMemberName(javaNameParts);
    String javaConstantName = assembleJavaConstantName(javaNameParts);
    String dashedName = assembleDashedName(javaNameParts);

    Id id = new Id(adapter, nameParts, canonicalSQLName, true, javaClassName, javaMemberName, javaConstantName,
        dashedName, sqlName.isQuoted());
    return id;
  }

  public static Id fromCanonicalSQLAndJavaClass(final String canonicalSQLName, final DatabaseAdapter adapter,
      final String javaClassName) throws InvalidIdentifierException {
    if (adapter == null) {
      throw new InvalidIdentifierException("'adapter' cannot be null.");
    }
    if (canonicalSQLName == null || canonicalSQLName.isEmpty()) {
      throw new InvalidIdentifierException("'canonicalSQLName' cannot be null or empty.");
    }
    if (javaClassName == null || javaClassName.isEmpty()) {
      throw new InvalidIdentifierException("'javaClassName' cannot be null or empty.");
    }
    if (!javaClassName.matches("[A-Z_].*")) {
      throw new InvalidIdentifierException("'javaClassName' must start with an upper case letter or an underscore.");
    }

    List<NamePart> nameParts = splitSQL(canonicalSQLName);
    if (nameParts == null || nameParts.isEmpty()) {
      throw new InvalidIdentifierException("SQL name must produce at least one part");
    }

    List<NamePart> javaNameParts = splitJava(javaClassName);
    String javaMemberName = assembleJavaMemberName(javaNameParts);
    String javaConstantName = assembleJavaConstantName(javaNameParts);
    String dashedName = assembleDashedName(javaNameParts);

    Id id = new Id(adapter, nameParts, canonicalSQLName, true, javaClassName, javaMemberName, javaConstantName,
        dashedName, true);
    return id;
  }

  public static Id fromTypedSQLAndJavaMember(final String typedName, final DatabaseAdapter adapter,
      final String javaMemberName) throws InvalidIdentifierException {
    if (adapter == null) {
      throw new InvalidIdentifierException("'adapter' cannot be null.");
    }
    if (typedName == null || typedName.isEmpty()) {
      throw new InvalidIdentifierException("'typedName' cannot be null or empty.");
    }
    if (javaMemberName == null || javaMemberName.isEmpty()) {
      throw new InvalidIdentifierException("'javaMemberName' cannot be null or empty.");
    }
    if (!javaMemberName.matches("[a-z_].*")) {
      throw new InvalidIdentifierException("'javaMemberName' must start with an lower case letter or an underscore.");
    }

    SQLName sqlName = new SQLName(typedName);

    String canonicalSQLName = adapter.canonizeName(sqlName.getName(), sqlName.isQuoted());

    List<NamePart> nameParts = splitSQL(sqlName.getName());
    if (nameParts == null || nameParts.isEmpty()) {
      throw new InvalidIdentifierException("SQL name must produce at least one part");
    }

    List<NamePart> javaNameParts = splitJava(javaMemberName);
    String javaClassName = assembleJavaClassName(javaNameParts);
    String javaConstantName = assembleJavaConstantName(javaNameParts);
    String dashedName = assembleDashedName(javaNameParts);

    Id id = new Id(adapter, nameParts, canonicalSQLName, true, javaClassName, javaMemberName, javaConstantName,
        dashedName, sqlName.isQuoted());
    return id;
  }

  public static Id fromCanonicalSQLAndJavaMember(final String canonicalSQLName, final DatabaseAdapter adapter,
      final String javaMemberName) throws InvalidIdentifierException {
    if (adapter == null) {
      throw new InvalidIdentifierException("'adapter' cannot be null.");
    }
    if (canonicalSQLName == null || canonicalSQLName.isEmpty()) {
      throw new InvalidIdentifierException("'canonicalSQLName' cannot be null or empty.");
    }
    if (javaMemberName == null || javaMemberName.isEmpty()) {
      throw new InvalidIdentifierException("'javaMemberName' cannot be null or empty.");
    }
    if (!javaMemberName.matches("[a-z_].*")) {
      throw new InvalidIdentifierException("'javaMemberName' must start with an lower case letter or an underscore.");
    }

    List<NamePart> nameParts = splitSQL(canonicalSQLName);
    if (nameParts == null || nameParts.isEmpty()) {
      throw new InvalidIdentifierException("SQL name must produce at least one part");
    }

    List<NamePart> javaNameParts = splitJava(javaMemberName);
    String javaClassName = assembleJavaClassName(javaNameParts);
    String javaConstantName = assembleJavaConstantName(javaNameParts);
    String dashedName = assembleDashedName(javaNameParts);

    Id id = new Id(adapter, nameParts, canonicalSQLName, true, javaClassName, javaMemberName, javaConstantName,
        dashedName, true);
    return id;
  }

  // Getters

  public String getCanonicalSQLName() {
    return this.canonicalSQLName;
  }

  public boolean isQuoted() {
    return isQuoted;
  }

  public String getRenderedSQLName() {
    return this.renderedSQLName;
  }

  public boolean wasJavaNameSpecified() {
    return wasJavaNameSpecified;
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
    return nameParts;
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
    for (NamePart p : canonicalParts) {
      String st = sanitizeJava(p.getToken());
      if (st != null) {
        if (p.isAcronym()) {
          sb.append(st.toUpperCase());
        } else {
          sb.append(SUtil.sentenceFormat(st.toLowerCase()));
        }
      }
    }
    String n = sb.toString();
    if (!n.matches("[A-Za-z_].*")) {
      n = "_" + n;
    }
    return n;
  }

  private static String assembleJavaMemberName(final List<NamePart> canonicalParts) {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (NamePart p : canonicalParts) {
      String st = sanitizeJava(p.getToken());
      if (st != null) {
        if (first) {
          sb.append(st.toLowerCase());
        } else {
          if (p.isAcronym()) {
            sb.append(st.toUpperCase());
          } else {
            sb.append(SUtil.sentenceFormat(st.toLowerCase()));
          }
        }
        first = false;
      }
    }
    String n = sb.toString();
    if (!n.matches("[A-Za-z_].*")) {
      n = "_" + n;
    }
    return n;
  }

  private static String assembleJavaConstantName(final List<NamePart> canonicalParts) {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (NamePart p : canonicalParts) {
      String st = sanitizeJava(p.getToken());
      if (st != null) {
        if (!first) {
          sb.append("_");
        }
        sb.append(st.toUpperCase());
        first = false;
      }
    }
    String n = sb.toString();
    if (!n.matches("[A-Za-z_].*")) {
      n = "_" + n;
    }
    return n;
  }

  private static String assembleDashedName(final List<NamePart> canonicalParts) {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (NamePart p : canonicalParts) {
      String st = sanitizeJava(p.getToken());
      if (st != null) {
        if (!first) {
          sb.append("-");
        }
        sb.append(st.toLowerCase());
        first = false;
      }
    }
    return sb.toString();
  }

  private static String sanitizeJava(final String txt) {
    if (txt == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < txt.length(); i++) {
      char c = txt.charAt(i);
      sb.append(validJavaIdentifierChar(c) ? c : "_");
    }
    return sb.toString();
  }

  private static boolean validJavaIdentifierChar(final char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || (c == '_');
  }

  // Indexable

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((nameParts == null) ? 0 : nameParts.size());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    log("Id.equals() 1");
    if (this == obj)
      return true;
    log("Id.equals() 2");
    if (obj == null)
      return false;
    log("Id.equals() 3");
    if (getClass() != obj.getClass())
      return false;
    Id other = (Id) obj;
    log("Id.equals() 4");
    if (this.nameParts.size() != other.nameParts.size()) {
      return false;
    }
    log("Id.equals() 5");
    for (int i = 0; i < this.nameParts.size(); i++) {
      NamePart thisPart = this.nameParts.get(i);
      NamePart otherPart = other.nameParts.get(i);
      log("Id.equals() 5.1 thisPart=" + thisPart + " otherPart=" + otherPart);
      if (!thisPart.equals(otherPart)) {
        return false;
      }
    }
    log("Id.equals() 6");
    return true;
  }

  // Helper methods

  private static String repeat(final String s, final int times) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < times; i++) {
      sb.append(s);
    }
    return sb.toString();
  }

  // comparable

  @Override
  public int compareTo(final Id o) {
    if (this.canonicalSQLName != null) {
      return this.canonicalSQLName.compareTo(o.canonicalSQLName);
    }
    return this.javaClassName.compareTo(o.javaClassName);
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

    public String toString() {
      return this.acronym ? "[" + this.token + "]" : this.token;
    }

    // equals

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (acronym ? 1231 : 1237);
      result = prime * result + ((token == null) ? 0 : token.hashCode());
      return result;
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      // As a NamePart
      try {
        NamePart p = (NamePart) obj;
        log("NamePart.equals() 3 this=" + this + " other=" + p);
        boolean eq = p.acronym == this.acronym && p.token.equals(this.token);
        log("NamePart.equals() 3.1 eq=" + eq);
        return eq;
      } catch (ClassCastException e) {
        // As a String
        try {
          String s = (String) obj;
          log("NamePart.equals() 4");
          return this.acronym ? this.token.toUpperCase().equals(s) : this.token.equals(s);
        } catch (ClassCastException e2) {
          log("NamePart.equals() 5");
          return false;
        }
      }
    }

  }

}
