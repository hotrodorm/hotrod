package org.hotrod.config;

import java.io.File;

import org.hotrod.ant.ControlledException;
import org.hotrod.ant.HotRodAntTask.DisplayMode;
import org.hotrod.config.tags.DaosTag;
import org.hotrod.config.tags.HotRodConfigTag;
import org.hotrod.config.tags.SelectGenerationTag;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.HotRodGenerator;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;

public abstract class AbstractGeneratorTag extends AbstractConfigurationTag {

  // Constructor

  protected AbstractGeneratorTag(final String tagName) {
    super(tagName);
  }

  // Abstract Methods

  public abstract String getName();

  public abstract void validate(File basedir) throws InvalidConfigurationFileException;

  public abstract DaosTag getDaos();

  public abstract SelectGenerationTag getSelectGeneration();

  public abstract HotRodGenerator getGenerator(DatabaseLocation loc, HotRodConfigTag config, DisplayMode displayMode)
      throws UncontrolledException, ControlledException;

}
