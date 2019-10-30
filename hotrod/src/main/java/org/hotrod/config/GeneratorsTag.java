package org.hotrod.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.ant.Constants;
import org.hotrod.exceptions.GeneratorNotFoundException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.Compare;

@XmlRootElement(name = "generators")
public class GeneratorsTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

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

  @XmlElement(name = "mybatis-spring")
  public void setMyBatisSpring(final MyBatisSpringTag g) {
    this.generators.add(g);
  }

  // Behavior

  public void validate(final File basedir, final File parentDir, final String generatorName)
      throws InvalidConfigurationFileException, GeneratorNotFoundException {
    for (AbstractGeneratorTag g : this.generators) {
      log.debug("g.getName()=" + g.getName() + " / generatorName=" + generatorName + " g=" + g);
      if (g.getName().equals(generatorName)) {
        this.selectedGeneratorTag = g;
        g.validate(basedir, parentDir);
        return;
      }
    }
    if (this.selectedGeneratorTag == null) {
      throw new GeneratorNotFoundException("No HotRod generator found with name '" + generatorName + "'. "
          + "This name was specified in the environment "
          + "but it was not declared inside the <generators> tag of the configuration file.");
    }
  }

  // Getters

  public AbstractGeneratorTag getSelectedGeneratorTag() {
    log.debug("this.selectedGeneratorTag=" + this.selectedGeneratorTag);
    return this.selectedGeneratorTag;
  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    return true;
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      GeneratorsTag f = (GeneratorsTag) fresh;
      boolean different = !same(fresh);

      this.generators = f.generators;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      GeneratorsTag f = (GeneratorsTag) fresh;
      return Compare.same(this.generators, f.generators);
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
