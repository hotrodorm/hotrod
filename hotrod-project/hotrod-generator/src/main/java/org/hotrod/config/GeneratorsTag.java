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
import org.hotrod.utils.Compare;
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
    for (AbstractGeneratorTag g : this.generators) {
      this.selectedGeneratorTag = g;
      g.validate(basedir, parentDir, adapter, currentCS);
      return;
    }
    if (this.selectedGeneratorTag == null) {
      throw new GeneratorNotFoundException("No HotRod generator found.");
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
