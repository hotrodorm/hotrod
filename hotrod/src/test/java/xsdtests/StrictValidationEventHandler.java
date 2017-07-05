package xsdtests;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

public class StrictValidationEventHandler implements ValidationEventHandler {

  @Override
  public boolean handleEvent(ValidationEvent event) {
    String loc = event.getLocator() == null ? "[no locaqtion]"
        : "[line " + event.getLocator().getLineNumber() + ", col " + event.getLocator().getColumnNumber() + "]";
    System.out.println("Validation error " + loc + ": " + event.getMessage());
    return false;
  }

}
