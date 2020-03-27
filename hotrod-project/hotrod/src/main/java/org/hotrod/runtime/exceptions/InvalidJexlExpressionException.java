package org.hotrod.runtime.exceptions;

public class InvalidJexlExpressionException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public InvalidJexlExpressionException(final String message) {
    super(message);
  }

}
