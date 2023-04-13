package org.hotrod.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.GeneratorNotFoundException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.nocrala.tools.database.tartarus.core.CatalogSchema;

@XmlRootElement(name = "generators")
public class GeneratorsTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(GeneratorsTag.class);

  // Properties

  private List<AbstractGeneratorTag> generators = new ArrayList<AbstractGeneratorTag>();

  private AbstractGeneratorTag selectedGeneratorTag = null;

  // Constructor

  public GeneratorsTag() {
    super("generators");
    log.debug("init");
  }

  // JAXB Setters

  @XmlElement(name = "mybatis-spring")
  public void setMyBatisSpring(final MyBatisSpringTag g) {
    this.generators.add(g);
  }

  // Behavior

  public void validate(final File basedir, final File parentDir, final DatabaseAdapter adapter,
      final CatalogSchema currentCS) throws InvalidConfigurationFileException, GeneratorNotFoundException {
    if (this.generators.isEmpty()) {
      throw new GeneratorNotFoundException("No HotRod generator found.");
    }
    this.selectedGeneratorTag = this.generators.get(0);
    this.selectedGeneratorTag.validate(basedir, parentDir, adapter, currentCS);
  }

  // Getters

  public AbstractGeneratorTag getSelectedGeneratorTag() {
    log.debug("this.selectedGeneratorTag=" + this.selectedGeneratorTag);
    return this.selectedGeneratorTag;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}
