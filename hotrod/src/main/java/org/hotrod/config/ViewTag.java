package org.hotrod.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.ant.Constants;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.identifiers.DataSetIdentifier;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

@XmlRootElement(name = "view")
public class ViewTag extends AbstractDAOTag {

  // Constants

  private static final Logger log = Logger.getLogger(ViewTag.class);

  // Properties

  private String name = null;
  private String javaClassName = null;
  private List<ColumnTag> columns = new ArrayList<ColumnTag>();

  private DaosTag daosTag;
  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  // Constructor

  public ViewTag() {
    super("view");
  }

  // JAXB Setters

  @XmlAttribute
  public void setName(final String name) {
    this.name = name;
  }

  @XmlAttribute(name = "java-name")
  public void setJavaName(final String javaName) {
    this.javaClassName = javaName;
  }

  @XmlElement
  public void setColumn(final ColumnTag c) {
    this.columns.add(c);
  }

  // Behavior

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig) throws InvalidConfigurationFileException {
    log.debug("validate");

    this.daosTag = daosTag;
    this.fragmentConfig = fragmentConfig;
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage() : null;

    // name

    if (SUtils.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Attribute 'name' of tag <"
          + super.getTagName() + "> cannot be empty. " + "Must specify a database view name.");
    }

    // java-name

    if (this.javaClassName != null) {
      this.javaClassName = this.javaClassName.trim();
      if (SUtils.isEmpty(this.javaClassName)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Invalid 'java-name' attribute value of tag <" + super.getTagName() + "> for the view '" + this.name
                + "'. When specified, the value cannot be empty.");
      }
      if (!this.javaClassName.matches(Patterns.VALID_JAVA_CLASS)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Invalid 'java-name' attribute value '" + this.javaClassName + "' of tag <" + super.getTagName()
                + "> for the view '" + this.name
                + "'. When specified, the java-name must start with an upper case letter, "
                + "and continue with any combination of letters, digits, underscores, or dollar signs.");
      }
    }

    // columns

    Set<ColumnTag> cols = new HashSet<ColumnTag>();
    for (ColumnTag c : this.columns) {
      c.validate(config);
      if (cols.contains(c)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Multiple <" + new ColumnTag().getTagName() + "> tags with the same name on tag <" + super.getTagName()
                + "> for table '" + this.name + "'. You cannot specify the same column name "
                + "multiple times on a same view.");
      }
      cols.add(c);
    }

    // sequences, queries, and selects

    super.validate(daosTag, config, fragmentConfig);

  }

  public void validateAgainstDatabase(final HotRodGenerator generator) throws InvalidConfigurationFileException {

    JdbcTable v = generator.findJdbcTable(this.name);
    if (v == null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Could not find database view '" + this.name + "' as specified in the <view> tag of the configuration file. "
              + "\n\nPlease verify the specified database catalog and schema names are correct according to this database. "
              + "You can try leaving the catalog/schema values empty, so " + Constants.TOOL_NAME
              + " will list all available values.");
    }

    for (ColumnTag c : this.columns) {
      JdbcColumn jc = generator.findJdbcColumn(v, c.getName());
      if (jc == null) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(), "Could not find column '" + c.getName()
            + "' on database view '" + this.name + "', as specified in the <column> tag of the configuration file. ");
      }
    }

  }

  // Search

  public ColumnTag findColumnTag(final String columnName, final DatabaseAdapter adapter) {
    for (ColumnTag ct : this.columns) {
      if (ct.isName(columnName, adapter)) {
        return ct;
      }
    }
    return null;
  }

  // Getters

  public String getName() {
    return name;
  }

  public String getJavaName() {
    return javaClassName;
  }

  public List<ColumnTag> getColumns() {
    return columns;
  }

  public HotRodFragmentConfigTag getFragmentConfig() {
    return fragmentConfig;
  }

  // DAO Tag implementation

  @Override
  public ClassPackage getPackage() {
    return this.daosTag.getDaoPackage(this.fragmentPackage);
  }

  @Override
  public String getJavaClassName() {
    if (this.javaClassName == null) {
      return new DataSetIdentifier(this.name).getJavaClassIdentifier();
    } else {
      return new DataSetIdentifier(this.name, this.javaClassName).getJavaClassIdentifier();
    }
  }

}
