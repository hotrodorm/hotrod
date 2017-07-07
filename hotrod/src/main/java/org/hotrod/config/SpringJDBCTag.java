package org.hotrod.config;

import java.io.File;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.HotRodAntTask.DisplayMode;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.springjdbc.SpringJDBCGenerator;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;

@XmlRootElement(name = "spring-jdbc")
public class SpringJDBCTag extends AbstractGeneratorTag {

  // Constants

  private static final Logger log = Logger.getLogger(SequenceTag.class);

  public static final String GENERATOR_NAME = "SpringJDBC";

  // Properties

  private DaosTag daos = null;
  private ConfigTag config = null;
  private SelectGenerationTag selectGeneration = null;

  // Constructor

  public SpringJDBCTag() {
    super("spring-jdbc");
    log.debug("init");
  }

  // JAXB Setters

  @XmlElement
  public void setDaos(final DaosTag daos) {
    this.daos = daos;
  }

  @XmlElement
  public void setConfig(final ConfigTag config) {
    this.config = config;
  }

  @XmlElement(name = "select-generation")
  public void setSelectGeneration(final SelectGenerationTag selectGeneration) {
    this.selectGeneration = selectGeneration;
  }

  // Behavior

  @Override
  public void validate(final File basedir) throws InvalidConfigurationFileException {
    this.daos.validate();
    this.config.validate();
    this.selectGeneration.validate();
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

  @Override
  public String getName() {
    return GENERATOR_NAME;
  }

}
