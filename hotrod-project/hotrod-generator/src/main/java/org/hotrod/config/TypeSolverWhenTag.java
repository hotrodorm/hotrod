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
import org.hotrod.utils.TypesUtil;

@XmlRootElement(name = "column")
public class TypeSolverWhenTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(TypeSolverWhenTag.class.getName());

  // Properties

  private String test = null;

  @Deprecated
  private String javaType = null;
  private String type = null;

  private String converter = null;
  private String forceJDBCTypeOnWrite = null;

  private DynamicExpression testExpression = null;
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

  @XmlAttribute(name = "force-jdbc-type-on-write")
  public void setForceJDBCTypeOnWrite(final String jdbcType) {
    this.forceJDBCTypeOnWrite = jdbcType;
  }

  // Behavior

  public void validate(final HotRodConfigTag config) throws InvalidConfigurationFileException {

    DynamicExpressionFactory factory = DynamicExpressionFactoryConfig.getFactory();

    // test

    if (SUtil.isEmpty(this.test)) {
      throw new InvalidConfigurationFileException(this, "The attribute 'test' cannot be empty");
    }
    this.testExpression = factory.expression(this.test);

    if (this.converter == null) {

      this.converterTag = null;

      // type & java-type

      if (this.javaType == null) {
        if (this.type == null) {
          throw new InvalidConfigurationFileException(this,
              "The attribute 'type' of the tag <" + super.getTagName() + "> must be specified.");
        } else {

          // type
          if (SUtil.isEmpty(this.type)) {
            throw new InvalidConfigurationFileException(this,
                "The attribute 'type' of the tag <" + super.getTagName() + "> cannot be empty. " + "When specified, "
                    + "this attribute must specify a class name for the database column.");
          }
          if (!TypesUtil.isValidClassName(this.type)) {
            throw new InvalidConfigurationFileException(this, "The attribute 'type' of the tag <" + super.getTagName()
                + "> must be a valid full class name, but '" + this.type + "' was specified.");
          }

        }
      } else {
        if (this.type != null) {
          throw new InvalidConfigurationFileException(this, "Either the attribute 'type' or 'java-type' of the tag <"
              + super.getTagName() + "> can be specified at the same time. " + "Use the former when possible.");

        } else {

          // java-type
          if (SUtil.isEmpty(this.javaType)) {
            throw new InvalidConfigurationFileException(this,
                "The attribute 'java-type' of the tag <" + super.getTagName() + "> cannot be empty. "
                    + "When specified, " + "this attribute must specify a class name for the database column.");
          }
          if (!TypesUtil.isValidClassName(this.javaType)) {
            throw new InvalidConfigurationFileException(this, "The attribute 'java-type' of the tag <"
                + super.getTagName() + "> must be a valid full class name, but '" + this.type + "' was specified.");
          }
          this.type = this.javaType;
          this.javaType = null;

        }
      }

    } else {

      // converter

      if (this.type != null) {
        throw new InvalidConfigurationFileException(this,
            "Invalid attributes 'type' and 'converter' in the tag <" + super.getTagName() + ">: "
                + "these attributes are mutually exclusive, so only one of them can be specified at the same time.");
      }
      if (this.javaType != null) {
        throw new InvalidConfigurationFileException(this,
            "Invalid attributes 'java-type' and 'converter' in the tag <" + super.getTagName() + ">: "
                + "these attributes are mutually exclusive, so only one of them can be specified at the same time.");
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

    if (this.forceJDBCTypeOnWrite == null) {
      this.jdbcType = null;
    } else {
      if (this.type == null && this.converter == null) {
        throw new InvalidConfigurationFileException(this, //
            "When the 'force-jdbc-type-on-write' attribute is specified 'type' or 'converter' must also be specified");
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

  public String getJavaType() {
    return this.type;
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

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ": test=" + this.test;
  }

}
