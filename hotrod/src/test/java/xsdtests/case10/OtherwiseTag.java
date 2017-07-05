package xsdtests.case10;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "otherwise")
public class OtherwiseTag {

  // Constructor

  public OtherwiseTag() {
    System.out.println("[Constructor] " + OtherwiseTag.class.getSimpleName());
  }

  // Properties

  @XmlMixed
  @XmlElementRefs({ @XmlElementRef(type = IfTag.class), @XmlElementRef(type = ChooseTag.class) })
  private List<Object> content = new ArrayList<Object>();

  // Getters & Setters

  public List<Object> getContent() {
    return this.content;
  }

  // render

  public String render() {
    StringBuilder sb = new StringBuilder();
    sb.append("<" + OtherwiseTag.class.getSimpleName() + ">");
    for (Object obj : this.content) {

      try {
        String s = (String) obj;
        sb.append(s);
      } catch (ClassCastException e1) {
        try {
          OtherwiseTag s = (OtherwiseTag) obj;
          sb.append(s.render());
        } catch (ClassCastException e2) {
          sb.append("" + obj);
        }
      }

    }
    sb.append("\n</" + OtherwiseTag.class.getSimpleName() + ">\n");
    return sb.toString();
  }

  // toString

  public String toString() {
    return "{" + OtherwiseTag.class.getSimpleName() + "}";
  }

}