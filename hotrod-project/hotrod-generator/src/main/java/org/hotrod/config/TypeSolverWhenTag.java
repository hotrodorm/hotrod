package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.Compare;
import org.hotrod.utils.JdbcTypes;
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
  private String jdbcType = null;

  private OgnlExpression testExpression = null;
  private ConverterTag converterTag = null;

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

  @XmlAttribute(name = "jdbc-type")
  public void setJdbcType(final String jdbcType) {
    this.jdbcType = jdbcType;
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
    log.info("this.testExpression=" + this.testExpression);

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

    if (this.jdbcType != null) {
      if (this.javaType == null && this.converter == null) {
        throw new InvalidConfigurationFileException(this, //
            "When the 'jdbc-type' attribute is specified 'java-type' or 'converter' must also be specified", //
            "'jdbc-type' attribute specified but no java-type attribute nor converter attribute found. "
                + "The jdbc-type attribute can only be specified when the java-type attribute or the converter is present.");
      }
      if (SUtil.isEmpty(this.jdbcType)) {
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

  }

  // Getters

  public String getTest() {
    return this.test;
  }

  public String getJavaType() {
    return this.javaType;
  }

  public String getJdbcType() {
    return this.jdbcType;
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
      this.jdbcType = f.jdbcType;
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
          Compare.same(this.jdbcType, f.jdbcType);
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
