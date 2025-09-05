package org.hotrod.dynamicsql;

import org.hotrod.exceptions.PersistenceException;

public class DynamicExpressionException extends PersistenceException {

  private static final long serialVersionUID = 1L;

  public DynamicExpressionException(String message) {
    super(message);
  }

  public DynamicExpressionException(String message, Throwable cause) {
    super(message, cause);
  }

}
