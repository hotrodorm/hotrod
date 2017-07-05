package xsdtests.case10;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "when")
public class WhenTag {

  // Constructor

  public WhenTag() {
    System.out.println("[Constructor] " + WhenTag.class.getSimpleName());
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
    sb.append("<" + WhenTag.class.getSimpleName() + " test=" + this.test + ">");
    for (Object obj : this.content) {

      try {
        String s = (String) obj;
        sb.append(s);
      } catch (ClassCastException e1) {
        try {
          WhenTag s = (WhenTag) obj;
          sb.append(s.render());
        } catch (ClassCastException e2) {
          sb.append("" + obj);
        }
      }

    }
    sb.append("\n</" + WhenTag.class.getSimpleName() + ">\n");
    return sb.toString();
  }

  // toString

  public String toString() {
    return "{" + WhenTag.class.getSimpleName() + ": test=" + this.test + "}";
  }

}