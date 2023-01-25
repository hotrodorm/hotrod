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
import org.hotrod.utils.Compare;
import org.nocrala.tools.database.tartarus.core.CatalogSchema;

@XmlRootElement(name = "mybatis-spring")
public class MyBatisSpringTag extends AbstractGeneratorTag implements NamePackageResolver {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(MyBatisSpringTag.class);

  public static final String GENERATOR_NAME = "MyBatis-Spring";

  // Properties

  private DiscoverTag discover = null;

  private DaosSpringMyBatisTag daos = null;
  private MappersTag mappers = null;
  private TemplateTag template = null;
  private SelectGenerationTag selectGeneration = null;
  private ClassicFKNavigationTag classicFKNavigation = new ClassicFKNavigationTag();
  private List<PropertyTag> propertyTags = new ArrayList<PropertyTag>();

  private MyBatisProperties properties = new MyBatisProperties();

  public boolean isDiscoverEnabled(final HotRodConfigTag config) {
    return this.discover != null;
  }

  // Constructor

  public MyBatisSpringTag() {
    super("mybatis-spring");
    log.debug("init");
  }

  // JAXB Setters

  @XmlElement(name = "discover")
  public void setDiscover(final DiscoverTag discover) {
    this.discover = discover;
  }

  @XmlElement(name = "daos")
  public void setDaos(final DaosSpringMyBatisTag daos) throws InvalidConfigurationFileException {
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

  @XmlElement(name = "mybatis-configuration-template")
  public void setTemplate(final TemplateTag template) throws InvalidConfigurationFileException {
    if (this.template != null) {
      throw new InvalidConfigurationFileException(this,
          "Duplicate <mybatis-configuration-template> tag; the generator can only have a single <mybatis-configuration-template> tag");
    }
    this.template = template;
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
    if (this.classicFKNavigation != null) {
      throw new InvalidConfigurationFileException(this,
          "Duplicate <classic-fk-navigation> tag; the generator can only have a single <classic-fk-navigation> tag");
    }
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

    // mybatis-configuration-template

    if (this.template != null && this.template.getTemplateFile() == null) {
      this.template.setTemplateFile(TemplateTag.DEFAULT_TEMPLATE_FILE);
    }

    // Mappers

    if (this.mappers == null) {
      this.mappers = MappersTag.DEFAULT_MAPPERS_TAG;
    }

    // select-generation

    if (this.selectGeneration == null) {
      this.selectGeneration = new SelectGenerationTag();
      this.selectGeneration.setTempViewBaseName(SelectGenerationTag.DEFAULT_TEMP_VIEW_NAME);
    }

    // Validate sub tags

    this.daos.validate(basedir);
    this.mappers.validate(basedir);
    if (this.template != null) {
      this.template.validate(basedir, parentDir);
    }
    this.selectGeneration.validate(basedir);

    // Properties

    Set<String> names = new HashSet<String>();
    for (PropertyTag p : this.propertyTags) {
      p.validate();
      if (names.contains(p.getName())) {
        throw new InvalidConfigurationFileException(this, //
            "Duplicate property '" + p.getName() + "'", //
            "Property with name '" + p.getName()
                + "' cannot be specified more than once, but multiple occurrences found.");
      }
      this.properties.set(this, p);
    }

  }

  // Getters

  public DiscoverTag getDiscover() {
    return this.discover;
  }

  public DaosSpringMyBatisTag getDaos() {
    return daos;
  }

  public MappersTag getMappers() {
    return mappers;
  }

  public TemplateTag getTemplate() {
    return template;
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

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    return true;
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      MyBatisSpringTag f = (MyBatisSpringTag) fresh;
      boolean different = !same(fresh);

      this.daos = f.daos;
      this.mappers = f.mappers;
      this.template = f.template;
      this.selectGeneration = f.selectGeneration;
      this.propertyTags = f.propertyTags;
      this.properties = f.properties;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      MyBatisSpringTag f = (MyBatisSpringTag) fresh;
      return //
      Compare.same(this.daos, f.daos) && //
          Compare.same(this.mappers, f.mappers) && //
          Compare.same(this.template, f.template) && //
          Compare.same(this.selectGeneration, f.selectGeneration) && //
          Compare.same(this.propertyTags, f.propertyTags) //
      ;
    } catch (ClassCastException e) {
      return false;
    }
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
