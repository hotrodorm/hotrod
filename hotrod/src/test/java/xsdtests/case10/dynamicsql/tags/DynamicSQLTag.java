package xsdtests.case10.dynamicsql.tags;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;

import xsdtests.case10.configuration.ConfigurationTag;
import xsdtests.case10.configuration.TagAttribute;

public abstract class DynamicSQLTag extends ConfigurationTag {

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

  protected DynamicSQLTag(final String tagName) {
    super(tagName);
  }

  // Constructor just for JAXB's sake - never used
  private DynamicSQLTag() {
    super("<dynamic-sql-tag>");
  }

  // Getters

  public List<Object> getContent() {
    return this.content;
  }

  // Rendering

  public String renderTag(final TagAttribute... attributes) {
    StringBuilder sb = new StringBuilder();
    sb.append(super.renderTagHeader(attributes));
    for (Object obj : this.content) {
      try {
        String s = (String) obj;
        s = s.replace("&", "&amp;").replace("<", "&lt;");
        sb.append(s);
      } catch (ClassCastException e1) {
        try {
          DynamicSQLTag s = (DynamicSQLTag) obj;
          sb.append(s.render());
        } catch (ClassCastException e2) {
          sb.append("[could not render object of class: " + obj.getClass().getName() + " ]");
        }
      }
    }
    sb.append(super.renderTagFooter());
    return sb.toString();
  }

}
