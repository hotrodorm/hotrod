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
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.Feedback;
import org.hotrod.generator.Generator;
import org.hotrod.generator.HotRodContext;
import org.hotrod.generator.NamePackageResolver;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.Compare;

@XmlRootElement(name = "mybatis")
public class MyBatisTag extends AbstractGeneratorTag implements NamePackageResolver {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(MyBatisTag.class);

  public static final String GENERATOR_NAME = "MyBatis";

  // Properties

  private DaosTag daos = null;
  private MappersTag mappers = null;
  private TemplateTag template = null;
  private SessionFactoryTag sessionFactory = null;
  private SelectGenerationTag selectGeneration = null;
  private List<PropertyTag> propertyTags = new ArrayList<PropertyTag>();

  private MyBatisProperties properties = new MyBatisProperties();

  // Constructor

  public MyBatisTag() {
    super("mybatis");
    log.debug("init");
  }

  // JAXB Setters

  @XmlElement
  public void setDaos(final DaosTag daos) {
    this.daos = daos;
  }

  @XmlElement
  public void setMappers(final MappersTag mappers) {
    this.mappers = mappers;
  }

  @XmlElement(name = "mybatis-configuration-template")
  public void setTemplate(final TemplateTag template) {
    this.template = template;
  }

  @XmlElement(name = "session-factory")
  public void setSessionFactory(final SessionFactoryTag sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @XmlElement(name = "select-generation")
  public void setSelectGeneration(final SelectGenerationTag selectGeneration) {
    this.selectGeneration = selectGeneration;
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
  public void validate(final File basedir, final File parentDir) throws InvalidConfigurationFileException {
    this.daos.validate(basedir);
    this.mappers.validate(basedir);
    this.template.validate(basedir, parentDir);
    this.sessionFactory.validate(basedir);
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

  @Override
  public DaosTag getDaos() {
    return daos;
  }

  public MappersTag getMappers() {
    return mappers;
  }

  public TemplateTag getTemplate() {
    return template;
  }

  public SessionFactoryTag getSessionFactory() {
    return sessionFactory;
  }

  @Override
  public SelectGenerationTag getSelectGeneration() {
    return selectGeneration;
  }

  // Produce Generator Instance

  public MyBatisProperties getProperties() {
    return properties;
  }

  @Override
  public Generator instantiateGenerator(final HotRodContext hc, final EnabledFKs enabledFKs,
      final DisplayMode displayMode, final boolean incrementalMode, final Feedback feedback)
      throws UncontrolledException, ControlledException, InvalidConfigurationFileException {
//    return new MyBatisGenerator(db, metadata, loc, config, displayMode, incrementalMode, adapter, feedback);
    return null;
  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    return true;
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      MyBatisTag f = (MyBatisTag) fresh;
      boolean different = !same(fresh);

      this.daos = f.daos;
      this.mappers = f.mappers;
      this.template = f.template;
      this.sessionFactory = f.sessionFactory;
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
      MyBatisTag f = (MyBatisTag) fresh;
      return //
      Compare.same(this.daos, f.daos) && //
          Compare.same(this.mappers, f.mappers) && //
          Compare.same(this.template, f.template) && //
          Compare.same(this.sessionFactory, f.sessionFactory) && //
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
