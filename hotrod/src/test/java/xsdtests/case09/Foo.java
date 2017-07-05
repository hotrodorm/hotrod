package xsdtests.case09;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Foo {

  public Foo() {
    System.out.println("*** [Constructor] Foo");
  }

  private String name;

  @XmlMixed
  @XmlElementRefs({ @XmlElementRef(type = Bar.class), @XmlElementRef(type = Wxyz.class) })
  private List<Object> content = new ArrayList<Object>();

  // Getters & Setters

  public String getName() {
    return name;
  }

  @XmlAttribute
  public void setName(String name) {
    System.out.println("***   Foo.setName(" + name + ")");
    this.name = name;
  }

  public List<Object> getContent() {
    return this.content;
  }

}