package org.hotrod.config;

import java.io.File;

import org.hotrod.ant.ControlledException;
import org.hotrod.ant.HotRodAntTask.DisplayMode;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.springjdbc.SpringJDBCGenerator;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;

public class SpringJDBCTag extends AbstractGeneratorTag {

  public static final String GENERATOR_NAME = "SpringJDBC";

  private DaosTag daos = null;
  private ConfigTag config = null;
  private SelectGenerationTag selectGeneration = null;

  @Override
  public String getName() {
    return GENERATOR_NAME;
  }

  // Validate

  @Override
  public void validate(final File basedir) throws InvalidConfigurationFileException {
    this.daos.validate();
    this.config.validate();
    this.selectGeneration.validate();
  }

  // Setters

  public void setDaos(final DaosTag daos) {
    this.daos = daos;
  }

  public void setConfig(final ConfigTag config) {
    this.config = config;
  }

  public void setSelectGeneration(final SelectGenerationTag selectGeneration) {
    this.selectGeneration = selectGeneration;
  }

  // Getters

  @Override
  public DaosTag getDaos() {
    return daos;
  }

  public ConfigTag getConfig() {
    return config;
  }

  // Produce Generator Instance

  @Override
  public HotRodGenerator getGenerator(final DatabaseLocation loc, final HotRodConfigTag config,
      final DisplayMode displayMode) throws UncontrolledException, ControlledException {
    return new SpringJDBCGenerator(loc, config, displayMode);
  }

  @Override
  public SelectGenerationTag getSelectGeneration() {
    return selectGeneration;
  }

}
