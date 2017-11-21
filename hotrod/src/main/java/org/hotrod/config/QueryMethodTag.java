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
import org.hotrod.utils.identifiers.DbIdentifier;
import org.hotrod.utils.identifiers.Identifier;

@XmlRootElement(name = "query")
public class QueryMethodTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(QueryMethodTag.class);

  // Properties

  protected String method = null;

  // Properties - Primitive content parsing by JAXB

  @XmlMixed
  @XmlElementRefs({ //
      @XmlElementRef(type = ParameterTag.class), //
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
  protected ParameterDefinitions parameters = null;

  // Constructor

  public QueryMethodTag() {
    super("query");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute(name = "method")
  public void setMethod(final String method) {
    this.method = method;
  }

  // Behavior

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig) throws InvalidConfigurationFileException {

    // method

    if (this.method == null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Attribute 'method' of tag <" + getTagName() + "> cannot be empty. " + "Must specify a unique name.");
    } else if (!this.method.matches(Patterns.VALID_JAVA_METHOD)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Attribute 'method' of tag <" + super.getTagName() + "> specifies '" + this.method
              + "' but must specify a valid java method name. "
              + "Valid method names must start with a lowercase letter, "
              + "and continue with letters, digits, dollarsign, and/or underscores.");
    }

    // content text, parameters, dynamic SQL tags

    this.parts = new ArrayList<DynamicSQLPart>();
    this.parameters = new ParameterDefinitions();

    for (Object obj : this.content) {
      try {
        String s = (String) obj; // content text
        DynamicSQLPart p = new ParameterisableTextPart(s, this.getSourceLocation(), this.parameters);
        p.validate(daosTag, config, fragmentConfig, this.parameters);
        this.parts.add(p);
      } catch (ClassCastException e1) {
        try {
          ParameterTag p = (ParameterTag) obj; // parameter
          p.validate();
          this.parameters.add(p);
        } catch (ClassCastException e2) {
          try {
            DynamicSQLPart p = (DynamicSQLPart) obj; // dynamic SQL part
            p.validate(daosTag, config, fragmentConfig, this.parameters);
            this.parts.add(p);
          } catch (ClassCastException e3) {
            throw new InvalidConfigurationFileException(super.getSourceLocation(), "The body of the tag <"
                + super.getTagName() + "> has an invalid tag (of class '" + obj.getClass().getName() + "').");
          }
        }
      }
    }

  }

  // Getters

  public String getMethod() {
    return method;
  }

  public Identifier getIdentifier() {
    return new DbIdentifier("a", this.method);
  }

  // Rendering

  public String renderSQLSentence(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (DynamicSQLPart p : this.parts) {
      sb.append(p.renderStatic(parameterRenderer));
    }
    return sb.toString();
  }

  public String renderXML(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (DynamicSQLPart p : this.parts) {
      sb.append(p.renderXML(parameterRenderer));
    }
    return sb.toString();
  }

  public List<ParameterTag> getParameterDefinitions() {
    return this.parameters.getDefinitions();
  }

}
