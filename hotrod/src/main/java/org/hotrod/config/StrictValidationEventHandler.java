package org.hotrod.config;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

public class StrictValidationEventHandler implements ValidationEventHandler {

  private ValidationEventLocator locator = null;

  @Override
  public boolean handleEvent(final ValidationEvent event) {
    this.locator = event.getLocator();
    return false;
  }

  public ValidationEventLocator getLocator() {
    return locator;
  }

}
