package org.hotrod.runtime.livesql.exceptions;

public class UnsupportedLiveSQLFeatureException extends InvalidLiveSQLStatementException {

  private static final long serialVersionUID = 1L;

  public UnsupportedLiveSQLFeatureException(String message) {
    super(message);
  }

}
