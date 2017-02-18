package org.hotrod.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.hotrod.exceptions.GeneratorNotFoundException;
import org.hotrod.exceptions.InvalidConfigurationFileException;

public class GeneratorsTag {

  private List<AbstractGeneratorTag> generators = new ArrayList<AbstractGeneratorTag>();

  private AbstractGeneratorTag selectedGeneratorTag = null;

  // Validate

  public void validate(final File basedir, final String generatorName)
      throws InvalidConfigurationFileException, GeneratorNotFoundException {
    for (AbstractGeneratorTag g : this.generators) {
      if (g.getName().equals(generatorName)) {
        this.selectedGeneratorTag = g;
        g.validate(basedir);
        return;
      }
    }
    if (this.selectedGeneratorTag == null) {
      throw new GeneratorNotFoundException("No HotRod generator found with name '" + generatorName
          + "'. Available generators are: " + MyBatisTag.GENERATOR_NAME + ", " + SpringJDBCTag.GENERATOR_NAME);
    }
  }

  // Setters

  public void addGenerator(final AbstractGeneratorTag g) {
    this.generators.add(g);
  }

  // Getters

  public AbstractGeneratorTag getSelectedGeneratorTag() {
    return this.selectedGeneratorTag;
  }

}
