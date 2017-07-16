package org.hotrod.config.dynamicsql;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;

import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.SQLParameter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.dynamicsql.expressions.DynamicExpression;

public abstract class DynamicSQLPart extends AbstractConfigurationTag {

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

  // Constructors

  protected DynamicSQLPart(final String tagName) {
    super(tagName);
  }

  // Constructor just for JAXB's sake - never used
  private DynamicSQLPart() {
    super("<dynamic-sql-tag>");
  }

  // Getters

  public List<Object> getContent() {
    return this.content;
  }

  // Behavior

  public void validateContent(final String tagIdentification) throws InvalidConfigurationFileException {
    List<SQLParameter> parameterDefinitions = new ArrayList<SQLParameter>();
    this.retrievePartsAndValidate(tagIdentification, parameterDefinitions);
  }

  protected abstract void validateAttributes(String tagIdentification) throws InvalidConfigurationFileException;

  protected final void retrievePartsAndValidate(final String tagIdentification,
      final List<SQLParameter> parameterDefinitions) throws InvalidConfigurationFileException {
    this.parts = new ArrayList<DynamicSQLPart>();
    for (Object obj : this.content) {
      DynamicSQLPart p = null;
      try {
        String s = (String) obj;
        p = new ParameterisableTextPart(s, tagIdentification);
      } catch (ClassCastException e1) {
        try {
          p = (DynamicSQLPart) obj;
          p.validateAttributes(tagIdentification);
        } catch (ClassCastException e2) {
          throw new InvalidConfigurationFileException("Malformed content of the query on tag " + tagIdentification
              + ". Invalid inner tag of class " + obj.getClass().getName());
        }
      }
      p.retrievePartsAndValidate(tagIdentification, parameterDefinitions);
      this.parts.add(p);
    }

    for (SQLParameter p : this.getParameters()) {
      SQLParameter definition = this.findDefinition(p, parameterDefinitions);
      if (p.isDefinition()) {
        if (definition == null) {
          parameterDefinitions.add(p);
        } else {
          throw new InvalidConfigurationFileException("The body of the tag " + tagIdentification
              + " has multiple parameter definitions with the same name: " + p.getName() + ".\n"
              + "* If you want them to be different parameters, please choose a different names for each one;\n"
              + "* If you want to use the same parameter multiple times, "
              + "then the 'javaType' and/or 'jdbcType' can only be specified " + "on the first occurrence of it.");
        }
      } else {
        if (definition != null) {
          p.setDefinition(definition);
        } else {
          throw new InvalidConfigurationFileException(
              "The body of the tag " + tagIdentification + " includes a parameter reference '" + p.getName()
                  + "' but there's no parameter defined with that name yet.\n"
                  + "The first time a parameter is specified, " + "it must be fully qualified with the 'javaType' and "
                  + "'jdbcType' values (i.e. must be a parameter definition, rather than a parameter occurence).");
        }
      }
    }

  }

  public final List<SQLParameter> getParameters() {
    List<SQLParameter> params = new ArrayList<SQLParameter>();
    for (DynamicSQLPart p : this.parts) {
      params.addAll(p.getParameters());
    }
    return params;
  }

  protected SQLParameter findDefinition(final SQLParameter p, final List<SQLParameter> parameterDefinitions) {
    for (SQLParameter pd : parameterDefinitions) {
      if (p.getName().equals(pd.getName())) {
        return pd;
      }
    }
    return null;
  }

  // Java Expression Rendering

  public String renderJavaExpression(final int margin, final ParameterRenderer parameterRenderer) {
    DynamicExpression expr = this.getJavaExpression(parameterRenderer);
    return expr.renderConstructor(margin);
  }

  protected abstract DynamicExpression getJavaExpression(ParameterRenderer parameterRenderer);

  // Rendering

  public String renderTag(final ParameterRenderer parameterRenderer) {
    StringBuilder sb = new StringBuilder();
    sb.append(renderTagHeader(parameterRenderer));
    for (Object obj : this.content) {
      try {
        String s = (String) obj;
        s = s.replace("&", "&amp;").replace("<", "&lt;");
        sb.append(s);
      } catch (ClassCastException e1) {
        try {
          DynamicSQLPart s = (DynamicSQLPart) obj;
          sb.append(s.renderTag(parameterRenderer));
        } catch (ClassCastException e2) {
          sb.append("[could not render object of class: " + obj.getClass().getName() + " ]");
        }
      }
    }
    sb.append(renderTagFooter());
    return sb.toString();
  }

  protected abstract TagAttribute[] getAttributes();

  // // TODO: remove
  // @Deprecated
  // public final String renderSQLSentence(final ParameterRenderer
  // parameterRenderer) {
  // return this.renderTag(parameterRenderer);
  // }

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
