package org.hotrod.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hotrod.ant.ControlledException;
import org.hotrod.ant.HotRodAntTask.DisplayMode;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.mybatis.MyBatisGenerator;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;

public class MyBatisTag extends AbstractGeneratorTag {

  public static final String GENERATOR_NAME = "MyBatis";

  private DaosTag daos = null;
  private MappersTag mappers = null;
  private TemplateTag template = null;
  private SessionFactoryTag sessionFactory = null;
  private SelectGenerationTag selectGeneration = null;
  private List<PropertyTag> propertyTags = new ArrayList<PropertyTag>();

  private MyBatisProperties properties = new MyBatisProperties();

  @Override
  public String getName() {
    return GENERATOR_NAME;
  }

  // Validate

  public void validate(final File basedir) throws InvalidConfigurationFileException {
    this.daos.validate();
    this.mappers.validate();
    this.template.validate(basedir);
    this.sessionFactory.validate();
    this.selectGeneration.validate();

    // Properties

    Set<String> names = new HashSet<String>();
    for (PropertyTag p : this.propertyTags) {
      p.validate();
      if (names.contains(p.getName())) {
        throw new InvalidConfigurationFileException("Property with name '" + p.getName()
            + "' cannot be specified more than once, but multiple occurrences found.");
      }
      this.properties.set(p);
    }

  }

  // Setters

  public void setDaos(final DaosTag daos) {
    this.daos = daos;
  }

  public void setMappers(final MappersTag mappers) {
    this.mappers = mappers;
  }

  public void setTemplate(final TemplateTag template) {
    this.template = template;
  }

  public void setSessionFactory(final SessionFactoryTag sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public void setSelectGeneration(final SelectGenerationTag selectGeneration) {
    this.selectGeneration = selectGeneration;
  }

  public void addProperty(final PropertyTag p) {
    this.propertyTags.add(p);
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
  public HotRodGenerator getGenerator(final DatabaseLocation loc, final HotRodConfigTag config,
      final DisplayMode displayMode) throws UncontrolledException, ControlledException {
    return new MyBatisGenerator(loc, config, displayMode);
  }

}
