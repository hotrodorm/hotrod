package org.hotrod.config.dynamicsql;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;

import org.apache.log4j.Logger;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.DaosTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.ParameterTag;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;
import org.hotrod.runtime.dynamicsql.expressions.LiteralExpression;
import org.hotrod.runtime.exceptions.InvalidJavaExpressionException;

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

  protected List<DynamicSQLPart> parts;

  // Constructors

  protected DynamicSQLPart(final String tagName) {
    super(tagName);
    log.debug("init");
  }

  // Constructor just for JAXB's sake - never used
  private DynamicSQLPart() {
    super("<dynamic-sql-tag>");
  }

  // Validation

  public final void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final ParameterDefinitions parameters)
      throws InvalidConfigurationFileException {
    retrievePartsAndValidate(parameters);
  }

  public void retrievePartsAndValidate(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {

    // 1. Validate attributes

    this.validateAttributes(parameterDefinitions);

    // 2. General body parsing & validation

    this.parts = new ArrayList<DynamicSQLPart>();
    for (Object obj : this.content) {
      DynamicSQLPart p = null;
      try {
        String s = (String) obj;
        p = new ParameterisableTextPart(s, this.getSourceLocation(), parameterDefinitions);
      } catch (ClassCastException e1) {
        try {
          p = (DynamicSQLPart) obj;
          p.retrievePartsAndValidate(parameterDefinitions);
        } catch (ClassCastException e2) {
          throw new InvalidConfigurationFileException(super.getSourceLocation(),
              "Malformed content of the <query> tag. Invalid inner tag of class " + obj.getClass().getName());
        }
      }
      this.parts.add(p);
    }

    // 3. Specific body validation

    this.specificBodyValidation(parameterDefinitions);

    // 4. Post tag validation

    this.postTagValidation(parameterDefinitions);

  }

  protected abstract void validateAttributes(ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException;

  protected abstract void specificBodyValidation(ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException;

  protected void postTagValidation(final ParameterDefinitions parameterDefinitions)
      throws InvalidConfigurationFileException {
    // By default, nothing to do
  }

  public void validateAgainstDatabase(final HotRodGenerator generator) throws InvalidConfigurationFileException {
    // Nothing to do
  }

  // Getters

  // Behavior

  public static class ParameterDefinitions {

    private List<ParameterTag> params = new ArrayList<ParameterTag>();

    private LinkedHashMap<String, ParameterTag> definitions = new LinkedHashMap<String, ParameterTag>();

    public void add(final ParameterTag p) throws InvalidConfigurationFileException {
      this.params.add(p);
    }

    public void validate() throws InvalidConfigurationFileException {
      for (ParameterTag p : this.params) {
        log.debug("p.getName()=" + p.getName());
        p.validate();
        if (this.definitions.containsKey(p.getName())) {
          throw new InvalidConfigurationFileException(p.getSourceLocation(),
              "Duplicate parameter '" + p.getName() + "'. Please specify different names for each parameter.");
        }
        this.definitions.put(p.getName(), p);
      }
      this.params = null;
    }

    public ParameterTag find(final String name) {
      return this.definitions.get(name);
    }

    public void remove(final ParameterTag p) {
      this.definitions.remove(p.getName());
    }

    public List<ParameterTag> getDefinitions() {
      return new ArrayList<ParameterTag>(this.definitions.values());
    }

  }

  // Simple Static (non-dynamic) Rendering

  public String renderStatic(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    for (DynamicSQLPart p : this.parts) {
      sb.append(p.renderStatic(parameterRenderer));
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
    for (DynamicSQLPart p : this.parts) {
      sb.append(p.renderXML(parameterRenderer));
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
      String value = a.getValue();
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

  protected abstract DynamicExpression getJavaExpression(ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException;

  // Utils

  protected DynamicExpression[] toArray(final List<DynamicSQLPart> parts, final ParameterRenderer parameterRenderer)
      throws InvalidJavaExpressionException {
    List<DynamicExpression> exps = new ArrayList<DynamicExpression>();
    LiteralExpression last = null;
    for (DynamicSQLPart p : parts) {
      DynamicExpression expr = p.getJavaExpression(parameterRenderer);
      try {
        LiteralExpression le = (LiteralExpression) expr;
        if (last == null) {
          last = le;
        } else {
          last.concat(le);
        }
      } catch (ClassCastException e) {
        if (last != null) {
          exps.add(last);
          last = null;
        }
        exps.add(expr);
      }
    }
    if (last != null) {
      exps.add(last);
    }
    return exps.toArray(new DynamicExpression[0]);
  }

}
