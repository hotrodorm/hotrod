package org.hotrod.config;

public class Patterns {

  public static final String VALID_JAVA_CLASS = "[A-Z][a-zA-Z0-9_$]*";

  public static final String VALID_JAVA_PROPERTY = "[a-z][a-zA-Z0-9_]*";

  public static final String VALID_JAVA_METHOD = "[a-z][a-zA-Z0-9_]*";

  public static final String VALID_JAVA_VARIABLE = "[a-z][a-zA-Z0-9_]*";

  public static final String VALID_JAVA_PACKAGE = "[a-z0-9_]+";
  public static final String VALID_JAVA_UNQUALIFIED_TYPE = "[A-Z][a-zA-Z0-9_$\\,\\s<>]*";
  public static final String VALID_JAVA_TYPE = "(" + VALID_JAVA_PACKAGE + "\\.)*" + VALID_JAVA_UNQUALIFIED_TYPE;

}
