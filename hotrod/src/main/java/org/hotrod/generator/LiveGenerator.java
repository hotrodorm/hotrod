package org.hotrod.generator;

import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;

public interface LiveGenerator {

  void generate(FileGenerator fileGenerator) throws UncontrolledException, ControlledException;

}
