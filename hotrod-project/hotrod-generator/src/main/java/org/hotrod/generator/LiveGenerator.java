package org.hotrod.generator;

import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.UncontrolledException;

public interface LiveGenerator {

  void generate(FileGenerator fileGenerator) throws UncontrolledException, ControlledException;

}
