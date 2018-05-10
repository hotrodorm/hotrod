package org.hotrod.config;

import java.io.File;

import org.hotrod.ant.HotRodAntTask.DisplayMode;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.CachedMetadata;
import org.hotrod.generator.HotRodGenerator;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;

public abstract class AbstractGeneratorTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constructor

  protected AbstractGeneratorTag(final String tagName) {
    super(tagName);
  }

  // Abstract Methods

  public abstract String getName();

  public abstract void validate(File basedir, File parentDir) throws InvalidConfigurationFileException;

  public abstract DaosTag getDaos();

  public abstract SelectGenerationTag getSelectGeneration();

  public abstract HotRodGenerator instantiateGenerator(CachedMetadata cachedMetadata, DatabaseLocation loc,
      HotRodConfigTag config, DisplayMode displayMode, boolean incrementalMode)
      throws UncontrolledException, ControlledException;

}
