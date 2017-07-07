package org.hotrod.config;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

public class StrictValidationEventHandler implements ValidationEventHandler {

  private ValidationEventLocator locator = null;
  private String location;

  @Override
  public boolean handleEvent(final ValidationEvent event) {
    this.locator = event.getLocator();
    this.location = locator == null ? "[no error location reported]"
        : "line " + locator.getLineNumber() + ", col " + locator.getColumnNumber();
    return false;
  }

  public ValidationEventLocator getLocator() {
    return locator;
  }

  public String getLocation() {
    return location;
  }

}
