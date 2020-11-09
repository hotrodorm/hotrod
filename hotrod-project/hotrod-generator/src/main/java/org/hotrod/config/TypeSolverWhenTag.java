package org.hotrod.config;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.Compare;
import org.hotrod.utils.JdbcTypes;
import org.hotrod.utils.JdbcTypes.JDBCType;
import org.hotrod.utils.OgnlExpression;
import org.hotrodorm.hotrod.utils.SUtil;

import ognl.OgnlException;

@XmlRootElement(name = "column")
public class TypeSolverWhenTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(TypeSolverWhenTag.class);

  // Properties

  private String test = null;
  private String javaType = null;
  private String converter = null;
  private String forceJDBCTypeOnWrite = null;

  private OgnlExpression testExpression = null;
  private ConverterTag converterTag = null;
  private JDBCType jdbcType = null;

  // Constructor

  public TypeSolverWhenTag() {
    super("when");
    log.debug("init");
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

    // test

    if (SUtil.isEmpty(this.test)) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute 'test' cannot be empty: must be a boolean expression", //
          "Attribute 'test' cannot be empty: must be a boolean expression");
    }
    try {
      this.testExpression = new OgnlExpression(this.test);
    } catch (OgnlException e) {
      throw new InvalidConfigurationFileException(this, //
          "Invalid OGNL expression: " + this.test, //
          "Invalid OGNL expression: " + this.test);
    }
    log.debug("this.testExpression=" + this.testExpression);

    // java-type

    if (this.javaType != null) {
      if (SUtil.isEmpty(this.javaType)) {
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
      if (SUtil.isEmpty(this.converter)) {
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
      this.jdbcType = JdbcTypes.nameToType(this.forceJDBCTypeOnWrite);
      if (this.jdbcType == null) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid 'force-jdbc-type-on-write' attribute: "
                + "must be a valid JDBC type as defined in the java class java.sql.Types. Valid values are: "
                + Stream.of(JDBCType.values()).map(t -> t.getShortTypeName()).collect(Collectors.joining(", ")));
      }
    }
    log.debug("##### this.jdbcType=" + this.jdbcType);

  }

  // Getters

  public String getTest() {
    return this.test;
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

  public OgnlExpression getTestExpression() {
    return this.testExpression;
  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    // TODO: needs to be properly implemented
    return false;
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    // TODO: needs to be properly implemented
    try {
      TypeSolverWhenTag f = (TypeSolverWhenTag) fresh;
      boolean different = !same(fresh);
      this.javaType = f.javaType;
      this.converter = f.converter;
      this.forceJDBCTypeOnWrite = f.forceJDBCTypeOnWrite;
      this.converterTag = f.converterTag;
      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    // TODO: needs to be properly implemented
    try {
      TypeSolverWhenTag f = (TypeSolverWhenTag) fresh;
      return //
      Compare.same(this.javaType, f.javaType) && //
          Compare.same(this.converter, f.converter) && //
          Compare.same(this.forceJDBCTypeOnWrite, f.forceJDBCTypeOnWrite);
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.test;
  }

}
