package org.hotrod.generator;

import org.hotrod.config.HotRodConfigTag;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.metadata.VORegistry;

public interface Generator {

  void display(String txt);

  VORegistry getVORegistry();

  HotRodConfigTag getConfig();

  /**
   * Gathers all extra metadata for the specific generator.
   * 
   * Fails if any inconsistency is found between the existing database and the
   * config file. No code generation is performed by this method, so any failure
   * will not override a previous code generation.
   * 
   * @throws UncontrolledException             When a technical error is found
   * @throws ControlledException               When a configuration error is found
   * @throws InvalidConfigurationFileException When an invalid configuration setting is found
   */
  void prepareGeneration() throws UncontrolledException, ControlledException, InvalidConfigurationFileException;

  /**
   * Generates new persistence code.
   * 
   * This is the second step of the generation and should be called only after the
   * prepareGeneration() method has succeeded.
   * 
   * @throws UncontrolledException When a technical error is found
   * @throws ControlledException   When a configuration error is found
   */

  void generate() throws UncontrolledException, ControlledException;

}