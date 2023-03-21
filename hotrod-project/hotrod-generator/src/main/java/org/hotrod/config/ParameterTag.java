package org.hotrod.config;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.identifiers.Id;
import org.hotrod.utils.JdbcTypes;
import org.hotrod.utils.JdbcTypes.JDBCType;
import org.hotrodorm.hotrod.utils.SUtil;

@XmlRootElement(name = "parameter")
public class ParameterTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(ParameterTag.class);

  static final String TAG_NAME = "parameter";

  // Properties

  private String name = null;
  private String javaType = null;
  private String jdbcTypeName = null;
  private String sampleSQLValue = null;

  private Id id = null;
  private JDBCType jdbcType;

  // Constructor

  public ParameterTag() {
    super("parameter");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute
  public void setName(final String name) {
    this.name = name;
  }

  @XmlAttribute(name = "java-type")
  public void setJavaType(final String javaType) {
    this.javaType = javaType;
  }

  @XmlAttribute(name = "jdbc-type")
  public void setJdbcType(final String jdbcType) {
    this.jdbcTypeName = jdbcType;
  }

  @XmlAttribute(name = "sample-sql-value")
  public void setSampleSQLValue(final String s) {
    this.sampleSQLValue = s;
  }

  // Behavior

  public void validate() throws InvalidConfigurationFileException {

    // name

    if (this.name == null) {
      throw new InvalidConfigurationFileException(this,
          "Invalid <parameter> tag -- the 'name' attribute must be specified.");
    }
    if (SUtil.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(this,
          "Invalid <parameter> tag -- the 'name' attribute cannot be blank.");
    }
    if (!this.name.matches(Patterns.VALID_JAVA_VARIABLE)) {
      throw new InvalidConfigurationFileException(this, "Invalid Java parameter name '" + this.name
          + "'. The 'name' attribute must start with a lower case letter and continue with letters, digits, and/or underscores.");
    }

    try {
      this.id = Id.fromJavaMember(this.name);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid name '" + this.name + "' for a Java parameter: " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg);
    }

    // java-type

    if (this.javaType == null) {
      throw new InvalidConfigurationFileException(this,
          "invalid <parameter> tag -- the 'java-type' attribute cannot be empty.");
    }
    if (SUtil.isEmpty(this.javaType)) {
      throw new InvalidConfigurationFileException(this,
          "invalid <parameter> tag -- the 'java-type' attribute cannot be blank.");
    }
    if (!this.javaType.matches(Patterns.VALID_JAVA_TYPE)) {
      throw new InvalidConfigurationFileException(this, "invalid <parameter> tag -- "
          + "the 'java-type' attribute must start with a letter and continue with letters, digits, and/or underscores, but found '"
          + this.javaType + "'.");
    }

    // jdbc-type

    if (this.jdbcTypeName == null) {
      this.jdbcType = getDefaultJDBCType(this.javaType);
      log.debug("this.jdbcType=" + this.jdbcType);
      if (this.jdbcType == null) {
        throw new InvalidConfigurationFileException(this,
            "Could not guess the JDBC type for the parameter based on its java type '" + this.javaType
                + "'. Please include the 'jdbc-type' attribute to specify it; "
                + "must be a valid JDBC type name from java.sql.Types. Valid type names are: "
                + Stream.of(JDBCType.values()).map(t -> t.getShortTypeName()).collect(Collectors.joining(", ")));
      }
    } else {
      if (SUtil.isEmpty(this.jdbcTypeName)) {
        throw new InvalidConfigurationFileException(this, //
            "When specified, the 'jdbc-type' attribute cannot be blank; "
                + "must be a valid JDBC type name from java.sql.Types. Valid type names are: "
                + Stream.of(JDBCType.values()).map(t -> t.getShortTypeName()).collect(Collectors.joining(", ")));
      }
      this.jdbcType = JdbcTypes.nameToType(this.jdbcTypeName);
      if (this.jdbcType == null) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid 'jdbc-type' attribute with value '" + this.jdbcTypeName
                + "': must be a valid JDBC type name from java.sql.Types. Valid type names are: "
                + Stream.of(JDBCType.values()).map(t -> t.name()).collect(Collectors.joining(", ")));
      }
    }

    // sample-sql-value

    if (this.sampleSQLValue != null) {
      if (this.sampleSQLValue.length() == 0) {
        throw new InvalidConfigurationFileException(this,
            "Invalid 'sample-sql-value' attribute; when specified it cannot be empty");
      }
    }

  }

  // Getters

  public String getName() {
    return name;
  }

  public String getJavaType() {
    return javaType;
  }

  public JDBCType getJDBCType() {
    return jdbcType;
  }

  public String getSampleSQLValue() {
    return sampleSQLValue;
  }

  public Id getId() {
    return this.id;
  }

  @Override
  public String toString() {
    return "ParameterTag [name=" + name + ", javaType=" + javaType + ", jdbcTypeName=" + jdbcTypeName + ", id=" + id
        + ", jdbcType=" + jdbcType + "]";
  }

  // JDBC type defaults

  private JDBCType getDefaultJDBCType(final String javaClass) {
    String c = javaClass.indexOf('.') == -1 ? "java.lang." + javaClass : javaClass;

    if ("java.lang.Byte".equals(c)) {
      return JDBCType.TINYINT;
    } else if ("java.lang.Short".equals(c)) {
      return JDBCType.SMALLINT;
    } else if ("java.lang.Integer".equals(c)) {
      return JDBCType.INTEGER;
    } else if ("java.lang.Long".equals(c)) {
      return JDBCType.BIGINT;
    } else if ("java.lang.Float".equals(c)) {
      return JDBCType.REAL;
    } else if ("java.lang.Double".equals(c)) {
      return JDBCType.DOUBLE;
    } else if ("java.math.BigInteger".equals(c)) {
      return JDBCType.DECIMAL;
    } else if ("java.math.BigDecimal".equals(c)) {
      return JDBCType.DECIMAL;

    } else if ("java.lang.Char".equals(c)) {
      return JDBCType.CHAR;
    } else if ("java.lang.String".equals(c)) {
      return JDBCType.VARCHAR;

    } else if ("java.util.Date".equals(c)) {
      return JDBCType.TIMESTAMP;
    } else if ("java.sql.Date".equals(c)) {
      return JDBCType.DATE;
    } else if ("java.sql.Timestamp".equals(c)) {
      return JDBCType.TIMESTAMP;
    } else if ("java.sql.Time".equals(c)) {
      return JDBCType.TIME;
    } else if ("java.time.LocalDate".equals(c)) {
      return JDBCType.DATE;
    } else if ("java.time.LocalTime".equals(c)) {
      return JDBCType.TIME;
    } else if ("java.time.LocalDateTime".equals(c)) {
      return JDBCType.TIMESTAMP;
    } else if ("java.time.OffsetDateTime".equals(c)) {
      return JDBCType.TIMESTAMP_WITH_TIMEZONE;
    } else if ("java.time.ZonedDateTime".equals(c)) {
      return JDBCType.TIMESTAMP_WITH_TIMEZONE;

    } else if ("java.lang.Boolean".equals(c)) {
      return JDBCType.BOOLEAN;

    } else if ("byte[]".equals(c)) {
      return JDBCType.BLOB;

    } else {
      return null;
    }

  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.name;
  }

}
