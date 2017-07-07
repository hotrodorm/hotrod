package org.hotrod.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.GeneratorNotFoundException;
import org.hotrod.exceptions.InvalidConfigurationFileException;

@XmlRootElement(name = "generators")
public class GeneratorsTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(GeneratorsTag.class);

  // Properties

  private List<AbstractGeneratorTag> generators = new ArrayList<AbstractGeneratorTag>();

  private AbstractGeneratorTag selectedGeneratorTag = null;

  // Constructor

  public GeneratorsTag() {
    super("generators");
    log.debug("init");
  }

  // JAXB Setters

  @XmlElement(name = "mybatis")
  public void setMyBatis(final MyBatisTag g) {
    this.generators.add(g);
  }

  @XmlElement(name = "spring-jdbc")
  public void setSpringJdbc(final SpringJDBCTag g) {
    this.generators.add(g);
  }

  // Behavior

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

  // Getters

  public AbstractGeneratorTag getSelectedGeneratorTag() {
    return this.selectedGeneratorTag;
  }

}
