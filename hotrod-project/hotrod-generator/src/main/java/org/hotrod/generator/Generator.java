package org.hotrod.generator;

import org.hotrod.config.HotRodConfigTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.metadata.TableDataSetMetadata;
import org.hotrod.metadata.VORegistry;
import org.hotrod.utils.identifiers.ObjectId;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

public interface Generator {

  TableDataSetMetadata findTableMetadata(ObjectId id);

  JdbcTable findJdbcTable(String name);

  JdbcTable findJdbcView(String name);

  JdbcColumn findJdbcColumn(JdbcTable t, String name);

  TableDataSetMetadata findViewMetadata(ObjectId id);

  void display(String txt);

  VORegistry getVORegistry();

  DatabaseAdapter getAdapter();

  HotRodConfigTag getConfig();

  JdbcDatabase getJdbcDatabase();

  DatabaseLocation getLoc();

  /**
   * Gathers all extra metadata for the specific generator.
   * 
   * Fails if any inconsistency is found between the existing database and the
   * config file. No code generation is performed by this method, so any failure
   * will not override a previous code generation.
   * 
   * @throws UncontrolledException             When a technical error is found
   * @throws ControlledException               When a configuration error is found
   * @throws InvalidConfigurationFileException
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