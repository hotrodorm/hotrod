package org.hotrod.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.Feedback;
import org.hotrod.generator.Generator;
import org.hotrod.generator.HotRodContext;
import org.hotrod.generator.NamePackageResolver;
import org.hotrod.generator.mybatisspring.MyBatisSpringGenerator;
import org.hotrod.identifiers.Id;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.Compare;
import org.nocrala.tools.database.tartarus.core.CatalogSchema;
import org.nocrala.tools.database.tartarus.utils.SUtil;
import org.nocrala.tools.lang.collector.listcollector.ListCollector;

@XmlRootElement(name = "mybatis-spring")
public class MyBatisSpringTag extends AbstractGeneratorTag implements NamePackageResolver {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(MyBatisSpringTag.class);

  public static final String GENERATOR_NAME = "MyBatis-Spring";
  private static final Discovery DEFAULT_DISCOVERY = Discovery.ENABLED_WHEN_NO_OBJECTS_DECLARED;

  // Properties

  private String sDiscovery = null;
  private String sSchemas = null;

  private DaosSpringMyBatisTag daos = null;
  private MappersTag mappers = null;
  private TemplateTag template = null;
  private SelectGenerationTag selectGeneration = null;
  private ClassicFKNavigationTag classicFKNavigation = new ClassicFKNavigationTag();
  private List<PropertyTag> propertyTags = new ArrayList<PropertyTag>();

  private Discovery discovery;
  private List<CatalogSchema> otherSchemas;
  private MyBatisProperties properties = new MyBatisProperties();

  // enums

  public enum Discovery {
    ENABLED("enabled"), DISABLED("disabled"), ENABLED_WHEN_NO_OBJECTS_DECLARED("enabled_when_no_objects_declared");

    private String caption;

    private Discovery(final String caption) {
      this.caption = caption;
    }

    public String getCaption() {
      return caption;
    }

  }

  public boolean isAutoDiscoveryEnabled(final HotRodConfigTag config) {
    if (this.discovery == Discovery.ENABLED) {
      return true;
    }
    if (this.discovery == Discovery.DISABLED) {
      return false;
    }
    boolean declaredObjects = !config.getAllTables().isEmpty() || !config.getAllEnums().isEmpty()
        || !config.getAllViews().isEmpty() || !config.getAllExecutors().isEmpty();
    return !declaredObjects;
  }

  // Constructor

  public MyBatisSpringTag() {
    super("mybatis-spring");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute(name = "discovery")
  public void setSDiscovery(final String autoDiscovery) {
    this.sDiscovery = autoDiscovery;
  }

  @XmlAttribute(name = "other-schemas")
  public void setSSchemas(final String s) {
    this.sSchemas = s;
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
  public void validate(final File basedir, final File parentDir, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException {

    // discovery

    if (this.sDiscovery == null) {
      this.discovery = DEFAULT_DISCOVERY;
    } else {
      Discovery.values();
      this.discovery = null;
      for (Discovery ad : Discovery.values()) {
        if (this.sDiscovery.equals(ad.getCaption())) {
          this.discovery = ad;
        }
      }
      if (this.discovery == null) {
        String msg = "Invalid value '" + this.sDiscovery + "' for 'discovery' attribute. Valid values are: "
            + Arrays.stream(Discovery.values()).map(d -> d.getCaption()).collect(ListCollector.joining(", ", ", and "));
        throw new InvalidConfigurationFileException(this, msg, msg);
      }
    }

    // schemas

    this.otherSchemas = new ArrayList<>();
//    log.info("--> otherSchemas: " + this.otherSchemas);
    if (!SUtil.isEmpty(this.sSchemas)) {
      for (String s : this.sSchemas.split(",")) {
        int p = s.indexOf(".");
        String cat = null;
        String sche = null;
        if (p == -1) {
          sche = s;
        } else {
          cat = s.substring(0, p);
          sche = p + 1 >= s.length() ? null : s.substring(p + 1);
        }
        if (cat != null && SUtil.isEmpty(cat)) {
          String msg = "Invalid empty catalog name found in section '" + s
              + "' of the 'other-schemas' attribute of the <mybatis-spring> tag. "
              + "Please include a comma-separated list of '[catalog.]schema' values.";
          throw new InvalidConfigurationFileException(this, msg, msg);
        }
        if (sche != null && SUtil.isEmpty(sche)) {
          String msg = "Invalid empty schema name found in section '" + s
              + "' of the 'other-schemas' attribute of the <mybatis-spring> tag. "
              + "Please include a comma-separated list of '[catalog.]schema' values.";
          throw new InvalidConfigurationFileException(this, msg, msg);
        }
        String catalog;
        try {
          catalog = cat == null ? null : Id.fromTypedSQL(cat, adapter).getCanonicalSQLName();
        } catch (InvalidIdentifierException e) {
          String msg = "Invalid catalog name name '" + cat
              + "' found in the 'schemas' attribute of the <mybatis-spring> tag. "
              + "It's not a valid catalog name in this database.";
          throw new InvalidConfigurationFileException(this, msg, msg);
        }
        String schema;
        try {
          schema = sche == null ? null : Id.fromTypedSQL(sche, adapter).getCanonicalSQLName();
        } catch (InvalidIdentifierException e) {
          String msg = "Invalid schema name name '" + sche
              + "' found in the 'schemas' attribute of the <mybatis-spring> tag. "
              + "It's not a valid catalog name in this database.";
          throw new InvalidConfigurationFileException(this, msg, msg);
        }
        CatalogSchema cs = new CatalogSchema(catalog, schema);
//        log.info("--> Added other schema: " + cs.toString());
        this.otherSchemas.add(cs);
      }
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

  public Discovery getDiscovery() {
    return discovery;
  }

  public List<CatalogSchema> getOtherSchemas() {
    return otherSchemas;
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
