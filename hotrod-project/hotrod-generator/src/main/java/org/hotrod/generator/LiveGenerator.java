package org.hotrod.generator;

import org.hotrod.exceptions.ErrorMessageException;
import org.hotrod.exceptions.FaultException;

public interface LiveGenerator {

  void generate(FileGenerator fileGenerator) throws FaultException, ErrorMessageException;

}
