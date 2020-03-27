package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.hotrod.utils.Compare;
import org.hotrod.utils.identifiers.Id;

@XmlRootElement(name = "query")
public class QueryMethodTag extends AbstractMethodTag<QueryMethodTag> {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(QueryMethodTag.class);

  // Properties - Primitive content parsing by JAXB

  // This property cannot be transient. JAXB fails when doing so with the
  // message: Transient field "content" cannot have any JAXB annotations.

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

  // Duplicate

  @Override
  public QueryMethodTag duplicate() {
    QueryMethodTag d = new QueryMethodTag();

    d.copyCommon(this);

    d.content = this.content;
    d.parts = this.parts;
    d.parameters = this.parameters;

    return d;
  }

  // JAXB Setters

  @XmlAttribute(name = "method")
  public void setMethod(final String method) {
    this.method = method;
  }

  // Behavior

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig) throws InvalidConfigurationFileException {

    super.validate(daosTag, config, fragmentConfig);

    // content text, parameters, dynamic SQL tags

    this.parts = new ArrayList<DynamicSQLPart>();
    this.parameters = new ParameterDefinitions();

    for (Object obj : this.content) {
      try {
        String s = (String) obj; // content text
        DynamicSQLPart p = new ParameterisableTextPart(s, this, this.parameters);
        p.validate(daosTag, config, fragmentConfig, this.parameters);
        this.parts.add(p);
        super.addChild(p);
      } catch (ClassCastException e1) {
        try {
          ParameterTag p = (ParameterTag) obj; // parameter
          p.validate();
          this.parameters.add(p);
          super.addChild(p);
        } catch (ClassCastException e2) {
          try {
            DynamicSQLPart p = (DynamicSQLPart) obj; // dynamic SQL part
            p.validate(daosTag, config, fragmentConfig, this.parameters);
            this.parts.add(p);
            super.addChild(p);
          } catch (ClassCastException e3) {
            throw new InvalidConfigurationFileException(this, //
                "The body of the tag <" + super.getTagName() + "> has an invalid tag: " + obj.getClass().getName(), //
                "The body of the tag <" + super.getTagName() + "> has an invalid tag (of class '"
                    + obj.getClass().getName() + "').");
          }
        }
      }
    }

  }

  // Getters

  @Override
  public String getMethod() {
    return super.method;
  }

  public Id getId() {
    return super.id;
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

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    try {
      QueryMethodTag f = (QueryMethodTag) fresh;
      return this.method.equals(f.method);
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      QueryMethodTag f = (QueryMethodTag) fresh;
      boolean different = !same(fresh);
      this.content = f.content;
      this.parts = f.parts;
      this.parameters = f.parameters;
      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      QueryMethodTag f = (QueryMethodTag) fresh;
      return //
      Compare.same(this.method, f.method) && //
          Compare.same(this.parts, f.parts) && //
          Compare.same(this.parameters, f.parameters) //
      ;
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.method;
  }

}
