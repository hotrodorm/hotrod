package org.hotrod.torcs.plan;

public class ParameterAlreadyConsumedException extends CouldNotRetrievePlanException {

  private static final long serialVersionUID = 1L;

  public ParameterAlreadyConsumedException(String message) {
    super(message);
  }

}
