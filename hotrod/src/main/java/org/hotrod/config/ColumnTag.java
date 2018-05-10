package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.PropertyType.ValueRange;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.Compare;
import org.hotrod.utils.JdbcTypes;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

@XmlRootElement(name = "column")
public class ColumnTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(ColumnTag.class);

  // Properties

  private String name = null;
  private String javaName = null;
  private String javaType = null;
  private String converter = null;
  private String jdbcType = null;
  private String sequence = null;
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

  @XmlAttribute
  public void setConverter(final String converter) {
    this.converter = converter;
  }

  public void setConverterTag(final ConverterTag converterTag) {
    this.converterTag = converterTag;
  }

  @XmlAttribute(name = "jdbc-type")
  public void setJdbcType(final String jdbcType) {
    this.jdbcType = jdbcType;
  }

  @XmlAttribute(name = "sequence")
  public void setSequence(final String sequence) {
    this.sequence = sequence;
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

  // Behavior

  public void validate(final HotRodConfigTag config) throws InvalidConfigurationFileException {

    log.debug("COLUMN DEF: " + this.toString());

    // name

    if (SUtils.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute 'name' cannot be empty", //
          "Attribute 'name' of tag <" + super.getTagName() + "> cannot be empty. "
              + "Must specify a database column name.");
    }

    // java-name

    if (this.javaName != null) {
      this.javaName = this.javaName.trim();
      if (SUtils.isEmpty(this.javaName)) {
        throw new InvalidConfigurationFileException(this, //
            "When specified, 'java-name' cannot be empty", //
            "Invalid 'java-name' attribute value of tag <" + super.getTagName() + ">. "
                + "When specified, the value cannot be empty.");
      }
      if (!this.javaName.matches(Patterns.VALID_JAVA_PROPERTY)) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid 'java-name': when specified, the java-name must start with an lower case letter, "
                + "and continue with any combination of letters, digits, underscores, or dollar signs", //
            "Invalid 'java-name' attribute value '" + this.javaName + "' of tag <" + super.getTagName()
                + ">. When specified, the java-name must start with an lower case letter, "
                + "and continue with any combination of letters, digits, underscores, or dollar signs.");
      }
    }

    // java-type

    if (this.javaType != null) {
      if (SUtils.isEmpty(this.javaType)) {
        throw new InvalidConfigurationFileException(this, //
            "Attribute 'java-type' cannot be empty: must specify a full java class name for the database column", //
            "Attribute 'java-type' of tag <" + super.getTagName() + "> cannot be empty. " + "When specified, "
                + "this attribute must specify a full java class name for the database column.");
      }
    }

    // converter

    if (this.converter != null) {
      if (this.javaType != null) {
        throw new InvalidConfigurationFileException(this, //
            "'java-type' and 'converter' are mutually exclusive", //
            "Invalid attributes 'java-type' and 'converter' of tag <" + super.getTagName() + ">: "
                + "these attributes are mutually exclusive, so only one of them can be specified in a column definition.");
      }
      if (SUtils.isEmpty(this.converter)) {
        throw new InvalidConfigurationFileException(this, //
            "Attribute 'converter' cannot be empty: must specify a valid converter name", //
            "Attribute 'converter' of tag <" + super.getTagName() + "> cannot be empty. "
                + "Must specify a valid converter name.");
      }
      this.converterTag = config.getConverterTagByName(this.converter);
      if (this.converterTag == null) {
        throw new InvalidConfigurationFileException(this, //
            "Converter '" + this.converter + "' not found", //
            "Converter '" + this.converter + "' not found.");
      }
    } else {
      this.converterTag = null;
    }

    // jdbc-type

    if (this.jdbcType != null) {
      if (this.javaType == null && this.converter == null) {
        throw new InvalidConfigurationFileException(this, //
            "When the 'jdbc-type' attribute is specified 'java-type' or 'converter' must also be specified", //
            "'jdbc-type' attribute specified but no java-type attribute nor converter attribute found. "
                + "The jdbc-type attribute can only be specified when the java-type attribute or the converter is present.");
      }
      if (SUtils.isEmpty(this.jdbcType)) {
        throw new InvalidConfigurationFileException(this, //
            "'jdbc-type' attribute cannot be empty", //
            "'jdbc-type' attribute cannot be empty. " + "When specified, the attribute 'jdbc-type' of the tag <"
                + super.getTagName() + "> must specify a valid JDBC type "
                + "as defined in the java class java.sql.Types. "
                + "Make sure you specify it in all uppercase letters.");
      }
      if (JdbcTypes.nameToCode(this.jdbcType) == null) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid 'jdbc-type' attribute: "
                + "must be a valid (upper case) JDBC type as defined in the java class java.sql.Types", //
            "Invalid 'jdbc-type' attribute with value '" + this.jdbcType
                + "'. When specified, the attribute 'jdbc-type' of the tag <" + super.getTagName()
                + "> must specify a valid JDBC type " + "as defined in the java class java.sql.Types. "
                + "Make sure you specify it in all uppercase letters.");
      }
    }

    // sequence

    if (this.sequence != null) {
      if (SUtils.isEmpty(this.sequence)) {
        throw new InvalidConfigurationFileException(this, //
            "Attribute 'sequence' cannot be empty", //
            "Attribute 'sequence' of tag <" + super.getTagName() + "> cannot be empty. " + "When specified, "
                + "this attribute must specify the name of the database sequence "
                + "that generates values for this column.");
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
      throw new InvalidConfigurationFileException(this, //
          "Invalid 'is-lob' attribute with value '" + this.sIsLOB
              + "': when specified it must be either 'true' or 'false'", //
          "Invalid 'is-lob' attribute value '" + this.sIsLOB + "'. When specified, the attribute 'is-lob' of the tag <"
              + super.getTagName() + "> must be either 'true' or 'false'.");
    }

    // initial-value, min-value, max-value

    if (this.sInitialValue == null && this.sMinValue == null && this.sMaxValue == null) {

      this.valueRange = null;

    } else {

      if (this.sInitialValue == null || this.sMinValue == null || this.sMaxValue == null) {

        throw new InvalidConfigurationFileException(this, //
            "Invalid partially specified value range: when a range is specified, "
                + "all three attributee 'initial-value', " + "'min-value' and 'max-value' must be specified", //
            "Invalid partially specified value range on tag <" + super.getTagName() + ">. When a range is specified, "
                + "all three attributee 'initial-value', " + "'min-value' and 'max-value' must be specified.");

      } else {

        // initial-value

        long initialValue;
        try {
          initialValue = Long.parseLong(this.sInitialValue);
        } catch (NumberFormatException e) {
          throw new InvalidConfigurationFileException(this, //
              "Invalid value '" + this.sInitialValue
                  + "' for attribute 'initial-value': when specified, it must be a numeric value in the range "
                  + Long.MIN_VALUE + " to " + Long.MAX_VALUE, //
              "Invalid value '" + this.sInitialValue + "' for attribute 'initial-value' on tag <" + super.getTagName()
                  + ">. When specified, it must be a numeric value in the range " + Long.MIN_VALUE + " to "
                  + Long.MAX_VALUE + ".");
        }

        // min-value

        long minValue;
        try {
          minValue = Long.parseLong(this.sMinValue);
        } catch (NumberFormatException e) {
          throw new InvalidConfigurationFileException(this, //
              "Invalid value '" + this.sMinValue
                  + "' for attribute 'min-value': when specified, it must be a numeric value in the range "
                  + Long.MIN_VALUE + " to " + Long.MAX_VALUE, //
              "Invalid value '" + this.sMinValue + "' for attribute 'min-value' on tag <" + super.getTagName()
                  + ">. When specified, it must be a numeric value in the range " + Long.MIN_VALUE + " to "
                  + Long.MAX_VALUE + ".");
        }

        // max-value

        long maxValue;
        try {
          maxValue = Long.parseLong(this.sMaxValue);
        } catch (NumberFormatException e) {
          throw new InvalidConfigurationFileException(this, //
              "Invalid value '" + this.sMaxValue
                  + "' for attribute 'max-value' : when specified, it must be a numeric value in the range "
                  + Long.MIN_VALUE + " to " + Long.MAX_VALUE, //
              "Invalid value '" + this.sMaxValue + "' for attribute 'max-value' on tag <" + super.getTagName()
                  + ">. When specified, it must be a numeric value in the range " + Long.MIN_VALUE + " to "
                  + Long.MAX_VALUE + ".");
        }

        this.valueRange = new ValueRange(initialValue, minValue, maxValue);

      }
    }

  }

  void populateJdbcElements(final HotRodGenerator generator, final JdbcTable t)
      throws InvalidConfigurationFileException {
    this.column = generator.findJdbcColumn(t, this.name);
  }

  public void validateAgainstDatabase(final HotRodGenerator generator) throws InvalidConfigurationFileException {
    if (this.sequence != null) {
      if (this.column.getAutogenerationType() != null && this.column.getAutogenerationType().isIdentity()) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid 'sequence' attribute on column '" + this.name
                + "': the 'sequence' attribute cannot be specified on identity or auto_increment columns", //
            "Invalid 'sequence' attribute on column '" + this.name
                + "'. The 'sequence' attribute cannot be specified on identity or auto_increment columns.");
      }
    }
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

  public String getSequence() {
    return sequence;
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

  public JdbcColumn getJdbcColumn() {
    return column;
  }

  // ToString

  public String toString() {
    return "name=" + name + " javaName=" + this.javaName + " javaType=" + this.javaType + ", converter="
        + this.converter + " jdbcType=" + this.jdbcType + " sequence=" + this.sequence + ", sInitialValue="
        + this.sInitialValue + " sMinValue=" + this.sMinValue + " sMaxValue=" + this.sMaxValue;
  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    try {
      ColumnTag f = (ColumnTag) fresh;
      return this.name.equals(f.name);
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      ColumnTag f = (ColumnTag) fresh;
      boolean different = !same(fresh);
      this.javaName = f.javaName;
      this.javaType = f.javaType;
      this.converter = f.converter;
      this.jdbcType = f.jdbcType;
      this.sequence = f.sequence;
      this.sInitialValue = f.sInitialValue;
      this.sMinValue = f.sMinValue;
      this.sMaxValue = f.sMaxValue;
      this.sIsLOB = f.sIsLOB;
      this.column = f.column;
      this.isLOB = f.isLOB;
      this.valueRange = f.valueRange;
      this.converterTag = f.converterTag;
      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      ColumnTag f = (ColumnTag) fresh;
      return //
      Compare.same(this.name, f.name) && //
          Compare.same(this.javaName, f.javaName) && //
          Compare.same(this.javaType, f.javaType) && //
          Compare.same(this.converter, f.converter) && //
          Compare.same(this.jdbcType, f.jdbcType) && //
          Compare.same(this.sequence, f.sequence) && //
          Compare.same(this.sInitialValue, f.sInitialValue) && //
          Compare.same(this.sMinValue, f.sMinValue) && //
          Compare.same(this.sMaxValue, f.sMaxValue) && //
          Compare.same(this.sIsLOB, f.sIsLOB);
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.name;
  }

}
