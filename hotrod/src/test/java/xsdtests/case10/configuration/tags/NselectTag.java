package xsdtests.case10.configuration.tags;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.AbstractConfigurationTag;

import xsdtests.case10.dynamicsql.tags.BindTag;
import xsdtests.case10.dynamicsql.tags.ChooseTag;
import xsdtests.case10.dynamicsql.tags.DynamicSQLTag;
import xsdtests.case10.dynamicsql.tags.ForEachTag;
import xsdtests.case10.dynamicsql.tags.IfTag;
import xsdtests.case10.dynamicsql.tags.TrimTag;
import xsdtests.case10.dynamicsql.tags.WhereTag;

@XmlRootElement(name = "select")
public class NselectTag extends AbstractConfigurationTag {

  protected NselectTag() {
    super("select");
  }

  private String name = null;
  private String vo = null;

  @XmlMixed
  @XmlElementRefs({ //
      @XmlElementRef(type = IfTag.class), //
      @XmlElementRef(type = ChooseTag.class), //
      @XmlElementRef(type = WhereTag.class), //
      @XmlElementRef(type = ForEachTag.class), //
      @XmlElementRef(type = BindTag.class), //
      @XmlElementRef(type = TrimTag.class) //
  })
  private List<Object> content = new ArrayList<Object>();

  // Getters & Setters

  public String getName() {
    return name;
  }

  @XmlAttribute
  public void setName(String name) {
    this.name = name;
  }

  public String getVo() {
    return vo;
  }

  @XmlAttribute
  public void setVo(String vo) {
    this.vo = vo;
  }

  public List<Object> getContent() {
    return this.content;
  }

  // render

  public String render() {
    StringBuilder sb = new StringBuilder();
    sb.append("<select name=\"" + this.name + "\" vo=\"" + this.vo + "\">");

    for (Object obj : this.content) {

      try {
        String s = (String) obj;
        sb.append(s);
      } catch (ClassCastException e1) {
        try {
          DynamicSQLTag t = (DynamicSQLTag) obj;
          sb.append(t.render());
        } catch (ClassCastException e2) {
          sb.append("[could not render tag of class: " + obj.getClass().getName() + " ]");
        }
      }

    }

    sb.append("</select>");

    return sb.toString();
  }

  // toString

}
