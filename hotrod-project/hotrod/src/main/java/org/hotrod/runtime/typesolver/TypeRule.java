package org.hotrod.runtime.typesolver;

public class TypeRule {

  private String test;

  private String errorMessage; // If present the rule produces an error

  private String type;
  private String converter;
  private String forceJDBCTypeOnWrite;

}
