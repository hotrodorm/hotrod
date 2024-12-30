package org.hotrod.config;

import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.DynamicExpressionFactoryConfig;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.JDBCTypes;
import org.hotrod.utils.JDBCTypes.JDBCType;
import org.hotrod.utils.SUtil;

@XmlRootElement(name = "column")
public class TypeSolverWhenTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(TypeSolverWhenTag.class.getName());

  // Properties

  private String test = null;
  private String testResultSet = null;
  private String javaType = null;
  private String converter = null;
  private String forceJDBCTypeOnWrite = null;

  private DynamicExpression testExpression = null;
  private DynamicExpression testResultSetExpression = null;
  private ConverterTag converterTag = null;
  private JDBCType jdbcType = null;

  // Constructor

  public TypeSolverWhenTag() {
    super("when");
    log.fine("init");
  }

  // JAXB Setters

  @XmlAttribute(name = "test")
  public void setTest(final String test) {
    this.test = test;
  }

  @XmlAttribute(name = "test-resultset")
  public void setTestResultSet(final String testResultSet) {
    this.testResultSet = testResultSet;
  }

  @XmlAttribute(name = "java-type")
  public void setJavaType(final String javaType) {
    this.javaType = javaType;
  }

  @XmlAttribute
  public void setConverter(final String converter) {
    this.converter = converter;
  }

  @XmlAttribute(name = "force-jdbc-type-on-write")
  public void setForceJDBCTypeOnWrite(final String jdbcType) {
    this.forceJDBCTypeOnWrite = jdbcType;
  }

  // Behavior

  public void validate(final HotRodConfigTag config) throws InvalidConfigurationFileException {

    DynamicExpressionFactory factory = DynamicExpressionFactoryConfig.getFactory();

    // test & test-resultset

    if (SUtil.isEmpty(this.test) && SUtil.isEmpty(this.testResultSet)) {
      throw new InvalidConfigurationFileException(this,
          "The attributes 'test' and 'test-resultset' cannot be empty at the same time");
    }

    if (!SUtil.isEmpty(this.test)) {
      this.testExpression = factory.expression(this.test);
      log.fine("this.testExpression=" + this.testExpression);
    }

    if (!SUtil.isEmpty(this.testResultSet)) {
      this.testResultSetExpression = factory.expression(this.testResultSet);
      log.fine("this.testResultSetExpression=" + this.testResultSetExpression);
    }

    // java-type

    if (this.javaType != null) {
      if (SUtil.isEmpty(this.javaType)) {
        throw new InvalidConfigurationFileException(this,
            "Attribute 'java-type' of tag <" + super.getTagName() + "> cannot be empty. " + "When specified, "
                + "this attribute must specify a full java class name for the database column.");
      }
    }

    // converter

    if (this.converter != null) {
      if (this.javaType != null) {
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
    } else {
      this.converterTag = null;
    }

    // jdbc-type

    if (this.forceJDBCTypeOnWrite == null) {
      this.jdbcType = null;
    } else {
      if (this.javaType == null && this.converter == null) {
        throw new InvalidConfigurationFileException(this, //
            "When the 'force-jdbc-type-on-write' attribute is specified 'java-type' or 'converter' must also be specified");
      }
      if (SUtil.isEmpty(this.forceJDBCTypeOnWrite)) {
        throw new InvalidConfigurationFileException(this, //
            "'force-jdbc-type-on-write' attribute cannot be empty");
      }
      this.jdbcType = JDBCTypes.nameToType(this.forceJDBCTypeOnWrite);
      if (this.jdbcType == null) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid 'force-jdbc-type-on-write' attribute: "
                + "must be a valid JDBC type as defined in the java class java.sql.Types. Valid values are: "
                + Stream.of(JDBCType.values()).map(t -> t.getShortTypeName()).collect(Collectors.joining(", ")));
      }
    }
    log.fine("##### this.jdbcType=" + this.jdbcType);

  }

  // Getters

  public String getTest() {
    return this.test;
  }

  public String getTestResultSet() {
    return testResultSet;
  }

  public String getJavaType() {
    return this.javaType;
  }

  public String getForceJDBCTypeOnWrite() {
    return this.forceJDBCTypeOnWrite;
  }

  public JDBCType getJDBCTypeOnWrite() {
    return jdbcType;
  }

  public ConverterTag getConverterTag() {
    return this.converterTag;
  }

  public DynamicExpression getTestExpression() {
    return this.testExpression;
  }

  public DynamicExpression getTestResultSetExpression() {
    return testResultSetExpression;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ": test=" + this.test + " test-resultset=" + this.testResultSet;
  }

}
