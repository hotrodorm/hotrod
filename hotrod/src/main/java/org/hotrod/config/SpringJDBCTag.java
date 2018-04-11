package org.hotrod.config;

import java.io.File;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.HotRodAntTask.DisplayMode;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.CachedMetadata;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.springjdbc.SpringJDBCGenerator;
import org.hotrod.utils.Compare;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;

@XmlRootElement(name = "spring-jdbc")
public class SpringJDBCTag extends AbstractGeneratorTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(SequenceMethodTag.class);

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
  public void validate(final File basedir, final File parentDir) throws InvalidConfigurationFileException {
    this.daos.validate(basedir);
    this.config.validate(basedir);
    this.selectGeneration.validate(basedir);
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
  public HotRodGenerator instantiateGenerator(final CachedMetadata cachedMetadata, final DatabaseLocation loc,
      final HotRodConfigTag config, final DisplayMode displayMode, final boolean incrementalMode)
      throws UncontrolledException, ControlledException {
    // Ignore cachedMetadata & selectedTags (for now)
    config.markGenerateTree(true);
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

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    return true;
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      SpringJDBCTag f = (SpringJDBCTag) fresh;
      boolean different = !same(fresh);

      this.daos = f.daos;
      this.config = f.config;
      this.selectGeneration = f.selectGeneration;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      SpringJDBCTag f = (SpringJDBCTag) fresh;
      return //
      Compare.same(this.daos, f.daos) && //
          Compare.same(this.config, f.config) && //
          Compare.same(this.selectGeneration, f.selectGeneration) //
      ;
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}
