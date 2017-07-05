package xsdtests.case10;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "select")
public class SelectTag {

  private String name = null;
  private String vo = null;

  public SelectTag() {
    System.out.println("[Constructor] " + SelectTag.class.getSimpleName());
  }

  @XmlMixed
  @XmlElementRefs({ @XmlElementRef(type = IfTag.class) })
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
    sb.append("<" + SelectTag.class.getSimpleName() + " name=" + this.name + " vo=" + this.vo + ">");

    for (Object obj : this.content) {

      try {
        String s = (String) obj;
        sb.append(s);
      } catch (ClassCastException e1) {
        try {
          IfTag s = (IfTag) obj;
          sb.append(s.render());
        } catch (ClassCastException e2) {
          sb.append("" + obj);
        }
      }

    }

    sb.append("\n</" + SelectTag.class.getSimpleName() + ">\n");

    return sb.toString();
  }

  // toString

  public String toString() {
    return "{" + SelectTag.class.getSimpleName() + ": name=" + this.name + " vo=" + this.vo + "}";
  }

}
