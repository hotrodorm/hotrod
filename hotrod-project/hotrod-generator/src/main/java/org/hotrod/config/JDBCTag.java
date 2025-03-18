package org.hotrod.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidPackageException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.Feedback;
import org.hotrod.generator.Generator;
import org.hotrod.generator.HotRodContext;
import org.hotrod.generator.jdbc.JDBCGenerator;
import org.hotrod.identifiers.ObjectId;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.SUtil;
import org.nocrala.tools.database.tartarus.core.CatalogSchema;

@XmlRootElement(name = "jdbc")
public class JDBCTag extends AbstractGeneratorTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(JDBCTag.class.getName());

  public static final String GENERATOR_NAME = "jdbc";

  private static final String DEFAULT_BASE_DIR = "src/main/java";
  private static final String DEFAULT_MAIN_PACKAGE = "app.persistence";

  private static final Pattern QUALIFIER_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");

  // Properties

  private String sBaseDir = null;
  private String sPackage = null;
  private String qualifier = null;

  private File baseDir;
  private ClassPackage mainPackage;

  private DiscoverTag discover = null;
  private JDBCDAOTag dao = null;
  private JDBCLayoutTag layout = null;
  private JDBCModelTag model = null;

  private List<PropertyTag> propertyTags = new ArrayList<PropertyTag>();

  private JDBCProperties properties = new JDBCProperties();

  // Constructor

  public JDBCTag() {
    super("jdbc");
    log.fine("init");
  }

  public void enableDiscover() {
    if (this.discover == null) {
      this.discover = new DiscoverTag();
      SchemaTag currentSchema = new SchemaTag();
      currentSchema.setCurrent();
      this.discover.setCurrentSchema(currentSchema);
    }
  }

  // No Config

  public static JDBCTag getNoConfigTag() {
    JDBCTag t = new JDBCTag();
    t.baseDir = new File("src/main/java");
//    t.mainPackage = new ClassPackage("app.persistence");
    return t;
  }

  // JAXB Setters

  @XmlAttribute(name = "base-dir")
  public void setBaseDir(final String sBaseDir) {
    this.sBaseDir = sBaseDir;
  }

  @XmlAttribute(name = "package")
  public void setSPackage(final String sPackage) {
    this.sPackage = sPackage;
  }

  @XmlAttribute(name = "qualifier")
  public void setQualifier(final String qualifier) {
    this.qualifier = qualifier;
  }

  @XmlElement(name = "discover")
  public void setDiscover(final DiscoverTag discover) {
    this.discover = discover;
  }

  @XmlElement(name = "dao")
  public void setDAO(final JDBCDAOTag dao) throws InvalidConfigurationFileException {
    if (this.dao != null) {
      throw new InvalidConfigurationFileException(this,
          "Duplicate <dao> tag; the JDBC generator can only have a single <dao> tag");
    }
    this.dao = dao;
  }

  @XmlElement(name = "layout")
  public void setLayout(final JDBCLayoutTag layout) throws InvalidConfigurationFileException {
    if (this.layout != null) {
      throw new InvalidConfigurationFileException(this,
          "Duplicate <layout> tag; the JDBC generator can only have a single <layout> tag");
    }
    this.layout = layout;
  }

  @XmlElement(name = "model")
  public void setModel(final JDBCModelTag model) throws InvalidConfigurationFileException {
    if (this.model != null) {
      throw new InvalidConfigurationFileException(this,
          "Duplicate <model> tag; the JDBC generator can only have a single <model> tag");
    }
    this.model = model;
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
  public void validate(final File currentDir, final File parentDir, final DatabaseAdapter adapter,
      final CatalogSchema currentCS) throws InvalidConfigurationFileException {

    // base-dir

    if (this.sBaseDir == null) {
      this.sBaseDir = DEFAULT_BASE_DIR;
    }
    if (SUtil.isEmpty(this.sBaseDir)) {
      throw new InvalidConfigurationFileException(this,
          "When specified, the attribute 'base-dir' of the tag <" + super.getTagName() + "> cannot be empty.");
    }
    this.baseDir = new File(currentDir, this.sBaseDir);
    if (!this.baseDir.exists()) {
      throw new InvalidConfigurationFileException(this, "Attribute 'base-dir' of the tag <" + super.getTagName()
          + "> with value '" + this.sBaseDir + "' must point to an existing dir.");
    }
    if (!this.baseDir.isDirectory()) {
      throw new InvalidConfigurationFileException(this, "Attribute 'base-dir' of the tag <" + super.getTagName()
          + "> with value '" + this.sBaseDir + "' points to a file entry that is not a directory.");
    }

    // package

    if (this.sPackage == null) {
      try {
        this.mainPackage = new ClassPackage(DEFAULT_MAIN_PACKAGE);
      } catch (InvalidPackageException e) {
        throw new InvalidConfigurationFileException(null,
            "The default package '" + DEFAULT_MAIN_PACKAGE + "' is invalid");
      }
    } else {
      try {
        this.mainPackage = new ClassPackage(this.sPackage);
      } catch (InvalidPackageException e) {
        throw new InvalidConfigurationFileException(this, "Invalid package '" + this.sPackage
            + "' on attribute 'package' of the tag <" + super.getTagName() + ">: " + e.getMessage());
      }
    }

    // qualifier

    if (this.qualifier != null) {
      if (SUtil.isEmpty(this.qualifier)) {
        throw new InvalidConfigurationFileException(null, "When specified, the qualifier cannot be empty.");
      }
      Matcher m = QUALIFIER_PATTERN.matcher(this.qualifier);
      if (!m.matches()) {
        throw new InvalidConfigurationFileException(null,
            "When specified, the qualifier must be an alphanumeric value (underscores are permitted).");
      }
    }

    // discovery

    if (this.discover != null) {
      this.discover.validate(adapter, currentCS);
    }

    // dao

    if (this.dao == null) {
      this.dao = new JDBCDAOTag();
    }
    this.dao.validate(currentDir, this.baseDir, this.mainPackage);

    // layout

    if (this.layout == null) {
      this.layout = new JDBCLayoutTag();
    }
    this.layout.validate(currentDir, this.baseDir, this.mainPackage);

    // model

    if (this.model == null) {
      this.model = new JDBCModelTag();
    }
    this.model.validate(currentDir, this.baseDir, this.mainPackage);

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

  public String getQualifier() {
    return qualifier;
  }

  public File getBaseDir() {
    return baseDir;
  }

  public ClassPackage getMainPackage() {
    return mainPackage;
  }

  public DiscoverTag getDiscover() {
    return this.discover;
  }

  public JDBCDAOTag getDao() {
    return dao;
  }

  public JDBCLayoutTag getLayout() {
    return layout;
  }

  public JDBCModelTag getModel() {
    return model;
  }

  public JDBCProperties getProperties() {
    return properties;
  }

  // Produce Generator Instance

  @Override
  public Generator instantiateGenerator(final HotRodContext hc, final EnabledFKs enabledFKs,
      final DisplayMode displayMode, final boolean incrementalMode, final Feedback feedback)
      throws UncontrolledException, ControlledException, InvalidConfigurationFileException {
    return new JDBCGenerator(hc, enabledFKs, displayMode, incrementalMode, feedback);
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

  // DAO: Names and Packages

  public String getDAOName(ObjectId id) {
    return null;
  }

  public ClassPackage getDAOPackage(ClassPackage fragmentPackage) {
    return null;
  }

  public ClassPackage getDAOPackage() {
    return null;
  }

  public File getDAOPackageDir(ClassPackage fragmentPackage) {
    return null;
  }

  // Layout: Names and Packages

  public String getLayoutName(ObjectId id) {
    return null;
  }

  public ClassPackage getLayoutPackage(ClassPackage fragmentPackage) {
    return null;
  }

  public File getLayoutPackageDir(ClassPackage fragmentPackage) {
    return null;
  }

  // Model: Names and Packages

  public String getModelName(ObjectId id) {
    return null;
  }

  public File getModelPackageDir(ClassPackage fragmentPackage) {
    return null;
  }

  // Nitro

  public String getNitroDAOName(String baseName) {
    return null;
  }

  public String getNitroLayoutName(String baseName) {
    return null;
  }

  public String getNitroModelName(String baseName) {
    return null;
  }

}
