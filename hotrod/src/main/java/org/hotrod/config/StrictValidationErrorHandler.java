package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class StrictValidationErrorHandler implements ErrorHandler {

  private List<String> errors = new ArrayList<String>();

  @Override
  public void warning(final SAXParseException e) throws SAXException {
  }

  @Override
  public void error(final SAXParseException e) throws SAXException {
    addMessage(e);
  }

  @Override
  public void fatalError(final SAXParseException e) throws SAXException {
    addMessage(e);
  }

  private void addMessage(final SAXParseException e) {
    this.errors.add("Error at line " + e.getLineNumber() + " column "
        + e.getColumnNumber() + ": " + e.getMessage());
  }

  public boolean isValid() {
    return this.errors.isEmpty();
  }

  public List<String> getMessages() {
    return this.errors;
  }

}
