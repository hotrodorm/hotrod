package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.PropertyType.ValueRange;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.JdbcTypes;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

@XmlRootElement(name = "column")
public class ColumnTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(ColumnTag.class);

  static final String VALID_JAVA_NAME_PATTERN = "[a-z][a-zA-Z0-9_$]*";

  // Properties

  private String name = null;
  private String javaName = null;
  private String javaType = null;
  private String converter = null;
  private String jdbcType = null;
  private String sInitialValue = null;
  private String sMinValue = null;
  private String sMaxValue = null;
  private String sIsLOB = null;

  private JdbcColumn column;
  private boolean isLOB;
  private ValueRange valueRange;
  private ConverterTag converterTag;

  // Constructor

  public ColumnTag() {
    super("column");
  }

  // JAXB Setters

  @XmlAttribute
  public void setName(final String name) {
    this.name = name;
  }

  @XmlAttribute(name = "java-name")
  public void setJavaName(final String javaName) {
    this.javaName = javaName;
  }

  @XmlAttribute(name = "java-type")
  public void setJavaType(final String javaType) {
    this.javaType = javaType;
  }

  @XmlAttribute(name = "jdbc-type")
  public void setJdbcType(final String jdbcType) {
    this.jdbcType = jdbcType;
  }

  @XmlAttribute(name = "is-lob")
  public void setIsLOB(final String sIsLOB) {
    this.sIsLOB = sIsLOB;
  }

  @XmlAttribute(name = "initial-value")
  public void setsInitialValue(String sInitialValue) {
    this.sInitialValue = sInitialValue;
  }

  @XmlAttribute(name = "min-value")
  public void setsMinValue(String sMinValue) {
    this.sMinValue = sMinValue;
  }

  @XmlAttribute(name = "max-value")
  public void setsMaxValue(String sMaxValue) {
    this.sMaxValue = sMaxValue;
  }

  @XmlAttribute
  public void setConverter(String converter) {
    this.converter = converter;
  }

  // Behavior

  public void validate(final String enclosingTagName, final String enclosingName, final HotRodConfigTag config)
      throws InvalidConfigurationFileException {

    log.debug("COLUMN DEF: " + this.toString());

    // name

    if (SUtils.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(
          "Attribute 'name' of tag <" + super.getTagName() + "> of <" + enclosingTagName + "> tag '" + enclosingName
              + "' cannot be empty. " + "Must specify a database column name.");
    }

    // java-name

    if (this.javaName != null) {
      this.javaName = this.javaName.trim();
      if (SUtils.isEmpty(this.javaName)) {
        throw new InvalidConfigurationFileException(
            "Invalid 'java-name' attribute value of tag <" + super.getTagName() + "> for the column '" + this.name
                + "' of table '" + enclosingName + "'. When specified, the value cannot be empty.");
      }
      if (!this.javaName.matches(VALID_JAVA_NAME_PATTERN)) {
        throw new InvalidConfigurationFileException("Invalid 'java-name' attribute value '" + this.javaName
            + "' of tag <" + super.getTagName() + "> for the column '" + this.name + "' of table '" + enclosingName
            + "'. When specified, the java-name must start with an lower case letter, "
            + "and continue with any combination of letters, digits, underscores, or dollar signs.");
      }
    }

    // java-type

    if (this.javaType != null) {
      if (SUtils.isEmpty(this.javaType)) {
        throw new InvalidConfigurationFileException("Attribute 'java-type' of tag <" + super.getTagName() + "> (table '"
            + enclosingName + "', column '" + this.name + "') cannot be empty. " + "When specified, "
            + "this attribute must specify a full java class name for the database column.");
      }
    }

    // converter

    if (this.converter != null) {
      if (this.javaType != null) {
        throw new InvalidConfigurationFileException("Invalid attributes 'java-type' and 'converter' of tag <"
            + super.getTagName() + "> for column '" + this.name + "' of <" + enclosingTagName + "> tag '"
            + enclosingName
            + "': these attributes are mutually exclusive, so only one of them can be specified for a column definition.");
      }
      if (SUtils.isEmpty(this.converter)) {
        throw new InvalidConfigurationFileException("Attribute 'converter' of tag <" + super.getTagName()
            + "> for column '" + this.name + "' of <" + enclosingTagName + "> tag '" + enclosingName
            + "' cannot be empty. " + "Must specify a valid converter name.");
      }
      this.converterTag = config.getConverterTagByName(this.converter);
      if (this.converterTag == null) {
        throw new InvalidConfigurationFileException(
            "Undefined converter '" + this.converter + "'. The <" + super.getTagName() + "> tag for column '"
                + this.name + "' of <" + enclosingTagName + "> tag '" + enclosingName + "' references it.");
      }
    } else {
      this.converterTag = null;
    }

    // jdbc-type

    if (this.jdbcType != null) {
      if (this.javaType == null && this.converter == null) {
        throw new InvalidConfigurationFileException(
            "jdbc-type attribute specified but no java-type attribute nor converter attibute found, for column '"
                + this.name + "' of table '" + enclosingName + "'. "
                + "The jdbc-type attribute can only be specified when the java-type attribute or the converter is present.");
      }
      if (SUtils.isEmpty(this.jdbcType)) {
        throw new InvalidConfigurationFileException(
            "Invalid jdbc-type value '" + this.jdbcType + "' on column '" + this.name + "' of table '" + enclosingName
                + "': cannot be empty. " + "When specified, the attribute 'jdbc-type' of the tag <" + super.getTagName()
                + "> must specify a valid JDBC type " + "as defined in the java class java.sql.Types. "
                + "Make sure you specify it in all uppercase letters.");
      }
      if (JdbcTypes.nameToCode(this.jdbcType) == null) {
        throw new InvalidConfigurationFileException("Invalid jdbc-type value '" + this.jdbcType + "' on column '"
            + this.name + "' of table '" + enclosingName + "'. When specified, the attribute 'jdbc-type' of the tag <"
            + super.getTagName() + "> must specify a valid JDBC type " + "as defined in the java class java.sql.Types. "
            + "Make sure you specify it in all uppercase letters.");
      }
    }

    // is-lob

    if (this.sIsLOB == null) {
      this.isLOB = false;
    } else if ("true".equals(this.sIsLOB)) {
      this.isLOB = true;
    } else if ("false".equals(this.sIsLOB)) {
      this.isLOB = false;
    } else {
      throw new InvalidConfigurationFileException("Invalid is-lob value '" + this.sIsLOB + "' on column '" + this.name
          + "' of table '" + enclosingName + "'. When specified, the attribute 'is-lob' of the tag <"
          + super.getTagName() + "> must be either 'true' or 'false'.");
    }

    // initial-value, min-value, max-value

    if (this.sInitialValue == null && this.sMinValue == null && this.sMaxValue == null) {

      this.valueRange = null;

    } else {

      if (this.sInitialValue == null || this.sMinValue == null || this.sMaxValue == null) {

        throw new InvalidConfigurationFileException(
            "Partially specified value range on tag <" + super.getTagName() + "> (table '" + enclosingName
                + "', column '" + this.name + "'). " + "When specified, all three attributee 'initial-value', "
                + "'min-value' and 'max-value' must be specified together, " + "but found only one or two of them.");

      } else {

        // initial-value

        long initialValue;
        try {
          initialValue = Long.parseLong(this.sInitialValue);
        } catch (NumberFormatException e) {
          throw new InvalidConfigurationFileException("Invalid value '" + this.sInitialValue
              + "' for attribute 'initial-value' on tag <" + super.getTagName() + "> (table '" + enclosingName
              + "', column '" + this.name + "'). When specified, it must be a numeric value in the range "
              + Long.MIN_VALUE + " to " + Long.MAX_VALUE + ".");
        }

        // min-value

        long minValue;
        try {
          minValue = Long.parseLong(this.sMinValue);
        } catch (NumberFormatException e) {
          throw new InvalidConfigurationFileException("Invalid value '" + this.sMinValue
              + "' for attribute 'min-value' on tag <" + super.getTagName() + "> (table '" + enclosingName
              + "', column '" + this.name + "'). When specified, it must be a numeric value in the range "
              + Long.MIN_VALUE + " to " + Long.MAX_VALUE + ".");
        }

        // max-value

        long maxValue;
        try {
          maxValue = Long.parseLong(this.sMaxValue);
        } catch (NumberFormatException e) {
          throw new InvalidConfigurationFileException("Invalid value '" + this.sMaxValue
              + "' for attribute 'max-value' on tag <" + super.getTagName() + "> (table '" + enclosingName
              + "', column '" + this.name + "'). When specified, it must be a numeric value in the range "
              + Long.MIN_VALUE + " to " + Long.MAX_VALUE + ".");
        }

        this.valueRange = new ValueRange(initialValue, minValue, maxValue);

      }
    }

  }

  public void populateJdbcElements(final JdbcDatabase db, final DatabaseAdapter adapter, final JdbcTable t,
      final String tableName) throws InvalidConfigurationFileException {
    this.column = findJdbcColumn(db, adapter, t);
    if (this.column == null) {
      throw new InvalidConfigurationFileException("Could not find column '" + tableName + "' on table '" + this.name
          + "' as specified in the the attribute 'name' of tag <" + super.getTagName() + ">.");
    }
  }

  private JdbcColumn findJdbcColumn(final JdbcDatabase db, final DatabaseAdapter adapter, final JdbcTable t) {
    for (JdbcColumn c : t.getColumns()) {
      if (adapter.isColumnIdentifier(c.getName(), this.name)) {
        return c;
      }
    }
    return null;
  }

  public boolean isName(final String jdbcName, final DatabaseAdapter adapter) {
    return adapter.isColumnIdentifier(jdbcName, this.name);
  }

  // Indexable

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ColumnTag other = (ColumnTag) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

  // Getters

  public String getName() {
    return name;
  }

  public String getJavaName() {
    return javaName;
  }

  public String getJavaType() {
    return javaType;
  }

  public String getJdbcType() {
    return jdbcType;
  }

  public boolean isLOB() {
    return isLOB;
  }

  public ValueRange getValueRange() {
    return valueRange;
  }

  public ConverterTag getConverterTag() {
    return converterTag;
  }

  // ToString

  public String toString() {
    return "name=" + name + " javaName=" + this.javaName + " javaType=" + this.javaType + ", converter="
        + this.converter + " jdbcType=" + this.jdbcType + ", sInitialValue=" + this.sInitialValue + " sMinValue="
        + this.sMinValue + " sMaxValue=" + this.sMaxValue;
  }

}
