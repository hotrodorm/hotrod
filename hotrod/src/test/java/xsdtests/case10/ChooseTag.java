package xsdtests.case10;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "choose")
public class ChooseTag {

  // Constructor

  public ChooseTag() {
    System.out.println("[Constructor] " + ChooseTag.class.getSimpleName());
  }

  // Properties

  private List<WhenTag> whens = new ArrayList<WhenTag>();
  private OtherwiseTag otherwise = null;

  // Getters & Setters

  public List<WhenTag> getWhens() {
    return whens;
  }

  @XmlElement
  public void setWhen(WhenTag when) {
    this.whens.add(when);
  }

  public OtherwiseTag getOtherwise() {
    return otherwise;
  }

  @XmlElement
  public void setOtherwise(OtherwiseTag otherwise) {
    this.otherwise = otherwise;
  }

  // render

  public String render() {
    StringBuilder sb = new StringBuilder();
    sb.append("<" + ChooseTag.class.getSimpleName() + ">");
    for (WhenTag w : this.whens) {
      sb.append(w.render());
    }
    if (this.otherwise != null) {
      sb.append(this.otherwise.render());
    }
    sb.append("\n</" + ChooseTag.class.getSimpleName() + ">\n");
    return sb.toString();
  }

  // toString

  public String toString() {
    return "{" + ChooseTag.class.getSimpleName() + "}";
  }

}