package org.hotrod.config.dynamicsql;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;

import org.apache.log4j.Logger;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.SQLParameter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;

public abstract class DynamicSQLPart extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(DynamicSQLPart.class);

  // Properties

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
  protected List<Object> content = new ArrayList<Object>();

  protected List<DynamicSQLPart> parts = null;

  private ParameterDefinitions parameterDefinitions = null;

  // Constructors

  protected DynamicSQLPart(final String tagName) {
    super(tagName);
    log.debug("init");
  }

  // Constructor just for JAXB's sake - never used
  private DynamicSQLPart() {
    super("<dynamic-sql-tag>");
  }

  // Getters

  // Behavior

  public static class ParameterDefinitions {

    private List<SQLParameter> definitions = new ArrayList<SQLParameter>();

    public void add(final SQLParameter p) {
      if (p == null) {
        throw new IllegalArgumentException();
      }
      this.definitions.add(p);
    }

    public SQLParameter find(final SQLParameter p) {
      for (SQLParameter pd : this.definitions) {
        if (p.getName().equals(pd.getName())) {
          return pd;
        }
      }
      return null;
    }

    public void remove(final SQLParameter p) {
      for (int i = 0; i < this.definitions.size(); i++) {
        SQLParameter pd = this.definitions.get(i);
        if (p.getName().equals(pd.getName())) {
          this.definitions.remove(i);
          return;
        }
      }
    }

    public List<SQLParameter> getDefinitions() {
      return definitions;
    }

  }

  public void validate(final String tagIdentification) throws InvalidConfigurationFileException {
    this.parameterDefinitions = new ParameterDefinitions();
    this.retrievePartsAndValidate(tagIdentification, this.parameterDefinitions);
  }

  public final List<SQLParameter> getParameters() {
    return this.parameterDefinitions.getDefinitions();
  }

  protected final void retrievePartsAndValidate(final String tagIdentification,
      final ParameterDefinitions parameterDefinitions) throws InvalidConfigurationFileException {

    // 1. Validate attributes

    this.validateAttributes(tagIdentification, parameterDefinitions);

    // 2. General body parsing & validation

    this.parts = new ArrayList<DynamicSQLPart>();
    for (Object obj : this.content) {
      DynamicSQLPart p = null;
      try {
        String s = (String) obj;
        p = new ParameterisableTextPart(s, tagIdentification, parameterDefinitions);
      } catch (ClassCastException e1) {
        try {
          p = (DynamicSQLPart) obj;
          p.retrievePartsAndValidate(tagIdentification, parameterDefinitions);
        } catch (ClassCastException e2) {
          throw new InvalidConfigurationFileException("Malformed content of the query on tag " + tagIdentification
              + ". Invalid inner tag of class " + obj.getClass().getName());
        }
      }
      this.parts.add(p);
    }

    // 3. Specific body validation

    this.specificBodyValidation(tagIdentification, parameterDefinitions);

    // 4. Post tag validation

    this.postTagValidation(tagIdentification, parameterDefinitions);

  }

  protected abstract void validateAttributes(String tagIdentification, ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException;

  protected abstract void specificBodyValidation(String tagIdentification, ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException;

  protected void postTagValidation(final String tagIdentification, final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // By default, nothing to do
  }

  // Java Expression Rendering

  public String renderJavaExpression(final int margin, final ParameterRenderer parameterRenderer) {
    DynamicExpression expr = this.getJavaExpression(parameterRenderer);
    return expr.renderConstructor(margin);
  }

  protected abstract DynamicExpression getJavaExpression(ParameterRenderer parameterRenderer);

  // Simple Static (non-dynamic) Rendering

  public String renderStatic(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (Object obj : this.content) {
      try {
        String s = (String) obj;
        sb.append(s);
      } catch (ClassCastException e1) {
        try {
          DynamicSQLPart s = (DynamicSQLPart) obj;
          sb.append(s.renderStatic(parameterRenderer));
        } catch (ClassCastException e2) {
          sb.append("[could not render object of class: " + obj.getClass().getName() + " ]");
        }
      }
    }
    String body = sb.toString();

    StringBuilder full = new StringBuilder();
    if (body == null || body.isEmpty()) {
      if (this.shouldRenderTag()) {
        full.append(this.renderEmptyTag(parameterRenderer));
      }
    } else {
      if (this.shouldRenderTag()) {
        full.append(renderTagHeader(parameterRenderer));
      }
      full.append(body);
      if (this.shouldRenderTag()) {
        full.append(renderTagFooter());
      }
    }
    return full.toString();
  }

  // XML Rendering

  protected abstract boolean shouldRenderTag();

  public String renderXML(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (Object obj : this.content) {
      try {
        String s = (String) obj;
        s = s.replace("&", "&amp;").replace("<", "&lt;");
        sb.append(s);
      } catch (ClassCastException e1) {
        try {
          DynamicSQLPart s = (DynamicSQLPart) obj;
          sb.append(s.renderXML(parameterRenderer));
        } catch (ClassCastException e2) {
          sb.append("[could not render object of class: " + obj.getClass().getName() + " ]");
        }
      }
    }
    String body = sb.toString();

    StringBuilder full = new StringBuilder();
    if (body == null || body.isEmpty()) {
      if (this.shouldRenderTag()) {
        full.append(this.renderEmptyTag(parameterRenderer));
      }
    } else {
      if (this.shouldRenderTag()) {
        full.append(renderTagHeader(parameterRenderer));
      }
      full.append(body);
      if (this.shouldRenderTag()) {
        full.append(renderTagFooter());
      }
    }
    return full.toString();
  }

  protected abstract TagAttribute[] getAttributes();

  protected String renderTagHeader(final ParameterRenderer parameterRenderer) {
    return renderHeader(false, parameterRenderer);
  }

  protected String renderEmptyTag(final ParameterRenderer parameterRenderer) {
    return renderHeader(true, parameterRenderer);
  }

  private String renderHeader(final boolean emptyTag, final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    sb.append("<" + super.getTagName());
    for (TagAttribute a : this.getAttributes()) {
      String value = a.render(parameterRenderer);
      if (value != null) {
        value = value.replace("&", "&amp;").replace("<", "&lt;").replace("\"", "&quot;");
        sb.append(" " + a.getName() + "=\"" + value + "\"");
      }
    }
    sb.append(emptyTag ? " />" : ">");
    return sb.toString();
  }

  protected String renderTagFooter() {
    StringBuilder sb = new StringBuilder();
    sb.append("</" + super.getTagName() + ">");
    return sb.toString();
  }

}
