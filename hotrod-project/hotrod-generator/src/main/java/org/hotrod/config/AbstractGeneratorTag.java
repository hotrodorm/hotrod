package org.hotrod.config;

import java.io.File;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ErrorMessageException;
import org.hotrod.exceptions.FaultException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.Feedback;
import org.hotrod.generator.Generator;
import org.hotrod.generator.HotRodContext;
import org.nocrala.tools.database.tartarus.core.CatalogSchema;

public abstract class AbstractGeneratorTag extends AbstractConfigurationTag {

  // Constructor

  protected AbstractGeneratorTag(final String tagName) {
    super(tagName);
  }

  // Abstract Methods

  public abstract void enableDiscover();

  public abstract String getName();

  public abstract void validate(File basedir, final File parentDir, final DatabaseAdapter adapter,
      final CatalogSchema currentCS) throws InvalidConfigurationFileException;

  public abstract Generator instantiateGenerator(HotRodContext hc, EnabledFKs enabledFKs, DisplayMode displayMode,
      boolean incrementalMode, Feedback feedback)
      throws FaultException, ErrorMessageException, InvalidConfigurationFileException;

}
