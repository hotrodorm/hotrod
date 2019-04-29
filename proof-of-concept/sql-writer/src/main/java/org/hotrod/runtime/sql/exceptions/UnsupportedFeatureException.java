package org.hotrod.runtime.sql.exceptions;

public class UnsupportedFeatureException extends InvalidSQLStatementException {

  private static final long serialVersionUID = 1L;

  public UnsupportedFeatureException(String message) {
    super(message);
  }

}
