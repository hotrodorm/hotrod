package org.hotrod.config.dynamicsql;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;

import org.hotrod.config.AbstractConfigurationTag;

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

  // Rendering

  protected String renderTag(final TagAttribute... attributes) {
    StringBuilder sb = new StringBuilder();
    sb.append(renderTagHeader(attributes));
    for (Object obj : this.content) {
      try {
        String s = (String) obj;
        s = s.replace("&", "&amp;").replace("<", "&lt;");
        sb.append(s);
      } catch (ClassCastException e1) {
        try {
          DynamicSQLPart s = (DynamicSQLPart) obj;
          sb.append(s.render());
        } catch (ClassCastException e2) {
          sb.append("[could not render object of class: " + obj.getClass().getName() + " ]");
        }
      }
    }
    sb.append(renderTagFooter());
    return sb.toString();
  }

  public abstract String render();

  protected String renderTagHeader(final TagAttribute... attributes) {
    return renderHeader(false, attributes);
  }

  protected String renderEmptyTag(final TagAttribute... attributes) {
    return renderHeader(true, attributes);
  }

  private String renderHeader(final boolean emptyTag, final TagAttribute... attributes) {
    StringBuilder sb = new StringBuilder();
    sb.append("<" + super.getTagName());
    for (TagAttribute a : attributes) {
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

}
