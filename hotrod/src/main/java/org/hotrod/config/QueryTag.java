package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.config.dynamicsql.BindTag;
import org.hotrod.config.dynamicsql.ChooseTag;
import org.hotrod.config.dynamicsql.DynamicSQLPart;
import org.hotrod.config.dynamicsql.DynamicSQLPart.ParameterDefinitions;
import org.hotrod.config.dynamicsql.ForEachTag;
import org.hotrod.config.dynamicsql.IfTag;
import org.hotrod.config.dynamicsql.OtherwiseTag;
import org.hotrod.config.dynamicsql.ParameterisableTextPart;
import org.hotrod.config.dynamicsql.SetTag;
import org.hotrod.config.dynamicsql.TrimTag;
import org.hotrod.config.dynamicsql.WhenTag;
import org.hotrod.config.dynamicsql.WhereTag;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.identifiers.DbIdentifier;
import org.hotrod.utils.identifiers.Identifier;

@XmlRootElement(name = "query")
public class QueryTag extends AbstractDAOTag {

  // Constants

  private static final Logger log = Logger.getLogger(QueryTag.class);

  // Properties

  protected String javaMethodName = null;

  // Properties - Primitive content parsing by JAXB

  @XmlMixed
  @XmlElementRefs({ //
      @XmlElementRef(type = IfTag.class), //
      @XmlElementRef(type = ChooseTag.class), //
      @XmlElementRef(type = WhereTag.class), //
      @XmlElementRef(type = WhenTag.class), //
      @XmlElementRef(type = OtherwiseTag.class), //
      @XmlElementRef(type = ForEachTag.class), //
      @XmlElementRef(type = BindTag.class), //
      @XmlElementRef(type = SetTag.class), //
      @XmlElementRef(type = TrimTag.class) //
  })
  private List<Object> content = new ArrayList<Object>();

  // Properties - Parsed

  protected List<DynamicSQLPart> parts = null;
  protected ParameterDefinitions parameterDefinitions = null;

  // Constructor

  public QueryTag() {
    super("query");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute(name = "java-method-name")
  public void setJavaMethodName(String javaMethodName) {
    this.javaMethodName = javaMethodName;
  }

  // Behavior

  public void validate() throws InvalidConfigurationFileException {

    // method-name

    if (this.javaMethodName == null) {
      throw new InvalidConfigurationFileException("Attribute 'java-method-name' of tag <" + getTagName()
          + "> cannot be empty. " + "Must specify a unique name.");
    } else if (!this.javaMethodName.matches(SequenceTag.VALID_JAVA_METHOD_PATTERN)) {
      throw new InvalidConfigurationFileException("Attribute 'java-method-name' of tag <" + super.getTagName()
          + "> specifies '" + this.javaMethodName + "' but must specify a valid java method name. "
          + "Valid method names must start with a lowercase letter, "
          + "and continue with letters, digits, dollarsign, and/or underscores.");
    }

    String tagIdentification = "<" + super.getTagName() + "> with java-method-name '" + this.javaMethodName + "'";

    // text, dynamic SQL tags

    this.parts = new ArrayList<DynamicSQLPart>();
    this.parameterDefinitions = new ParameterDefinitions();

    for (Object obj : this.content) {
      DynamicSQLPart p;
      try {
        String s = (String) obj;
        p = new ParameterisableTextPart(s, tagIdentification, this.parameterDefinitions);
      } catch (ClassCastException e1) {
        try {
          p = (DynamicSQLPart) obj;
        } catch (ClassCastException e2) {
          throw new InvalidConfigurationFileException("The body of the tag " + tagIdentification
              + " has an invalid tag (of class '" + obj.getClass().getName() + "').");
        }
      }
      p.validate(tagIdentification);
      this.parts.add(p);
    }

  }

  // Getters

  public String getJavaMethodName() {
    return javaMethodName;
  }

  public Identifier getIdentifier() {
    return new DbIdentifier("a", this.javaMethodName);
  }

  // Rendering

  public String renderSQLSentence(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (DynamicSQLPart p : this.parts) {
      sb.append(p.renderStatic(parameterRenderer));
    }
    return sb.toString();
  }

  public List<SQLParameter> getParameterDefinitions() {
    return this.parameterDefinitions.getDefinitions();
  }

  // public String getAugmentedSQL() {
  // ParameterRenderer parameterRenderer = new ParameterRenderer() {
  //
  // @Override
  // public String render(final SQLParameter parameter) {
  // return "#{" + parameter.getName() + "}";
  // }
  //
  // };
  // return this.renderSQLSentence(parameterRenderer);
  // }

  @Override
  public ClassPackage getPackage() {
    // Unused
    return null;
  }

  @Override
  public String getJavaClassName() {
    // Unused
    return null;
  }

}
