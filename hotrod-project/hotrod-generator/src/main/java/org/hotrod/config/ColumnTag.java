package org.hotrod.config;

import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.ValueRange;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.identifiers.Id;
import org.hotrod.identifiers.ObjectId;
import org.hotrod.metadata.Metadata;
import org.hotrod.utils.JDBCTypes;
import org.hotrod.utils.SUtil;
import org.hotrod.utils.TypesUtil;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

@XmlRootElement(name = "column")
public class ColumnTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(ColumnTag.class.getName());

  // Properties

  private String name = null;

  @Deprecated
  private String javaName = null;
  private String property = null;

  @Deprecated
  private String javaType = null;
  private String type = null;

  private String converter = null;
  private String jdbcType = null;

  private String sequence = null;
  private String catalog = null;
  private String schema = null;

  private String sInitialValue = null;
  private String sMinValue = null;
  private String sMaxValue = null;
  private String sIsLOB = null;

  private JdbcColumn column;
  private boolean isLOB;
  private ValueRange valueRange;
  private ConverterTag converterTag;

  private ObjectId sequenceId = null;

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

  @XmlAttribute(name = "property")
  public void setProperty(final String property) {
    this.property = property;
  }

  @XmlAttribute(name = "java-type")
  public void setJavaType(final String javaType) {
    this.javaType = javaType;
  }

  @XmlAttribute(name = "type")
  public void setType(final String type) {
    this.type = type;
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

  @XmlAttribute
  public void setCatalog(final String catalog) {
    this.catalog = catalog;
  }

  @XmlAttribute
  public void setSchema(final String schema) {
    this.schema = schema;
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

  private static final String PROPERTY_PATTERN = "[a-z][a-zA-Z0-9_]*+";

  public void validate(final HotRodConfigTag config, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException {

    log.fine("COLUMN DEF: " + this.toString());

    // name

    if (SUtil.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(this, "Attribute 'name' of tag <" + super.getTagName()
          + "> cannot be empty. " + "Must specify a database column name.");
    }

    // property (and old java-name)

    if (this.javaName == null) {
      if (this.property != null) {
        if (!this.property.matches(PROPERTY_PATTERN)) {
          throw new InvalidConfigurationFileException(this,
              "The attribute 'property' of the tag <" + super.getTagName()
                  + "> must start with a lower case letter and continue with letters, digits, "
                  + "and/or underscore symbols, but '" + this.property + "' was specified.");
        }
      }
    } else {
      if (this.property != null) {
        throw new InvalidConfigurationFileException(this, "Either the attribute 'property' or 'java-name' of the tag <"
            + super.getTagName() + "> can be specified at the same time. " + "Use the former when possible.");
      }
      if (!this.javaName.matches(PROPERTY_PATTERN)) {
        throw new InvalidConfigurationFileException(this,
            "The attribute 'java-name' of the tag <" + super.getTagName()
                + "> must start with a lower case letter and continue with letters, digits, "
                + "and/or underscore symbols, but '" + this.javaName + "' was specified.");
      }
      this.property = this.javaName;
      this.javaName = null;
    }

    // type (and old java-type)

    if (this.converter == null) {

      this.converterTag = null;

      String t = null;
      if (this.javaType != null) {
        if (this.type != null) {
          throw new InvalidConfigurationFileException(this,
              "The old attribute 'java-type' of the tag <" + super.getTagName()
                  + "> cannot at the same time as the new 'type' attribute. " + "Use the latter only.");
        }
        if (SUtil.isEmpty(this.javaType)) {
          throw new InvalidConfigurationFileException(this,
              "The attribute 'java-type' of the tag <" + super.getTagName() + "> cannot be empty. " + "When specified "
                  + "this attribute must specify the class name to represent the database column.");
        }
        if (!TypesUtil.isValidClassName(this.javaType)) {
          throw new InvalidConfigurationFileException(this, "The attribute 'java-type' of the tag <"
              + super.getTagName() + "> must specify a valid class name, but found '" + this.javaType + "'.");
        }
        t = this.javaType;
      } else if (this.type != null) {
        if (SUtil.isEmpty(this.type)) {
          throw new InvalidConfigurationFileException(this,
              "The attribute 'type' of the tag <" + super.getTagName() + "> cannot be empty. " + "When specified "
                  + "this attribute must specify the class name to represent the database column.");
        }
        if (!TypesUtil.isValidClassName(this.type)) {
          throw new InvalidConfigurationFileException(this, "The attribute 'type' of the tag <" + super.getTagName()
              + "> must specify a valid class name, but found '" + this.type + "'.");
        }
        t = this.type;
      } else {
        throw new InvalidConfigurationFileException(this,
            "The 'type' attribute of the tag <" + super.getTagName() + "> must be specified.");
      }
      this.type = TypesUtil.expand(t);

    } else {

      // converter

      if (this.type != null) {
        throw new InvalidConfigurationFileException(this, "Invalid attributes 'java-type' and 'converter' of tag <"
            + super.getTagName() + ">: "
            + "these attributes are mutually exclusive, so only one of them can be specified in a column definition.");
      }
      if (SUtil.isEmpty(this.converter)) {
        throw new InvalidConfigurationFileException(this, "Attribute 'converter' of tag <" + super.getTagName()
            + "> cannot be empty. " + "Must specify a valid converter name.");
      }
      this.converterTag = config.getConverterTagByName(this.converter);
      if (this.converterTag == null) {
        throw new InvalidConfigurationFileException(this, "Converter '" + this.converter + "' not found.");
      }

    }

    // jdbc-type

    if (this.jdbcType != null) {
      if (this.type == null && this.converter == null) {
        throw new InvalidConfigurationFileException(this,
            "'jdbc-type' attribute specified but no java-type attribute nor converter attribute found. "
                + "The jdbc-type attribute can only be specified when the java-type attribute or the converter is present.");
      }
      if (SUtil.isEmpty(this.jdbcType)) {
        throw new InvalidConfigurationFileException(this,
            "'jdbc-type' attribute cannot be empty. " + "When specified, the attribute 'jdbc-type' of the tag <"
                + super.getTagName() + "> must specify a valid JDBC type "
                + "as defined in the java class java.sql.Types. "
                + "Make sure you specify it in all uppercase letters.");
      }
      if (JDBCTypes.nameToCode(this.jdbcType) == null) {
        throw new InvalidConfigurationFileException(this,
            "Invalid 'jdbc-type' attribute with value '" + this.jdbcType
                + "'. When specified, the attribute 'jdbc-type' of the tag <" + super.getTagName()
                + "> must specify a valid JDBC type " + "as defined in the java class java.sql.Types. "
                + "Make sure you specify it in all uppercase letters.");
      }
    }

    // sequence

    if (this.sequence != null) {
      if (SUtil.isEmpty(this.sequence)) {
        throw new InvalidConfigurationFileException(this, "When specified, attribute 'sequence' cannot be empty");
      }
    }

    // catalog

    if (this.sequence == null && this.catalog != null) {
      throw new InvalidConfigurationFileException(this,
          "The catalog attribute cannot be specified when the sequence attribute is not present");
    }
    Id catalogId;
    try {
      catalogId = this.catalog == null ? null : Id.fromTypedSQL(this.catalog, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid catalog name '" + this.catalog + "' on tag <" + super.getTagName() + "> for the table '"
          + this.name + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg);
    }

    // schema

    if (this.sequence == null && this.schema != null) {
      throw new InvalidConfigurationFileException(this,
          "The schema attribute cannot be specified when the sequence attribute is not present");
    }
    Id schemaId;
    try {
      schemaId = this.schema == null ? null : Id.fromTypedSQL(this.schema, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid schema name '" + this.schema + "' on tag <" + super.getTagName() + "> for the table '"
          + this.name + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg);
    }

    // Assemble sequence's object id

    if (this.sequence != null) {
      Id sequenceNameId;
      try {
        sequenceNameId = Id.fromTypedSQL(this.sequence, adapter);
      } catch (InvalidIdentifierException e) {
        String msg = "Invalid sequence name '" + this.sequence + "': " + e.getMessage();
        throw new InvalidConfigurationFileException(this, msg);
      }

      try {
        this.sequenceId = new ObjectId(catalogId, schemaId, sequenceNameId, adapter);
      } catch (InvalidIdentifierException e) {
        String msg = "Invalid table object name: " + e.getMessage();
        throw new InvalidConfigurationFileException(this, msg);
      }
    } else {
      this.sequenceId = null;
    }

    // is-lob

    if (this.sIsLOB == null) {
      this.isLOB = false;
    } else if ("true".equals(this.sIsLOB)) {
      this.isLOB = true;
    } else if ("false".equals(this.sIsLOB)) {
      this.isLOB = false;
    } else {
      throw new InvalidConfigurationFileException(this,
          "Invalid 'is-lob' attribute value '" + this.sIsLOB + "'. When specified, the attribute 'is-lob' of the tag <"
              + super.getTagName() + "> must be either 'true' or 'false'.");
    }

    // initial-value, min-value, max-value

    if (this.sInitialValue == null && this.sMinValue == null && this.sMaxValue == null) {

      this.valueRange = null;

    } else {

      if (this.sInitialValue == null || this.sMinValue == null || this.sMaxValue == null) {

        throw new InvalidConfigurationFileException(this,
            "Invalid partially specified value range on tag <" + super.getTagName() + ">. When a range is specified, "
                + "all three attributee 'initial-value', " + "'min-value' and 'max-value' must be specified.");

      } else {

        // initial-value

        long initialValue;
        try {
          initialValue = Long.parseLong(this.sInitialValue);
        } catch (NumberFormatException e) {
          throw new InvalidConfigurationFileException(this,
              "Invalid value '" + this.sInitialValue + "' for attribute 'initial-value' on tag <" + super.getTagName()
                  + ">. When specified, it must be a numeric value in the range " + Long.MIN_VALUE + " to "
                  + Long.MAX_VALUE + ".");
        }

        // min-value

        long minValue;
        try {
          minValue = Long.parseLong(this.sMinValue);
        } catch (NumberFormatException e) {
          throw new InvalidConfigurationFileException(this,
              "Invalid value '" + this.sMinValue + "' for attribute 'min-value' on tag <" + super.getTagName()
                  + ">. When specified, it must be a numeric value in the range " + Long.MIN_VALUE + " to "
                  + Long.MAX_VALUE + ".");
        }

        // max-value

        long maxValue;
        try {
          maxValue = Long.parseLong(this.sMaxValue);
        } catch (NumberFormatException e) {
          throw new InvalidConfigurationFileException(this,
              "Invalid value '" + this.sMaxValue + "' for attribute 'max-value' on tag <" + super.getTagName()
                  + ">. When specified, it must be a numeric value in the range " + Long.MIN_VALUE + " to "
                  + Long.MAX_VALUE + ".");
        }

        this.valueRange = new ValueRange(initialValue, minValue, maxValue);

      }
    }

  }

  void populateJdbcElements(final Metadata metadata, final JdbcTable t) throws InvalidConfigurationFileException {
    this.column = metadata.findJdbcColumn(t, this.name);
  }

  public void validateAgainstDatabase(final Metadata metadata) throws InvalidConfigurationFileException {
    if (this.sequence != null) {
      if (this.column.getAutogenerationType() != null && this.column.getAutogenerationType().isIdentity()) {
        throw new InvalidConfigurationFileException(this, "Invalid 'sequence' attribute on column '" + this.name
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

  public String getProperty() {
    return this.property;
  }

  public String getType() {
    return type;
  }

  public String getJdbcType() {
    return jdbcType;
  }

  public ObjectId getSequenceId() {
    return this.sequenceId;
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
    return "name=" + name + " property=" + this.property + " type=" + this.type + ", converter=" + this.converter
        + " jdbcType=" + this.jdbcType + " sequence=" + this.sequence + ", sInitialValue=" + this.sInitialValue
        + " sMinValue=" + this.sMinValue + " sMaxValue=" + this.sMaxValue;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.name;
  }

}
