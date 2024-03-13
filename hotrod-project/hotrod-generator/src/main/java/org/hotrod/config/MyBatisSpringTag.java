package org.hotrod.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.Feedback;
import org.hotrod.generator.Generator;
import org.hotrod.generator.HotRodContext;
import org.hotrod.generator.NamePackageResolver;
import org.hotrod.generator.mybatisspring.MyBatisSpringGenerator;
import org.hotrod.utils.ClassPackage;
import org.nocrala.tools.database.tartarus.core.CatalogSchema;

@XmlRootElement(name = "mybatis-spring")
public class MyBatisSpringTag extends AbstractGeneratorTag implements NamePackageResolver {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(MyBatisSpringTag.class);

  public static final String GENERATOR_NAME = "MyBatis-Spring";

  // Properties

  private DiscoverTag discover = null;

  private DaosTag daos = null;
  private MappersTag mappers = null;
  private SelectGenerationTag selectGeneration = null;
  private ClassicFKNavigationTag classicFKNavigation = new ClassicFKNavigationTag();
  private List<PropertyTag> propertyTags = new ArrayList<PropertyTag>();

  private MyBatisProperties properties = new MyBatisProperties();

  // Constructor

  public MyBatisSpringTag() {
    super("mybatis-spring");
    log.debug("init");
  }

  public void enableDiscover() {
    if (this.discover == null) {
      this.discover = new DiscoverTag();
      SchemaTag currentSchema = new SchemaTag();
      currentSchema.setCurrent();
      this.discover.setCurrentSchema(currentSchema);
    }
  }

  // JAXB Setters

  @XmlElement(name = "discover")
  public void setDiscover(final DiscoverTag discover) {
    this.discover = discover;
  }

  @XmlElement(name = "daos")
  public void setDaos(final DaosTag daos) throws InvalidConfigurationFileException {
    if (this.daos != null) {
      throw new InvalidConfigurationFileException(this,
          "Duplicate <daos> tag; the generator can only have a single <dao> tag");
    }
    this.daos = daos;
  }

  @XmlElement
  public void setMappers(final MappersTag mappers) throws InvalidConfigurationFileException {
    if (this.mappers != null) {
      throw new InvalidConfigurationFileException(this,
          "Duplicate <mappers> tag; the generator can only have a single <mappers> tag");
    }
    this.mappers = mappers;
  }

  @XmlElement(name = "select-generation")
  public void setSelectGeneration(final SelectGenerationTag selectGeneration) throws InvalidConfigurationFileException {
    if (this.selectGeneration != null) {
      throw new InvalidConfigurationFileException(this,
          "Duplicate <select-generation> tag; the generator can only have a single <select-generation> tag");
    }
    this.selectGeneration = selectGeneration;
  }

  @XmlElement(name = "classic-fk-navigation")
  public void setClassicFKNavigation(final ClassicFKNavigationTag classicFKNavigation)
      throws InvalidConfigurationFileException {
    this.classicFKNavigation = classicFKNavigation;
  }

  @XmlElement
  public void setProperty(final PropertyTag p) {
    this.propertyTags.add(p);
  }

  // Behavior

  @Override
  public String getName() {
    return GENERATOR_NAME;
  }

  // Validate

  @Override
  public void validate(final File basedir, final File parentDir, final DatabaseAdapter adapter,
      final CatalogSchema currentCS) throws InvalidConfigurationFileException {

    // discovery

    if (this.discover != null) {
      this.discover.validate(adapter, currentCS);
    }

    // daos

    if (this.daos == null) {
      this.daos = new DaosTag();
    }
    this.daos.validate(basedir);

    // mappers

    if (this.mappers == null) {
      this.mappers = MappersTag.DEFAULT_MAPPERS_TAG;
    }

    // select-generation

    if (this.selectGeneration == null) {
      this.selectGeneration = new SelectGenerationTag();
      this.selectGeneration.setTempViewBaseName(SelectGenerationTag.DEFAULT_TEMP_VIEW_NAME);
    }
    this.selectGeneration.validate(basedir);

    // mappers

    if (this.mappers == null) {
      this.mappers = new MappersTag();
    }
    this.mappers.validate(basedir);

    // properties

    Set<String> names = new HashSet<String>();
    for (PropertyTag p : this.propertyTags) {
      p.validate();
      if (names.contains(p.getName())) {
        throw new InvalidConfigurationFileException(this, "Property with name '" + p.getName()
            + "' cannot be specified more than once, but multiple occurrences found.");
      }
      this.properties.set(this, p);
    }

  }

  // Getters

  public DiscoverTag getDiscover() {
    return this.discover;
  }

  public DaosTag getDaos() {
    return daos;
  }

  public MappersTag getMappers() {
    return mappers;
  }

  @Override
  public SelectGenerationTag getSelectGeneration() {
    return selectGeneration;
  }

  public ClassicFKNavigationTag getClassicFKNavigation() {
    return classicFKNavigation;
  }

  // Produce Generator Instance

  public MyBatisProperties getProperties() {
    return properties;
  }

  @Override
  public Generator instantiateGenerator(final HotRodContext hc, final EnabledFKs enabledFKs,
      final DisplayMode displayMode, final boolean incrementalMode, final Feedback feedback)
      throws UncontrolledException, ControlledException, InvalidConfigurationFileException {
    return new MyBatisSpringGenerator(hc, enabledFKs, displayMode, incrementalMode, feedback);
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

  // NamePackageResolver implementation

  @Override
  public String generateAbstractVOName(final String name) {
    return this.getDaos().generateAbstractVOName(name);
  }

  @Override
  public String generateVOName(final String name) {
    return this.getDaos().generateVOName(name);
  }

  @Override
  public ClassPackage getPrimitivesVOPackage(final ClassPackage cp) {
    return this.getDaos().getPrimitivesVOPackage(cp);
  }

}
