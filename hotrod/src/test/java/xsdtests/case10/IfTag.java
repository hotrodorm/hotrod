package xsdtests.case10;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "if")
public class IfTag {

  // Constructor

  public IfTag() {
    System.out.println("[Constructor] " + IfTag.class.getSimpleName());
  }

  // Properties

  private String test = null;

  @XmlMixed
  @XmlElementRefs({ @XmlElementRef(type = IfTag.class), @XmlElementRef(type = ChooseTag.class) })
  private List<Object> content = new ArrayList<Object>();

  // Getters & Setters

  public String getTest() {
    return test;
  }

  @XmlAttribute
  public void setTest(String test) {
    this.test = test;
  }

  public List<Object> getContent() {
    return this.content;
  }

  // render

  public String render() {
    StringBuilder sb = new StringBuilder();
    sb.append("<" + IfTag.class.getSimpleName() + " test=" + this.test + ">");
    for (Object obj : this.content) {

      try {
        String s = (String) obj;
        sb.append(s);
      } catch (ClassCastException e1) {
        try {
          IfTag s = (IfTag) obj;
          sb.append(s.render());
        } catch (ClassCastException e2) {
          try {
            ChooseTag s = (ChooseTag) obj;
            sb.append(s.render());
          } catch (ClassCastException e3) {
            sb.append("" + obj);
          }
        }
      }

    }
    sb.append("\n</" + IfTag.class.getSimpleName() + ">\n");
    return sb.toString();
  }

  // toString

  public String toString() {
    return "{" + IfTag.class.getSimpleName() + ": test=" + this.test + "}";
  }

}