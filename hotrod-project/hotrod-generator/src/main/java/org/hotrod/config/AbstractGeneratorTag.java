package org.hotrod.config;

import java.io.File;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.Feedback;
import org.hotrod.generator.Generator;
import org.hotrod.generator.HotRodContext;

public abstract class AbstractGeneratorTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constructor

  protected AbstractGeneratorTag(final String tagName) {
    super(tagName);
  }

  // Abstract Methods

  public abstract String getName();

  public abstract void validate(File basedir, File parentDir, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException;

  public abstract DaosTag getDaos();

  public abstract SelectGenerationTag getSelectGeneration();

  public abstract Generator instantiateGenerator(HotRodContext hc, EnabledFKs enabledFKs, DisplayMode displayMode,
      boolean incrementalMode, Feedback feedback)
      throws UncontrolledException, ControlledException, InvalidConfigurationFileException;

}
